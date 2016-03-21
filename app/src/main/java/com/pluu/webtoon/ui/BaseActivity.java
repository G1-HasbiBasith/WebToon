package com.pluu.webtoon.ui;

import android.support.v7.app.AppCompatActivity;

import com.pluu.webtoon.AppController;
import com.pluu.webtoon.di.components.NetworkComponent;

/**
 * Base ActionBar Activity
 * Created by nohhs on 2015-04-06.
 */
public abstract class BaseActivity extends AppCompatActivity {

	protected NetworkComponent getNetworkComponent() {
		return ((AppController) getApplication()).getNetworkComponent();
	}

}
