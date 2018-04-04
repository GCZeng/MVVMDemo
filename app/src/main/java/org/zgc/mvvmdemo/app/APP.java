package org.zgc.mvvmdemo.app;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

import org.zgc.mvvmdemo.BuildConfig;
import org.zgc.mvvmdemo.R;
import org.zgc.mvvmdemo.di.component.DaggerAppComponent;
import org.zgc.mvvmdemo.util.DisplayUtiil;
import org.zgc.mvvmdemo.util.LogUtil;
import org.zgc.mvvmdemo.util.image.ImageLoader;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by Nick on 2017/12/1
 */
public class APP extends DaggerApplication {
    public static APP sInstance;

    public final static int pagesize = 20;

    @Override
    public void onCreate() {
        super.onCreate();

        APP.sInstance = this;

        LogUtil.init(BuildConfig.LOG_DEBUG);
        DisplayUtiil.init();

        ImageLoader.init(R.mipmap.pic_placeholder);

        //Arouter
        if (BuildConfig.LOG_DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }


}
