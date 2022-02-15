package com.lrnews.article.mapper;

import com.lrnews.api.common.MyMapper;
import com.lrnews.pojo.Article;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomMapper extends MyMapper<Article> {
    int updateStatusToPublish();
}