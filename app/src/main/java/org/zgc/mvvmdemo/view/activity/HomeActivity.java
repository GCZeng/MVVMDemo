package org.zgc.mvvmdemo.view.activity;


import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;

import org.zgc.mvvmdemo.R;
import org.zgc.mvvmdemo.databinding.ActivityHomeBinding;
import org.zgc.mvvmdemo.view.activity.base.BaseDiActivity;
import org.zgc.mvvmdemo.view.activity.iview.IHomeView;
import org.zgc.mvvmdemo.view.adapter.HomeAdapter2;
import org.zgc.mvvmdemo.view.adapter.decoration.HomeItemDecoration;
import org.zgc.mvvmdemo.viewmodel.HomeVM;

import javax.inject.Inject;

import static android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE;

/**
 * Author: zgc
 * Time: 2018/4/4 下午1:49
 * Description:
 **/
public class HomeActivity extends BaseDiActivity<ActivityHomeBinding> implements IHomeView {

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_home;
    }

    @Inject
    HomeVM homeVM;

    @Override
    protected void initView() {

        setTitle(R.string.app_name);

        binding.rlList.refresh(() -> {
            homeVM.refresh();
        });

        // 这句话是为了，第一次进入页面的时候显示加载进度条
        binding.rlList.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        //设置布局管理器
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(GAP_HANDLING_NONE);
        binding.rvList.setLayoutManager(layoutManager);

        binding.rvList.addItemDecoration(new HomeItemDecoration(this));

        binding.rvList.setPadding(0, 0, 0, 0);
        binding.rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager.invalidateSpanAssignments();
            }
        });

        //设置adapter
        binding.rvList.setHasFixedSize(true);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void refreshComplete() {
        binding.rlList.refreshComplete();
    }

    @Override
    public RecyclerView getRecyclerView() {
        return binding.rvList;
    }

    @Override
    public void setAdapter(HomeAdapter2 homeAdapter) {
        binding.rvList.setAdapter(homeAdapter);
    }

    @Override
    public void startPhotoActivity(View view, String url) {
        ActivityOptionsCompat compatOptions = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, getString(R.string.photo_transiton_tag));
        ARouter.getInstance()
                .build("/util/PhotoViewActivity")
                .withString(PhotoViewActivity.PHOTO_URL, url)
                .withOptionsCompat(compatOptions)
                .navigation(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        homeVM.takeView(this);
        if (isFirstLoadData) {
            isFirstLoadData = false;
            homeVM.init();
            homeVM.refresh();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        homeVM.dropView();
    }
}
