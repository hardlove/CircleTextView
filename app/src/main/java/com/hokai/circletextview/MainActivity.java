package com.hokai.circletextview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CircleTextView circleTextView = (CircleTextView) findViewById(R.id.tv);
        circleTextView.setBorderColor(Color.RED).setTextColor(Color.BLUE).setTopText("24.598").setBottomText("u mol/L").setStatusText("").invalidate();

    }
}
