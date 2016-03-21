package com.pluu.webtoon.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.pluu.event.RxBusProvider;
import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.R;
import com.pluu.webtoon.adapter.MainListAdapter;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.db.RealmHelper;
import com.pluu.webtoon.event.MainEpisodeLoadedEvent;
import com.pluu.webtoon.event.MainEpisodeStartEvent;
import com.pluu.webtoon.item.WebToonInfo;
import com.pluu.webtoon.network.NetworkTask;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Main EpisodePage List Fragment
 * Created by PLUUSYSTEM-NEW on 2015-10-27.
 */
public class WebtoonListFragment extends BaseFragment {
	private final String TAG = WebtoonListFragment.class.getSimpleName();

	private RecyclerView recyclerView;
	private GridLayoutManager manager;
	private int position;

	private final int REQUEST_DETAIL = 1000;
	public static final int REQUEST_DETAIL_REFERRER = 1001;

	@Inject
	NetworkTask networkTask;

	private AbstractWeekApi serviceApi;
	private int columnCount;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		position = getArguments().getInt(Const.EXTRA_POS);
		columnCount = getResources().getInteger(R.integer.webtoon_column_count);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_webtoon_list, container, false);
		manager = new GridLayoutManager(getActivity(), columnCount);
		recyclerView.setLayoutManager(manager);

		return recyclerView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getNetworkComponent().inject(this);
		ServiceConst.NAV_ITEM service = ServiceConst.getApiType(getArguments());
		serviceApi = AbstractWeekApi.getApi(networkTask, service);

		getApiRequest()
			.subscribeOn(Schedulers.newThread())
			.observeOn(AndroidSchedulers.mainThread())
			.map(getFavoriteProcessFunc())
			.doOnSubscribe(getSubscribeAction())
			.doOnUnsubscribe(getUnsubscribeAction())
			.subscribe(getRequestSubscriber());
	}

	@NonNull
	private Action0 getSubscribeAction() {
		return new Action0() {
			@Override
			public void call() {
				RxBusProvider.getInstance().send(new MainEpisodeStartEvent());
			}
		};
	}

	@NonNull
	private Action0 getUnsubscribeAction() {
		return new Action0() {
            @Override
            public void call() {
				RxBusProvider.getInstance().send(new MainEpisodeLoadedEvent());
            }
        };
	}

	//	@RxLogSubscriber
	@NonNull
	private Subscriber<List<WebToonInfo>> getRequestSubscriber() {
		return new Subscriber<List<WebToonInfo>>() {
			@Override
			public void onCompleted() { }

			@Override
			public void onError(Throwable e) { }

			@Override
			public void onNext(List<WebToonInfo> list) {
				final FragmentActivity activity = getActivity();
				if (activity == null || activity.isFinishing()) {
					return;
				}

				recyclerView.setAdapter(new MainListAdapter(activity, list) {
					@Override
					public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
						ViewHolder vh = super.onCreateViewHolder(viewGroup, i);
						setClickListener(vh);
						return vh;
					}
				});
			}
		};
	}

	@NonNull
	private Func1<List<WebToonInfo>, List<WebToonInfo>> getFavoriteProcessFunc() {
		return new Func1<List<WebToonInfo>, List<WebToonInfo>>() {
			@Override
			public List<WebToonInfo> call(List<WebToonInfo> list) {
				RealmHelper helper = RealmHelper.getInstance();
				for (final WebToonInfo item : list) {
					item.setIsFavorite(
						helper.getFavoriteToon(getContext(), serviceApi.getNaviItem(), item.getToonId())
					);
				}
				return list;
			}
		};
	}

	//	@RxLogObservable
	private Observable<List<WebToonInfo>> getApiRequest() {
		return Observable.defer(new Func0<Observable<List<WebToonInfo>>>() {
			@Override
			public Observable<List<WebToonInfo>> call() {
				Log.i(TAG, "Load pos=" + position);
				return Observable.just(serviceApi.parseMain(position));
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		if (requestCode == REQUEST_DETAIL) {
			// 즐겨찾기 변경 처리 > 다른 ViewPager의 Fragment도 수신받기위해 Referrer
			Fragment frag = getFragmentManager().findFragmentByTag(Const.MAIN_FRAG_TAG);
			if (frag != null) {
				frag.onActivityResult(REQUEST_DETAIL_REFERRER, resultCode, data);
			}
		} else if (requestCode == REQUEST_DETAIL_REFERRER) {
			// ViewPager 로부터 전달받은 Referrer
			WebToonInfo info = data.getParcelableExtra(Const.EXTRA_EPISODE);
			favoriteUpdate(info);
		}
	}

	private void favoriteUpdate(WebToonInfo info) {
		MainListAdapter adapter = (MainListAdapter) recyclerView.getAdapter();
		int position = adapter.modifyInfo(info);
		if (position != -1) {
			adapter.notifyItemChanged(position);
		}
	}

	private void setClickListener(final MainListAdapter.ViewHolder vh) {
		View v = vh.itemView;
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final WebToonInfo item = (WebToonInfo) vh.titleView.getTag();
				loadPalette(item);
			}
		});
	}

	private void loadPalette(final WebToonInfo item) {
		final Context context = getActivity();
		Glide.with(context)
			 .load(item.getImage())
			 .asBitmap()
			 .into(new SimpleTarget<Bitmap>() {
				 @Override
				 public void onResourceReady(Bitmap resource,
											 GlideAnimation<? super Bitmap> glideAnimation) {
					 asyncPalette(item, resource);
				 }
			 });
	}

	private void asyncPalette(final WebToonInfo item, Bitmap bitmap) {
		final Context context = getActivity();
		Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
			public void onGenerated(Palette p) {
				int bgColor = p.getDarkVibrantColor(
					Color.BLACK);
				int statusColor = p.getDarkMutedColor(
					ContextCompat.getColor(context, R.color.theme_primary_dark));
				moveEpisode(item, bgColor, statusColor);
			}
		});
	}

	private void moveEpisode(WebToonInfo item, int bgColor, int statusColor) {
		Intent intent = new Intent(getActivity(), EpisodesActivity.class);
		intent.putExtra(Const.EXTRA_API, serviceApi.getNaviItem());
		intent.putExtra(Const.EXTRA_EPISODE, item);
		intent.putExtra(Const.EXTRA_MAIN_COLOR, bgColor);
		intent.putExtra(Const.EXTRA_STATUS_COLOR, statusColor);
		startActivityForResult(intent, REQUEST_DETAIL);
	}

	private void updateSpanCount() {
		int columnCount = getResources().getInteger(R.integer.webtoon_column_count);
		manager.setSpanCount(columnCount);
		recyclerView.getAdapter().notifyDataSetChanged();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
			|| newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			updateSpanCount();
		}
	}
}