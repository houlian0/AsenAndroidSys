# AsenAndroidSys
@Author:Asen

开发Android的个人码分享


LibBase介绍
======
功能点:<br>
    * GPS定位(Android原生定位,支持扩展定位)<br>
    * 网络部分(含断点下载)<br>
    * 反射机制的快速FindView<br>
    * 快速的自定义Adapter<br>
    * 无限极树结构(支持小数据量)<br>
    * 一些工具类的整合<br>
    * 几个常用的简单自定义View<br>
    
## GPS定位

首先在AndroidManifest.xml增加定位权限

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
     
## 网络部分

文字与文件的上传,都可以通过HttpUtil来实现

        HttpUtil.get();
        HttpUtil.post();
        
文件的断点下载可以通过如下方法实现,第三个参数或下载进度与速度等的监听,该方式会堵塞当前执行的线程

        DownloadFileService downloadFileService = new DownloadFileService(url, folder, null);
        downloadFileService.startDownload();
        
也可以通过下面的方式在UI线程中操作

        SenAsyncDownTask task = new SenAsyncDownTask(url, folder, new OnDownloadFileListener() {
            @Override
            public void progress(long downloadSize, long totalSize, float progress, float speed) {
                
            }

            @Override
            public void success(File file) {

            }

            @Override
            public void error(int errorCode, Exception e) {

            }
        });
        task.execute();
        
还有两个异步任务的类<br>
1. 仿照AsyncTask写的异步类SenAsyncTask<br>
2. 定时任务异步类SenTimingTask<br>

## 反射机制的快速FindView

首先在控件上加AFindView注解,此处tvShow与xml中的id同名,不同名时可以通过id来设置.也可以快速的给控件设置onClick监听

    @AFindView
    private TextView tvShow;
    
Activity的onCreate中调用如下方式即可findView

     FindViewUtil.getInstance(mContext).findViews(this, this);
     
Fragment中或者其他需要在View中查找控件的,可用如下方法

     FindViewUtil.getInstance(mContext).findViews(view, holder);
     
## 快速的自定义Adapter

示例如下:

     public class TestAdapter extends QuickHolderBaseAdapter<Object, TestAdapter.Holder> {
    
         public TestAdapter(Context context, int layoutResId, List<Object> data) {
             super(context, layoutResId, data);
         }
     
         @Override
         public Holder getInstance() {
             return new Holder();
         }
     
         @Override
         public void convert(Holder holder, Object info, int position) {
             holder.tvShow.setText(info + "");
         }
     
         class Holder {
             @AFindView
             TextView tvShow;
         }
     }
     
## 无限极树结构

首次写一个Bean类,该类可以根据自己的数据结构自行定义,但一定要包含TreeNodeId和TreeNodePid的注解

     public class TreeBean {
         
         @TreeNodeId
         private String id;
         
         @TreeNodePid
         private String pid;
         
         private String name;
         
         ...
     }
     
写一个Adapter,如下

     public class TreeAdapter extends BaseTreeAdapter<TreeBean> {
         
         public TreeAdapter(ListView listView, List<TreeBean> datas, OnTreeNodeClickListener onTreeNodeClickListener) {
             super(listView, datas, onTreeNodeClickListener);
         }
     
         @Override
         public float paddingLeftSize() {
             return 0; // 子节点相对于父节点的左缩进大小
         }
     
         @Override
         public View getConvertView(TreeNode<TreeBean> node, int position, View convertView, ViewGroup parent) {
             TreeBean treeBean = node.getObject();
             return null;
         }
     }

然后将自己的ListView控件绑定这个Adapter即可


## 一些工具类的整合

     如: AppUtil ConvertUtil ToastUtil 等等,此处不一一介绍了

## 几个常用的简单自定义View

     书架式GridView: BookcaseGridView
     垂直进度条: VerticalProgressBar
     可嵌套入ScrollView的 GridViewForScroll 和 ListViewForScroll