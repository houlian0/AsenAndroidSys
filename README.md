# AsenAndroidSys
@Author:Asen

开发Android的个人码分享


LibBase介绍
======
功能点:<br>
    * GPS定位(Android原生定位,支持扩展定位)<br>
    * 网络部分封装(含断点下载)<br>
    * 反射机制的快速FindView<br>
    * 快速的自定义Adapter<br>
    * 无限极树结构(支持小数据量)<br>
    * 一些工具类的整合<br>
    * 几个常用的简单自定义View<br>
    
## GPS定位
    首先在AndroidManifest.xml增加定位权限<br>

    ｀<!--用于进行网络定位-->
     <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
     <!--用于访问GPS定位-->
     <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />`
