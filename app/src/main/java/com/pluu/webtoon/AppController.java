package com.pluu.webtoon;

import android.app.Application;
import android.content.Context;

import java.io.File;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import dagger.ObjectGraph;

/**
 * Created by nohhs on 2015-03-17.
 */
public class AppController extends Application {
	private ObjectGraph objectGraph;

	private final static int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	private final static int cacheSize = maxMemory / 8;
	private final static int DISK_CACHE_SIZE = 1024 * 1024 * 10;

	@Override
	public void onCreate() {
		super.onCreate();
		objectGraph = ObjectGraph.create(new AppModule(this));

		new GlideBuilder(this)
			.setMemoryCache(new LruResourceCache(cacheSize))
			.setDiskCache(new DiskCache.Factory() {
				@Override
				public DiskCache build() {
					File cacheLocation = new File(getExternalCacheDir(), "cache");
					cacheLocation.mkdirs();
					return DiskLruCacheWrapper.get(cacheLocation, DISK_CACHE_SIZE);
				}
			});
	}

	public static ObjectGraph objectGraph(Context context) {
		return ((AppController) context.getApplicationContext()).objectGraph;
	}
}