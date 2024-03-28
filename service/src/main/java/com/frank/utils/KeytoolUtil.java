/*
 * UICSOFT 公司拥有本软件版权2021,并保留所有权利。
 * Copyright 2021, UICSOFT Company Limited.
 * All rights reserved.
 *
 */

package com.frank.utils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.frank.license.LicenseCheckModel;
import com.frank.license.LicenseCreator;
import com.frank.license.LicenseCreatorParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;
import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.X500Name;

import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * keytool工具类
 * @author: uicsoft-xpf
 * @create: 2022-08-23 15:05
 */
@Component
@Slf4j
public class KeytoolUtil {

	public static final int keySize = 1024;
	public static final String creatorName = "sunyong";
	public static final String organizationalUnit = "uicsoft";
	public static final String organization = "uicsoft";
	public static final String city = "hefei";
	public static final String area = "hefei";
	public static final String country = "cn";
	public static final long expireTime = 3650;
	public static final String keyType = "PKCS12";
	public static final String keyalg = "DSA";
	public static final String sha1Type = "SHA1WithDSA";
	//私钥别名
	public static final String privateAlias = "privatekey";
	//私钥库密码,不是数字+英文就报错了
	public static final String privatekeyPassword = "2024uicsoft";
	//公钥别名
	public static final String publicAlias = "publiccert";
	//主体
	public static final String subject = "smartseed";
	//产品名称
	public static final String prodName = "smartseed";


	public static void main(String[] args) throws Exception {

		//密钥生成的位置
		String path = "D:/license/";
//
//		LicenseCreatorParam param = new LicenseCreatorParam();
//		// 主体名字
//		param.setSubject("smartseed");
//		// 私钥别名
//		param.setPrivateAlias("privatekey");
//		// 密钥密码
//		param.setKeyPass("2024uicsoft");
//		//不知道干啥的
//		param.setStorePass("2024uicsoft");
//		// 证书生成位置
//		param.setLicensePath(path+"license.lic");
//		// 私钥位置
//		param.setPrivateKeysStorePath(path+"privateKeys.store");
//		// 证书的有效日期 和 结束日期
//		param.setIssuedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-03-28 00:00:00"));
//		param.setExpiryTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-12-31 23:59:59"));
//		// 用户名字，改了之后不行，最好不要改，没测试过
//		param.setConsumerType("user");
//		// 用户类型
//		param.setConsumerAmount(1);
//		// 证书描述
//		param.setDescription("UicSoft - 联智创新");
//		param.setLicenseCheckModel(new LicenseCheckModel(
//				// ip地址
//				Arrays.asList("192.168.0.112"),
//				// mac地址
//				Arrays.asList("2C-F0-5D-D4-9B-47"),
//				// cpu编号
//				"178BFBFF00A50F00",
//				// 主板编号
//				"KB1E589930"
//		));

		//创建公钥和私钥
		createKeyStoreFile(path);
		//生成证书
//		new LicenseCreator(param).generateLicense();
	}

	/**
	 * 生成私钥库与公钥库(私钥用于证书加密  公钥用于解密)
	 */
	public static JSONObject createKeyStoreFile(String path) throws Exception {
		//私钥库路径
		String privateKeyStorePath = StrUtil.format("{}/{}.store", path, "privateKeys");
		//公钥库路径
		String publicKeyStorePath = StrUtil.format("{}/{}.store", path, "publicKeys");
		// keytool工具
		CertAndKeyGen keyGen = new CertAndKeyGen(keyalg, sha1Type);
		// 通用信息
		X500Name x500Name = new X500Name(creatorName, organizationalUnit, organization, city, area, country);
		// 根据密钥长度生成公钥和私钥
		keyGen.generate(keySize);
		PrivateKey privateKey = keyGen.getPrivateKey();
		// 证书
		X509Certificate certificate = keyGen.getSelfCertificate(x500Name, new Date(), (long) expireTime * 24 * 60 * 60);

		KeyStore keyStore = KeyStore.getInstance(keyType);
		keyStore.load(null,privatekeyPassword.toCharArray());
		//私钥
		keyStore.setKeyEntry(privateAlias,privateKey,privatekeyPassword.toCharArray(),new Certificate[]{certificate});

		FileOutputStream outputStream = new FileOutputStream(privateKeyStorePath);
		keyStore.store(outputStream,privatekeyPassword.toCharArray());
		outputStream.close();
		log.info("私钥库生成成功");

		importPublicKeyStore(publicKeyStorePath,privateKeyStorePath);
		log.info("公钥库生成成功");


		JSONObject data = JSONUtil.createObj()
				.set("privateKeysStorePath", privateKeyStorePath)
				.set("privateKeysStoreFile", fileToByteArray(new File(privateKeyStorePath)))
				.set("publicKeyStorePath", publicKeyStorePath)
				.set("publicKeyStoreFile", fileToByteArray(new File(publicKeyStorePath)));
		return data;
	}


	/**
	 * 将证书导入到新文件生成公钥库
	 */
	public static void importPublicKeyStore(String publicKeyStorePath,String privateKeyStorePath){
		File keyStoreFile = new File(publicKeyStorePath);
			try {
				KeyStore keystore = KeyStore.getInstance(keyType);
				InputStream in = new FileInputStream(new File(privateKeyStorePath));
				keystore.load(in, privatekeyPassword.toCharArray());

				Certificate certificate = keystore.getCertificate(privateAlias);
				keystore.setCertificateEntry(publicAlias,certificate);

				FileOutputStream fos = new FileOutputStream(publicKeyStorePath);
				keystore.store(fos, privatekeyPassword.toCharArray());
				fos.close();
			} catch (Exception exp) {
				exp.printStackTrace();
			}
	}


	public static void getPrivateKey () throws Exception {
		BASE64Encoder base64Encoder = new BASE64Encoder();
		KeyStore keystore = KeyStore.getInstance(keyType);
		InputStream in = new FileInputStream(new File("D:/license/privateKeys.store"));
		keystore.load(in, privatekeyPassword.toCharArray());
		PrivateKey key = (PrivateKey) keystore.getKey(privateAlias, privatekeyPassword.toCharArray());
		System.out.println(key.toString());
		String privateKeyStr = base64Encoder.encode(key.getEncoded());
		System.out.println();
		System.out.println("-----BEGIN PRIVATE KEY-----");
		System.out.println(privateKeyStr);
		System.out.println("-----END PRIVATE KEY-----");
		System.out.println();
		System.out.println();
		Certificate certificate = keystore.getCertificate(privateAlias);
		System.out.println(certificate);

		String certificateString = base64Encoder.encode(certificate.getEncoded());
		System.out.println();
		System.out.println("-----BEGIN CERTIFICATE-----");
		System.out.println(certificateString);
		System.out.println("-----END CERTIFICATE-----");
	}
	public static byte[] fileToByteArray(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = fis.read(buffer)) != -1) {
			bos.write(buffer, 0, length);
		}
		fis.close();
		bos.close();
		return bos.toByteArray();
	}
}
