package com.hokai.circletextview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hokai.www.library.CircleTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CircleTextView circleTextView = (CircleTextView) findViewById(R.id.tv);
        circleTextView.setTopText("2.8").setBottomText("u mol/L").setStatusText("↓").invalidate();

    }
}
