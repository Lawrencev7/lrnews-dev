package com.lrnews.article.controller;

import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.article.ArticleControllerApi;
import com.lrnews.article.service.ArticleService;
import com.lrnews.bo.ArticleBO;
import com.lrnews.enums.ArticleCoverType;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.Category;
import com.lrnews.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.lrnews.values.CommonValueStrings.REDIS_CATEGORY_KEY;

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
                if(c.getCategoryName().equals(categoryName)){
                    category = c;
                    break;
                }

            }
        }else {
            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

        if (category == null) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ARTICLE_CATEGORY_NOT_EXIST_ERROR);
        }

        articleService.createArticle(articleBO, category);


        return JsonResultObject.ok();
    }
}
