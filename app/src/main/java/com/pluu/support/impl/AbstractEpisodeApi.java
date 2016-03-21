package com.pluu.support.impl;

import android.content.res.Resources;

import com.pluu.support.daum.DaumEpisodeApi;
import com.pluu.support.impl.ServiceConst.NAV_ITEM;
import com.pluu.support.kakao.KakaoEpisodeApi;
import com.pluu.support.nate.NateEpisodeApi;
import com.pluu.support.naver.NaverEpisodeApi;
import com.pluu.support.olleh.OllehEpisodeApi;
import com.pluu.support.tstore.TStoreEpisodeApi;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.EpisodePage;
import com.pluu.webtoon.item.WebToonInfo;
import com.pluu.webtoon.network.NetworkTask;

/**
 * Episode API
 * Created by PLUUSYSTEM-NEW on 2015-10-26.
 */
public abstract class AbstractEpisodeApi extends NetworkSupportApi {

	public AbstractEpisodeApi(NetworkTask task) {
		super(task);
	}

	public void init() { }

	public abstract EpisodePage parseEpisode(WebToonInfo info);

	public abstract String moreParseEpisode(EpisodePage item);

	public abstract Episode getFirstEpisode(Episode item);

	public static AbstractEpisodeApi getApi(NetworkTask task, NAV_ITEM item) {
		switch (item) {
			case NAVER:
				return new NaverEpisodeApi(task);
			case DAUM:
				return new DaumEpisodeApi(task);
			case OLLEH:
				return new OllehEpisodeApi(task);
			case KAKAOPAGE:
				return new KakaoEpisodeApi(task);
			case NATE:
				return new NateEpisodeApi(task);
			case T_STORE:
				return new TStoreEpisodeApi(task);
			default:
				throw new Resources.NotFoundException("Not Found API");
		}
	}

}
