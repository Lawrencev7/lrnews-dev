package com.lrnews.article.controller;

import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.article.ArticlePortalControllerApi;
import com.lrnews.article.service.ArticlePortalService;
import com.lrnews.enums.YesOrNo;
import com.lrnews.exception.CustomExceptionFactory;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.Article;
import com.lrnews.utils.IPUtil;
import com.lrnews.utils.JsonUtils;
import com.lrnews.vo.ArticleDetailVO;
import com.lrnews.vo.CommonUserVO;
import com.lrnews.vo.DisplayArticleVO;
import com.lrnews.vo.PagedGridVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.lrnews.values.CommonValueStrings.REDIS_ARTICLE_READ_COUNT_KEY;
import static com.lrnews.values.CommonValueStrings.REDIS_IP_READ_LINK_KEY;

@RestController
public class ArticlePortalController extends BaseController implements ArticlePortalControllerApi {

    private static final String REMOTE_CALL_QUERY_USER_BY_IDS_URL = "http://localhost:8003/user/queryUserByIds?userIds=";

    final ArticlePortalService articlePortalService;

    final RestTemplate restTemplate;

    public ArticlePortalController(ArticlePortalService service, RestTemplate restTemplate) {
        this.articlePortalService = service;
        this.restTemplate = restTemplate;
    }

    @Override
    public JsonResultObject queryArticleList(String keyword, String category, Integer page, Integer pageSize) {
        if (page == null) page = DEFAULT_PAGE;
        if (pageSize == null) pageSize = DEFAULT_PAGE_SIZE;

        PagedGridVO pagedGridVO = articlePortalService.queryAllArticles(keyword, category, page, pageSize);

        // Remote access to user service
        List<Article> rows = (List<Article>) pagedGridVO.getRows();

        Set<String> allPublisher = new HashSet<>();
        rows.forEach(a -> {
            allPublisher.add(a.getPublishUserId());
        });

        List<CommonUserVO> publisherList = remoteQueryPublisherInfos(allPublisher);

        List<DisplayArticleVO> resultList = new ArrayList<>();
        rows.forEach(article -> {
            for (CommonUserVO uvo : publisherList) {
                if(uvo.getId().equals(article.getPublishUserId())){
                    resultList.add(generateDisplayArticleVO(uvo, article));
                }
            }
        });

        return JsonResultObject.ok(resultList);
    }

    @Override
    public JsonResultObject queryPopularArticle() {
        return JsonResultObject.ok(articlePortalService.queryTopReadArticleList());
    }

    @Override
    public JsonResultObject queryArticleListForWriter(String userId, Integer page, Integer pageSize) {
        return JsonResultObject.ok(articlePortalService.queryArticleForWriter(userId, page, pageSize));
    }

    @Override
    public JsonResultObject queryArticleDetail(String articleId) {
        if(StringUtils.isBlank(articleId)){
            return JsonResultObject.errorCustom(ResponseStatusEnum.ILLEGAL_ARGUMENT);
        }

        ArticleDetailVO articleDetailVO = articlePortalService.queryArticleDetail(articleId);
        CommonUserVO commonUserVO = remoteQueryPublisherInfo(articleDetailVO.getPublishUserId());

        articleDetailVO.setReadCounts(Integer.valueOf(redis.get(REDIS_ARTICLE_READ_COUNT_KEY + ':' + articleId)));
        articleDetailVO.setPublishUserName(commonUserVO.getNickname());

        return JsonResultObject.ok(articleDetailVO);
    }

    @Override
    public JsonResultObject readArticle(String articleId, HttpServletRequest request) {
        String userIP = IPUtil.getRequestIp(request);
        if(!redis.keyExist(userIP + REDIS_IP_READ_LINK_KEY + articleId)){
            redis.increase(REDIS_ARTICLE_READ_COUNT_KEY + ":" + articleId, 1);
        } else {
            redis.set(REDIS_ARTICLE_READ_COUNT_KEY + ":" + articleId, "1");
            redis.set(userIP + REDIS_IP_READ_LINK_KEY + articleId, YesOrNo.YES.value, ONE_DAY_KEY_AGE);
        }
        return JsonResultObject.ok();
    }

    private DisplayArticleVO generateDisplayArticleVO(CommonUserVO commonUserVO, Article article) {
        DisplayArticleVO res = new DisplayArticleVO();
        res.setArticleCover(article.getArticleCover());
        res.setId(article.getId());
        res.setContent(article.getContent());
        res.setPublishTime(article.getPublishTime());
        res.setCategoryId(article.getCategoryId());
        res.setReadCounts(Integer.valueOf(redis.get(REDIS_ARTICLE_READ_COUNT_KEY + ":" + article.getId())));
        res.setCommentCounts(article.getCommentCounts());
        res.setPublisherNickname(commonUserVO.getNickname());
        res.setPublisherFace(commonUserVO.getFace());
        res.setPublisherId(commonUserVO.getId());
        return res;
    }

    private List<CommonUserVO> remoteQueryPublisherInfos(Set<String> allPublisher){
        List<CommonUserVO> publisherList = null;
        String userServerUrl = REMOTE_CALL_QUERY_USER_BY_IDS_URL + JsonUtils.objectToJson(allPublisher);
        ResponseEntity<JsonResultObject> entity = restTemplate.getForEntity(userServerUrl, JsonResultObject.class);
        JsonResultObject responseData = entity.getBody();

        if (!Objects.nonNull(responseData)) {
            CustomExceptionFactory.onException(ResponseStatusEnum.SYSTEM_CONNECTION_FAIL);
        }

        if (responseData.getStatus().equals(ResponseStatusEnum.SUCCESS.status())) {
            String userJson = JsonUtils.objectToJson(responseData.getData());
            publisherList = JsonUtils.jsonToList(userJson, CommonUserVO.class);

            if (publisherList == null) {
                CustomExceptionFactory.onException(ResponseStatusEnum.SYSTEM_ERROR);
            }
        } else {
            CustomExceptionFactory.onException(ResponseStatusEnum.SYSTEM_ERROR);
        }

        return publisherList;
    }

    private CommonUserVO remoteQueryPublisherInfo(String publisherId){
        return remoteQueryPublisherInfos(Set.of(publisherId)).get(0);
    }
}
