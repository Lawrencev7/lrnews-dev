package com.lrnews.article.controller;

import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.article.CommentControllerApi;
import com.lrnews.article.service.ArticlePortalService;
import com.lrnews.article.service.CommentPortalService;
import com.lrnews.bo.CommentReplyBO;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.vo.CommonUserVO;
import com.lrnews.vo.PagedGridVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import static com.lrnews.values.CommonRedisKeySet.REDIS_ARTICLE_READ_COUNT_KEY;

@RestController
public class CommentController extends BaseController implements CommentControllerApi {

    final ArticlePortalService articlePortalService;

    final CommentPortalService commentPortalService;

    public CommentController(ArticlePortalService articlePortalService, CommentPortalService commentPortalService) {
        this.articlePortalService = articlePortalService;
        this.commentPortalService = commentPortalService;
    }


    @Override
    public JsonResultObject addComment(CommentReplyBO commentReplyBO) {

        CommonUserVO user = remoteQueryUserInfo(commentReplyBO.getCommentUserId());

        commentPortalService.addComment(commentReplyBO.getArticleId(), commentReplyBO.getFatherId(), commentReplyBO.getContent(),
                user.getId(), user.getNickname(), user.getFace());

        return JsonResultObject.ok();
    }

    @Override
    public JsonResultObject queryCommentCount(String articleId) {
        if (StringUtils.isBlank(articleId))
            return JsonResultObject.errorCustom(ResponseStatusEnum.ILLEGAL_ARGUMENT);

        if (!redis.keyExist(REDIS_ARTICLE_READ_COUNT_KEY + articleId)) {
            Integer counts = commentPortalService.countComments(articleId);
            redis.set(REDIS_ARTICLE_READ_COUNT_KEY + articleId, String.valueOf(counts));
            return JsonResultObject.ok(counts);
        } else {
            return JsonResultObject.ok(redis.get(REDIS_ARTICLE_READ_COUNT_KEY + articleId));
        }
    }

    @Override
    public JsonResultObject listAllComment(String articleId, Integer page, Integer pageSize) {
        if (StringUtils.isBlank(articleId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ILLEGAL_ARGUMENT);
        }

        if (page == null) page = DEFAULT_PAGE;
        if (pageSize == null) pageSize = DEFAULT_PAGE_SIZE;

        PagedGridVO allComment = commentPortalService.listAllComment(articleId, page, pageSize);

        return JsonResultObject.ok(allComment);
    }

    @Override
    public JsonResultObject queryCommentOfMyArticle(String writerId, Integer page, Integer pageSize) {
        if (StringUtils.isBlank(writerId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ILLEGAL_ARGUMENT);
        }

        if (page == null) page = DEFAULT_PAGE;
        if (pageSize == null) pageSize = DEFAULT_PAGE_SIZE;

        PagedGridVO res = commentPortalService.queryCommentsByWriterID(writerId, page, pageSize);

        return JsonResultObject.ok(res);
    }

    @Override
    public JsonResultObject deleteComment(String writerId, String commentId) {
        if (StringUtils.isBlank(writerId) || StringUtils.isBlank(commentId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ILLEGAL_ARGUMENT);
        }
        commentPortalService.deleteComment(writerId, commentId);
        return JsonResultObject.ok();
    }
}
