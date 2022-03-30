package com.lrnews.article.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequestMapping("/fmc")
@Controller // Do not use rest controller
public class FreeMarkerController {

    @Value("${freemarker.html.target}")
    private String fmHtmlTarget;

    private final String fmTempPath = Objects.requireNonNull(this.getClass().getResource("/")).getPath() + "templates";

    @Bean
    public String getFmHtmlTarget() {
        return fmHtmlTarget;
    }

    @GetMapping("/hello")
    public String freemarkerTest(Model model) {
        String testStr = "This string is coming from FreeMarkerController";

        model.addAttribute("text", testStr); // match with symbol in expression
        List<String> list = new ArrayList<>();
        System.out.println(list.toString());
        return "hello"; // route --> resource/templates/ and find hello.ftl
    }

    @ResponseBody
    @GetMapping("/createHtml")
    public String createHtml(Model model) throws IOException, TemplateException {
        // 1.Config for freemarker
        Configuration cfg = new Configuration(Configuration.getVersion());
        cfg.setDirectoryForTemplateLoading(new File(fmTempPath));
        String filename = "hello";

        // 2.Get existed ftl file
        Template tmp = cfg.getTemplate(filename + ".ftl", "utf-8");

        // 3.Get dynamic data
        String data = "FreeMarkerController";
        model.addAttribute("text", data); // match with symbol in expression

        // 4.Generate
        System.out.println(fmHtmlTarget);
        File target = new File(fmHtmlTarget);
        if (!target.exists()) {
            target.mkdirs();
        }

        Writer out = new FileWriter(target + File.separator + filename + ".html");
        tmp.process(model, out);
        out.close();

        return "";
    }

}
