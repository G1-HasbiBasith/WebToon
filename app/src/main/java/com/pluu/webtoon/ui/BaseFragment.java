package com.pluu.webtoon.ui;

import android.support.v4.app.Fragment;

import com.pluu.webtoon.AppController;
import com.pluu.webtoon.di.components.NetworkComponent;

/**
 * BaseFragment
 * Created by PLUUSYSTEM-NEW on 2016-03-21.
 */
public class BaseFragment extends Fragment {

    protected NetworkComponent getNetworkComponent() {
        return ((AppController) getActivity().getApplication()).getNetworkComponent();
    }

}
