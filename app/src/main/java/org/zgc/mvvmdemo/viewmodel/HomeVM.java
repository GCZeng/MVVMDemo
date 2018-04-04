package org.zgc.mvvmdemo.viewmodel;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.zgc.mvvmdemo.R;
import org.zgc.mvvmdemo.app.APP;
import org.zgc.mvvmdemo.bean.GankDataBean;
import org.zgc.mvvmdemo.bean.entity.GankData;
import org.zgc.mvvmdemo.service.ApiService;
import org.zgc.mvvmdemo.view.activity.iview.IHomeView;
import org.zgc.mvvmdemo.view.adapter.HomeAdapter2;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: zgc
 * Time: 2018/4/4 下午2:12
 * Description:
 **/
public class HomeVM implements IHomeVM<IHomeView, GankData> {
    private IHomeView mHomeView;
    private int currPage = 1; //当前页数

    private List<GankData> mHomeList = null;
    private HomeAdapter2 mHomeAdapter;

    @Inject
    ApiService apiService;

    @Inject
    public HomeVM() {
    }

    public void refresh() {
        getData(true);
    }

    private void loadMore() {
        getData(false);
    }

    private void getData(boolean isClean) {
        currPage = isClean ? 1 : currPage + 1;

        Flowable.zip(apiService.getPicList(APP.pagesize, currPage),
                apiService.getVideoList(APP.pagesize, currPage),
                this::createHomeData)
                .map(gankDataModel -> gankDataModel.getResults())
                .flatMap(Flowable::fromIterable)
                .toSortedList((o1, o2) -> o2.getPublishedAt().compareTo(o1.getPublishedAt()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gankData -> {
                    loadData(gankData);
                }, Throwable::printStackTrace);
    }

    @Override
    public void takeView(IHomeView view) {
        this.mHomeView = view;
    }

    @Override
    public void dropView() {
        this.mHomeView = null;
    }

    @Override
    public void init() {
        mHomeList = new ArrayList<>();
        mHomeAdapter = new HomeAdapter2(mHomeList);
        mHomeAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.iv_pic:
                    Picasso.get().load(mHomeList.get(position).getUrl()).fetch(new Callback() {

                        @Override
                        public void onSuccess() {
                            mHomeView.startPhotoActivity(view.findViewById(R.id.iv_pic), mHomeList.get(position).getUrl());
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }

                    });
                    break;
            }
        });
        mHomeView.setAdapter(mHomeAdapter);

        //加载更多
        mHomeAdapter.setOnLoadMoreListener(() -> loadMore(), mHomeView.getRecyclerView());
    }

    public void loadData(List<GankData> list) {
        if (currPage > 1) {
            mHomeAdapter.addData(list);
            mHomeAdapter.loadMoreComplete();
        } else {
            mHomeAdapter.getData().clear();
            mHomeAdapter.addData(list);
            mHomeView.refreshComplete();
        }
    }

    private GankDataBean createHomeData(GankDataBean picDataModel, GankDataBean videoDataModel) {
        for (int i = 0; i < picDataModel.getResults().size(); i++) {
            GankData picData = picDataModel.getResults().get(i);
            GankData videoData = videoDataModel.getResults().get(i);

            picData.setDesc(picData.getDesc() + " " + videoData.getDesc());
        }
        return picDataModel;
    }

}
