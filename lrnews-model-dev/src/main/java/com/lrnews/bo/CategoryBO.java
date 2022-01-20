package com.lrnews.bo;

import javax.validation.constraints.NotBlank;

public class CategoryBO {
    @NotBlank
    private String categoryName;

    @NotBlank
    private String colorRGB;

    private String comment;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getColorRGB() {
        return colorRGB;
    }

    public void setColorRGB(String colorRGB) {
        this.colorRGB = colorRGB;
    }
}
