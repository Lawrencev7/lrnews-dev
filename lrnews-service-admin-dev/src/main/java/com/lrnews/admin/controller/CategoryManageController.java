package com.lrnews.admin.controller;

import com.lrnews.admin.service.CategoryService;
import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.admin.CategoryManageControllerApi;
import com.lrnews.bo.CategoryBO;
import com.lrnews.dbm.CategoryDBModel;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.lrnews.values.CommonApiDefStrings.COOKIE_ADMIN_NAME;

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
        return JsonResultObject.ok("Updated");
    }

    @Override
    public JsonResultObject removeCategory(CategoryBO categoryBO) {
        CategoryDBModel model = new CategoryDBModel();
        BeanUtils.copyProperties(categoryBO, model);
        categoryService.removeCategory(model);
        return JsonResultObject.ok("Deleted.");
    }

    @Override
    public JsonResultObject queryAllCategory() {
        return JsonResultObject.ok(categoryService.queryCategoryList());
    }
}
