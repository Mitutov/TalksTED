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

package net.alloapps.ted.di.components;

import net.alloapps.ted.di.ActivityScope;
import net.alloapps.ted.di.modules.MainActivityModule;
import net.alloapps.ted.view.DetailFragment;
import net.alloapps.ted.view.ListFragment;
import net.alloapps.ted.view.MainActivity;
import net.alloapps.ted.view.ShowFragment;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = ITalksTEDAppComponent.class,
        modules = MainActivityModule.class
)
public interface IMainActivityComponent {
    void inject(MainActivity activity);
    void inject(ListFragment talksListFragment);
    void inject(DetailFragment talkDetailFragment);
    void inject(ShowFragment showFragment);
}
