/**
 * Copyright 2017-2018 Gregory Moyer and contributors.
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
package org.openhab.binding.lametrictime.api.local.model;

public class NotificationResult
{
    private Success success;

    public Success getSuccess()
    {
        return success;
    }

    public void setSuccess(Success success)
    {
        this.success = success;
    }

    public NotificationResult withSuccess(Success success)
    {
        this.success = success;
        return this;
    }

    public static class Success
    {
        private String id;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public Success withId(String id)
        {
            this.id = id;
            return this;
        }
    }
}
