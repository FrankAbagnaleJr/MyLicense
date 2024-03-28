package com.frank.license;

import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

/**
 * @Auther: uicsoft-admin
 * @Date: 2024/3/27 9:15 周三
 * @Project_Name: MyLicense
 * @Version: 1.0
 * @description TODO
 */
public class LicenseManagerHolder {
    private static volatile LicenseManager LICENSE_MANAGER;
    public static LicenseManager getInstance(LicenseParam param) {
        if (LICENSE_MANAGER == null) {
            synchronized (LicenseManagerHolder.class) {
                if (LICENSE_MANAGER == null) {
                    LICENSE_MANAGER = new CustomLicenseManager(param);
                }
            }
        }
        return LICENSE_MANAGER;
    }
}
