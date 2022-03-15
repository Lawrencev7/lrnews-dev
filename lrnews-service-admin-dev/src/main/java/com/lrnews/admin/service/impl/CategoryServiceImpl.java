package com.lrnews.admin.service.impl;

import com.lrnews.admin.repository.CategoryMongoRepository;
import com.lrnews.admin.service.CategoryService;
import com.lrnews.dbm.CategoryDBModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final static Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryMongoRepository repository;

    private static final ExampleMatcher NAME_MATCHER = ExampleMatcher.matching()
            .withMatcher("cate_name", ExampleMatcher.GenericPropertyMatchers.caseSensitive()
                    .stringMatcher(ExampleMatcher.StringMatcher.DEFAULT))
            .withIgnoreNullValues();

    public CategoryServiceImpl(CategoryMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addNewCategory(CategoryDBModel category) {
        repository.insert(category);
        logger.info("Create.");
    }

    @Override
    public void removeCategory(CategoryDBModel category) {
        CategoryDBModel emp = new CategoryDBModel();
        emp.setCategoryName(category.getCategoryName());
        Optional<CategoryDBModel> toDel = repository.findOne(Example.of(emp, NAME_MATCHER));
        if (toDel.isPresent()) {
            logger.info("Delete");
            repository.deleteById(toDel.get().getId());
        } else {
            logger.info("Not found.");
        }
    }

    @Override
    public void updateCategory(CategoryDBModel category) {
        CategoryDBModel emp = new CategoryDBModel();
        emp.setCategoryName(category.getCategoryName());
        Optional<CategoryDBModel> toUpdate = repository.findOne(Example.of(emp, NAME_MATCHER));
        if (toUpdate.isEmpty()) {
            logger.info("Not found.");
        } else {
            CategoryDBModel nCate = toUpdate.get();
            nCate.setUpdateTime(new Date());
            nCate.setComment(category.getComment());
            logger.info("Update.");
            repository.save(nCate);
        }
    }

    @Override
    public List<CategoryDBModel> queryCategoryList() {
        return repository.findAll();
    }

    @Override
    public boolean isExisted(CategoryDBModel category) {
        CategoryDBModel emp = new CategoryDBModel();
        emp.setCategoryName(category.getCategoryName());
        Example<CategoryDBModel> example = Example.of(emp, NAME_MATCHER);
        return repository.findOne(example).isPresent();
    }
}
