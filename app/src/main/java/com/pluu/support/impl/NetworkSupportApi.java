package com.pluu.support.impl;

import com.pluu.webtoon.network.NetworkTask;
import com.squareup.okhttp.Request;

import java.util.Collections;
import java.util.Map;

/**
 * Network Support API Class
 * Created by PLUUSYSTEM-NEW on 2015-10-29.
 */
public abstract class NetworkSupportApi implements IRequest {

	public static final String POST = "POST";
	public static final String GET = "GET";

	NetworkTask networkTask;

	public NetworkSupportApi(NetworkTask task) {
		networkTask = task;
	}

	public abstract String getMethod();

	public abstract String getUrl();

	@Override
	public Map<String, String> getParams() {
		return Collections.emptyMap();
	}

	@Override
	public Map<String, String> getHeaders() {
		return Collections.emptyMap();
	}

	protected String requestApi() throws Exception {
		return networkTask.requestApi(this);
	}

	protected String requestApi(Request request) throws Exception {
		return networkTask.requestApi(request);
	}

}
