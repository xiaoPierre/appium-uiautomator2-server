/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
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

package io.appium.uiautomator2.model.settings;

public class DisableIdLocatorAutocompletion extends AbstractSetting<Boolean> {
    private static final String SETTING_NAME = "disableIdLocatorAutocompletion";
    private static final boolean DEFAULT_VALUE = false;
    private boolean disableIdLocatorAutocompletion = DEFAULT_VALUE;

    public DisableIdLocatorAutocompletion() {
        super(Boolean.class, SETTING_NAME);
    }

    @Override
    public Boolean getValue() {
        return disableIdLocatorAutocompletion;
    }

    @Override
    public Boolean getDefaultValue() {
        return DEFAULT_VALUE;
    }

    @Override
    protected void apply(Boolean value) {
        this.disableIdLocatorAutocompletion = value;
    }
}
