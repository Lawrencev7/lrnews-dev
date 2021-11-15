package com.lrnews.user.controller;

import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.user.UserInfoControllerApi;
import com.lrnews.bo.UpdateUserInfoBO;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.AppUser;
import com.lrnews.user.service.UserService;
import com.lrnews.vo.CommonUserVO;
import com.lrnews.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserInfoController extends BaseController implements UserInfoControllerApi {
    final UserService userService;

    public UserInfoController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public JsonResultObject getUserInfo(String userId) {
        if(StringUtils.isBlank(userId)){
            return JsonResultObject.errorCustom(ResponseStatusEnum.USER_NOT_LOGIN);
        }

        AppUser user = getUser(userId);
        UserVO infoVo = new UserVO();
        BeanUtils.copyProperties(user, infoVo);
        return JsonResultObject.ok(infoVo);
    }

    @Override
    public JsonResultObject getUserCommonInfo(String userId) {
        if(StringUtils.isBlank(userId)){
            return JsonResultObject.errorCustom(ResponseStatusEnum.USER_NOT_LOGIN);
        }

        AppUser user = getUser(userId);
        CommonUserVO infoVo = new CommonUserVO();
        BeanUtils.copyProperties(user, infoVo);
        return JsonResultObject.ok(infoVo);
    }

    @Override
    public JsonResultObject updateUserInfo(UpdateUserInfoBO updateUserInfoBO, @NotNull BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors(result);
            return JsonResultObject.errorMap(errors);
        }
        userService.updateUserInfo(updateUserInfoBO);
        return JsonResultObject.ok();
    }

    private AppUser getUser(String userId){
        // This function is going to be extended
        return userService.getUser(userId);
    }
}
