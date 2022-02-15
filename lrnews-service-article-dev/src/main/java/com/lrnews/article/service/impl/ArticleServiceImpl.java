package com.lrnews.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.lrnews.article.mapper.ArticleMapper;
import com.lrnews.article.mapper.CustomMapper;
import com.lrnews.article.service.ArticleService;
import com.lrnews.bo.ArticleBO;
import com.lrnews.bo.ArticleQueryBO;
import com.lrnews.enums.ArticleAppointType;
import com.lrnews.enums.ArticleReviewStatus;
import com.lrnews.enums.YesOrNo;
import com.lrnews.exception.CustomExceptionFactory;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.Article;
import com.lrnews.pojo.Category;
import com.lrnews.utils.RandomStringName;
import com.lrnews.vo.PagedGridVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ArticleServiceImpl implements ArticleService {

    final ArticleMapper articleMapper;

    final CustomMapper customMapper;

    public ArticleServiceImpl(ArticleMapper articleMapper, CustomMapper customMapper) {
        this.articleMapper = articleMapper;
        this.customMapper = customMapper;
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

    @Override
    public Integer publishAppointedArticle() {
        // DevTask: Avoid full table scan.
        return customMapper.updateStatusToPublish();
    }

    @Override
    public PagedGridVO queryArticleList(ArticleQueryBO query) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        fillCriteriaForArticleQuery(criteria, query);
        PageHelper.startPage(query.getPage(), query.getPageSize());

        List<Article> articles = articleMapper.selectByExample(example);

        return PagedGridVO.getPagedGrid(articles, query.getPage());
    }

    private void fillCriteriaForArticleQuery(Example.Criteria criteria, ArticleQueryBO query){
        criteria.andEqualTo("publishUserId", query.getUserId());
        if(StringUtils.isNotBlank(query.getKeyword())) {
            criteria.andLike("title", query.getKeyword());
        }

        if(!query.getStatus().equals("0")){
            criteria.andEqualTo("articleStatus", query.getStatus());
        }

        // For user, do not show deleted article
        criteria.andEqualTo("isDelete", YesOrNo.NO);

        if(query.getStartDate() != null){
            criteria.andGreaterThanOrEqualTo("publishTime", query.getStartDate());
        }

        if(query.getEndDate() != null){
            criteria.andLessThanOrEqualTo("publishTime", query.getEndDate());
        }
    }
}
