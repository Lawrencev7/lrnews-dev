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

        // Potential query limit
        // 1. is not appointed
        criteria.andEqualTo("isAppoint", YesOrNo.NO.type);
        // 2. is not deleted
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);
        // 3. is review-passed
        criteria.andEqualTo("articleStatus", ArticleReviewStatus.SUCCESS.type);

        // Parameter query limit
        if (StringUtils.isNotBlank(keyword))
            criteria.andLike("title", "%" + keyword + "%");

        if (Objects.nonNull(category))
            criteria.andEqualTo("categoryId", category);

        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.selectByExample(example);

        return PagedGridVO.getPagedGrid(list, page);
    }
}
