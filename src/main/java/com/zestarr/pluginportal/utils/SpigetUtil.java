package com.zestarr.pluginportal.utils;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.lang.reflect.Type;
import java.util.Map;

public class SpigetUtil {

    public static String getJsonData(int id) {
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
        JsonAdapter<Map<String, Object>> jsonAdapter = moshi.adapter(type);

        try {
            OkHttpClient client = new OkHttpClient();

            // Build the URL for the API request
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("api.spiget.org")
                    .addPathSegment("v2")
                    .addPathSegment("resources")
                    .addPathSegment(String.valueOf(id))
                    .build();

            // Create the request
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            // Send the request and get the response
            Response response = client.newCall(request).execute();

            // Check the status code
            int status = response.code();
            if (status == 200) {
                // Read the response body
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
