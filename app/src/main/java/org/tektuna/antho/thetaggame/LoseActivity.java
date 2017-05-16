package org.tektuna.antho.thetaggame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.games.Games;

public class LoseActivity extends AppCompatActivity {

    TextView ScoreCtr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose);

        int score = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           score = extras.getInt("Score");
        }

        SharedPreferences prefs = this.getSharedPreferences("HighScore", Context.MODE_PRIVATE);
        int currentHighScore = prefs.getInt("HighScore", 0);
        if(score > currentHighScore) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("HighScore", score);
            edit.apply();
            Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.LEADERBOARD_ID), score);
        }

        ScoreCtr = (TextView) findViewById(R.id.ScotreCtr);
        ScoreCtr.setText("Score: " + score);
    }


    public void playAgain(View v){
        Intent myIntent = new Intent(this, GameActivity.class);
        startActivity(myIntent);
        finish();
    }
    public void mainMenu(View v){
        Intent myIntent = new Intent(this, TitleScreen.class);
        startActivity(myIntent);
        finish();
    }
}
