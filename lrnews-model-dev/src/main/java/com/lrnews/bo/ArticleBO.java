package com.lrnews.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class ArticleBO {

    @NotBlank(message = "Please fill article title")
    @Length(max = 30, message = "Article title length too long, must less than 30 characters")
    private String title;

    @NotBlank(message = "Please fill article content")
    @Length(max = 9999, message = "Article content too long, must less than 10000 characters")
    private String content;

    @NotNull(message = "Please select category")
    private String categoryName;

    @NotNull(message = "Please select type of article cover")
    @Min(value = 1, message = "Please select correct type of article cover, 1 or 2")
    @Max(value = 2, message = "Please select correct type of article cover, 1 or 2")
    private Integer articleType;
    private String articleCover;

    @NotNull(message = "Please assign the publish type")
    @Min(value = 0, message = "Please assign the publish type, 0-not appointed, 1-appointed")
    @Max(value = 1, message = "Please assign the publish type, 0-not appointed, 1-appointed")
    private Integer isAppoint;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss") // 前端日期字符串传到后端后，转换为Date类型
    private Date publishTime;

    @NotBlank(message = "Please login first")
    private String publishUserId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getArticleType() {
        return articleType;
    }

    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    public String getArticleCover() {
        return articleCover;
    }

    public void setArticleCover(String articleCover) {
        this.articleCover = articleCover;
    }

    public Integer getIsAppoint() {
        return isAppoint;
    }

    public void setIsAppoint(Integer isAppoint) {
        this.isAppoint = isAppoint;
    }

    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "NewArticleBO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", categoryId=" + categoryName +
                ", articleType=" + articleType +
                ", articleCover='" + articleCover + '\'' +
                ", isAppoint=" + isAppoint +
                ", publishTime=" + publishTime +
                ", publishUserId='" + publishUserId + '\'' +
                '}';
    }
}
