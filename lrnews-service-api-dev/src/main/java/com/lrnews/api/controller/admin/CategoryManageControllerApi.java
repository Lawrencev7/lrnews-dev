package com.lrnews.api.controller.admin;

import com.lrnews.bo.CategoryBO;
import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "Admin category management controller", tags = {"Category", "Controller"})
@RequestMapping("/categoryMng")
public interface CategoryManageControllerApi {

    @ApiOperation(value = "Add new category", notes = "Add new category")
    @PostMapping("/addNewCategory")
    JsonResultObject addNewCategory(@RequestBody CategoryBO categoryBO, HttpServletRequest request, HttpServletResponse response);

    @ApiOperation(value = "Update category", notes = "Update existed category infos")
    @PostMapping("/updateCategory")
    JsonResultObject updateCategory(@RequestBody CategoryBO categoryBO,
                                    HttpServletRequest request, HttpServletResponse response);

    @ApiOperation(value = "Remove category", notes = "Remove a exist category")
    @PostMapping("/removeCategory")
    JsonResultObject removeCategory(@RequestBody CategoryBO categoryBO);

    @ApiOperation(value = "Query category list", notes = "This interface provide for admin to query all category")
    @PostMapping("/queryAllCategory")
    JsonResultObject queryAllCategory();

    @ApiOperation(value = "User query category list", notes = "This interface provide for user to query all category")
    @GetMapping("/getAllCategory")
    JsonResultObject getAllCategory();
}