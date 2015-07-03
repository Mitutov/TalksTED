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
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.octo.android.robospice.SpiceManager;
import com.squareup.picasso.Picasso;

import net.alloapps.ted.R;
import net.alloapps.ted.common.BaseFragment;
import net.alloapps.ted.di.components.IMainActivityComponent;
import net.alloapps.ted.network.TalkTEDService;
import net.alloapps.ted.presenter.DetailFragmentPresenterImpl;

import javax.inject.Inject;

public class DetailFragment extends BaseFragment implements IDetailFragmentView {

    @Inject
    DetailFragmentPresenterImpl presenter;

    protected SpiceManager spiceManager = new SpiceManager(TalkTEDService.class);

    public static final String BUNDLE_ID = "bundleID";

    private Activity activity;
    private int id;

    private ImageView imageView;
    private TextView nameTextView;
    private TextView descTextView;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(int id) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_ID, id);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    // ----- Lifecycle override method

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         if (getArguments() != null && getArguments().containsKey(BUNDLE_ID)) {
            this.id = getArguments().getInt(BUNDLE_ID);
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
        presenter.onResume(spiceManager, id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        imageView = (ImageView)view.findViewById(R.id.detailImageView);
        nameTextView = (TextView)view.findViewById(R.id.detailNameTextView);
        descTextView = (TextView)view.findViewById(R.id.detaliDescTextView);
        Button playButton = (Button) view.findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.playButtonOnClick();
            }
        });
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    // -----  IDetailFragmentView implement method

    @Override
    public void updateViews(String url, String name, String desc) {
        Picasso.with(activity)
                .load(url)
                .placeholder(R.drawable.picasso_loading_animation)
                .into(imageView);
        nameTextView.setText(name);
        descTextView.setText(desc);
    }

    @Override
    public void showProgressDialog() {
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.toolbar_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressDialog() {
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.toolbar_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void startService() {
        if (!spiceManager.isStarted()){
            spiceManager.start(activity);
        }
    }

    @Override
    public void stopService() {
        if (spiceManager.isStarted()){
            spiceManager.shouldStop();
        }
    }

    @Override
    public void replaceToShowFragment(String mediaURL) {
        FragmentManager fragmentManager = getFragmentManager();
        ShowFragment showFragment = ShowFragment.newInstance(mediaURL);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, showFragment, ShowFragment.TAG)
                .addToBackStack(null)
                .commit();
    }
}
