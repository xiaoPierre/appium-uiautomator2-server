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

import java.util.Objects;

import io.appium.uiautomator2.common.exceptions.InvalidArgumentException;
import io.appium.uiautomator2.utils.Logger;

public abstract class AbstractSetting<T> implements ISetting<T> {
    private final Class<T> valueType;
    private final String settingName;

    public AbstractSetting(Class<T> valueType, String settingName) {
        this.valueType = valueType;
        this.settingName = settingName;
    }

    @Override
    public void update(Object value) {
        Logger.debug(String.format("Set the %s to %s", getName(), value));
        T convertedValue = convertValue(value);
        try {
            apply(convertedValue);
        } catch (Exception e) {
            Logger.error(String.format("Unable to update the setting %s", getName()), e);
        }
    }

    @Override
    public String getName() {
        return settingName;
    }

    public Class<T> getValueType() {
        return valueType;
    }

    protected abstract void apply(T value);

    @Override
    public boolean reset() {
        if (Objects.equals(getDefaultValue(), getValue())) {
            return false;
        }
        apply(getDefaultValue());
        return true;
    }

    @Override
    public boolean isTiedToSession() {
        return true;
    }

    private T convertValue(Object value) {
        try {
            if (value instanceof Number) {
                if (valueType == Integer.class) {
                    return valueType.cast(((Number) value).intValue());
                } else if (valueType == Long.class) {
                    return valueType.cast(((Number) value).longValue());
                }
            }
            return valueType.cast(value);
        } catch (ClassCastException e) {
            String errorMsg = String.format("Invalid '%s' setting value type. Got: %s. Expected: %s.",
                    getName(), null == value ? null : value.getClass().getName(), valueType.getName());
            throw new InvalidArgumentException(errorMsg);
        }
    }
}
