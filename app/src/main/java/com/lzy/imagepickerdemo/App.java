package com.lzy.imagepickerdemo;

import android.app.Application;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.ayo.Ayo;
import org.ayo.component.core.Core;
import org.ayo.file.Files;
import org.ayo.imageloader.VanGogh;
import org.ayo.notify.AyoUI_notify;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）
 * 版    本：1.0
 * 创建日期：2016/4/13
 * 描    述：我的Github地址  https://github.com/jeasonlzy0216
 * 修订历史：
 * ================================================
 */
public class App extends Application {

    public static Application app;

    public static DisplayImageOptions imageLoaderOptions = new DisplayImageOptions.Builder()//
            .showImageOnLoading(R.mipmap.default_image)         //设置图片在下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.default_image)       //设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.default_image)            //设置图片加载/解码过程中错误时候显示的图片
            .cacheInMemory(true)                                //设置下载的图片是否缓存在内存中
            .cacheOnDisk(true)                                  //设置下载的图片是否缓存在SD卡中
            .build();                                           //构建完成

    public static ImageOptions xUtilsOptions = new ImageOptions.Builder()//
            .setIgnoreGif(false)                                //是否忽略GIF格式的图片
            .setImageScaleType(ImageView.ScaleType.FIT_CENTER)  //缩放模式
            .setLoadingDrawableId(R.mipmap.default_image)       //下载中显示的图片
            .setFailureDrawableId(R.mipmap.default_image)       //下载失败显示的图片
            .build();                                           //得到ImageOptions对象

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(this);

        ImageLoader.getInstance().init(config);     //UniversalImageLoader初始化
        x.Ext.init(this);                           //xUtils3初始化

        app = this;
        Core.init(this);

        //初始化Ayo SDK
        Ayo.init(this, "/ayoo/", true, true);
        Ayo.debug = true;
        AyoUI_notify.init(this);

        //初始化ImageLoader
        VanGogh.initImageBig(R.mipmap.ic_launcher);
        VanGogh.initImageMiddle(R.mipmap.ic_launcher);
        VanGogh.initImageSmall(R.mipmap.ic_launcher);
        VanGogh.init(this, Files.path.getDirInRoot("uil"));
    }
}