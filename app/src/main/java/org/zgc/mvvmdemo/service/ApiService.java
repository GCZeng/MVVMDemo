package org.zgc.mvvmdemo.service;


import org.zgc.mvvmdemo.bean.GankDataBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Nick on 2017/1/6
 */
public interface ApiService {
    /**
     * 获取图片数据列表
     * @param pagesize
     * @param page
     * @return
     */
    @GET("data/福利/{pagesize}/{page}")
    Flowable<GankDataBean> getPicList(@Path("pagesize") int pagesize, @Path("page") int page);

    /**
     * 获取休息视频列表
     * @param pagesize
     * @param page
     * @return
     */
    @GET("data/休息视频/{pagesize}/{page}")
    Flowable<GankDataBean> getVideoList(@Path("pagesize") int pagesize, @Path("page") int page);
}
