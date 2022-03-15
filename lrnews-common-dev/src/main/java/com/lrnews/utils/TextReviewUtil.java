package com.lrnews.utils;

import com.lrnews.enums.ArticleReviewLevel;
import org.springframework.stereotype.Component;

@Component
public class TextReviewUtil {
    //This interface should be replaced by Ali AI review interface
    public Integer review(String context) {
        if (context.length() > 10000)
            return ArticleReviewLevel.BLOCK.type;
        else if (context.length() < 200)
            return ArticleReviewLevel.PASS.type;
        else
            return ArticleReviewLevel.REVIEW.type;
    }
}
