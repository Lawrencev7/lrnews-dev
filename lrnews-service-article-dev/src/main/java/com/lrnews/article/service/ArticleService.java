package com.lrnews.article.service;

import com.lrnews.bo.ArticleBO;
import com.lrnews.bo.ArticleQueryBO;
import com.lrnews.pojo.Category;
import com.lrnews.vo.PagedGridVO;

public interface ArticleService {
    void createArticle(ArticleBO articleBO, Category category);

    Integer publishAppointedArticle();

    /**
     * Query article list for someone user
     *
     * @return PagedGridVO
     */
    PagedGridVO queryArticleList(ArticleQueryBO query);

    PagedGridVO queryAllArticles(Integer status, Integer page, Integer pageSize);

    /**
     * Update article status to target status
     */
    void updateArticleStatus(String articleId, Integer targetStatus);

    void deleteArticle(String articleId, String userId);

    void withdrawArticle(String articleId, String userId);

    void updateArticleToPublish(String articleId);
}
