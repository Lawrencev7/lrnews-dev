package com.lrnews.vo;

import java.util.Date;

// This object displays information about an article on a homepage
// including article common info and user nickname, avatar, etc.
public class DisplayArticleVO {

    private String id;
    private String title;
    private Integer categoryId;
    private String articleCover;
    private Date publishTime;
    private Integer readCounts;
    private Integer commentCounts;
    private String content;

    private String publisherId;
    private String publisherNickname;
    private String publisherFace;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getArticleCover() {
        return articleCover;
    }

    public void setArticleCover(String articleCover) {
        this.articleCover = articleCover;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getReadCounts() {
        return readCounts;
    }

    public void setReadCounts(Integer readCounts) {
        this.readCounts = readCounts;
    }

    public Integer getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(Integer commentCounts) {
        this.commentCounts = commentCounts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisherNickname() {
        return publisherNickname;
    }

    public void setPublisherNickname(String publisherNickname) {
        this.publisherNickname = publisherNickname;
    }

    public String getPublisherFace() {
        return publisherFace;
    }

    public void setPublisherFace(String publisherFace) {
        this.publisherFace = publisherFace;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }
}
