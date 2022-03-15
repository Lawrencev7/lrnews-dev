package com.lrnews.bo;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 文章留言的BO
 */
public class CommentReplyBO {

    @NotBlank(message = "Lack of argument")
    private String articleId;

    private String fatherId;

    @NotBlank(message = "Please login to add a comment")
    private String commentUserId;

    @NotBlank(message = "Comment must be not null")
    @Length(max = 100, message = "Comment length out of 100")
    private String content;

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @Override
    public String toString() {
        return "CommentReplyBO{" +
                "articleId='" + articleId + '\'' +
                ", fatherId='" + fatherId + '\'' +
                ", commentUserId='" + commentUserId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}