package com.lrnews.api.config;

import com.lrnews.api.interceptors.AdminTokenInterceptor;
import com.lrnews.api.interceptors.PassportInterceptor;
import com.lrnews.api.interceptors.UserTokenInterceptor;
import com.lrnews.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public PassportInterceptor getPassportInterceptor() {
        return new PassportInterceptor();
    }

    @Bean
    public UserTokenInterceptor getUserTokenInterceptor() {
        return new UserTokenInterceptor();
    }

   @Bean
   public AdminTokenInterceptor getAdminTokenInterceptor(){
        return new AdminTokenInterceptor();
   }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getPassportInterceptor())
                .addPathPatterns("/verify/get-verify-code");

        registry.addInterceptor(getUserTokenInterceptor())
                .addPathPatterns("/user/get-user-info")
                .addPathPatterns("/user/update-user-info");

        registry.addInterceptor(getAdminTokenInterceptor())
                .addPathPatterns("/admin/adminIsExist")
                .addPathPatterns("/admin/createNewAdmin")
                .addPathPatterns("/admin/getAdminList");
//                .addPathPatterns("/fs/uploadToGridFS")
//                .addPathPatterns("/fs/readInGridFS")
//                .addPathPatterns("/friendLinkMng/saveOrUpdateFriendLink")
//                .addPathPatterns("/friendLinkMng/getFriendLinkList")
//                .addPathPatterns("/friendLinkMng/delete")
//                .addPathPatterns("/categoryMng/saveOrUpdateCategory")
//                .addPathPatterns("/categoryMng/getCatList");

    }


}
