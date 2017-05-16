package org.tektuna.antho.thetaggame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.games.leaderboard.LeaderboardMetadataResponse;
import com.google.android.gms.games.leaderboard.LoadPlayerScoreResponse;
import com.google.android.gms.games.leaderboard.LoadScoresResponse;
import com.google.android.gms.games.leaderboard.SubmitScoreResponse;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;

public class TitleScreen extends AppCompatActivity{

    TextView highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);



        highScore = (TextView) findViewById(R.id.highScore);
        SharedPreferences prefs = this.getSharedPreferences("HighScore", Context.MODE_PRIVATE);

        int score = prefs.getInt("HighScore", 0);
        highScore.setText("HighScore: " + score);

    }

    public void onPlay(View v) {

        int score = 0;

        Intent intent = new Intent(this, GameActivity.class);
        //intent.putExtra("HighScore", highScore.getText());
        startActivity(intent);
        finish();
    }

    public void onLeaderboard(View v){

        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                getString(R.string.LEADERBOARD_ID)), 2);

    }

}
