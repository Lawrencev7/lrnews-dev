package com.lrnews.zuul.filters;

import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.utils.IPUtil;
import com.lrnews.utils.JsonUtils;
import com.lrnews.utils.RedisOperator;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.lrnews.values.CommonRedisKeySet.REDIS_ZUUL_BLOCKED_IP_KEY;
import static com.lrnews.values.CommonRedisKeySet.REDIS_ZUUL_IP_REQUEST_TIME_KEY;

@Component
@RefreshScope // 开启自刷新
public class IpFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(IpFilter.class);

    @Value("#{${IpBlockSetting.requestLimit}}")
    private Integer requestLimit;

    @Value("#{${IpBlockSetting.timeInterval}}")
    private Integer timeInterval;

    @Value("#{${IpBlockSetting.limitTime}}")
    private Integer limitTime;

    private final RedisOperator redis;

    public IpFilter(RedisOperator redis) {
        this.redis = redis;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        logger.info("IpFilter ===> start");
//        printConfigParam(); 该方法用于打印输出配置参数，以确认动态刷新是否生效
        RequestContext context = RequestContext.getCurrentContext();
        String requestIp = IPUtil.getRequestIp(context.getRequest());
        logger.debug("[Request Ip] " + requestIp);
        if (isIpLimited(requestIp)){ // IP 已经被限制，停止访问，并重置限制时间
            sendStopRequest(context);
            redis.setExpire(REDIS_ZUUL_IP_REQUEST_TIME_KEY + requestIp, timeInterval);
        } else {
            long counts = redis.increase(REDIS_ZUUL_IP_REQUEST_TIME_KEY + requestIp, 1);
            if(counts == 1){ // 第一次访问进入时设置过期时间
                redis.setExpire(REDIS_ZUUL_IP_REQUEST_TIME_KEY + requestIp, timeInterval);
            }

            if(counts > requestLimit){ // 未被限制但是目前请求次数超过限制，阻止该IP的后续请求
                redis.set(REDIS_ZUUL_BLOCKED_IP_KEY + requestIp, "BLOCKED", limitTime);

                sendStopRequest(context);
            }
        }
        logger.info("IpFilter ===> end");
        return null;
    }

    private static void sendStopRequest(RequestContext context) {
        context.setSendZuulResponse(false);
        context.setResponseStatusCode(200);
        String result = JsonUtils.objectToJson(
                JsonResultObject.errorCustom(ResponseStatusEnum.REQUEST_BLOCKED));
        context.setResponseBody(result);
        context.getResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
        logger.debug("[Request blocked]");
    }

    private boolean isIpLimited(String requestIp) {
        Optional<Long> expire = redis.getExpire(REDIS_ZUUL_BLOCKED_IP_KEY + requestIp);
        return expire.isPresent() && expire.get() > 0;
    }

//    private void printConfigParam() {
//        System.out.println("\t requestLimit: " + requestLimit);
//        System.out.println("\t timeInterval: " + timeInterval);
//        System.out.println("\t limitTime: " + limitTime);
//    }
}
