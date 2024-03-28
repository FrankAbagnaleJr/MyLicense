package com.frank.interceptor;

import com.alibaba.fastjson.JSON;
import com.frank.license.LicenseVerify;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: uicsoft-admin
 * @Date: 2024/3/27 9:11 周三
 * @Project_Name: MyLicense
 * @Version: 1.0
 * @description TODO
 */
public class LicenseCheckInterceptor implements HandlerInterceptor {
    private static Logger logger = LogManager.getLogger(LicenseCheckInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LicenseVerify licenseVerify = new LicenseVerify();

        //校验证书是否有效
        boolean verifyResult = licenseVerify.verify();

        if(verifyResult){
            return true;
        }else{
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json"); // 设置内容类型为 JSON
            response.setCharacterEncoding("UTF-8"); // 设置字符编码为 UTF-8
            Map<String,String> result = new HashMap<>(1);
            result.put("code","403");
            result.put("success","false");
            result.put("msg","您的证书无效，请核查服务器是否取得授权或重新申请证书！");
            response.getWriter().write(JSON.toJSONString(result));
            return false;
        }
    }
}
