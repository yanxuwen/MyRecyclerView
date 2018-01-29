package com.example.xrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.xrecyclerview.PinnedHeader.PinnedHeaderActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void goto1(View v) {
        Intent intent = new Intent();
        intent.setClass(this,LinearActivity.class);
        startActivity(intent);
    }
    public void goto2(View v) {
        Intent intent = new Intent();
        intent.setClass(this,LinearActivity2.class);
        startActivity(intent);
    }
    public void goto3(View v) {
        Intent intent = new Intent();
        intent.setClass(this,LinearActivity3.class);
        startActivity(intent);
    }
    public void goto4(View v) {
        Intent intent = new Intent();
        intent.setClass(this,PinnedHeaderActivity.class);
        startActivity(intent);
    }

    public void goto5(View v) {
        Intent intent = new Intent();
        intent.setClass(this, EmptyViewActivity.class);
        startActivity(intent);
    }
    public void goto6(View v) {
        Intent intent = new Intent();
        intent.setClass(this, GridActivity.class);
        startActivity(intent);
    }
    public void goto7(View v) {
        Intent intent = new Intent();
        intent.setClass(this, StaggeredGridActivity.class);
        startActivity(intent);
    }
    public void goto8(View v) {
        Intent intent = new Intent();
        intent.setClass(this, customListView.class);
        startActivity(intent);
    }
}
