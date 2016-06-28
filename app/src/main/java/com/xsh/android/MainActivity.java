package com.xsh.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xsh.android.activity.ScollViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doIntentScroll(View view){
        Intent intent =new Intent(this, ScollViewActivity.class);
        startActivity(intent);
    }
}
