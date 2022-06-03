package com.lrnews.api.controller.user.fallbacks;

import com.lrnews.api.controller.user.UserInfoControllerApi;
import com.lrnews.bo.UpdateUserInfoBO;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Component
public class UserInfoCtrlFallback implements FallbackFactory<UserInfoControllerApi> {
    @Override
    public UserInfoControllerApi create(Throwable cause) {
        return fallbackApi;
    }

    UserInfoControllerApi fallbackApi = new UserInfoControllerApi() {
        private Logger logger = LoggerFactory.getLogger("fallbackUserInfoController");

        @Override
        public JsonResultObject getUserInfo(String userId) {
            logger.info("Fallback api function called: getUserInfo");
            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

        @Override
        public JsonResultObject getUserCommonInfo(String userId) {
            logger.info("Fallback api function called: getUserCommonInfo");
            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

        @Override
        public JsonResultObject updateUserInfo(@Valid UpdateUserInfoBO updateUserInfoBO) {
            logger.info("Fallback api function called: updateUserInfo");
            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

        @Override
        public JsonResultObject queryUserByIds(String userIds) {
            logger.info("Fallback api function called: queryUserByIds");
            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

        @Override
        public JsonResultObject getPersonalPageInfo(String userIds, Integer page, Integer pageSize, HttpServletRequest request) {
            logger.info("Fallback api function called: getPersonalPageInfo");
            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

        @Override
        public JsonResultObject blockSimulator() {
            logger.info("Fallback api function called: blockSimulator");
            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }
    };
}
