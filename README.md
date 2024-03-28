# License证书
    
## 说明
    config       - SpringMvC过滤器，添加自定义拦截器. StartShowInfo项目启动会显示硬件信息
    controller   - 测试请求 和 获取用户服务器的硬件信息接口
    interceptor  - 自定义拦截器，用于写拦截规则，和处理方式
    license      - License的主要配置方法
    listener     - 监听器，监听Spring启动上下文刷新的时候，安装证书，子容器调用getParent()获取的值是null，所以要判断这里是为null安装还是不为null安装

    license-config.properties - 存放license证书验证信息
    启动类上加 @PropertySource({"license-config.properties"}) 注解获取license-config.properties的信息
## 注意：
    生成证书的时候，不要A项目给B项目生成正常，容易出现下面包路径不对的问题，
    生成证书的时候会把LicenseCheckModel的路径写在证书里，给另一个项目使用会出现下面找不到类的错误，导致安装证书失败
    除非两个项目的路径一致，证书才能通用

    java.lang.ClassNotFoundException: com/frank/license/LicenseCheckModel
    Continuing ...
    java.lang.NullPointerException: target should not be null
    Continuing ...
    java.lang.IllegalStateException: The outer element does not return value
    Continuing ...
    java.lang.IllegalStateException: The outer element does not return value
    Continuing ...
    java.lang.IllegalStateException: The outer element does not return value
    Continuing ...
    java.lang.IllegalStateException: The outer element does not return value
    Continuing ...

## service生成证书方式
    1.执行utils里面的main方法，会在指定的路径生成两个文件
    2.GET请求 http://localhost:8089/license//getServerInfos ，获取服务器的ip,mac,cpu序列号,主板序列号，填在第三步的请求中
    3.POST请求 http://localhost:8089/license/generateLicense
        {
            "subject": "smartseed",
            "privateAlias": "privatekey",
            "keyPass": "2024uicsoft",
            "storePass": "2024uicsoft",
            "licensePath": "D:/license1/license.lic",
            "privateKeysStorePath": "D:/license1/privateKeys.store",
            "issuedTime": "2024-02-27 00:00:01",
            "expiryTime": "2024-04-27 20:04:07",
            "consumerType": "user",
            "consumerAmount": 1,
            "description": "UicSoft - 联智创新",
            "licenseCheckModel": {
                "ipAddress": [
                    "192.168.0.113"
                ],
                "macAddress": [
                    "2C-F0-5D-D4-9B-47"
                ],
                "cpuSerial": "178BFBFF00A50F00",
                "mainBoardSerial": "KB1E589930"
            }
        }
    4. 公钥文件publicKeys.store 和 license.lic 放在客户服务机上，不要放项目中