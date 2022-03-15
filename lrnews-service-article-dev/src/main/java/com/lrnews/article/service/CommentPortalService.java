package com.lrnews.article.service;

import com.lrnews.pojo.Article;
import com.lrnews.vo.ArticleDetailVO;
import com.lrnews.vo.PagedGridVO;

import java.util.List;

public interface CommentPortalService {
    void addComment(String articleId, String fatherCommentId, String content, String userId, String nickname, String face);
}
