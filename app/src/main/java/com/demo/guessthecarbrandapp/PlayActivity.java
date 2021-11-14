package com.demo.guessthecarbrandapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayActivity extends AppCompatActivity {

    private String url;

    private ImageView imageView;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;

    private ArrayList<String> urls;
    private ArrayList<String> brands;
    private ArrayList<Button> buttons;

    private int numberOfQuestion;
    private int numberOfRightAnswer;

    private int rightAnswers = 0;
    private int wrongAnswers = 0;
    private int clickNumber = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.red));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Intent intent = getIntent();
        if (intent.hasExtra("url")) {
            url = intent.getStringExtra("url");
        }
        imageView = findViewById(R.id.imageViewBrand);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        urls = new ArrayList<>();
        brands = new ArrayList<>();
        buttons = new ArrayList<>();
        buttons.add(button0);
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        getContent();
        playGame();
    }

    public void onClickAnswer(View view) {
        if (clickNumber < 10) {
            Button button = (Button) view;
            String tag = button.getTag().toString();
            if (Integer.parseInt(tag) == numberOfRightAnswer) {
                Toast.makeText(this, "Верно", Toast.LENGTH_SHORT).show();
                rightAnswers++;
            } else {
                Toast.makeText(this, "Неверно, правильный ответ: " + brands.get(numberOfQuestion), Toast.LENGTH_SHORT).show();
                wrongAnswers++;
            }
            clickNumber++;
            playGame();
        } else {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("right", rightAnswers);
            intent.putExtra("wrong", wrongAnswers);
            startActivity(intent);
        }
    }

    private static class DownloadContentTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    result.append(line);
                    line = reader.readLine();
                }
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return null;
        }
    }

    private static class DownloadImageTask extends AsyncTask <String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return null;
        }
    }

    private void getContent (){
        DownloadContentTask task = new DownloadContentTask();
        try {
            String content = task.execute(url).get();
            String start = "<div class=\"row margin-brands\">";
            String finish = " <p>Ниже вы можете посмотреть видео, в котором рассказывается про историю некоторых автомобильных марок.</p>";
            Pattern pattern = Pattern.compile(start + "(.*?)" + finish);
            Matcher matcher = pattern.matcher(content);
            String splitContent = "";
            while (matcher.find()) {
                splitContent = matcher.group(1);
            }
            Pattern patternImg = Pattern.compile(" <img class=\"emblema-img\" src=\"(.*?)\"");
            Pattern patternName = Pattern.compile("alt=\"Значок-эмблема (.*?)\"");
            Matcher matcherImg = patternImg.matcher(splitContent);
            Matcher matcherName = patternName.matcher(splitContent);
            while (matcherImg.find()) {
                urls.add(matcherImg.group(1));
            }
            while (matcherName.find()) {
                brands.add(matcherName.group(1));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void playGame () {
        generateQuestion();
        DownloadImageTask task = new DownloadImageTask();
        try {
            Bitmap bitmap = task.execute(urls.get(numberOfQuestion)).get();
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                for (int i = 0; i < buttons.size(); i++) {
                    if (i == numberOfRightAnswer) {
                        buttons.get(i).setText(brands.get(numberOfQuestion));
                    } else {
                        int wrongAnswer = generateWrongAnswer();
                        buttons.get(i).setText(brands.get(wrongAnswer));
                    }
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void generateQuestion () {
        numberOfQuestion = (int) (Math.random() * brands.size());
        numberOfRightAnswer = (int) (Math.random() * buttons.size());
    }

    private int generateWrongAnswer () {
        return (int) (Math.random() * brands.size());
    }

}