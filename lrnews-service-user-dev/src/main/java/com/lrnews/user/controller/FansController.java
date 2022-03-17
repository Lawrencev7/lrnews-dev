package com.lrnews.user.controller;

import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.user.FansControllerApi;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.AppUser;
import com.lrnews.user.service.FansService;
import com.lrnews.user.service.UserService;
import com.lrnews.values.CommonApiDefStrings;
import com.lrnews.vo.FansGraphVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import static com.lrnews.values.CommonRedisKeySet.REDIS_MY_SUBSCRIBE_NUM_KEY;
import static com.lrnews.values.CommonRedisKeySet.REDIS_WRITER_FOLLOWER_NUM_KEY;

public class FansController extends BaseController implements FansControllerApi {

    private final FansService fansService;

    private final UserService userService;

    public FansController(FansService fansService, UserService userService) {
        this.fansService = fansService;
        this.userService = userService;
    }

    @Override
    public JsonResultObject isFollowedByMe(String writerId, HttpServletRequest request) {
        String loginUserId = getLoginUser(request);
        if (StringUtils.isBlank(loginUserId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.USER_NOT_LOGIN);
        } else if (StringUtils.isBlank(writerId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ILLEGAL_ARGUMENT);
        }

        return JsonResultObject.ok(fansService.isFollowedBy(writerId, loginUserId));
    }

    @Override
    public JsonResultObject follow(String writerId, HttpServletRequest request) {
        String loginUserId = getLoginUser(request);
        if (StringUtils.isBlank(loginUserId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.USER_NOT_LOGIN);
        } else if (StringUtils.isBlank(writerId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ILLEGAL_ARGUMENT);
        }

        AppUser fanUser = userService.getUser(loginUserId);
        Boolean result = fansService.follow(writerId, fanUser);

        if (!result) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

        redis.increase(REDIS_WRITER_FOLLOWER_NUM_KEY + writerId, 1);
        redis.increase(REDIS_MY_SUBSCRIBE_NUM_KEY + loginUserId, 1);

        return JsonResultObject.ok();
    }

    @Override
    public JsonResultObject unfollow(String writerId, HttpServletRequest request) {
        String loginUserId = getLoginUser(request);
        if (StringUtils.isBlank(loginUserId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.USER_NOT_LOGIN);
        } else if (StringUtils.isBlank(writerId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ILLEGAL_ARGUMENT);
        }

        AppUser fanUser = userService.getUser(loginUserId);
        Boolean result = fansService.unfollow(writerId, fanUser);

        if (!result) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

        redis.decrease(REDIS_WRITER_FOLLOWER_NUM_KEY + writerId, 1);
        redis.decrease(REDIS_MY_SUBSCRIBE_NUM_KEY + loginUserId, 1);

        return JsonResultObject.ok();
    }

    @Override
    public JsonResultObject fansList(@RequestParam String writerId, @
            RequestParam Integer page, @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(writerId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ILLEGAL_ARGUMENT);
        }
        if (page == null) page = DEFAULT_PAGE;
        if (pageSize == null) page = DEFAULT_PAGE_SIZE;

        return JsonResultObject.ok(fansService.queryMyFans(writerId, page, pageSize));
    }

    @Override
    public JsonResultObject fansGraph(String writerId) {
        if (StringUtils.isBlank(writerId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }

        return JsonResultObject.ok(FansGraphVO.generateFromFans(fansService.getAllFans(writerId)));
    }

    private String getLoginUser(HttpServletRequest request) {
        return (String) request.getSession().getAttribute(CommonApiDefStrings.COOKIE_USER_ID);
    }
}
