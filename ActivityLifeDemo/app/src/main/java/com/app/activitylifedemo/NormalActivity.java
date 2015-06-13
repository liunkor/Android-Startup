package com.app.activitylifedemo;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Administrator on 2015/6/5.
 */
public class NormalActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_layout);
    }
}
