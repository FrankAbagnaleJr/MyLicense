package com.frank.listener;

import com.frank.license.LicenseVerify;
import com.frank.license.LicenseVerifyParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Auther: uicsoft-admin
 * @Date: 2024/3/27 9:10 周三
 * @Project_Name: MyLicense
 * @Version: 1.0
 * @description TODO
 */
@Component
public class LicenseCheckListener implements ApplicationListener<ContextRefreshedEvent> {
    private static Logger logger = LogManager.getLogger(LicenseCheckListener.class);

    /**
     * 证书subject
     */
    @Value("${license.subject}")
    private String subject;

    /**
     * 公钥别称
     */
    @Value("${license.publicAlias}")
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    @Value("${license.storePass}")
    private String storePass;

    /**
     * 证书生成路径
     */
    @Value("${license.licensePath}")
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    @Value("${license.publicKeysStorePath}")
    private String publicKeysStorePath;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //root application context 没有parent，
        ApplicationContext context = event.getApplicationContext().getParent();
        // context == null则context是父容器,安装证书，不为null则为子容器
        if(context == null){
            if(StringUtils.isNotBlank(licensePath)){
                logger.info("++++++++ 开始安装证书 ++++++++");

                LicenseVerifyParam param = new LicenseVerifyParam();
                param.setSubject(subject);
                param.setPublicAlias(publicAlias);
                param.setStorePass(storePass);
                param.setLicensePath(licensePath);
                param.setPublicKeysStorePath(publicKeysStorePath);

                LicenseVerify licenseVerify = new LicenseVerify();
                //安装证书
                licenseVerify.install(param);

                logger.info("++++++++ 证书安装结束 ++++++++");
            }
        }
    }
}