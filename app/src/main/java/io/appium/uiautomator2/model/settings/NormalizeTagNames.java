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

/**
 * Setting this value to true will enforce source tree dumper
 * to transliterate all class names to the limited
 * set of ASCII characters supported by Apache Harmony
 * lib and used by default in Android to avoid possible
 * XML parsing exceptions caused by XPath lookup.
 * The Unicode -> ASCII transliteration is based on
 * JUnidecode library (https://github.com/gcardone/junidecode)
 */
public class NormalizeTagNames extends AbstractSetting<Boolean> {
    private static final String SETTING_NAME = "normalizeTagNames";
    private static final boolean DEFAULT_VALUE = false;
    private boolean shouldNormalizeTagNames = DEFAULT_VALUE;

    public NormalizeTagNames() {
        super(Boolean.class, SETTING_NAME);
    }

    @Override
    public Boolean getValue() {
        return shouldNormalizeTagNames;
    }

    @Override
    public Boolean getDefaultValue() {
        return DEFAULT_VALUE;
    }

    @Override
    protected void apply(Boolean value) {
        this.shouldNormalizeTagNames = value;
    }
}
