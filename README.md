# 金山云Android HTTPCache SDK
金山云android平台http缓存SDK，可方便地与播放器集成，实现http视频边播放边下载（缓存）功能。ksyun http cache sdk for android, it's easy to integrated with media players to provide caching capability when watching http videos.

## 1. 产品概述
金山云Android HTTPCache SDK可以方便地和播放器进行集成，提供对HTTP视频边播放缓存的功能，缓存完成的内容可以离线工作。

KSY HTTPCache与播放器及视频服务器的关系如下图：
![](https://github.com/sujia/image_foder/blob/master/ksy_http_cache.png)

KSY HTTPCache相当于本地的代理服务，使用KSY HTTPCache后，播放器不直接请求视频服务器，而是向KSY HTTPCache请求数据。KSY HTTPCache在代理HTTP请求的同时，缓存视频数据到本地。

## 2.功能说明
它可以很方便的和播放器进行集成，提供以下功能：
1. http点播视频边缓存边播放，且播放器可从通过回调得到缓存的进度以及错误码

2. 缓存完成的视频，再次点播时可以离线播放，不再请求视频server

3. 查询已完成的文件列表( 展示缩略图，url)， 未完成的文件列表（缓存进度，url）

4. 清除缓存（清除所有缓存，或删除某个url缓存）
      
5. 提供两种缓存策略供选择（限制缓存区总大小或者限制缓存文件总个数


## 3. 集成说明
下载libs目录下的jar包导入工程

为了保证正常工作，推荐一个app只使用一个KSYProxyService实例，例如:
```java
public class App extends Application {

    private KSYProxyService proxy;

    public static KSYProxyService getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private KSYProxyService newProxy() {
        return new KSYProxyService(this);
    }
}
```

proxy与播放器的集成如下所示：
对于点播，播放器通过getProxyUrl接口获得播放地址，进行播放。
对于直播，则可通过getProxyUrl(url, newCache)接口获得播放地址，并通过参数newCache控制播放和缓存的行为。详情请见下方接口说明。


```java
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

    HttpProxyCacheServer proxy = getProxy();
    proxy.startServer();

    String proxyUrl = proxy.getProxyUrl(VIDEO_URL);
    mediaPlayer.setVideoPath(proxyUrl);
}
```

使用以上方法，proxy将采用默认配置。

## 4. 使用

请阅读[API使用指南](https://github.com/ksvc/ksyhttpcache_android/wiki/api)

## 5.其他文档
请见[wiki](https://github.com/ksvc/ksyhttpcache_android/wiki)

## 6.反馈与建议
- 主页：[金山云](http://www.ksyun.com/)
- 邮箱：<zengfanping@kingsoft.com>
- QQ讨论群：574179720
- Issues:https://github.com/ksvc/ksyhttpcache_android/issues
