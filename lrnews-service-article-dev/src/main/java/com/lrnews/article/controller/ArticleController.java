package com.lrnews.article.controller;

import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.article.ArticleControllerApi;
import com.lrnews.article.service.ArticleService;
import com.lrnews.bo.ArticleBO;
import com.lrnews.bo.ArticleQueryBO;
import com.lrnews.enums.ArticleCoverType;
import com.lrnews.enums.ArticleReviewStatus;
import com.lrnews.enums.YesOrNo;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.Category;
import com.lrnews.utils.JsonUtils;
import com.lrnews.vo.PagedGridVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.lrnews.values.CommonRedisKeySet.REDIS_CATEGORY_KEY;

@RestController
public class ArticleController extends BaseController implements ArticleControllerApi {

    final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public JsonResultObject publish(ArticleBO articleBO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = getErrors(bindingResult);
            return JsonResultObject.errorMap(errorMap);
        }

        // Set article cover depending on user selection
        if (Objects.equals(articleBO.getArticleType(), ArticleCoverType.ONE_IMAGE.type)) {
            if (StringUtils.isBlank(articleBO.getArticleCover())) {
                return JsonResultObject.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
            }
        } else if (Objects.equals(articleBO.getArticleType(), ArticleCoverType.WORDS.type)) {
            articleBO.setArticleCover("");
        }

        // Check category
        String valueString = redis.get(REDIS_CATEGORY_KEY);
        String categoryName = articleBO.getCategoryName();
        if (StringUtils.isBlank(valueString)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

        List<Category> list = JsonUtils.jsonToList(valueString, Category.class);
        Category category = null;
        if (list != null) {
            for (Category c : list) {
                if (c.getCategoryName().equals(categoryName)) {
                    category = c;
                    break;
                }

            }
        } else {
            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

        if (category == null) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ARTICLE_CATEGORY_NOT_EXIST_ERROR);
        }

        articleService.createArticle(articleBO, category);


        return JsonResultObject.ok();
    }

    @Override
    public JsonResultObject queryMyArticles(ArticleQueryBO query, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = getErrors(bindingResult);
            return JsonResultObject.errorMap(errorMap);
        }

        if (query.getPage() == null) query.setPage(DEFAULT_PAGE);
        if (query.getPageSize() == null) query.setPageSize(DEFAULT_PAGE_SIZE);


        PagedGridVO pagedGridVO = articleService.queryArticleList(query);
        return JsonResultObject.ok(pagedGridVO);
    }

    @Override
    public JsonResultObject queryAllArticles(Integer status, Integer page, Integer pageSize) {
        if (status == null) return JsonResultObject.errorCustom(ResponseStatusEnum.ILLEGAL_ARGUMENT);

        if (page == null) page = DEFAULT_PAGE;
        if (pageSize == null) pageSize = DEFAULT_PAGE_SIZE;

        return JsonResultObject.ok(articleService.queryAllArticles(status, page, pageSize));
    }

    @Override
    public JsonResultObject doReview(String articleId, Integer reviewResult) {
        Integer status;
        if (Objects.equals(reviewResult, YesOrNo.YES.type)) {
            status = ArticleReviewStatus.SUCCESS.type;
        } else if (Objects.equals(reviewResult, YesOrNo.NO.type)) {
            status = ArticleReviewStatus.FAILED.type;
        } else {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ILLEGAL_ARGUMENT);
        }

        articleService.updateArticleStatus(articleId, status);
        return JsonResultObject.ok();
    }

    @Override
    public JsonResultObject deleteArticle(String userId, String articleId) {
        if (StringUtils.isBlank(userId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.USER_NOT_LOGIN);
        }

        if (StringUtils.isBlank(articleId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ILLEGAL_ARGUMENT);
        }

        articleService.deleteArticle(userId, articleId);
        return JsonResultObject.ok();
    }

    @Override
    public JsonResultObject withdraw(String userId, String articleId) {
        if (StringUtils.isBlank(userId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.USER_NOT_LOGIN);
        }

        if (StringUtils.isBlank(articleId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ILLEGAL_ARGUMENT);
        }

        articleService.withdrawArticle(userId, articleId);
        return JsonResultObject.ok();
    }
}
