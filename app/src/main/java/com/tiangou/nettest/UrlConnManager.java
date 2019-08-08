package com.tiangou.nettest;

import android.text.TextUtils;
import android.util.Log;

import org.apache.http.NameValuePair;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class UrlConnManager {


    private static final String TAG = "UrlConnManager";

    public static void postParams(OutputStream outputStream, List<NameValuePair> paramList) {


        try {

            StringBuilder stringBuilder = new StringBuilder();


            for (NameValuePair pair : paramList) {

                if (!TextUtils.isEmpty(stringBuilder)) {


                    stringBuilder.append("&");

                }

                stringBuilder.append(URLEncoder.encode(pair.getName(), "UTF-8"));

                stringBuilder.append("=");

                stringBuilder.append(URLEncoder.encode(pair.getValue(), "UTF-8"));


            }

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8")
            );

            Log.d(TAG, "postParams: >>>  " + stringBuilder.toString());
            
            writer.write(stringBuilder.toString());

            writer.flush();

            writer.close();

        } catch (Exception e) {

            e.printStackTrace();

        }






    }

    public static HttpURLConnection getHttpURLConnection(String url) {



        HttpURLConnection httpURLConnection = null;

        try {


            URL url1 = new URL(url);

            httpURLConnection = (HttpURLConnection) url1.openConnection();

            httpURLConnection.setReadTimeout(15000);

            httpURLConnection.setRequestMethod("POST");

            //httpURLConnection.setRequestMethod("GET");

            //Header
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");


            httpURLConnection.setDoInput(true);


            httpURLConnection.setDoOutput(true);


        } catch (Exception e) {


            e.printStackTrace();
        }


        return httpURLConnection;

    }
}
