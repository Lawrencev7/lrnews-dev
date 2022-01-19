package com.lrnews.admin.service;

import com.lrnews.dbm.CategoryDBModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    void addNewCategory(CategoryDBModel category);

    void removeCategory(CategoryDBModel category);

    void updateCategory(CategoryDBModel category);

    List<CategoryDBModel> queryCategoryList();

    boolean isExisted(CategoryDBModel category);
}
