package com.lrnews.user.controller;

import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.user.UserMngControllerApi;
import com.lrnews.enums.UserStatus;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.user.service.UserMngService;
import com.lrnews.user.service.UserService;
import com.lrnews.vo.PagedGridVO;
import org.springframework.web.bind.annotation.RestController;

import static com.lrnews.values.CommonValueStrings.*;

import java.util.Date;

@RestController
public class UserMngController extends BaseController implements UserMngControllerApi {

    private final UserMngService userMngService;

    private final UserService userService;

    public UserMngController(UserMngService userMngService, UserService userService) {
        this.userMngService = userMngService;
        this.userService = userService;
    }

    @Override
    public JsonResultObject queryAllUser(String nickname, Integer status, Date startDate,
                                         Date endDate, Integer page, Integer pageSize) {
        if (page == null) page = DEFAULT_PAGE;
        if (pageSize == null) pageSize = DEFAULT_PAGE_SIZE;

        PagedGridVO users = userMngService.queryAllUserList(nickname, status, startDate, endDate, page, pageSize);
        if (users.getRows().size() != 0) {
            return JsonResultObject.ok(users);
        } else
            return JsonResultObject.ok("No more infos");
    }

    @Override
    public JsonResultObject userDetails(String userId) {
        return JsonResultObject.ok(userService.getUser(userId));
    }

    @Override
    public JsonResultObject setStatus(String userId, String doStatus) {
        if (UserStatus.isUserStatusValid(Integer.valueOf(doStatus))) {
            userMngService.setUserStatus(userId, Integer.valueOf(doStatus));
            redis.delete(REDIS_USER_CACHE_TAG + ':' + userId);
            return JsonResultObject.ok("Set user " + userId + " status to [" + doStatus + "].");
        } else {
            return JsonResultObject.errorCustom(ResponseStatusEnum.USER_STATUS_ERROR);
        }
    }
}
