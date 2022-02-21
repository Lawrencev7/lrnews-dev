package com.lrnews.article.task;

import com.lrnews.article.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
//Do NOT enable schedule task for now
//@EnableScheduling
public class ArticlePublishSchedule {

    private final ArticleService articleService;

    public ArticlePublishSchedule(ArticleService articleService) {
        this.articleService = articleService;
    }

    private static final Logger logger = LoggerFactory.getLogger(ArticlePublishSchedule.class);

    @Scheduled(cron = "0/3 * * * * ?")
    private void publish() {
        Integer affectedRows = articleService.publishAppointedArticle();
        logger.info("==> Scheduled task ====> publish appointed article: {}", affectedRows);
    }

}
