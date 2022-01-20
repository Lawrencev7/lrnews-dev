package com.lrnews.admin.controller;

import com.lrnews.admin.service.CategoryService;
import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.admin.CategoryManageControllerApi;
import com.lrnews.bo.CategoryBO;
import com.lrnews.dbm.CategoryDBModel;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.lrnews.values.CommonApiDefStrings.COOKIE_ADMIN_NAME;
import static com.lrnews.values.CommonValueStrings.REDIS_CATEGORY_KEY;

@RestController
public class CategoryManageController extends BaseController implements CategoryManageControllerApi {
    public static final Logger logger = LoggerFactory.getLogger(CategoryManageController.class);

    private final CategoryService categoryService;

    public CategoryManageController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public JsonResultObject addNewCategory(CategoryBO categoryBO,
                                           HttpServletRequest request, HttpServletResponse response) {
        CategoryDBModel model = new CategoryDBModel();
        BeanUtils.copyProperties(categoryBO, model);
        if (categoryService.isExisted(model)) {
            logger.info("Category with name {} is existed", categoryBO.getCategoryName());
            return JsonResultObject.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
        } else {
            model.setCreateTime(new Date());
            model.setUpdateTime(new Date());
            // If enable the interceptor, admin user should be already login here
            // For test env, interceptor is disabled, this field should be null or blank string.
            model.setCreator(request.getHeader(COOKIE_ADMIN_NAME));
            deleteRedisCategoryCache();
            categoryService.addNewCategory(model);
            return JsonResultObject.ok("Saved");
        }
    }

    @Override
    public JsonResultObject updateCategory(CategoryBO categoryBO,
                                           HttpServletRequest request, HttpServletResponse response) {
        CategoryDBModel model = new CategoryDBModel();
        BeanUtils.copyProperties(categoryBO, model);
        model.setUpdateTime(new Date());
        model.setCreator(request.getHeader(COOKIE_ADMIN_NAME));
        categoryService.updateCategory(model);
        deleteRedisCategoryCache();
        return JsonResultObject.ok("Updated");
    }

    @Override
    public JsonResultObject removeCategory(CategoryBO categoryBO) {
        CategoryDBModel model = new CategoryDBModel();
        BeanUtils.copyProperties(categoryBO, model);
        categoryService.removeCategory(model);
        deleteRedisCategoryCache();
        return JsonResultObject.ok("Deleted.");
    }

    @Override
    public JsonResultObject queryAllCategory() {
        return JsonResultObject.ok(categoryService.queryCategoryList());
    }

    @Override
    public JsonResultObject getAllCategory() {
        List<CategoryBO> categoryList = new ArrayList<>();
        String valueString = redis.get(REDIS_CATEGORY_KEY);
        if (StringUtils.isBlank(valueString)) {
            List<CategoryBO> finalCategoryList = categoryList;
            categoryService.queryCategoryList().forEach(model -> {
                CategoryBO bo = new CategoryBO();
                BeanUtils.copyProperties(model, bo);
                finalCategoryList.add(bo);
            });
            redis.set(REDIS_CATEGORY_KEY, JsonUtils.objectToJson(categoryList));
        } else {
            categoryList = JsonUtils.jsonToList(valueString, CategoryBO.class);
        }

        return JsonResultObject.ok(categoryList);
    }
}
