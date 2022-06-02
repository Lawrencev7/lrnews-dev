package com.lrnews.api.controller.admin;

import com.lrnews.bo.LinksBO;
import com.lrnews.graceresult.JsonResultObject;
import feign.RequestLine;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "Friendly link manage controller", tags = {"Friendly links", "Controller"})
@RequestMapping("/links")
public interface LinksManageControllerApi {
    @ApiOperation(value = "Save link", notes = "Save a new link or update an existed link")
    @PostMapping("/saveLinks")
    JsonResultObject saveLinks(@RequestBody @Validated LinksBO saveLinksBO);

    @ApiOperation(value = "Query link list", notes = "Query list of all links")
    @PostMapping("/queryLinks")
    JsonResultObject queryLinks();

    @ApiOperation(value = "Delete link", notes = "Delete link")
    @PostMapping("/delLink")
    JsonResultObject delLink(@RequestBody LinksBO deleteLinkBO);
}
