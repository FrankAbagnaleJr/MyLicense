package com.frank.config;

import com.frank.license.AbstractServerInfos;
import com.frank.license.LicenseCheckModel;
import com.frank.license.LinuxServerInfos;
import com.frank.license.WindowsServerInfos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;

/**
 * @Auther: uicsoft-frank
 * @Date: 2024/3/28 9:11 周四
 * @Project_Name: MyLicense
 * @Version: 1.0
 * @description TODO
 */
@Component
public class StartShowInfo implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //操作系统类型
        String osName = System.getProperty("os.name");
        osName = osName.toLowerCase();

        AbstractServerInfos abstractServerInfos = null;

        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfos();
        } else {//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }

        LicenseCheckModel serverInfos = abstractServerInfos.getServerInfos();
        System.out.println("系统的硬件信息是：" + serverInfos.toString());
    }
}
