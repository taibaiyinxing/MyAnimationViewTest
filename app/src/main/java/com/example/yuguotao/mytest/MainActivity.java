package com.example.yuguotao.mytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TestView v = (TestView) findViewById(R.id.test);
//        v.initKeyPoints(4);
        v.startInitAnim();
        AnimProgressbar progressbar = (AnimProgressbar) findViewById(R.id.progressbar);
        progressbar.moveToProgress(60);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
