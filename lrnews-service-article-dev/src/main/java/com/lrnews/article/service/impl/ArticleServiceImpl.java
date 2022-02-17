package com.lrnews.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.lrnews.article.mapper.ArticleMapper;
import com.lrnews.article.mapper.CustomMapper;
import com.lrnews.article.service.ArticleService;
import com.lrnews.bo.ArticleBO;
import com.lrnews.bo.ArticleQueryBO;
import com.lrnews.enums.ArticleAppointType;
import com.lrnews.enums.ArticleReviewLevel;
import com.lrnews.enums.ArticleReviewStatus;
import com.lrnews.enums.YesOrNo;
import com.lrnews.exception.CustomExceptionFactory;
import com.lrnews.exception.LrCustomException;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.Article;
import com.lrnews.pojo.Category;
import com.lrnews.utils.RandomStringName;
import com.lrnews.utils.TextReviewUtil;
import com.lrnews.vo.PagedGridVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ArticleServiceImpl implements ArticleService {

    final ArticleMapper articleMapper;

    final CustomMapper customMapper;

    final TextReviewUtil reviewUtil;

    public ArticleServiceImpl(ArticleMapper articleMapper, CustomMapper customMapper, TextReviewUtil reviewUtil) {
        this.articleMapper = articleMapper;
        this.customMapper = customMapper;
        this.reviewUtil = reviewUtil;
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
        if (Objects.equals(article.getIsAppoint(), ArticleAppointType.TIMING.type)) {
            article.setPublishTime(articleBO.getPublishTime());
        } else {
            article.setPublishTime(new Date());
        }

        // Defect: can not get category id at article controller.
        int res = articleMapper.insert(article);
        if (res != 1) {
            CustomExceptionFactory.onException(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }

        Integer reviewLevel = reviewUtil.review(article.getContent());
        if (Objects.equals(reviewLevel, ArticleReviewLevel.PASS.type)) {
            updateArticleStatus(article.getId(), ArticleReviewStatus.SUCCESS.type);
        } else if (Objects.equals(reviewLevel, ArticleReviewLevel.REVIEW.type)) {
            updateArticleStatus(article.getId(), ArticleReviewStatus.WAITING_MANUAL.type);
        } else {
            updateArticleStatus(article.getId(), ArticleReviewStatus.FAILED.type);
        }

    }

    @Override
    @Transactional
    public Integer publishAppointedArticle() {
        // DevTask: Avoid full table scan.
        return customMapper.updateStatusToPublish();
    }

    @Override
    public PagedGridVO queryArticleList(ArticleQueryBO query) {
        Example example = createExampleForArticleQuery(query);
        PageHelper.startPage(query.getPage(), query.getPageSize());
        List<Article> articles = articleMapper.selectByExample(example);

        return PagedGridVO.getPagedGrid(articles, query.getPage());
    }

    private Example createExampleForArticleQuery(ArticleQueryBO query) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("publishUserId", query.getUserId());
        if (StringUtils.isNotBlank(query.getKeyword())) {
            criteria.andLike("title", query.getKeyword());
        }

        if (!query.getStatus().equals("0")) {
            criteria.andEqualTo("articleStatus", query.getStatus());
        }

        // For user, do not show deleted article
        criteria.andEqualTo("isDelete", YesOrNo.NO);

        if (query.getStartDate() != null) {
            criteria.andGreaterThanOrEqualTo("publishTime", query.getStartDate());
        }

        if (query.getEndDate() != null) {
            criteria.andLessThanOrEqualTo("publishTime", query.getEndDate());
        }

        return example;
    }

    @Override
    public PagedGridVO queryAllArticles(Integer status, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("articleStatus", status);

        PageHelper.startPage(page, pageSize);
        List<Article> result = articleMapper.selectByExample(example);

        return PagedGridVO.getPagedGrid(result, page);
    }

    @Override
    public void updateArticleStatus(String articleId, Integer targetStatus) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", articleId);

        Article targetArticle = new Article();
        targetArticle.setArticleStatus(targetStatus);

        articleMapper.updateByExampleSelective(targetArticle, example);
    }

    @Override
    public void deleteArticle(String articleId, String userId) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", articleId);
        criteria.andEqualTo("publishUserId", userId);

        Article article = new Article();
        article.setIsDelete(YesOrNo.YES.type);

        int res = articleMapper.updateByExampleSelective(article, example);
        if (res != 1) {
            CustomExceptionFactory.onException(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
        }
    }

    @Override
    public void withdrawArticle(String articleId, String userId) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", articleId);
        criteria.andEqualTo("publishUserId", userId);

        Article pending = new Article();
        pending.setArticleStatus(ArticleReviewStatus.WITHDRAW.type);

        int res = articleMapper.updateByExampleSelective(pending, example);
        if (res != 1) {
            CustomExceptionFactory.onException(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
        }
    }
}
