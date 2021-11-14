package com.demo.guessthecarbrandapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView textViewNumberOfRight;
    TextView textViewNumberOfWrong;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.red));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        textViewNumberOfRight = findViewById(R.id.textViewNumberOfRaght);
        textViewNumberOfWrong = findViewById(R.id.textViewNumberOfWrong);
        Intent intent = getIntent();
        if (intent.hasExtra("right") && intent.hasExtra("wrong")) {
            String numberOfRightAnswer = String.valueOf(intent.getIntExtra("right", 0));
            String numberOfWrongAnswer = String.valueOf(intent.getIntExtra("wrong", 0));
            textViewNumberOfRight.setText(numberOfRightAnswer);
            textViewNumberOfWrong.setText(numberOfWrongAnswer);
        }
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}