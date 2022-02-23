package com.lrnews.article.controller;

import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.article.ArticlePortalControllerApi;
import com.lrnews.article.service.ArticlePortalService;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.Article;
import com.lrnews.utils.JsonUtils;
import com.lrnews.vo.CommonUserVO;
import com.lrnews.vo.DisplayArticleVO;
import com.lrnews.vo.PagedGridVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
public class ArticlePortalController extends BaseController implements ArticlePortalControllerApi {

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

        String userServerUrl = "http://localhost:8003/user/queryUserByIds?userIds=" + JsonUtils.objectToJson(allPublisher);
        ResponseEntity<JsonResultObject> entity = restTemplate.getForEntity(userServerUrl, JsonResultObject.class);
        JsonResultObject responseData = entity.getBody();

        if (!Objects.nonNull(responseData))
            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);

        List<CommonUserVO> publisherList;
        if (responseData.getStatus().equals(ResponseStatusEnum.SUCCESS.status())) {
            String userJson = JsonUtils.objectToJson(responseData.getData());
            publisherList = JsonUtils.jsonToList(userJson, CommonUserVO.class);

            if (publisherList == null) {
                return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }
        } else {

            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

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

    private static DisplayArticleVO generateDisplayArticleVO(CommonUserVO commonUserVO, Article article) {
        DisplayArticleVO res = new DisplayArticleVO();
        res.setArticleCover(article.getArticleCover());
        res.setId(article.getId());
        res.setContent(article.getContent());
        res.setPublishTime(article.getPublishTime());
        res.setCategoryId(article.getCategoryId());
        res.setReadCounts(article.getReadCounts());
        res.setCommentCounts(article.getCommentCounts());
        res.setPublisherNickname(commonUserVO.getNickname());
        res.setPublisherFace(commonUserVO.getFace());
        res.setPublisherId(commonUserVO.getId());
        return res;
    }
}
