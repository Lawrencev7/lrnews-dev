package com.lrnews.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.lrnews.utils.extend.AliyunResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class SMSUtils {

    private final Logger logger = LoggerFactory.getLogger(SMSUtils.class);

    private final AliyunResource resource;


    public SMSUtils(AliyunResource resource) {
        this.resource = resource;
    }

    public void sendMessage(String phoneNumber, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-qingdao",
                resource.getAccessKeyId(), resource.getAccessSecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("SignName", "Lawrence");
        // From message template
        request.putQueryParameter("TemplateCode", "SMS_227248824");
        // Input a json object
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mock SMS service
     *
     * @param code verify code to be sent.
     */
    public void sendVerifyCode(String code) {
        logger.info("Mock SMS service: get a verify code: [{}]", code);
    }

}
