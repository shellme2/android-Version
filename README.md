# bfc-Version
中间件项目：版本更新库

# 号外
**带界面**的版本更新库BfcVersionUI已经上线了！不用自己再苦逼苦地写升级界面了！**如果没有特别的app升级提示界面需求，强烈建议使用BfcVersionUI。**     
BfcVersionUI文档地址：     
[http://172.28.2.93/bfc/BfcVersionUI](http://172.28.2.93/bfc/BfcVersionUI "文档地址")

>如果使用BfcVersionUI，直接按照上面的BfcVersionUI文档配置，无需看下面文档。

# 关于
- 版本：V1.0
- API >= 15

# 功能列表
- 检测app是否有更新
- 下载最新apk功能 
- 有可以给指定用户更新的功能（像灰度发布）
- 有下载完成后能够自行安装的功能
- app增量更新
- 推送app更新
- 机型适配

# 升级清单文档
- 文档名称：UPDATE.md (http://172.28.2.93/bfc/BfcVersion/blob/develop/UPDATE.md)

# 配置

请根据下面文档进行配置：    
[http://172.28.2.93/bfc/Bfc/blob/master/public-config/%E4%BE%9D%E8%B5%96%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E.md](http://172.28.2.93/bfc/Bfc/blob/master/public-config/%E4%BE%9D%E8%B5%96%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E.md "配置文件地址")

```groovy
	// 版本更新的依赖
	compile bfcBuildConfig.deps.'bfc-version'
```

# 使用

## 前置条件  
#### 1. 需要动态申请的敏感权限
```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
```
> 5.0以上系统还需要在代码中动态申请权限,具体请查看Android API

#### 2. 已申请的权限
```
<!-- bfc-download merge -->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<!-- bfc-common merge -->
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.VIBRATE"/>
<uses-permission android:name="android.permission.WRITE_SECURE_SETTINGS"/>
<uses-permission android:name="android.permission.WRITE_SETTINGS"/>
```
> 在版本更新库中的AndroidManifest.xml中已申请以上所有权限

## 初始化
### 简单初始化配置
最基本的初始化方法，所有设置都是默认设置：
```java
		BfcVersion bfcVersion = new BfcVersion.Builder().build(context);
```

### 带参初始化配置
如果需要附加其他设置的，可以调用下面的代码：
```java
		BfcVersion bfcVersion = new BfcVersion.Builder()
                .autoDownload(true)
                .setDebugMode(false)
                .autoReloadDownloadFailedTask(true)
                .autoReloadCheckFailedTask(true)
                .build(context);
```

>目前还兼容旧版的初始化方式：VersionManager versionManager = new VersionManager(context);还有旧版的基本功能和接口。
>但是旧版的将无法享受新功能。

### 初始化配置参数说明：
- autoDownload(boolean enable)
<br> 开启/关闭 自动下载新版本
- alwayFullUpgrade(boolean enable)
<br> 开启/关闭 优先全量升级
- setDebugMode(boolean enable)
<br> 开启/关闭 调试模式
- setMultipleRequests(boolean enable)
<br> 开启/关闭 商城接口
- autoReloadDownloadFailedTask(boolean enable)
<br> 开启/关闭 自动重新下载下载失败任务
- autoReloadCheckFailedTask(boolean enable)
<br> 开启/关闭 自动重新下载校验失败任务
- setUrl(IUrl url)
<br> 设置检查服务器url
- setCheckStrategy(int checkStrategy)
<br> 版本更新检查策略，VersionConstants.CHECK_STRATEGY_LOCAL_FIRST，VersionConstants.CHECK_STRATEGY_REMOTE_FIRST
<br> 默认 VersionConstants.CHECK_STRATEGY_LOCAL_FIRST
- setRequestNetwork(int network)
<br> 设置允许请求更新信息的网络类型，Constants.NETWORK_WIFI，Constants.NETWORK_MOBILE，Constants.NETWORK_MOBILE_2G
<br> 默认 Constants.NETWORK_WIFI | Constants.NETWORK_MOBILE | Constants.NETWORK_MOBILE_2G
<br> 注：单独有2G网络设置是，M2000有的旧系统在2G网络请求会导致无法打电话的问题。如果只设置了Constants.NETWORK_MOBILE，没有设置Constants.NETWORK_MOBILE_2G，则在3G、4G可以访问，2G不能访问。请根据自身需求自行设置。
- setDownloadNetwork(int network)
<br> 设置允许下载的网络类型，Constants.NETWORK_WIFI，Constants.NETWORK_MOBILE
<br> 默认 Constants.NETWORK_WIFI | Constants.NETWORK_MOBILE
<br> 注：如果要限制2G下载，因为只有全局设置，不能对单个任务设置，所以请自行参考bfc-download库文档设置。

## 重要功能使用演示：

### 检查当前app更新信息:
```java
        bfcVersion.checkVersion(); 
```

>默认配置下，调用bfcVersion.checkVersion()完后会自动下载apk。   
>如果没有下载完成就退出，下次启动再调用此方法可以继续下载。

### 批量检查app更新信息：
```java
        bfcVersion.checkVersion(List<ChcekParams> entities); 
```

>由于服务器接口限制，请批量请求检查更新的list size控制在10条以内，否则会抛异常！

### 下载监听:
```java
        OnVersionDownloadListener mDownloadListener = new OnVersionDownloadListener() {
            @Override
            public void onDownloadWaiting(ITask task) {
                // 下载等待中，排队或者缺少运行资源处于等待状态，一旦轮到或者满足条件则会立即开始
            }

            @Override
            public void onDownloadStarted(ITask task) {
                // 下载已开始，
            }

            @Override
            public void onDownloadConnected(ITask task, boolean resuming, long finishedSize, long totalSize) {
            }

            @Override
            public void onDownloading(ITask task, long finishedSize, long totalSize) {
            }

            @Override
            public void onDownloadPause(ITask task, String errorCode) {
            }

            @Override
            public void onDownloadRetry(ITask task, int retries, String errorCode, Throwable throwable) {
            }

            @Override
            public void onDownloadFailure(ITask task, String errorCode, Throwable throwable) {
            }

            @Override
            public void onDownloadSuccess(ITask task) {
            }
        };

	bfcVersion.setOnVersionDownloadListener(mDownloadListener)；

```

>版本更新中的下载是引用bfc-download,具体每个回调函数含义，请查看bfc-download说明文档中**“2.1创建并设置单任务下载监听”**：<br>
>[http://172.28.2.93/bfc/BfcDownload/tree/master](http://172.28.2.93/bfc/BfcDownload/tree/master "下载库文档地址")

### 版本检查监听:
```java
        OnVersionCheckListener mOnVersionCheckListener = new OnVersionCheckListener() {

            /**
             * 可以升级
             *
             * @param context
             * @param info      安装apk版本信息
             */
            @Override
            public void onUpdateReady(Context context, VersionInfo info) {
				
            }

            /**
             * 有新版本被检测到时的回调
             *
             * @param newVersions 新版本信息
             */
            @Override
            public void onNewVersionChecked(List<Version> newVersions) {

            }

            /**
             * 版本检测发生异常的回调
             *
             * @param errorCode 错误码
             */
            @Override
            public void onVersionCheckException(String errorCode) {

            }

            /**
             * 检查结束
             */
            @Override
            public void onCheckOver() {

            }
        };

	bfcVersion.setOnVersionCheckListener(mOnVersionCheckListener)；

```

>由于新版的bfc-download的回调方式修改了（不使用广播），所以版本更新库也相应修改，之前继承VersionReceiver广播的，可以修改成监听此接口。并将onUpdateReady()和onDownloadFinish()统一合并成onUpdateReady()。

### 销毁
```java
        bfcVersion.destroy();
```
>请在不需要或者退出app调用此方法。可以暂停当前下载任务。

## 扩展功能使用演示：
### 上报app升级信息:
>如果初始化时候autoDownload()设置为false，并且是自行实现的下载功能( 没有使用bfcVersion.beginDownload() )，请下载结束后，>无论成功与否，都调用bfcVersion.reportUpdate()上报服务器app升级信息。<br>
>默认配置下会自动调用次接口，无需重复调用。

```java
        bfcVersion.reportUpdate(Version version, int state, String info);
```

### 批量上报app升级信息:
```java
        bfcVersion.reportUpdate(List<RequestReportEntity> requests);
```

### 手动添加新版本apk下载使用:
```java
        bfcVersion.beginDownload(Version version);
```

```java
        bfcVersion.beginDownload(ITask...itask);
```

### 批量手动添加新版本apk下载使用:
```java
        bfcVersion.beginDownload(List<Version> version);
```

### 安装apk接口:
```java
        bfcVersion.installApk(Context context, VersionInfo info);
```

```java
        bfcVersion.installApk(Context context, File file);
```

>建议使用bfcVersion.installApk(Context context, VersionInfo info)，会校验安装apk信息，提高防篡改的安全性。

### 设置通用请求头信息:
```java
        bfcVersion.setRequestHead(String machineId, String accountId, String apkPackageName
            , String apkVersionCode, String deviceModel, String deviceOSVersion);
```

### 差分包合并
```java
        bfcVersion.patchApk(Context context, DownloadInfo info);
```

### 获取当前应用版本信息
```java
    	bfcVersion.getLocalVersion();
```

### 删除所有下载任务
```java
    	bfcVersion.deleteAllDownloadTask();
```

### 根据包名删除下载任务
```java
    	bfcVersion.deleteDownloadTask(String... packageNames);
```

### 获取库的版本信息
```java
    	bfcVersion.getLibVersion();
```

### 获取所有下载任务
```java
    	bfcVersion.getAllDownloadTask();
```

### 根据错误码获取错误信息
```java
        bfcVersion.getErrorMsg(String errorCode);
```

### 忽略升级版本
```java
        bfcVersion.ignoreVersion(VersionInfo info);

		bfcVersion.ignoreVersion(String packageName, int versionCode);
```

### 获取允许请求更新信息的网络类型
```java
        bfcVersion.getRequestNetworkType();
```

### 获取允许下载的网络类型
```java
        bfcVersion.getDownloadNetworkType();
```

### 是否需要静默安装
```java
        bfcVersion.isSilentInstall(VersionInfo info);

		bfcVersion.isSilentInstall(Version version);

		bfcVersion.isSilentInstall(int updateMode);
```
>由于静默安装涉及敏感权限，所以库里不集成静默安装功能，请自行实现。

### 下载相关设置

>版本更新库不单独提供下载设置相关接口，所有与下载相关的设置，可以通过此接口返回的ITask.Builder来设置。

```java
        bfcVersion.getDownloadTaskBuilder(Version version);
```

eg:

```java
		// 此是根据版本更新返回的信息，生成下载任务的builder,可以自行设置一些下载相关的设置
        ITask.Builder builder = bfcVersion.getDownloadTaskBuilder(version);
		// 下载相关设置,此处是下载进度回调时间间隔设置演示
		ITask itask = builder.setMinProgressTime(750).build(); 
		// 开始下载
		bfcVersion.beginDownload(itask);
```

## 上传测试升级apk
目前暂时没有平台上传，需要联系葛晓丽,陈相英（彭春或者吴言九）上传升级测试apk。

## 封装参数说明
>如果不需要自行处理下载、差分包合并，可以无视此说明

- Version为:检查更新后，服务器返回的数据信息封装类。<br>
- DownloadInfo:继承Version，下载需要的信息封装类。<br>
- VersionInfo:为下载完成后，可以安装的apk信息封装类。

>上一个操作会返回下一个操作所需要的信息封装类。<br>
>比如：下载函数bfcVersion.beginDownload(Version version)需要用到的Version类，会在OnVersionCheckListener接口的提示有更新onNewVersionChecked(List<Version> newVersions)中会提供。

# 常见问题
- Q：同样的测试版本，有的能升级，有的不能升级，为什么？   
A：请检测后台是否已经添加发布该机器序列号。测试时，很多时候是指定某些机器序列号来升级测试，所以如果没有添加的机器，是无法检测到更新信息的。

# 错误码
- 0D010301  检查版本更新失败,context不能为空
- 0D010302  检查版本更新失败,OnCoreVerCheckListener不能为空
- 0D010303  检查版本更新失败,List<RequestVersionEntity>不能为空
- 0D010304  检查版本更新失败,请求服务器url不能为空
- 0D010305  上报升级信息失败,context不能为空
- 0D010306  上报升级信息失败,List<RequestReportEntity>不能为空
- 0D010307  上报升级信息失败,请求服务器url不能为空
- 0D010501  检查版本更新失败,参数序列化json数据异常
- 0D010502  上报升级信息失败,参数序列化json数据异常
- 0D020201  升级方式与预期不一致.
- 0D020301  检查版本更新失败,请求的数据 List<CheckParams> 不能为空
- 0D020302  上报APP升级信息 List<RequestReportEntity> 不能为空
- 0D020303  beginDownload()参数不能为空
- 0D020304  安装失败, 无法安装.patch文件.
- 0D020305  安装失败, 安装文件MD5值不匹配.
- 0D020306  安装失败, 安装文件信息不匹配.
- 0D020307  安装失败, context == null.
- 0D020401  网络请求异常
- 0D020501  已下载数据已无效,请从新检查更新下载
- 0D020601  无更新信息
- 0D020602  安装失败, VersionInfo 为空.
- 0D020603  安装失败, 找不到安装文件.
- 0D020604  检查策略为空,没有初始化或者已经调用了destroy()需要重新初始化.
- 0D02ff01  差分包合并失败,尝试下载全量包.

# 依赖
本项目已集成:

- bfc-log
- bfc-json
- bfc-http
- bfc-download
- bfc-common
	
# 联系人
- 何思宁
- 工号：20251494