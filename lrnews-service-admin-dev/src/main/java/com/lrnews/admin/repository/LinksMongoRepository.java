package com.lrnews.admin.repository;

import com.lrnews.dbm.LinksDBModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinksMongoRepository extends MongoRepository<LinksDBModel, String> {
}