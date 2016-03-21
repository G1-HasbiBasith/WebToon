package com.pluu.webtoon.di.components;

import com.pluu.webtoon.di.modules.NetworkModule;
import com.pluu.webtoon.ui.DetailActivity;
import com.pluu.webtoon.ui.EpisodeFragment;
import com.pluu.webtoon.ui.WebtoonListFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Network Component
 * Created by PLUUSYSTEM-NEW on 2016-03-21.
 */
@Singleton
@Component(modules = NetworkModule.class)
public interface NetworkComponent {
    void inject(WebtoonListFragment fragment);
    void inject(EpisodeFragment fragment);
    void inject(DetailActivity activity);
}
