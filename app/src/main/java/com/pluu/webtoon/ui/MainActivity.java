package com.pluu.webtoon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.pluu.event.RxBusProvider;
import com.pluu.support.impl.ServiceConst.NAV_ITEM;
import com.pluu.webtoon.R;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.event.ThemeEvent;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends MainBaseActivity {

	private NAV_ITEM selfDrawerItem;

	@Bind(R.id.navTitle)
	View navTitle;

	private CompositeSubscription mCompositeSubscription;

	@Override
	protected NAV_ITEM getSelfNavDrawerItem() {
		return selfDrawerItem;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
		}

		selfDrawerItem = NAV_ITEM.getDefault();

		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.container, new MainFragment(), Const.MAIN_FRAG_TAG)
			.commit();
	}

	public void setSelfDrawerItem(NAV_ITEM selfDrawerItem) {
		this.selfDrawerItem = selfDrawerItem;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mCompositeSubscription = new CompositeSubscription();
		mCompositeSubscription.add(
				RxBusProvider.getInstance()
						.toObservable()
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(getBusEvent())
		);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mCompositeSubscription.unsubscribe();
	}

	@NonNull
	private Action1<Object> getBusEvent() {
		return new Action1<Object>() {
			@Override
			public void call(Object o) {
				if (o instanceof ThemeEvent) {
					themeChange((ThemeEvent) o);
				}
			}
		};
	}

	private void themeChange(ThemeEvent event) {
		navTitle.setBackgroundColor(event.getDarlColor());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			Fragment fragment = getSupportFragmentManager().findFragmentByTag(Const.MAIN_FRAG_TAG);
			if (fragment != null) {
				fragment.onActivityResult(requestCode, resultCode, data);
			}
		}
	}
}
