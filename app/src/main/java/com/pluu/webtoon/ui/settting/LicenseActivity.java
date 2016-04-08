package com.pluu.webtoon.ui.settting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.pluu.event.RxBusProvider;
import com.pluu.webtoon.R;
import com.pluu.webtoon.adapter.LicenseAdapter;
import com.pluu.webtoon.event.RecyclerViewEvent;
import com.pluu.webtoon.ui.WebViewActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * License Activity
 * Created by PLUUSYSTEM-SURFACE on 2016-04-08.
 */
public class LicenseActivity extends AppCompatActivity {

    @Bind(R.id.listView)
    RecyclerView listView;

    private CompositeSubscription mCompositeSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        ButterKnife.bind(this);
        setupActionBar();
        initView();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initView() {
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(new LicenseAdapter(this, R.array.license_title));
    }

    @Override
    public void onResume() {
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
    public void onPause() {
        super.onPause();
        mCompositeSubscription.unsubscribe();
    }

    @NonNull
    private Action1<Object> getBusEvent() {
        return new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o instanceof RecyclerViewEvent) {
                    itemClick((RecyclerViewEvent) o);
                }
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * RecyclerView Item Click
     * @param event Click Event
     */
    private void itemClick(RecyclerViewEvent event) {
        String title = getResources().getStringArray(R.array.license_title)[event.getPos()];
        String url = getResources().getStringArray(R.array.license_url)[event.getPos()];
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(WebViewActivity.KEY_URL, url);
        intent.putExtra(WebViewActivity.KEY_TITLE, title);
        startActivity(intent);
    }

}
