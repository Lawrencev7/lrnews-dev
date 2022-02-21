package com.lrnews.admin.service;

import com.lrnews.dbm.LinksDBModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LinksService {

    void addLink(LinksDBModel link);

    void removeLink(LinksDBModel link);

    void updateLink(LinksDBModel link);

    List<LinksDBModel> queryLinkList();

    boolean linkExist(LinksDBModel link);
}
