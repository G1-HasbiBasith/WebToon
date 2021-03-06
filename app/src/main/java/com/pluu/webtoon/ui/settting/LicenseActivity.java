package com.pluu.webtoon.ui.settting;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.pluu.event.RxBusProvider;
import com.pluu.webtoon.R;
import com.pluu.webtoon.adapter.LicenseAdapter;
import com.pluu.webtoon.event.RecyclerViewEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * License Activity
 * Created by PLUUSYSTEM-SURFACE on 2016-04-08.
 */
public class LicenseActivity extends AppCompatActivity {

    @BindView(R.id.listView)
    RecyclerView listView;

    private CompositeDisposable mCompositeDisposable;

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
        listView.setAdapter(new LicenseAdapter(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(
                RxBusProvider.getInstance()
                        .toObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getBusEvent())
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        mCompositeDisposable.dispose();
    }

    @NonNull
    private Consumer<Object> getBusEvent() {
        return o -> {
            if (o instanceof RecyclerViewEvent) {
                itemClick((RecyclerViewEvent) o);
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
        String url = getResources().getStringArray(R.array.license_url)[event.getPos()];

        // http://qiita.com/droibit/items/66704f96a602adec5a35

        final CustomTabsIntent tabsIntent = new CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setToolbarColor(ContextCompat.getColor(this, R.color.theme_primary))
                .build();
        tabsIntent.launchUrl(this, Uri.parse(url));
    }

}
