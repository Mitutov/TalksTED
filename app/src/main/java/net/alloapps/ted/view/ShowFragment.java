/*
 * Copyright (2015) Alexey Mitutov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.alloapps.ted.view;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import net.alloapps.ted.R;
import net.alloapps.ted.common.BaseFragment;
import net.alloapps.ted.di.components.IMainActivityComponent;
import net.alloapps.ted.presenter.ShowFragmentPresenterImpl;

import javax.inject.Inject;

public class ShowFragment extends BaseFragment implements IShowFragmentView {

    @Inject
    ShowFragmentPresenterImpl presenter;

    public static final String TAG = "ShowFragment";
    public static final String BUNDLE_URL = "bundleURL";

    private Activity activity;
    private String mediaURL;

    public ShowFragment(){
    }

    public static ShowFragment newInstance(String mediaURL) {
        ShowFragment showFragment = new ShowFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_URL, mediaURL);
        showFragment.setArguments(bundle);
        return showFragment;
    }

    // ----- Lifecycle override method

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(BUNDLE_URL)) {
            this.mediaURL = getArguments().getString(BUNDLE_URL);
        } else {
            throw new IllegalArgumentException("Must be created through newInstance(int id)");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getComponent(IMainActivityComponent.class).inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.init(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        VideoView videoView = (VideoView)view.findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(mediaURL));
        videoView.setMediaController(new MediaController(activity));
        videoView.requestFocus(0);
        videoView.start();
    }
}
