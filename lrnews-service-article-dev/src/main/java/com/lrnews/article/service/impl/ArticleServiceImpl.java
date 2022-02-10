package com.lrnews.article.service.impl;

import com.lrnews.article.mapper.ArticleMapper;
import com.lrnews.article.service.ArticleService;
import com.lrnews.bo.ArticleBO;
import com.lrnews.enums.ArticleAppointType;
import com.lrnews.enums.ArticleReviewStatus;
import com.lrnews.enums.YesOrNo;
import com.lrnews.exception.CustomExceptionFactory;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.Article;
import com.lrnews.pojo.Category;
import com.lrnews.utils.RandomStringName;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class ArticleServiceImpl implements ArticleService {

    final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @Override
    public void createArticle(ArticleBO articleBO, Category category) {
        Article article = new Article();
        BeanUtils.copyProperties(articleBO, article);

        article.setId(RandomStringName.getRandomArticleId());
        article.setCategoryId(category.getId());
        article.setArticleStatus(ArticleReviewStatus.REVIEWING.type);
        article.setReadCounts(0);
        article.setIsDelete(YesOrNo.NO.type);
        if(Objects.equals(article.getIsAppoint(), ArticleAppointType.TIMING.type)){
            article.setPublishTime(articleBO.getPublishTime());
        } else {
            article.setPublishTime(new Date());
        }

        // Defect: can not get category id at article controller.
        int res = articleMapper.insert(article);
        if(res != 1){
            CustomExceptionFactory.onException(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }
    }
}
