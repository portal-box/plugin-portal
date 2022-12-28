package com.zestarr.pluginportal.utils;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpigetUtil {

    public static Map<String, Object> getJsonData(int id) {

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
                String responseBody = response.body().string();

                // Parse the response using the JSON adapter
                return jsonAdapter.fromJson(responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getLatestVersion(int id) {
        Map<String, Object> root = getJsonData(id);
        if (root == null) {
            return null;
        }

        // Consider updating to jackson. Moshi is a bit of a pain to work with.
        Map<String, Object> firstVersion = ((List<Map<String, Object>>) root.get("versions")).get(0);
        return (String) firstVersion.get("name");
    }

}
