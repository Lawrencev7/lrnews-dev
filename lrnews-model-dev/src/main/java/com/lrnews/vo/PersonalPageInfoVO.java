package com.lrnews.vo;

import com.lrnews.pojo.Article;

import java.util.List;

public class PersonalPageInfoVO {
    private String nickname;

    private Integer subscriber;

    private Integer follows;

    private Boolean isFollowedByMe;

    private List<Article> articleList;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Integer subscriber) {
        this.subscriber = subscriber;
    }

    public Integer getFollows() {
        return follows;
    }

    public void setFollows(Integer follows) {
        this.follows = follows;
    }

    public Boolean getIsFollowedByMe() {
        return isFollowedByMe;
    }

    public void setIsFollowedByMe(Boolean isFollowedByMe) {
        this.isFollowedByMe = isFollowedByMe;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}
