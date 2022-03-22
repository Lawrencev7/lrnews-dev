package com.lrnews.article.service;

import com.lrnews.vo.PagedGridVO;

public interface CommentPortalService {
    void addComment(String articleId, String fatherCommentId, String content,
                    String userId, String nickname, String face);

    Integer countComments(String articleId);

    PagedGridVO listAllComment(String articleID, Integer page, Integer pageSize);

    PagedGridVO queryCommentsByWriterID(String writerId, Integer page, Integer pageSize);

    void deleteComment(String writerId, String commentId);
}
