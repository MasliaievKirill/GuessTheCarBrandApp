package com.demo.guessthecarbrandapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void onClickChooseCountry(View view) {
        RadioButton radioButton = (RadioButton) view;
        int id = radioButton.getId();
        switch (id) {
            case R.id.radioButton0:
                url = "https://mashintop.ru/brands.php?country_id=152";
                break;
            case R.id.radioButton1:
                url = "https://mashintop.ru/brands.php?country_id=52";
                break;
            case R.id.radioButton2:
                url = "https://mashintop.ru/brands.php?country_id=197";
                break;
            case R.id.radioButton3:
                url = "https://mashintop.ru/brands.php?country_id=38";
                break;
        }
    }

    public void onClickStartGame(View view) {
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}