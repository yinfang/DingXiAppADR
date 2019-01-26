 
安卓混合开发 js和原生方法的交互：
https://www.jianshu.com/p/3d9a93c9fea2
webview控件使用及优化：
https://mp.weixin.qq.com/s/Rn7s31nxxY3vWnFRyjyjiA
https://tech.meituan.com/2017/06/09/webviewperf.html

adb连接模拟器/真机：
1.查看adb（真机）或模拟器所在进程号；
2. netstat -ano | findstr pid；
3.adb connect ip：pid；
4.adb devices；查看设备所在进程 ip：pid。

Crodova 环境配置：
1.node.js安装，path环境配置；
2.安装Android SDK和Java JDK JRE，配置环境变量；
3.安装Android Studio;
4.安装git，path配置环境变量；
5.安装Cordova ： npm install -g cordova
  检查Cordova版本：cordova -v
6.cd到目标盘 创建Cordova项目目录和Cordova应用：cordova create 项目名称 包名 应用名称
7.添加平台 cordova platform add android 
  删除平台 cordova platform rm android
8.构建和运行 cordova build android
 默认模拟器运行（AS的模拟器）：cordova emulate android
 外部模拟器或真实设备运行：cordova run android
 
