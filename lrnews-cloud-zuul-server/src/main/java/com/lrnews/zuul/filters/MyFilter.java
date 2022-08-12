package com.lrnews.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

@Component
public class MyFilter extends ZuulFilter {
    /**
     * 过滤器类型：
     *  pre：    在被请求的路由之前执行
     *  route：  在路由请求时执行
     *  post：   在路由请求之后执行
     *  error：  在路由请求处理出错时执行
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器执行顺序，配置多个过滤器时，数字小的先执行
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * 是否开启过滤器
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器的业务方法
     * @return 无任何意义
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("[Zuul Filter] pre-filter");
        return null;
    }
}
