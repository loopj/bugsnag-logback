/**
 * Copyright 2014 www.codereligion.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codereligion.bugsnag.logback.resource;

import com.codereligion.bugsnag.logback.model.TabVO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Custom {@link JsonSerializer} for {@link TabVO}s which directly serializes the underlying data structure.
 *
 * <p/>Additionally this serializer filters out specified key/value pairs according to the given
 * {@link JsonFilterProvider}.
 *
 * @author Sebastian Gröbler
 */
public class TabVOSerializer implements JsonSerializer<TabVO> {

    private final JsonFilterProvider jsonFilterProvider;

    /**
     * Creates a new instance with the given {@code jsonFilterProvider}.
     *
     * @param jsonFilterProvider the {@link JsonFilterProvider} to use for filtering
     */
    public TabVOSerializer(final JsonFilterProvider jsonFilterProvider) {
        this.jsonFilterProvider = jsonFilterProvider;
    }

    @Override
    public JsonElement serialize(final TabVO src, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonElement jsonElement = context.serialize(src.getValuesByKey());
        filterJsonElement(jsonElement);
        return jsonElement;
    }

    private void filterJsonElement(final JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            filterJsonArray(jsonElement.getAsJsonArray());
        } else if (jsonElement.isJsonObject()) {
            filterJsonObject(jsonElement.getAsJsonObject());
        }
    }

    private void filterJsonArray(final JsonArray jsonArray) {
        for (final JsonElement jsonElement : jsonArray) {
            filterJsonElement(jsonElement);
        }
    }

    private void filterJsonObject(final JsonObject jsonObject) {
        for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            final String key = entry.getKey();
            if (jsonFilterProvider.isIgnoredByFilter(key)) {
                jsonObject.remove(key);
            } else {
                filterJsonElement(entry.getValue());
            }
        }
    }
}
