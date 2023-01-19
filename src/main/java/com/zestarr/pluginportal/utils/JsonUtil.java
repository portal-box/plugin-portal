package com.zestarr.pluginportal.utils;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JsonUtil {
    public static String getJsonData(int id) {
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
            try (Response response = client.newCall(request).execute()) {

                // Check the status code
                int status = response.code();
                if (status == 200) {
                    // Read the response body
                    return response.body().string();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getPluginJson() {
        try {
            OkHttpClient client = new OkHttpClient();

            // Build the URL for the API request
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("raw.githubusercontent.com")
                    .addPathSegment("portal-box")
                    .addPathSegment("plugin-portal")
                    .addPathSegment("master")
                    .addPathSegment("resources")
                    .addPathSegment("PluginList.json")
                    .build();

            // Create the request
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            // Send the request and get the response
            try (Response response = client.newCall(request).execute()) {

                // Check the status code
                int status = response.code();
                if (status == 200) {
                    // Read the response body
                    return response.body().string();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}