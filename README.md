# 连接手机WI-FI调试的方式

- 保证手机和电脑在同一个局域网内
- 查看手机Wi-Fi的ip地址
- 手机开启了WI-FI调试功能

````
# 连接命令
adb connect 192.168.1.100
或
adb connect 192.168.1.100:5555


# 断开命令
adb disconnect 192.168.1.100
或
adb disconnect 192.168.1.100:5555

````
