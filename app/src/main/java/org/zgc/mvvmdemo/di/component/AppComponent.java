package org.zgc.mvvmdemo.di.component;

import org.zgc.mvvmdemo.app.APP;
import org.zgc.mvvmdemo.di.module.ActivityBindingModule;
import org.zgc.mvvmdemo.di.module.AppModule;
import org.zgc.mvvmdemo.di.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by Nick on 2017/12/1
 */
@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class
})
public interface AppComponent extends AndroidInjector<APP> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<APP> {}

}
