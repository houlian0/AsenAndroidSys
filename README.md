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

     <!--用于进行网络定位-->
     <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
     <!--用于访问GPS定位-->
     <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

其次在代码中通过下面的方式打开或者关闭GPS定位

     GpsLocation.getInstance(mContext).start();
     GpsLocation.getInstance(mContext).stop();
     
在GPS的start方法调用之前,可以通过如下方式对Android原生的定位扩展第三方定位方式(参数的内容在以后的示例中介绍)
     
     GpsLocation.getInstance(mContext).setExtensionLocation();
     
在GPS定位开始之后,可以通过设置监听,这样可以实时的获取最新的GPS点位信息

     GpsLocation.getInstance(mContext).setOnSatelliteChangedListener();
     GpsLocation.getInstance(mContext).addOnAddressChangedListener();
     GpsLocation.getInstance(mContext).addOnLocationChangedListener();
     
两个Add的方法,别忘了remove哦

     GpsLocation.getInstance(mContext).removeOnAddressChangedListener();
     GpsLocation.getInstance(mContext).removeOnLocationChangedListener();
     

     