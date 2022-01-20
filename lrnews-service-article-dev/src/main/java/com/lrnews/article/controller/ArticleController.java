package com.lrnews.article.controller;

import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.HelloControllerApi;
import com.lrnews.api.controller.article.ArticleControllerApi;
import com.lrnews.bo.ArticleBO;
import com.lrnews.bo.CategoryBO;
import com.lrnews.enums.ArticleCoverType;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lrnews.values.CommonValueStrings.REDIS_CATEGORY_KEY;

@RestController
public class ArticleController extends BaseController implements ArticleControllerApi {

    @Override
    public JsonResultObject publish(ArticleBO articleBO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = getErrors(bindingResult);
            return JsonResultObject.errorMap(errorMap);
        }

        if(Objects.equals(articleBO.getArticleType(), ArticleCoverType.ONE_IMAGE.type)){
            if(StringUtils.isBlank(articleBO.getArticleCover())){
                return JsonResultObject.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
            }
        }else if (Objects.equals(articleBO.getArticleType(), ArticleCoverType.WORDS.type)){
            articleBO.setArticleCover("");
        }

        String valueString = redis.get(REDIS_CATEGORY_KEY);
        String categoryName = articleBO.getCategoryName();
        if(StringUtils.isBlank(valueString)){
            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }else {
            List<CategoryBO> categoryBOList = JsonUtils.jsonToList(valueString, CategoryBO.class);

            assert categoryBOList != null;
            Set<CategoryBO> collect = categoryBOList.stream().filter(
                    categoryBO -> (categoryBO.getCategoryName().equals(categoryName))
            ).collect(Collectors.toSet());

            if(collect.isEmpty()){
                return JsonResultObject.errorCustom(ResponseStatusEnum.ARTICLE_CATEGORY_NOT_EXIST_ERROR);
            }
        }


        return JsonResultObject.ok();
    }
}
