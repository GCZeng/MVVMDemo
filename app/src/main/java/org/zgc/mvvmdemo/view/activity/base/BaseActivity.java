package org.zgc.mvvmdemo.view.activity.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import org.zgc.mvvmdemo.R;
import org.zgc.mvvmdemo.databinding.ActivityHomeBinding;

/**
 * Created by Nick on 2017/12/1
 */
public abstract class BaseActivity<V extends ViewDataBinding> extends AppCompatActivity {
    protected V binding;

    protected Toolbar mToolbar;

    private BackListener mBackListener = null;

    protected abstract int provideContentViewId();

    protected abstract void initView();

    protected abstract void initData();

    protected boolean isFirstLoadData = true;//用于判断页面是否加载过一次数据

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = DataBindingUtil.setContentView(this, provideContentViewId());

        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        initView();
        initData();
    }

    protected void showBackPress(BackListener backListener) {
        this.mBackListener = backListener;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> {
            if (mBackListener != null) {
                mBackListener.click();
            }
        });
    }

    public void setTitle(int resTitle) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(resTitle);
        }
    }

    protected void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    protected void goAct(Class clazz) {
        goAct(clazz, null);
    }

    protected void goAct(Class clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mBackListener != null) {
                    mBackListener.click();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface BackListener {
        void click();
    }

    protected void hideTitle() {
        mToolbar.animate()
                .translationY(-mToolbar.getHeight())
                .setInterpolator(new AccelerateDecelerateInterpolator());
    }

    protected void showTitle() {
        mToolbar.animate()
                .translationY(0)
                .setInterpolator(new AccelerateDecelerateInterpolator());
    }

    protected void setAppBarAlpha(float alpha) {
        mToolbar.setAlpha(alpha);
    }

    protected void hideToolbar() {
        mToolbar.setVisibility(View.GONE);
    }
}
