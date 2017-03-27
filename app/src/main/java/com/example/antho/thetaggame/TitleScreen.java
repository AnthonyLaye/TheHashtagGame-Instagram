package com.example.antho.thetaggame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleScreen extends AppCompatActivity {

    TextView highScore;
    ImageView tagGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        ImageView tagGame = (ImageView) findViewById(R.id.imageView);

        highScore = (TextView) findViewById(R.id.highScore);
        SharedPreferences prefs = this.getSharedPreferences("HighScore", Context.MODE_PRIVATE);

        int score = prefs.getInt("HighScore", 0);
        highScore.setText("HighScore: " + score);

    }

    public void onPlay(View v) {

        int score = 0;

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("HighScore", highScore.getText());
        startActivity(intent);
    }
}
