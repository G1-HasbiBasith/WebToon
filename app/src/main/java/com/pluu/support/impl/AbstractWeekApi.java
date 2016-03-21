package com.pluu.support.impl;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import com.pluu.support.daum.DaumWeekApi;
import com.pluu.support.impl.ServiceConst.NAV_ITEM;
import com.pluu.support.kakao.KakaoWeekApi;
import com.pluu.support.nate.NateWeekApi;
import com.pluu.support.naver.NaverWeekApi;
import com.pluu.support.olleh.OllehWeekApi;
import com.pluu.support.tstore.TStorerWeekApi;
import com.pluu.webtoon.item.WebToonInfo;
import com.pluu.webtoon.network.NetworkTask;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Week API
 * Created by PLUUSYSTEM-NEW on 2015-10-26.
 */
public abstract class AbstractWeekApi extends NetworkSupportApi {

	private final String[] CURRENT_TABS;

	public AbstractWeekApi(NetworkTask task, String[]tabs) {
		super(task);
		this.CURRENT_TABS = tabs;
	}

	public abstract NAV_ITEM getNaviItem();

	public int getTitleColor(Context context) {
		return ContextCompat.getColor(context, getMainTitleColor());
	}

	protected abstract int getMainTitleColor();

	public int getTitleColorDark(Context context) {
		return ContextCompat.getColor(context, getMainTitleColorDark());
	}

	protected abstract int getMainTitleColorDark();

	public int getWeeklyTabSize() {
		return CURRENT_TABS.length;
	}

	public String getWeeklyTabName(int position) {
		return CURRENT_TABS[position];
	}

	public int getTodayTabPosition() {
		return (Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK) + 5) % 7;
	}

	public abstract List<WebToonInfo> parseMain(int position);

	public static AbstractWeekApi getApi(NAV_ITEM item) {
		return getApi(null, item);
	}

	public static AbstractWeekApi getApi(NetworkTask task, NAV_ITEM item) {
		switch (item) {
			case NAVER:
				return new NaverWeekApi(task);
			case DAUM:
				return new DaumWeekApi(task);
			case OLLEH:
				return new OllehWeekApi(task);
			case KAKAOPAGE:
				return new KakaoWeekApi(task);
			case NATE:
				return new NateWeekApi(task);
			case T_STORE:
				return new TStorerWeekApi(task);
			default:
				throw new Resources.NotFoundException("Not Found API");
		}
	}

}
