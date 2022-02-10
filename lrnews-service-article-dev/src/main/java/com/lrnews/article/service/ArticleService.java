package com.lrnews.article.service;

import com.lrnews.bo.ArticleBO;
import com.lrnews.pojo.Category;

public interface ArticleService {
    void createArticle(ArticleBO articleBO, Category category);
}
