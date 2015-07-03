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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.octo.android.robospice.SpiceManager;

import net.alloapps.ted.R;
import net.alloapps.ted.common.BaseFragment;
import net.alloapps.ted.common.EndlessScrollListener;
import net.alloapps.ted.common.TalkListAdapter;
import net.alloapps.ted.di.components.IMainActivityComponent;
import net.alloapps.ted.model.ItemTalk;
import net.alloapps.ted.network.TalkTEDService;
import net.alloapps.ted.presenter.ListFragmentPresenterImpl;

import java.util.List;

import javax.inject.Inject;

public class ListFragment extends BaseFragment implements IListFragmentView {

    @Inject
    ListFragmentPresenterImpl presenter;

    protected SpiceManager spiceManager = new SpiceManager(TalkTEDService.class);

    private Activity activity;
    private ListView listView;
    private TalkListAdapter talkListAdapter;
    private View rootView;

    public ListFragment() {
    }

    // ----- Lifecycle override method

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
        presenter.onResume(spiceManager);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_list, container, false);
            listView = (ListView) rootView.findViewById(R.id.talkListView);
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore() {
                presenter.onLoadMore();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onItemClick((ItemTalk) listView.getAdapter().getItem(position));
            }
        });
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    // -----  IListFragmentView implement method

    @Override
    public void setTalkListAdapter(List<ItemTalk> itemTalkList, int totalTalks) {
        if (talkListAdapter == null) {
            talkListAdapter = new TalkListAdapter(activity, itemTalkList, totalTalks);
            listView.setAdapter(talkListAdapter);
        } else {
            presenter.addListToAdapter(itemTalkList);
        }
    }

    @Override
    public void addListToAdapter(List<ItemTalk> itemTalkList) {
        talkListAdapter.add(itemTalkList);
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
    public void replaceToDetailFragment(int id) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        DetailFragment detailFragment = DetailFragment.newInstance(id);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
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
}
