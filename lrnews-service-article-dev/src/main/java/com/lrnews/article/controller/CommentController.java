package com.lrnews.article.controller;

import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.article.CommentControllerApi;
import com.lrnews.article.service.ArticlePortalService;
import com.lrnews.article.service.CommentPortalService;
import com.lrnews.bo.CommentReplyBO;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.vo.CommonUserVO;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController extends BaseController implements CommentControllerApi {

    final ArticlePortalService articlePortalService;

    final CommentPortalService commentPortalService;

    public CommentController(ArticlePortalService articlePortalService, CommentPortalService commentPortalService) {
        this.articlePortalService = articlePortalService;
        this.commentPortalService = commentPortalService;
    }


    @Override
    public JsonResultObject addComment(CommentReplyBO commentReplyBO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResultObject.errorMap(getErrors(bindingResult));
        }

        CommonUserVO user = remoteQueryUserInfo(commentReplyBO.getCommentUserId());

        commentPortalService.addComment(commentReplyBO.getArticleId(), commentReplyBO.getFatherId(), commentReplyBO.getContent(),
                user.getId(), user.getNickname(), user.getFace());

        return JsonResultObject.ok();
    }
}
