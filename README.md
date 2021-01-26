# PowerScanner
* 面向HW的红队半自动扫描器(Burp插件)
* 适合有经验的渗透测试人员。

* 注：误报率高！！！所有报告结果需经手动手动确认。
* By Brian.W AKA BigCHAN

## Features
* 通过缩短payload长度、降低发包数量提高对WAF的隐蔽性，同时提高误报率。
* 所有测试项不依赖回显，发现隐蔽漏洞
* 反制WAF

## Check list
* 扫描Sql注入
* 扫描命令注入
* 扫描敏感文件
* 扫描路径穿越
* BypassWAF:各种Headers随机化(IP随机化、Cookie清空、User-agent随机化、HOST随机化)
* BypassWAF:锚点随机化，随机锚点{{|RANDOMSTR|}},{{|RANDOMINT|}}
* 报告敏感参数(参数明看起来可能是漏洞点)

## TODO
* 不增加发报量的前提下，修改对照逻辑，降低误报率
* fastjson<=2.6.7漏洞检测
* 敏感文件扫描按照目录扫描(最高遍历一级目录)
* BypassWAF:增加json unicode编码
* BypassWAF:膨胀Post body
* BypassWAF:膨胀Get URL
* CMS识别


# How To Use
加载插件，设置主动在线扫描，打开浏览器，设置代理到burp，剩下的就是点点点就行了。

## 主动扫描
* (optional) 为了控制发包数目，防止被WAF封，取消勾选其他所有主动扫描插件
* 加载插件
![image](https://raw.githubusercontent.com/usualwyy/PowerScanner/master/images/loadext.png)
* 新建在线主动扫描
![image](https://raw.githubusercontent.com/usualwyy/PowerScanner/master/images/livescan1.jpg)
![image](https://raw.githubusercontent.com/usualwyy/PowerScanner/master/images/livescan2.png)
![image](https://raw.githubusercontent.com/usualwyy/PowerScanner/master/images/livescan3.png)
![image](https://raw.githubusercontent.com/usualwyy/PowerScanner/master/images/livescan4.png)
![image](https://raw.githubusercontent.com/usualwyy/PowerScanner/master/images/livescan5.png)
![image](https://raw.githubusercontent.com/usualwyy/PowerScanner/master/images/livescan6.png)

## Bypass WAF
![image](https://raw.githubusercontent.com/usualwyy/PowerScanner/master/images/BypassWAF1.png)
![image](https://raw.githubusercontent.com/usualwyy/PowerScanner/master/images/BypassWAF2.png)
![image](https://raw.githubusercontent.com/usualwyy/PowerScanner/master/images/BypassWAF3.png)

## 扫描结果
![image](https://raw.githubusercontent.com/usualwyy/PowerScanner/master/images/report1.png)