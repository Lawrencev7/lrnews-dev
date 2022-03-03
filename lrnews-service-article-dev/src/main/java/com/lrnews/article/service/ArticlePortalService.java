package com.lrnews.article.service;

import com.lrnews.pojo.Article;
import com.lrnews.vo.PagedGridVO;

import java.util.List;

public interface ArticlePortalService {
    PagedGridVO queryAllArticles(String keyword, String category, Integer page, Integer pageSize);

    List<Article> queryTopReadArticleList();

    PagedGridVO queryArticleForWriter(String userId, Integer page, Integer pageSize);
}
