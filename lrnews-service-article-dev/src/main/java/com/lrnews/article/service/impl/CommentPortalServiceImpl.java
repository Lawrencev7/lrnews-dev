package com.lrnews.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.lrnews.article.mapper.CommentsMapper;
import com.lrnews.article.mapper.CustomMapper;
import com.lrnews.article.service.ArticlePortalService;
import com.lrnews.article.service.CommentPortalService;
import com.lrnews.pojo.Comments;
import com.lrnews.vo.ArticleDetailVO;
import com.lrnews.vo.CommentsVO;
import com.lrnews.vo.PagedGridVO;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentPortalServiceImpl implements CommentPortalService {

    final ArticlePortalService articlePortalService;

    final CommentsMapper commentsMapper;

    final CustomMapper customMapper;

    public CommentPortalServiceImpl(ArticlePortalService articlePortalService, CommentsMapper commentsMapper, CustomMapper customMapper) {
        this.articlePortalService = articlePortalService;
        this.commentsMapper = commentsMapper;
        this.customMapper = customMapper;
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

    @Override
    public PagedGridVO listAllComment(String articleID, Integer page, Integer pageSize) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("articleId", articleID);

        PageHelper.startPage(page, pageSize);
        List<CommentsVO> commentList = customMapper.queryArticleCommentList(queryMap);

        return PagedGridVO.getPagedGrid(commentList, page);
    }

    @Override
    public PagedGridVO queryCommentsByWriterID(String writerId, Integer page, Integer pageSize) {
        Comments comments = new Comments();
        comments.setWriterId(writerId);
        PageHelper.startPage(page, pageSize);
        List<Comments> commentList = commentsMapper.select(comments);

        return PagedGridVO.getPagedGrid(commentList, page);
    }

    @Override
    public void deleteComment(String writerId, String commentId) {
        Comments comments = new Comments();
        comments.setId(commentId);
        comments.setWriterId(writerId);

        /* Be care when use this part
        Stack<String> deleteRelation = new Stack<>();
        deleteRelation.push(commentId);
        while (!deleteRelation.isEmpty()){
            String toDel = deleteRelation.pop();
            List<String> related = customMapper.queryFatherComments(toDel);
            deleteRelation.addAll(related);
            customMapper.deleteRelatedComments(toDel);
        }
        */

        commentsMapper.delete(comments);
    }
}
