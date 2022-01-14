package com.lrnews.bo;

import com.lrnews.validate.CheckUrl;

import javax.validation.constraints.NotBlank;

public class LinksBO {
    private String id;

    @NotBlank(message = "Link name is required")
    private String linkName;

    @NotBlank(message = "Link url is required")
    @CheckUrl
    private String linkUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

}
