package com.lrnews.article.service;

import com.lrnews.vo.PagedGridVO;

public interface ArticlePortalService {
    PagedGridVO queryAllArticles(String keyword, String category, Integer page, Integer pageSize);
}
