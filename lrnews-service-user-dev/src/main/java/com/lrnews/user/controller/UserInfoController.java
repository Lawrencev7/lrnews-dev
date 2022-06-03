package com.lrnews.user.controller;

import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.user.UserInfoControllerApi;
import com.lrnews.bo.UpdateUserInfoBO;
import com.lrnews.exception.CustomExceptionFactory;
import com.lrnews.exception.LrCustomException;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.AppUser;
import com.lrnews.pojo.Article;
import com.lrnews.user.service.FansService;
import com.lrnews.user.service.UserService;
import com.lrnews.utils.JsonUtils;
import com.lrnews.vo.CommonUserVO;
import com.lrnews.vo.PersonalPageInfoVO;
import com.lrnews.vo.UserVO;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.lrnews.values.CommonApiDefStrings.SESSION_HEADER_USER_ID;
import static com.lrnews.values.CommonRedisKeySet.*;

@RestController
@DefaultProperties(defaultFallback = "defaultFallback")
public class UserInfoController extends BaseController implements UserInfoControllerApi {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    final UserService userService;

    final FansService fansService;

    public UserInfoController(UserService userService, FansService fansService, RestOperations restOperations) {
        this.userService = userService;
        this.fansService = fansService;
        this.restOperations = restOperations;
    }

    @Override
    public JsonResultObject getUserInfo(String userId) {
        if (StringUtils.isBlank(userId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.USER_NOT_LOGIN);
        }

        AppUser user = getUser(userId);
        UserVO infoVo = new UserVO();
        BeanUtils.copyProperties(user, infoVo);
        logger.info("getUserInfo: Query user info of [" + userId + ']');
        return JsonResultObject.ok(infoVo);
    }

    @Override
    public JsonResultObject getUserCommonInfo(String userId) {
        if (StringUtils.isBlank(userId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.USER_NOT_LOGIN);
        }

        AppUser user = getUser(userId);
        CommonUserVO infoVo = genCommonInfoVO(user);
        logger.info("getUserCommonInfo: Query common info of [" + userId + ']');
        return JsonResultObject.ok(infoVo);
    }

    @Override
    public JsonResultObject updateUserInfo(UpdateUserInfoBO updateUserInfoBO) {
        String id = updateUserInfoBO.getId();
        String userInfoTag = redisCachedInfoTag(id);
        // Ensure double-write consistence
        redis.delete(userInfoTag);

        userService.updateUserInfo(updateUserInfoBO);

        // Update redis cached user info.
        AppUser user = userService.getUser(id);
        // If redis set failed, redis will be updated at next query.
        redis.set(userInfoTag, JsonUtils.objectToJson(user));
        logger.info("updateUserInfo: Update user info for [" + id + ']');
        try {
            // Redis cache double-delete
            Thread.sleep(100);
            redis.delete(userInfoTag);
        } catch (InterruptedException e) {
            throw new LrCustomException(ResponseStatusEnum.SYSTEM_ERROR);
        }
        return JsonResultObject.ok();
    }

    private AppUser getUser(String userId) {
        String userInfoTag = redisCachedInfoTag(userId);
        AppUser user;
        if (redis.keyExist(userInfoTag)) {
            String userJson = redis.get(userInfoTag);
            if (!StringUtils.isBlank(userJson)) {
                user = JsonUtils.jsonToPojo(userJson, AppUser.class);
                return user;
            }
        }

        user = userService.getUser(userId);
        redis.set(userInfoTag, JsonUtils.objectToJson(user));
        return user;
    }

