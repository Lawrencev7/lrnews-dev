package com.lrnews.article.fs.controller;

import com.lrnews.api.controller.article.fs.ArticleFSControllerApi;
import com.mongodb.client.gridfs.GridFSBucket;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

@Controller
public class ArticleFSController implements ArticleFSControllerApi {

    private final GridFSBucket gridFSBucket;

    @Value("${freemarker.article.target}")
    public String articlePath;

    public ArticleFSController(GridFSBucket gridFSBucket) {
        this.gridFSBucket = gridFSBucket;
    }

    @Override
    public Integer download(String articleId, String articleMongoId) throws FileNotFoundException {
        OutputStream outputStream = new FileOutputStream(filePath(articleId));
        gridFSBucket.downloadToStream(new ObjectId(articleMongoId), outputStream);

        return HttpStatus.OK.value();
    }

    @Override
    public Integer delete(String articleId) {
        return new File(filePath(articleId)).delete() ? HttpStatus.OK.value() : HttpStatus.EXPECTATION_FAILED.value();
    }

    private String filePath(String articleId){
        return articlePath + File.separator + articleId + ".html";
    }
}
