# LauncherShortcut
android 7.1 ShortcutManager using

在Android 7.1之后添加的新功能，这里就暂且翻译成：快捷方式，简单地理解：在长按应用图标的情况下，在应用图标上显示的快捷方式，该快捷方式可以点击进入Activity，长按拖动创建一个在Launcher上的图标。
  现在市场上已经是有很多应用增加了这项功能，如：印象笔记、支付宝、哔哩哔哩、IT之家、知乎、美团，以上是我的手机安装的应用中看到的，其他的就暂时没了解到，可以看一下效果。
  
 ![支付宝](http://olah6fzfx.bkt.clouddn.com/%E6%94%AF%E4%BB%98%E5%AE%9D_shortcut.png) ![哔哩哔哩](http://olah6fzfx.bkt.clouddn.com/%E5%93%94%E5%93%A9%E5%93%94%E5%93%A9_shortcut.png)
#### 实现有两种方式

 1. 静态方式，通过在xml中配置快捷方式，配置名称、图标、意图这些必须的参数，最后在应用启动的Activity配置一下就可以了。
 2. 动态方式，通过Android7.1新添加的ShortcutManager进行管理，可以对快捷方式进行添加、删除、更新，也可以修改上面的静态方式显示的快捷方式。

#### 静态方式
静态方式就是通过xml文件进行配置，就像ActionBar的选项一样。
先配置快捷方式的选项：
res/xml/static_shortcuts.xml 下面配置的参数也是比较清晰的，有：id、图标、标题、意图，还有分类，是否可用。
``` xml
<!--离线视频-->
<shortcut
    android:enabled="true"
    android:icon="@drawable/ic_shortcut_download"
    android:shortcutDisabledMessage="@string/lable_shortcut_static_download_disable"
    android:shortcutId="shortcut_id_download"
    android:shortcutLongLabel="@string/lable_shortcut_static_download_long"
    android:shortcutShortLabel="@string/lable_shortcut_static_download_short">
    <intent
        android:action="android.intent.action.VIEW"
        android:targetClass="com.young.demo.launchershortcut.StaticShortcutDownload"
        android:targetPackage="com.young.demo.launchershortcut" />
    <!--当前只有这个分类-->
    <categories android:name="android.shortcut.conversation" />
</shortcut>

```
配置完图标的之后，再打开AndroidManiFest.xml配置启动的Activity
AndroidManiFest.xml
``` xml
<activity android:name=".MainActivity">
           <intent-filter>
               <action android:name="android.intent.action.MAIN" />

               <category android:name="android.intent.category.LAUNCHER" />
           </intent-filter>
           <!--add static shortcut-->
           <meta-data android:name="android.app.shortcuts"
               android:resource="@xml/static_shortcut" />
       </activity>
```

那么静态方式就配置好了，上面快捷方式配置的Activity的就不贴上来了，就是一个简单的Activity而已。
看一下效果图：


![静态方式](http://olah6fzfx.bkt.clouddn.com/%E9%9D%99%E6%80%81%E6%96%B9%E5%BC%8F.PNG)



#### 动态方式
那么动态方式，是通过ShortcutManager实现快捷方式的增加、删除、更新的操作，google的大神都配置好了方法，使用起来很简单。下面就放一下简单的配置代码。

``` java
/**
 * 先生成一个一个的快捷方式的对象
 * 对象需要配置：图标、标题、意图，其他的配置具体可以到官网查看
 */
ShortcutInfo likeShortcut = new ShortcutInfo.Builder(this, SHORTCUT_ID_LIKE)
                .setShortLabelResId(R.string.lable_shortcut_dynamic_like_short)
                .setLongLabelResId(R.string.lable_shortcut_dynamic_like_long)
                .setIcon(Icon.createWithResource(this, R.drawable.ic_shortcut_like))
                .setIntent(new Intent(this, DynamicShortcutLike.class))
                .build();

ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
//这样就可以通过长按图标显示出快捷方式了
shortcutManager.setDynamicShortcuts(Arrays.asList(likeShortcut));


```
上面两个是通过动态方式添加的。

![动态方式](http://olah6fzfx.bkt.clouddn.com/%E5%8A%A8%E6%80%81%E6%96%B9%E5%BC%8F.PNG)


移除快捷方式
void removeDynamicShortcuts(@NonNull List<String> shortcutIds);

移除快捷方式，并且拖到launcher上的图标会变灰，这个方法底层最终调用的方法跟removeDynamicShortcuts一样
void disableShortcuts(@NonNull List<String> shortcutIds);

使拖到launcher的图标可用。  静态添加的快捷方式不能使用该方法，使用会报错
void enableShortcuts(@NonNull List<String> shortcutIds);

更新快捷方式，例如图标、标题
boolean updateShortcuts(List<ShortcutInfo> shortcutInfoList);

显示的顺序问题：添加在前的显示在下方，后面添加的显示在上面。

这个类的使用还是比较简单的。添加了这项功能的APP，长按的效果跟iOS的3D touch差不多的视觉体验。
