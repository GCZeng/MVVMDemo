package org.zgc.mvvmdemo.view.activity.iview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.zgc.mvvmdemo.view.adapter.HomeAdapter2;

/**
 * Author: zgc
 * Time: 2018/4/4 下午2:39
 * Description:
 **/
public interface IHomeView {
    void refreshComplete();

    RecyclerView getRecyclerView();

    void setAdapter(HomeAdapter2 homeAdapter);

    void startPhotoActivity(View view, String url);
}
