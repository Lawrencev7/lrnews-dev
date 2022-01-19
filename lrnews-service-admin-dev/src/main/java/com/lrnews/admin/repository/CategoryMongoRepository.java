package com.lrnews.admin.repository;

import com.lrnews.dbm.CategoryDBModel;
import com.lrnews.dbm.LinksDBModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryMongoRepository extends MongoRepository<CategoryDBModel, String> {
}