    @Override
    public JsonResultObject queryUserByIds(String userIds) {
        if (StringUtils.isBlank(userIds)) {
            CustomExceptionFactory.onException(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }


        //DEV-TEST ONLY
        if(userIds.equals("DEV-TEST-SIM-RUNTIME-ERROR")){
            System.out.println("Simulate a runtime error");
            throw new RuntimeException("DEV-TEST-SIM-RUNTIME-ERROR");
        }
        //DEV-TEST ONLY

        List<CommonUserVO> userCommonInfoList = new ArrayList<>();
        List<String> idList = JsonUtils.jsonToList(userIds, String.class);

        if (idList == null) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

        idList.forEach(id -> userCommonInfoList.add(genCommonInfoVO(getUser(id))));

        return JsonResultObject.ok(userCommonInfoList);
    }

    @Override
    public JsonResultObject getPersonalPageInfo(String userId, Integer page, Integer pageSize, HttpServletRequest request) {
        AppUser writer = userService.getUser(userId);
        if (Objects.isNull(writer)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }

        String loginUserId = (String) request.getSession().getAttribute(SESSION_HEADER_USER_ID);
        boolean isFollowedByMe = fansService.isFollowedBy(writer.getId(), loginUserId);
        Integer follows = getMyFollowerNum(loginUserId);
        Integer subscribers = getMySubscribeNum(loginUserId);


        String articleServiceUrl = "http://localhost:8001/portal/article/writerArticleList?" +
                "userIds=" + JsonUtils.objectToJson(userId) +
                "page" + page +
                "pageSize" + pageSize;

        ResponseEntity<JsonResultObject> entity = restOperations.getForEntity(articleServiceUrl, JsonResultObject.class);
        JsonResultObject responseData = entity.getBody();
        List<Article> writerArticles = null;
        if (Objects.isNull(responseData)) {
            CustomExceptionFactory.onException(ResponseStatusEnum.SYSTEM_CONNECTION_FAIL);
        } else {
            writerArticles = JsonUtils.jsonToList(responseData.getData().toString(), Article.class);
        }

        PersonalPageInfoVO infoVO = new PersonalPageInfoVO();
        infoVO.setNickname(writer.getNickname());
        infoVO.setFollows(follows);
        infoVO.setSubscriber(subscribers);
        infoVO.setIsFollowedByMe(isFollowedByMe);
        infoVO.setArticleList(writerArticles);

        return JsonResultObject.ok(infoVO);
    }

    private CommonUserVO genCommonInfoVO(AppUser user) {
        if (Objects.isNull(user)) {
            CustomExceptionFactory.onException(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }
        CommonUserVO infoVo = new CommonUserVO();
        BeanUtils.copyProperties(user, infoVo);
        return infoVo;
    }

    private static String redisCachedInfoTag(String id) {
        return REDIS_USER_CACHE_KEY + id;
    }

    private Integer getMyFollowerNum(String myId) {
        if (redis.keyExist(REDIS_WRITER_FOLLOWER_NUM_KEY + myId)) {
            return Integer.parseInt(redis.get(REDIS_WRITER_FOLLOWER_NUM_KEY + myId));
        } else {
            Integer myFansNum = fansService.countMyFans(myId);
            redis.set(REDIS_WRITER_FOLLOWER_NUM_KEY + myId, myFansNum.toString());
            return myFansNum;
        }
    }

    private Integer getMySubscribeNum(String myId) {
        if (redis.keyExist(REDIS_MY_SUBSCRIBE_NUM_KEY + myId)) {
            return Integer.parseInt(redis.get(REDIS_MY_SUBSCRIBE_NUM_KEY + myId));
        } else {
            Integer myFansNum = fansService.countMySubscribe(myId);
            redis.set(REDIS_MY_SUBSCRIBE_NUM_KEY + myId, myFansNum.toString());
            return myFansNum;
        }
    }

    @Override   // Set fallback function for one interface
    @HystrixCommand//(fallbackMethod = "blockSimulatorFallback")
    public JsonResultObject blockSimulator() {
        // 1.Trigger an simple exception
        int a = 1 / 0;

        // 2.Simulate a timeout exception
//        try {
//            Thread.sleep(9999);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return JsonResultObject.ok();
    }

    private JsonResultObject blockSimulatorFallback() {
        logger.info("Hystrix fallback called: blockSimulatorFallback");
        return JsonResultObject.ok("blockSimulatorFallback was called");
    }

    private JsonResultObject defaultFallback(){
        logger.info("Hystrix: Global fallback");
        return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_BUSY);
    }
}
