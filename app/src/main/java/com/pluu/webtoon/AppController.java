package com.pluu.webtoon;

import android.app.Application;
import android.content.Context;

import com.pluu.webtoon.di.components.DaggerNetworkComponent;
import com.pluu.webtoon.di.components.NetworkComponent;
import com.squareup.leakcanary.RefWatcher;

/**
 * Application Controller
 * Created by nohhs on 2015-03-17.
 */
public class AppController extends Application {

	private NetworkComponent networkComponent;

	@Override
	public void onCreate() {
		super.onCreate();

		networkComponent = DaggerNetworkComponent.builder().build();

//		refWatcher = LeakCanary.install(this);
	}

	public NetworkComponent getNetworkComponent() {
		return networkComponent;
	}

	public static RefWatcher getRefWatcher(Context context) {
		AppController application = (AppController) context.getApplicationContext();
		return application.refWatcher;
	}

	private RefWatcher refWatcher;

}
