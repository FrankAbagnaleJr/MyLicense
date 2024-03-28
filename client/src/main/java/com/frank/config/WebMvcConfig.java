package com.frank.config;

import com.frank.interceptor.LicenseCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: uicsoft-admin
 * @Date: 2024/3/27 9:14 周三
 * @Project_Name: MyLicense
 * @Version: 1.0
 * @description TODO
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加自定义拦截器
        //拦截指定的路径
        //registry.addInterceptor(new LicenseCheckInterceptor()).addPathPatterns("/check");
        //全部拦截
        //registry.addInterceptor(new LicenseCheckInterceptor()).addPathPatterns("/**");
        //全部拦截
        registry.addInterceptor(new LicenseCheckInterceptor())
                .addPathPatterns("/**")
                //获取客户服务器硬件信息接口放开
                .excludePathPatterns("/license/getServerInfos");
    }
}
