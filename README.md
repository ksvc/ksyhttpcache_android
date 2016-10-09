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


## 3.下载和使用
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

使用以上方法，proxy将采用默认配置。可采用如下方法自定义配置(需在startServer前设置)：

-  设置缓存存储位置
   proxy.setCacheRoot(fileRoot)

-  缓存区大小限制策略（文件个数限制、文件总大小限制)，目前这两种策略只能二选一，且策略在每次播放完成或者退出播放时生效。
   
   - 使用限制文件总大小的策略，默认使用的是该策略，且缓存大小为500M

   ```
   proxy.setMaxCacheSize(maxCacheSize)
   ```
   
   - 使用限制文件总个数的策略

   ```
   proxy.setMaxFilesCount(maxFilesCount)
   ```

-  设置对单个文件大小限制，超过该大小的文件将不被缓存

   ```
   proxy.setMaxSingleFileSize(long maxSingleFileSize)
  ```

-  设置不缓存任何文件

   ```
   proxy.setDisableCache(true)
   ```

-  状态监听

   监听proxy的error

   ```
   void registerErrorListener(OnErrorListener onErrorListener)
   ```

   监听某个url对应的缓存进度

   ```
   void registerCacheStatusListener(OnCacheStatusListener onCacheStatusListener, String url)
  ```


## 4.其他接口说明
```
void getProxyUrl(url, newCache)
```

获得播放地址
- 对于http flv直播，如果播放器通过接口getProxyUrl( ur）获得播放地址，播放行为是：首次播放，边播放边缓存；以后播放相同url，则是回看缓存好的视频。
- 而如果播放器通过getProxyUrl(url, newCache)获得播放地址，播放行为是：newCache参数为true，无论是否有url对应的缓存内容，都是播放并缓存新的直播内容。newCache为false，如果有url对应的缓存内容（命中缓存），播放时回看已缓存的直播内容；没有命中的缓存视频（未命中缓存），则播放并缓存新的直播内容。


```
void startServer() 
```

启动server

```
void shutDownServer() 
```

关闭server

```
void cleanCaches()
```

清除缓存区所有缓存（cacheRoot目录下的所有文件及数据库记录）

```
void cleanCache(String url)
```

删除某个url对于的缓存

```
boolean isCached(String url)
```
查询某个url是否缓存完成

```
HashMap<String, File> getCachedFileList()
```

获得缓存区中已缓存完成的文件列表（url和缓存文件）

```
HashMap<String, Integer> getCachingPercentsList() 
```

获得缓存区中缓存未完成的文件列表（url和缓存完成百分比）

```
void resumeDownload(String url)
```

启用后台线程对于已缓冲但未完成的文件进行下载，该线程在以下情况下退出:
-  下载完成
-  server接收到相同url的播放请求
-  shutDownServer方法被调用

如果设置了缓存区文件个数限制,后台下载文件的个数不应超过文件限制


```
Bitmap getCachedFileThumbnail(String url)
```

获得缓存区已缓存完成的url对应的缩略图, 此调用为耗时操作，当文件较多时建议不要放在主线程中

```
File getCacheFile(String url)
```

获得缓存区已缓存完成的url对应的文件

```
File getCacheRoot()
```

获得缓存区路径

## 5.其他文档
请见[wiki](https://github.com/ksvc/ksyhttpcache_android/wiki)

## 6.反馈与建议
- 主页：[金山云](http://www.ksyun.com/)
- 邮箱：<zengfanping@kingsoft.com>
- QQ讨论群：574179720
- Issues:https://github.com/ksvc/ksyhttpcache_android/issues
