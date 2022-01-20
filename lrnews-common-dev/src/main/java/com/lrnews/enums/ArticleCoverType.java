package com.lrnews.enums;

/**
 * @Desc: 文章封面类型 枚举
 */
public enum ArticleCoverType {
    ONE_IMAGE(1, "Single Cover"),
    WORDS(2, "Only Characters");

    public final Integer type;
    public final String value;

    ArticleCoverType(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
