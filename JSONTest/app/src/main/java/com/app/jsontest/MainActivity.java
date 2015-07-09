package com.app.jsontest;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button getJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getJSON = (Button) findViewById(R.id.get_json);
        getJSON.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.get_json) {
            ConnectivityManager connectivityManager = (ConnectivityManager ) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if( networkInfo != null && networkInfo.isConnected()) {
                sendRequestWithHttpURLConnection();
                Log.d("NetworkdConnectStatus", "connected");
            }
            else {
                Log.d("NetworkdConnectStatus", "unconnected");
            }
        }
    }

    private void sendRequestWithHttpURLConnection() {
       new Thread(new Runnable() {
           @Override
           public void run() {
               HttpURLConnection connection = null;
               try {
                   URL url = new URL("http://10.21.66.108/get_data.json");
                   connection = (HttpURLConnection) url.openConnection();
                   connection.setRequestMethod("GET");
                   connection.setConnectTimeout(18000);
                   connection.setReadTimeout(18000);
                   InputStream in = connection.getInputStream();  // 不能在主线程中调用它
                   BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                   StringBuilder response = new StringBuilder();
                   String line;
                   while ((line = reader.readLine()) != null) {
                       response.append(line);
                   }

                   //parseJSONWithJSONObject(response.toString());
                   parseJSONWithGSON(response.toString());

               } catch (Exception e) {
                   e.printStackTrace();
               } finally {
                   connection.disconnect();
               }
           }
       }).start();
    }

    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int  i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
                Log.d("MMMainActivity", "id is " + id);
                Log.d("MMMainActivity", "name is " + name);
                Log.d("MMMainActivity", "version is " + version);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        List<App> appList = gson.fromJson(jsonData, new TypeToken<List<App>>(){}.getType());
        for (App app : appList) {
            Log.d("MainActivity", "id is " + app.getId());
            Log.d("MainActivity", "name is " + app.getName());
            Log.d("MainActivity", "version is " + app.getVersion());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
