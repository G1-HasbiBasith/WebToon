package com.pluu.support.impl;

import android.content.res.Resources;

import com.pluu.support.daum.DaumDetailApi;
import com.pluu.support.impl.ServiceConst.NAV_ITEM;
import com.pluu.support.kakao.KakaoDetailApi;
import com.pluu.support.nate.NateDetailApi;
import com.pluu.support.naver.NaverDetailApi;
import com.pluu.support.olleh.OllehDetailApi;
import com.pluu.support.tstore.TStoreDetailApi;
import com.pluu.webtoon.item.Detail;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.ShareItem;
import com.pluu.webtoon.network.NetworkTask;

/**
 * Detail Parse API
 * Created by PLUUSYSTEM-NEW on 2015-10-26.
 */
public abstract class AbstractDetailApi extends NetworkSupportApi {

	public AbstractDetailApi(NetworkTask task) {
		super(task);
	}

	public abstract Detail parseDetail(Episode episode);

	public abstract ShareItem getDetailShare(Episode episode, Detail detail);

	public static AbstractDetailApi getApi(NetworkTask task, NAV_ITEM item) {
		switch (item) {
			case NAVER:
				return new NaverDetailApi(task);
			case DAUM:
				return new DaumDetailApi(task);
			case OLLEH:
				return new OllehDetailApi(task);
			case KAKAOPAGE:
				return new KakaoDetailApi(task);
			case NATE:
				return new NateDetailApi(task);
			case T_STORE:
				return new TStoreDetailApi(task);
			default:
				throw new Resources.NotFoundException("Not Found API");
		}
	}

}
