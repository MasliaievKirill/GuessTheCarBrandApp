package com.demo.guessthecarbrandapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean connection = false;

    private String url = "https://mashintop.ru/brands.php?country_id=152";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.red));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        checkConnection();
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
        if (connection) {
            Intent intent = new Intent(this, PlayActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Отсутствует Интернет-соединение", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkConnection () {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connection = true;
        }
    }


}