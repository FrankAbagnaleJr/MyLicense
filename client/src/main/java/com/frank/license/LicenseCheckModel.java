package com.frank.license;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: uicsoft-admin
 * @Date: 2024/3/27 8:48 周三
 * @Project_Name: MyLicense
 * @Version: 1.0
 * @description TODO
 */
@Data
@ToString
public class LicenseCheckModel implements Serializable {
    private static final long serialVersionUID = 8600137500316662317L;
    /**
     * 可被允许的IP地址
     */
    private List<String> ipAddress;

    /**
     * 可被允许的MAC地址
     */
    private List<String> macAddress;

    /**
     * 可被允许的CPU序列号
     */
    private String cpuSerial;

    /**
     * 可被允许的主板序列号
     */
    private String mainBoardSerial;
}
