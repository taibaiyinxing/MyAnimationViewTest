package com.example.yuguotao.mytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TestView2 v = (TestView2) findViewById(R.id.test);
//        v.initKeyPoints(4);
        v.startInitAnim();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
