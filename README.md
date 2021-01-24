# PowerScanner
* 面向HW的红队半自动扫描器(Burp插件)
* 通过缩短payload长度、降低发包数量提高对WAF的隐蔽性，同时提高误报率。
* 适合有经验的渗透测试人员。

* 注：误报率高！！！所有报告结果需经手动手动确认。
* By Brian.W AKA BigCHAN

## Feature
* Sql注入
* 命令注入
* 敏感文件
* BypassWAF:IP随机化
* BypassWAF:Cookie清空
* BypassWAF:User-agent随机化
* BypassWAF:锚点随机化，随机锚点{{|RANDOMSTR|}},{{|RANDOMINT|}}
* BypassWAF:HOST随机化

## TODO
* BypassWAF:膨胀Post body
* BypassWAF:膨胀Get URL
* 路径穿越
* CMS识别

# How To Use
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