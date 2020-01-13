package com.dominykas.jurkus.WordQuiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SCORE = "score";
    private int score;

    TextView textQuestion, textTitle, textBtn;
    ImageView bigboss;
    Animation smalltobig;

    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/FredokaOneRegular.ttf");

        textQuestion = (TextView) findViewById(R.id.textScore);
        textTitle = (TextView) findViewById(R.id.textGameOver);
        textBtn = (TextView) findViewById(R.id.buttonGameOverContinue);

        bigboss = (ImageView) findViewById(R.id.bigboss);
        bigboss.startAnimation(smalltobig);

        textQuestion.setTypeface(typeface);
        textTitle.setTypeface(typeface);
        textBtn.setTypeface(typeface);

        TextView scoreText = (TextView) findViewById(R.id.textScore);
        scoreText.setText("Score: 1");

        loadScore();
        setScore();

        continueButton = findViewById(R.id.buttonGameOverContinue);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                openHomeActivity();
            }
        });
    }

    private void loadScore() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        score = sharedPreferences.getInt(SCORE, 0);
    }

    private void setScore(){
        TextView scoreText = (TextView) findViewById(R.id.textScore);
        scoreText.setText("Score: " + score);
    }

    public void openHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
