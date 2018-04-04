package org.zgc.mvvmdemo.di.module;

import org.zgc.mvvmdemo.di.scope.ActivityScoped;
import org.zgc.mvvmdemo.view.activity.HomeActivity;
import org.zgc.mvvmdemo.view.activity.PhotoViewActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Nick on 2017/12/2
 */
@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = DefaultActivityModule.class)
    abstract HomeActivity homeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = DefaultActivityModule.class)
    abstract PhotoViewActivity photoViewActivity();

}
