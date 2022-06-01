package com.lrnews.article.fs.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.function.Consumer;

@Component
public class ControllerKeeper {
    private final ArticleFSController controller;

    public ControllerKeeper(ArticleFSController controller) {
        this.controller = controller;
    }

    public void download(String hyperMsg){
        try {
            String articleId = hyperMsg.split(",")[0];
            String articleMongoId = hyperMsg.split(",")[1];
            controller.download(articleId, articleMongoId);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void delete(String articleId){
        controller.delete(articleId);
    }

    @Bean
    public Consumer<String> download(){
        return this::download;
    }

    @Bean
    public Consumer<String> delete(){
        return this::delete;
    }

}
