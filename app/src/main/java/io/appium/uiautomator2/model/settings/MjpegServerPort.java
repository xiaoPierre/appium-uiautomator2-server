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

import io.appium.uiautomator2.common.exceptions.InvalidArgumentException;
import io.appium.uiautomator2.server.ServerConfig;

/**
 * Controls the MJPEG server port
 *
 * Type: `Integer`
 * Acceptable range: `1024` to `65535`
 * Default value: `7810`
 */
public class MjpegServerPort extends AbstractSetting<Integer> {
    public static final String SETTING_NAME = "mjpegServerPort";
    private static final int DEFAULT_VALUE = 7810;

    public MjpegServerPort() {
        super(Integer.class, SETTING_NAME);
    }

    @Override
    public Integer getValue() {
        return ServerConfig.getMjpegServerPort();
    }

    @Override
    public Integer getDefaultValue() {
        return DEFAULT_VALUE;
    }

    @Override
    public boolean isTiedToSession() {
        return false;
    }

    @Override
    protected void apply(Integer value) {
        if (value == null || value < 1024 || value > 65535) {
            throw new InvalidArgumentException(String.format(
                "Invalid %s value specified, must be in range 1024..65535. %s was given",
                SETTING_NAME,
                value
            ));
        }
        ServerConfig.setMjpegServerPort(value);
    }
}
