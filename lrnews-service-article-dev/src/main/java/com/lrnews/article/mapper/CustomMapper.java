package com.lrnews.article.mapper;

import com.lrnews.api.common.MyMapper;
import com.lrnews.pojo.Article;
import com.lrnews.vo.CommentsVO;
import feign.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CustomMapper extends MyMapper<Article> {
    int updateStatusToPublish();

    List<CommentsVO> queryArticleCommentList(@Param("paramMap") Map<String, Object> paramMap);

    void deleteRelatedComments(@Param("deleteId") String deleteID);

    List<String> queryFatherComments(@Param("commentId") String commentId);
}