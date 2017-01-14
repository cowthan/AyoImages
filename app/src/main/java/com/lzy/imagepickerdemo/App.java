package com.lzy.imagepickerdemo;

import android.app.Application;

import org.ayo.Ayo;
import org.ayo.component.core.Core;
import org.ayo.notify.AyoUI_notify;

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


    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
        Core.init(this);

        //初始化Ayo SDK
        Ayo.init(this, "/ayoo/", true, true);
        Ayo.debug = true;
        AyoUI_notify.init(this);

    }
}