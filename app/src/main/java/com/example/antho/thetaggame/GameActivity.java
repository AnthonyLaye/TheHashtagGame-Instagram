package com.example.antho.thetaggame;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.example.antho.thetaggame.R.drawable.button_shape;
import static com.example.antho.thetaggame.R.drawable.greenbutton_shape;


public class GameActivity extends AppCompatActivity {

    public static int score = 0;
    public static int tagQuantity = 319;
    public static int[] quantity = new int[tagQuantity];
    public static String[] hashTags = new String[tagQuantity];
    public static String[] hashPics = new String[tagQuantity];
    public boolean isToggled = false;

    private Button button1, button2;
    private ImageButton button, volToggle;
    private TextView scoreKeep, ViewB1, ViewB2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        score = 0;
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544/6300978111");

        /*  ONLY FOR FINISHED APP
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        */
        AdView mAdView = (AdView) findViewById(R.id.adView);

        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("4DE6D475009CE71562EA9B632A64920B")  // An example device ID
                .build();


        mAdView.loadAd(request);

        volToggle = (ImageButton) findViewById(R.id.volToggle);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isToggled = prefs.getBoolean("Switch", isToggled);

        if(!isToggled)
            volToggle.setBackgroundResource(R.drawable.volon);
        else
            volToggle.setBackgroundResource(R.drawable.voloff);

