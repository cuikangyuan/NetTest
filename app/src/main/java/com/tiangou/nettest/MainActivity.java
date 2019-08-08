package com.tiangou.nettest;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    
    Button button1;

    Button button2;

    Button button3;

    Button button4;

    Button button5;

    Button button6;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button1 = findViewById(R.id.button1);


        button2 = findViewById(R.id.button2);

        button3 = findViewById(R.id.button3);

        button4 = findViewById(R.id.button4);

        button5 = findViewById(R.id.button5);

        button6 = findViewById(R.id.button6);


        textView = findViewById(R.id.textview);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        useHttpClientGet("http://www.baidu.com");

                    }
                }).start();


            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        useHttpClientPost("http://ip.taobao.com/service/getIpInfo.php");


                    }
                }).start();


            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        useHttpUrlConnectionPost("http://ip.taobao.com/service/getIpInfo.php");


                    }
                }).start();
            }
        });


        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                useVolleyGet();
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                useOkHttpGet();
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                useOkHttpPost();
            }
        });
    }


    private void useOkHttpGet() {


        Log.d(TAG, "useOkHttpGet: Thread >>> " + Thread.currentThread().getName());

        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();

        requestBuilder.url("http://www.baidu.com");

        requestBuilder.method("GET", null);

        okhttp3.Request request = requestBuilder.build();

        OkHttpClient okHttpClient = new OkHttpClient();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.d(TAG, "useOkHttpGet onFailure: e >>> "  + e.toString());
                Log.d(TAG, "useOkHttpGet onFailure: Thread >>> " + Thread.currentThread().getName());


            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                String str = response.body().string();

                Log.d(TAG, "useOkHttpGet onResponse: >>> " + str);
                Log.d(TAG, "useOkHttpGet onResponse: Thread >>> " + Thread.currentThread().getName());


            }
        });

    }

    private void useOkHttpPost() {

        RequestBody requestBody = new FormBody.Builder()
                .add("ip", "59.108.54.37")
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://ip.taobao.com/service/getIpInfo.php")
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.d(TAG, "useOkHttpGet onFailure: e >>> "  + e.toString());

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                String str = response.body().string();

                Log.d(TAG, "useOkHttpGet onResponse: >>> " + str);

            }
        });

    }


    private void useVolleyGet() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "https://www.baidu.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, "onResponse: " + response);
                        
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d(TAG, "onErrorResponse: " + error);

                    }
                });


        requestQueue.add(stringRequest);

    }

    private void useHttpUrlConnectionPost(String url) {

        InputStream inputStream = null;


        HttpURLConnection httpURLConnection = UrlConnManager.getHttpURLConnection(url);

        try {


            List<NameValuePair> postParams = new ArrayList<>();

            postParams.add(new BasicNameValuePair("ip", "59.108.54.37"));


            UrlConnManager.postParams(httpURLConnection.getOutputStream(), postParams);


            httpURLConnection.connect();


            inputStream = httpURLConnection.getInputStream();

            int code = httpURLConnection.getResponseCode();

            String response = convertStreamToString(inputStream);


            Log.d(TAG, "useHttpUrlConnectionPost: code >>> " + code + " response >>> " + response);


            inputStream.close();




        } catch (Exception e) {


            e.printStackTrace();

        }


    }

    private void useHttpClientPost(String url) {


        try {
            HttpPost httpPost = new HttpPost(url);

            httpPost.addHeader("Connection", "Keep-Alive");


            HttpClient httpClient = createHttpClient();

            List<NameValuePair> postParams = new ArrayList<>();

            postParams.add(new BasicNameValuePair("ip", "59.108.54.37"));


            httpPost.setEntity(new UrlEncodedFormEntity(postParams));

            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();

            int code = httpResponse.getStatusLine().getStatusCode();


            if (null != httpEntity) {


                InputStream inputStream = httpEntity.getContent();

                String response = convertStreamToString(inputStream);


                Log.d(TAG, "useHttpClientPost: code >>> " + code + " response >>> " + response);


                inputStream.close();


            }

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private String convertStreamToString(InputStream is) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));


        StringBuilder stringBuilder = new StringBuilder();

        String line = null;

        while ((line = bufferedReader.readLine()) != null) {

            stringBuilder.append(line + "\n");

        }

        String response = stringBuilder.toString();

        return response;

    }

    private void useHttpClientGet(String url) {


        HttpGet httpGet = new HttpGet(url);

        httpGet.addHeader("Connection", "Keep-Allive");


        try {


            HttpClient httpClient = createHttpClient();


            HttpResponse httpResponse = httpClient.execute(httpGet);

            HttpEntity httpEntity = httpResponse.getEntity();

            int code = httpResponse.getStatusLine().getStatusCode();


            if (null != httpEntity) {


                InputStream inputStream = httpEntity.getContent();

                String response = convertStreamToString(inputStream);


                Log.d(TAG, "useHttpClientGet: code >>> " + code + " response >>> " + response);
                
                
                inputStream.close();


            }



        } catch (IOException e) {

            e.printStackTrace();
        }



    }



    private HttpClient createHttpClient() {

        HttpParams httpParams = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(httpParams, 15000);

        HttpConnectionParams.setSoTimeout(httpParams, 15000);

        HttpConnectionParams.setTcpNoDelay(httpParams, true);


        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

        HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);


        HttpProtocolParams.setUseExpectContinue(httpParams, true);

        HttpClient httpClient = new DefaultHttpClient(httpParams);


        return httpClient;

    }

}
