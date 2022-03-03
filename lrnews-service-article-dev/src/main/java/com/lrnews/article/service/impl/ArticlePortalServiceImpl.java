package com.lrnews.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.lrnews.article.mapper.ArticleMapper;
import com.lrnews.article.mapper.CustomMapper;
import com.lrnews.article.service.ArticlePortalService;
import com.lrnews.enums.ArticleReviewStatus;
import com.lrnews.enums.YesOrNo;
import com.lrnews.pojo.Article;
import com.lrnews.utils.TextReviewUtil;
import com.lrnews.vo.PagedGridVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;

@Service
public class ArticlePortalServiceImpl implements ArticlePortalService {

    final ArticleMapper articleMapper;

    final CustomMapper customMapper;

    final TextReviewUtil reviewUtil;

    public ArticlePortalServiceImpl(ArticleMapper articleMapper, CustomMapper customMapper, TextReviewUtil reviewUtil) {
        this.articleMapper = articleMapper;
        this.customMapper = customMapper;
        this.reviewUtil = reviewUtil;
    }

    @Override
    public PagedGridVO queryAllArticles(String keyword, String category, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("publishTime").desc();
        Example.Criteria criteria = example.createCriteria();

        setPotentialLimit(criteria);

        // Parameter query limit
        if (StringUtils.isNotBlank(keyword))
            criteria.andLike("title", "%" + keyword + "%");

        if (Objects.nonNull(category))
            criteria.andEqualTo("categoryId", category);

        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.selectByExample(example);

        return PagedGridVO.getPagedGrid(list, page);
    }

    /**
     * Set potential limit to query articles that has been published. All published article must be:
     * 1.Not Appointed
     * 2.Not logical deleted
     * 3.Passed the review
     */
    private void setPotentialLimit(Example.Criteria criteria) {
        // Potential query limit
        // 1. is not appointed
        criteria.andEqualTo("isAppoint", YesOrNo.NO.type);
        // 2. is not deleted
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);
        // 3. is review-passed
        criteria.andEqualTo("articleStatus", ArticleReviewStatus.SUCCESS.type);
    }

    @Override
    public List<Article> queryTopReadArticleList() {
        Example example = new Example(Article.class);
        example.orderBy("readCounts").desc();
        Example.Criteria criteria = example.createCriteria();

        setPotentialLimit(criteria);

        PageHelper.startPage(1, 5);
        return articleMapper.selectByExample(example);
    }

    @Override
    public PagedGridVO queryArticleForWriter(String userId, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("publishTime").desc();
        Example.Criteria criteria = example.createCriteria();
        setPotentialLimit(criteria);

        PageHelper.startPage(page, pageSize);
        List<Article> articles = articleMapper.selectByExample(example);
        return PagedGridVO.getPagedGrid(articles, page);
    }
}
