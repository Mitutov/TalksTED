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

package net.alloapps.ted.presenter;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.simple.SimpleTextRequest;

import net.alloapps.ted.model.Tags;
import net.alloapps.ted.view.IDetailFragmentView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class DetailFragmentPresenterImpl implements IDetailFragmentPresenter {

    private IDetailFragmentView view;
    private String mediaURL;

    private static final String URL_LIST_TALKS_API = "https://api.ted.com/v1/talks/";

    @Inject
    public DetailFragmentPresenterImpl() {
    }

    @Override
    public void init(IDetailFragmentView view) {
        this.view = view;
    }

    @Override
    public void onResume(SpiceManager spiceManager, int id) {
        view.startService();
        String url = URL_LIST_TALKS_API + String.valueOf(id) + ".json?api-key=umdz5qctsk4g9nmqnp5btsmf";
        sendRequest(url, spiceManager);
    }

    @Override
    public void onPause() {
        view.stopService();
    }

    @Override
    public void playButtonOnClick() {
        view.replaceToShowFragment(mediaURL);
    }

    private void sendRequest(String url, SpiceManager spiceManager) {
        SimpleTextRequest request = new SimpleTextRequest(url);
        spiceManager.execute(request, "jsonTEDTalks" + url, DurationInMillis.ONE_HOUR, new TedApiJsonRequestListener());
        view.showProgressDialog();
    }

    // ----- Inner Class for RequestListener

    private final class TedApiJsonRequestListener implements RequestListener<String> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            view.hideProgressDialog();
        }

        @Override
        public void onRequestSuccess(String result) {
            String imageUrl = null;
            String name = null;
            String desc = null;
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject talk = jsonObject.getJSONObject(Tags.TAG_TALK);
                name = talk.getString(Tags.TAG_NAME);
                desc = talk.getString(Tags.TAG_DESC);
                JSONArray images = talk.getJSONArray(Tags.TAG_IMAGES);
                JSONObject imageItem = images.getJSONObject(2);
                JSONObject image = imageItem.getJSONObject(Tags.TAG_IMAGE);
                imageUrl = image.getString(Tags.TAG_URL);
                // parse media URL
                JSONObject media = talk.getJSONObject(Tags.TAG_MEDIA);
                JSONObject internal = media.getJSONObject(Tags.TAG_INTERNAL);
                JSONObject variant = internal.getJSONObject(Tags.TAG_320K);
                mediaURL = variant.getString(Tags.TAG_URI);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            view.hideProgressDialog();
            view.updateViews(imageUrl, name, desc);
        }
    }
}
