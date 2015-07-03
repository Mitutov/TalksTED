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

import net.alloapps.ted.common.BaseFragmentPresenter;
import net.alloapps.ted.view.IDetailFragmentView;

public interface IDetailFragmentPresenter extends BaseFragmentPresenter<IDetailFragmentView> {
    void onResume(SpiceManager spiceManager, int id);
    void onPause();
    void playButtonOnClick();
}