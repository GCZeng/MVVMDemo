package org.zgc.mvvmdemo.view.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.squareup.picasso.Picasso;

import org.zgc.mvvmdemo.R;
import org.zgc.mvvmdemo.databinding.ActivityPhotoViewBinding;
import org.zgc.mvvmdemo.view.activity.base.BaseActivity;
import org.zgc.mvvmdemo.view.activity.iview.IPhotoView;

/**
 * Created by Nick on 2017/12/7
 */
@Route(path = "/util/PhotoViewActivity")
public class PhotoViewActivity extends BaseActivity<ActivityPhotoViewBinding> implements IPhotoView {

    public static String PHOTO_URL = "photo_url";

    @Autowired
    String photo_url;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_photo_view;
    }

    @Override
    protected void initView() {

        ARouter.getInstance().inject(this);

        showBackPress(() -> finish());

        binding.pvPic.setOnPhotoTapListener((view, x, y) -> {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar.isShowing()) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        });
    }

    @Override
    protected void initData() {
        setTitle(R.string.photo_view);

        Picasso.get().load(photo_url).into(binding.pvPic);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewCompat.setTransitionName(binding.pvPic, getString(R.string.photo_transiton_tag));
    }


}
