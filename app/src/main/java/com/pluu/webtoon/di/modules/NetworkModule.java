package com.pluu.webtoon.di.modules;

import com.pluu.webtoon.common.LoggingInterceptor;
import com.pluu.webtoon.network.NetworkTask;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Network Module
 * Created by PLUUSYSTEM-NEW on 2016-03-21.
 */
@Module
public class NetworkModule {
    @Provides @Singleton
    public OkHttpClient provideGetNetworkClient() {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new LoggingInterceptor());
        return client;
    }

    @Provides
    public NetworkTask provideGetNetworkTask(OkHttpClient client) {
        return new NetworkTask(client);
    }
}
