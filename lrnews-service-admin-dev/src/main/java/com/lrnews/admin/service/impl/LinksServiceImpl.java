package com.lrnews.admin.service.impl;

import com.lrnews.admin.repository.LinksMongoRepository;
import com.lrnews.admin.service.LinksService;
import com.lrnews.dbm.LinksDBModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinksServiceImpl implements LinksService {

    private static Logger logger = LoggerFactory.getLogger(LinksService.class);

    final LinksMongoRepository linksRepository;

    private static final ExampleMatcher NAME_MATCHER = ExampleMatcher.matching()
            .withMatcher("link_name", ExampleMatcher.GenericPropertyMatchers.caseSensitive()
                    .stringMatcher(ExampleMatcher.StringMatcher.DEFAULT))
            .withIgnorePaths("_id", "url", "create_time", "update_time");

    public LinksServiceImpl(LinksMongoRepository linksRepository) {
        this.linksRepository = linksRepository;
    }

    @Override
    public void addLink(LinksDBModel link) {
        linksRepository.insert(link);
        logger.info("Create.");
    }

    @Override
    public void removeLink(LinksDBModel link) {
        Example<LinksDBModel> example = Example.of(link, NAME_MATCHER);
        Optional<LinksDBModel> model = linksRepository.findOne(example);
        if (model.isPresent()) {
            linksRepository.delete(model.get());
            logger.info("Delete");
        } else {
            logger.info("Link not found.");
        }
    }

    @Override
    public void updateLink(LinksDBModel link) {
        Example<LinksDBModel> example = Example.of(link, NAME_MATCHER);
        Optional<LinksDBModel> model = linksRepository.findOne(example);
        if (model.isPresent()) {
            link.setCreateTime(model.get().getCreateTime());
            link.setId(model.get().getId());
            linksRepository.save(link);
            logger.info("Link Updated.");
        } else {
            logger.info("Link not found.");
        }
    }

    @Override
    public List<LinksDBModel> queryLinkList() {
        List<LinksDBModel> res = linksRepository.findAll();
        logger.info("Get data. total {}", res.size());
        return res;
    }

    @Override
    public boolean linkExist(LinksDBModel link) {
        return linksRepository.findOne(Example.of(link, NAME_MATCHER)).isPresent();
    }
}
