package com.example.datebook.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ApiService {

    public ApiService() { }

    public String  AddressLookUp(String latitude, String longitude) {
        String url;
        String apiKey;
        String response;
        HttpsURLConnection mConn;
        StringBuilder mBuilder = new StringBuilder();

        apiKey = "AIzaSyBK7T8h0JBJ4OMcotsWUYeoL8CeHgm9Lh4";
        url = "https://maps.googleapis.com/maps/api/geocode/json?key="+apiKey+"&latlng="+latitude+","+longitude+"&sensor=true";

        try {

            mConn = (HttpsURLConnection) (new URL(url).openConnection());
            mConn.setRequestMethod("GET");
            mConn.connect();

            BufferedReader mReader = new BufferedReader(new InputStreamReader(mConn.getInputStream()));

            while ((response = mReader.readLine()) != null) {
                mBuilder.append(response);
            }

            mReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mBuilder.toString();
    }
}
