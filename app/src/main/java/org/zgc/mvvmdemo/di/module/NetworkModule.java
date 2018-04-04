package org.zgc.mvvmdemo.di.module;


import org.zgc.mvvmdemo.app.API;
import org.zgc.mvvmdemo.service.ApiService;
import org.zgc.mvvmdemo.util.LogUtil;
import org.zgc.mvvmdemo.util.http.HttpLogInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nick on 2017/1/4
 */
@Module
public class NetworkModule {


    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(60 * 1000, TimeUnit.MILLISECONDS)
//                .addInterceptor(new HttpLoggingInterceptor(message -> LogUtil.d(message))
//                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new HttpLogInterceptor(message -> LogUtil.d(message))
                    .setLevel(HttpLogInterceptor.Level.BODY))
                .build();


        return okHttpClient;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }


}
