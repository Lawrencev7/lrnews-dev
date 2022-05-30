package com.lrnews.article.controller;

import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.article.ArticlePortalControllerApi;
import com.lrnews.article.service.ArticlePortalService;
import com.lrnews.enums.YesOrNo;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.Article;
import com.lrnews.utils.IPUtil;
import com.lrnews.vo.ArticleDetailVO;
import com.lrnews.vo.CommonUserVO;
import com.lrnews.vo.DisplayArticleVO;
import com.lrnews.vo.PagedGridVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.lrnews.values.CommonRedisKeySet.REDIS_ARTICLE_READ_COUNT_KEY;
import static com.lrnews.values.CommonRedisKeySet.REDIS_IP_READ_LINK_KEY;

@RestController
public class ArticlePortalController extends BaseController implements ArticlePortalControllerApi {

    final ArticlePortalService articlePortalService;

    public ArticlePortalController(ArticlePortalService service, RestTemplate restTemplate) {
        this.articlePortalService = service;
        this.restOperations = restTemplate;
    }

    @Override
    public JsonResultObject queryArticleList(String keyword, String category, Integer page, Integer pageSize) {
        if (page == null) page = DEFAULT_PAGE;
        if (pageSize == null) pageSize = DEFAULT_PAGE_SIZE;

        PagedGridVO pagedGridVO = articlePortalService.queryAllArticles(keyword, category, page, pageSize);

        // Remote access to user service
        List<Article> rows = (List<Article>) pagedGridVO.getRows();

        Set<String> allPublisher = new HashSet<>();
        List<String> ids = new ArrayList<>();
        rows.forEach(a -> {
            ids.add(a.getId());
            allPublisher.add(a.getPublishUserId());
        });

        List<CommonUserVO> publisherList = remoteQueryUserInfos(allPublisher);
        Map<String, Integer> readCounts = compositeReadCountForArticles(ids);

        List<DisplayArticleVO> resultList = new ArrayList<>();
        rows.forEach(article -> {
            for (CommonUserVO uvo : publisherList) {
                if (uvo.getId().equals(article.getPublishUserId())) {
                    DisplayArticleVO vo = generateDisplayArticleVO(uvo, article);
                    vo.setReadCounts(readCounts.get(vo.getId()));
                    resultList.add(vo);
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
        if (StringUtils.isBlank(articleId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ILLEGAL_ARGUMENT);
        }

        ArticleDetailVO articleDetailVO = articlePortalService.queryArticleDetail(articleId);
        CommonUserVO commonUserVO = remoteQueryPublisherInfo(articleDetailVO.getPublishUserId());

        articleDetailVO.setReadCounts(Integer.valueOf(redis.get(REDIS_ARTICLE_READ_COUNT_KEY + articleId)));
        articleDetailVO.setPublishUserName(commonUserVO.getNickname());

        return JsonResultObject.ok(articleDetailVO);
    }

    @Override
    public JsonResultObject readArticle(String articleId, HttpServletRequest request) {
        String userIP = IPUtil.getRequestIp(request);
        if (!redis.keyExist(userIP + REDIS_IP_READ_LINK_KEY + articleId)) {
            redis.increase(REDIS_ARTICLE_READ_COUNT_KEY + articleId, 1);
        } else {
            redis.set(REDIS_ARTICLE_READ_COUNT_KEY + articleId, "1");
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
        res.setCommentCounts(article.getCommentCounts());
        res.setPublisherNickname(commonUserVO.getNickname());
        res.setPublisherFace(commonUserVO.getFace());
        res.setPublisherId(commonUserVO.getId());
        return res;
    }

    private CommonUserVO remoteQueryPublisherInfo(String publisherId) {
        return remoteQueryUserInfos(Set.of(publisherId)).get(0);
    }

    @Override
    public Integer queryReadCount(String articleId) {
        return Integer.valueOf(redis.get(REDIS_ARTICLE_READ_COUNT_KEY + ":" + articleId));
    }

    /**
     * Query multiple records for articles. Using mget to release pressure on Redis
     *
     * @param ids article id list
     * @return map from article id to read count
     */
    private Map<String, Integer> compositeReadCountForArticles(List<String> ids) {
        List<String> keys = new ArrayList<>();
        ids.forEach(s -> keys.add(REDIS_ARTICLE_READ_COUNT_KEY + s));
        List<String> readCountList = redis.mget(keys);
        return readCountList.stream()
                .collect(Collectors.toMap(key -> ids.get(readCountList.indexOf(key)), Integer::parseInt));
    }
}
