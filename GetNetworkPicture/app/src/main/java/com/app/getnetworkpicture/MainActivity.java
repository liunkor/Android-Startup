package com.app.getnetworkpicture;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity implements View.OnClickListener {

    private ImageView imageView;

    private Button getImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image_1);
        getImage = (Button) findViewById(R.id.getImage);
        getImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            String url = "http://pic2.zhimg.com/2bd4ff42e52b1bf1cbb36723413f7e11_b.jpg";
            new DownloadImageTask().execute(url);
        } else {
            Toast.makeText(this, "Network connected failed", Toast.LENGTH_SHORT).show();
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, String> {

        private Bitmap bitmap;

        @Override
        protected String doInBackground(String... urls) {    //DowloadImageTask 对象调用execute()方法是调用该方法

            return downloadImage(urls[0]);
        }

        @Override
        protected void onPostExecute(String s) {  //将结果呈现到main activity的UI上

            imageView.setImageBitmap(bitmap);  // 注意改变imageView代码的位置
        }

        private String downloadImage(String url) {
            InputStream is = null;
            try {
                URL imageUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(20000);
                conn.setReadTimeout(20000);
                conn.setDoInput(true);
                conn.connect();
                if(conn.getResponseCode() != 200) {
                    Toast.makeText(MainActivity.this, "Get image failed", Toast.LENGTH_SHORT).show();
                    return "Failed";
                }
                is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "OK";
        }
    }


}
