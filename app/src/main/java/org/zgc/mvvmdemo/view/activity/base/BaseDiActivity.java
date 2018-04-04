package org.zgc.mvvmdemo.view.activity.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import dagger.android.AndroidInjection;

/**
 * Author: zgc
 * Time: 2018/3/28 下午10:42
 * Description: BaseDiActivity
 **/

public abstract class BaseDiActivity<V extends ViewDataBinding> extends BaseActivity<V> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }
}
