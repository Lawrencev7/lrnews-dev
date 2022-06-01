package com.lrnews.admin.controller;

import com.lrnews.admin.service.LinksService;
import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.admin.LinksManageControllerApi;
import com.lrnews.bo.LinksBO;
import com.lrnews.dbm.LinksDBModel;
import com.lrnews.graceresult.JsonResultObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class LinksManageController extends BaseController implements LinksManageControllerApi {

    final static Logger logger = LoggerFactory.getLogger(LinksManageController.class);

    final LinksService linksService;

    public LinksManageController(LinksService linksService) {
        this.linksService = linksService;
    }

    @Override
    public JsonResultObject saveLinks(LinksBO saveLinksBO) {
        LinksDBModel linkMO = new LinksDBModel();
        BeanUtils.copyProperties(saveLinksBO, linkMO);
        if (linksService.linkExist(linkMO)) {
            linkMO.setUpdateTime(new Date());
            linksService.updateLink(linkMO);
            logAddOrUpdate(linkMO.getLinkName(), linkMO.getLinkUrl());
            return JsonResultObject.ok("Updated.");
        } else {
            linkMO.setCreateTime(new Date());
            linkMO.setUpdateTime(new Date());
            linksService.addLink(linkMO);
            logAddOrUpdate(linkMO.getLinkName(), linkMO.getLinkUrl());
            return JsonResultObject.ok("Created.");
        }
    }

    @Override
    public JsonResultObject queryLinks() {
        List<LinksDBModel> links = linksService.queryLinkList();
        List<LinksBO> retList = new ArrayList<>();
        links.forEach(model -> {
            LinksBO linksBO = new LinksBO();
            BeanUtils.copyProperties(model, linksBO);
            retList.add(linksBO);
        });
        logger.info("Query links list. Total: {} ", retList.size());
        return JsonResultObject.ok(retList);
    }

    @Override
    public JsonResultObject delLink(LinksBO deleteLinkBO) {
        LinksDBModel model = new LinksDBModel();
        BeanUtils.copyProperties(deleteLinkBO, model);
        linksService.removeLink(model);
        return JsonResultObject.ok();
    }

    private static void logAddOrUpdate(String linkName, String linkUrl) {
        logger.info("Add or update link: [{}]-[{}]", linkName, linkUrl);
    }
}
