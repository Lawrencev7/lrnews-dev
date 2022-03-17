package com.lrnews.article.service.impl;

import com.lrnews.article.mapper.CommentsMapper;
import com.lrnews.article.service.ArticlePortalService;
import com.lrnews.article.service.CommentPortalService;
import com.lrnews.pojo.Comments;
import com.lrnews.vo.ArticleDetailVO;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommentPortalServiceImpl implements CommentPortalService {

    final ArticlePortalService articlePortalService;

    final CommentsMapper commentsMapper;

    public CommentPortalServiceImpl(ArticlePortalService articlePortalService, CommentsMapper commentsMapper) {
        this.articlePortalService = articlePortalService;
        this.commentsMapper = commentsMapper;
    }

    @Override
    public void addComment(String articleId, String fatherCommentId, String content, String userId, String nickname, String face) {
        ArticleDetailVO articleDetail = articlePortalService.queryArticleDetail(articleId);
        Comments comments = new Comments();
        comments.setId(Sid.nextShort());
        comments.setWriterId(articleDetail.getPublishUserId());
        comments.setArticleTitle(articleDetail.getTitle());
        comments.setArticleCover(articleDetail.getCover());
        comments.setArticleId(articleId);
        comments.setFatherId(fatherCommentId);
        comments.setCommentUserId(userId);
        comments.setCommentUserFace(face);
        comments.setCommentUserNickname(nickname);
        comments.setContent(content);
        comments.setCreateTime(new Date());

        commentsMapper.insert(comments);
    }

    @Override
    public Integer countComments(String articleId) {
        Comments comments = new Comments();
        comments.setArticleId(articleId);
        return commentsMapper.selectCount(comments);
    }
}