        readFiles();
    }

    public void readFiles() {

        InputStream input;

        try {
            input = getAssets().open("HashTags.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder out = new StringBuilder();
            String line;
            String[] myArray;

            while ((line = reader.readLine()) != null) {
                out.append(line).append(" ");   //Append each line into a StringBuilder
            }

            reader.close();

            myArray = out.toString().split(" ");
            hashTags[0] = myArray[0];
            quantity[0] = Integer.parseInt(myArray[1]);
            hashPics[0] = "hashtag_0";

            for (int i = 1; i < tagQuantity; i++) {
                hashTags[i] = myArray[2 * i];   //Hashtags are the even numbers
                quantity[i] = Integer.parseInt(myArray[2 * i + 1]); //Quantities are the odd numbers
                hashPics[i] = "hashtag_" + i;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        reLoad();
    }

    public void reLoad() {

        //mySwitch = (Switch) findViewById(R.id.switch1);
        button1 = (Button) findViewById(R.id.buttonq1);
        button2 = (Button) findViewById(R.id.buttonq2);
        button = (ImageButton) findViewById(R.id.button4);
        ViewB1 = (TextView) findViewById(R.id.ViewB1);  //To display number of times b1 used
        ViewB2 = (TextView) findViewById(R.id.ViewB2);
        scoreKeep = (TextView) findViewById(R.id.textView);

        button1.setBackgroundResource(R.drawable.button_shape);
        button2.setBackgroundResource(R.drawable.button_shape);


        ViewB1.setText("");
        ViewB2.setText("");

        int tagID1, tagID2;
        ImageView myImageView2 = (ImageView) findViewById(R.id.imageView2);
        ImageView myImageView1 = (ImageView) findViewById(R.id.imageView3);

        tagID1 = (int) (Math.random() * tagQuantity);
        tagID2 = (int) (Math.random() * tagQuantity);

        if (tagID1 == tagID2)
            tagID2 = (int) (Math.random() * tagQuantity); //Ensure Hashtags aren't the same...hopefully

        button1.setText(hashTags[tagID1]);
        button2.setText(hashTags[tagID2]);

        myImageView1.setImageResource(getImageId(this, hashPics[tagID1]));
        myImageView2.setImageResource(getImageId(this, hashPics[tagID2]));

        scoreKeep.setText("SCORE: " + score);

        button1.setClickable(true);
        button2.setClickable(true);
        button.setClickable(true);
        volToggle.setClickable(true);
        //mySwitch.setClickable(true);
    }



    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    public void buttonClicked(View v) {

        String answer = (String) ((Button) v).getText();   //Get value of text on button pressed
        calculateScore(answer);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("Switch", isToggled);
        editor.apply();
    }

    public void calculateScore(String answer) {

        int answerID = 0, indexTag1 = 0, indexTag2 = 0;
        String tagText1, tagText2;

        button1.setClickable(false);
        button2.setClickable(false);
        button.setClickable(false);
        volToggle.setClickable(false);

        tagText1 = (String) button1.getText();    //Button1 text
        tagText2 = (String) button2.getText();    //Button2 text

        for (int i = 0; i < hashTags.length; i++) {   //Get the ID numbers from the strings by looping through hashTags array
            if (hashTags[i].equals(tagText1)) {
                indexTag1 = i;
            }
            if (hashTags[i].equals(tagText2)) {
                indexTag2 = i;
            }
            if (hashTags[i].equals(answer)) {
                answerID = i;
            }
        }

        if (quantity[answerID] > quantity[indexTag1] || quantity[answerID] > quantity[indexTag2])
            isCorrect(tagText1, tagText2, indexTag1, indexTag2, answer);
        else if (quantity[answerID] < quantity[indexTag1] || quantity[answerID] < quantity[indexTag2])
            isFalse(tagText1, tagText2, indexTag1, indexTag2, answer);
    }


    public void isCorrect(String tagID1, String tagID2, int indexTag1, int indexTag2, String answer){

        if(!isToggled) {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.ding);
            mp.start();
        }

        if(button1.getText().equals(answer))
            button1.setBackgroundResource(R.drawable.greenbutton_shape);
        else if(button2.getText().equals(answer))
            button2.setBackgroundResource(R.drawable.greenbutton_shape);

        score++;
        printTotal(tagID1, tagID2, indexTag1, indexTag2);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                reLoad();
            }
        }, 2600);

    }

    public void isFalse(String tagID1, String tagID2, int indexTag1, int indexTag2, String answer){

        if(!isToggled) {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.wrongok);
            mp.start();
        }

        if(button1.getText().equals(answer))
            button1.setBackgroundResource(R.drawable.redbutton_shape);
        else if(button2.getText().equals(answer))
            button2.setBackgroundResource(R.drawable.redbutton_shape);

        final int scoreHolder = score;
        score = 0;

        printTotal(tagID1, tagID2, indexTag1, indexTag2);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startLoseIntent(scoreHolder);
            }
        }, 2600);
    }

    public void startLoseIntent(int scoreHolder){

        finish();
        Intent myIntent = new Intent(this, LoseActivity.class);
        myIntent.putExtra("Score", scoreHolder);
        startActivity(myIntent);
    }


    public void printTotal(String tagID1, String tagID2, int indexTag1, int indexTag2){

        String total1 = Integer.toString(quantity[indexTag1]);  //Set total times hashtagged1
        String total2 = Integer.toString(quantity[indexTag2]);  //Set total times hashtagged2

        List<String> totalf1 = getCommas(total1);
        List<String> totalf2 = getCommas(total2);

        total1 = "";
        total2 = "";

        for(String s : totalf1){
            total1 += s;
        }
        for(String s : totalf2){
            total2 += s;
        }

        ViewB1.setText(tagID1 + " was used " + total1 + " times");
        ViewB2.setText(tagID2 + " was used " + total2 + " times");
        scoreKeep.setText("SCORE: " + score);
    }

    public List<String> getCommas(String total){  //Add decimals where appropriate in String

        List<String> tot = new ArrayList<String>();
        int adder = 0;

        while(adder < total.length()) {
            tot.add(String.valueOf(total.charAt(adder)));
            adder++;
        }
        int i = tot.size() - 1, ctr = 1;

        while(i != 1){
            ctr++;
            if(ctr == 3){
                ctr = 0;
                tot.add(i - 1, ",");
            }
            i--;
        }

        return tot;
    }

    public void onMenu(View v){

        Intent myIntent = new Intent(this, TitleScreen.class);
        startActivity(myIntent);
    }

    public void volToggle(View v){

        isToggled = !isToggled;

        if(!isToggled)
            volToggle.setBackgroundResource(R.drawable.volon);
        else
            volToggle.setBackgroundResource(R.drawable.voloff);
    }


}







