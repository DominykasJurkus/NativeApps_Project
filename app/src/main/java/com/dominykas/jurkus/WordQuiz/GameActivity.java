package com.dominykas.jurkus.WordQuiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SCORE = "score";
    private int score;

    private int presCounter = 0;
    private int maxPresCounter = 4;

    private String[] keys1 = {"R", "I", "B", "D", "X"};
    private String[] keys2 = {"H", "A", "N", "D", "O"};
    private String[] keys3 = {"F", "I", "S", "H", "E"};
    private String[] keys4 = {"D", "R", "U", "M", "A"};
    private String[] keys5 = {"B", "O", "O", "K", "C"};
    private String[] keys6 = {"B", "E", "L", "L", "Y"};
    private String[] keys7 = {"G", "O", "A", "T", "E"};

    private String textAnswer;
    private String textAnswer1 = "BIRD";
    private String textAnswer2 = "HAND";
    private String textAnswer3 = "FISH";
    private String textAnswer4 = "DRUM";
    private String textAnswer5 = "BOOK";
    private String textAnswer6 = "BELL";
    private String textAnswer7 = "GOAT";

    static int randomNum;

    String[][] keySets = {keys1, keys2, keys3, keys4, keys5, keys6, keys7};
    String[] AnswerArray = {textAnswer1, textAnswer2, textAnswer3, textAnswer4, textAnswer5, textAnswer6, textAnswer7};

    TextView textScreen, textQuestion, textTitle;
    Animation smallbigforth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        smallbigforth = AnimationUtils.loadAnimation(this, R.anim.smallbigforth);

        String[] keys = shuffleArray(getRandomKeySet(keySets));

        for (String key : keys) {
            addView(((LinearLayout) findViewById(R.id.layoutParent)), key, ((EditText) findViewById(R.id.editText)));
        }

        loadScore();
        setScore();

        maxPresCounter = 4;
    }

    public static String[] getRandomKeySet(String[][] array) {
        randomNum = new Random().nextInt(array.length);
        return array[randomNum];
    }

    public String getAnswer()
    {
        return AnswerArray[randomNum];
    }

    private void loadScore() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        score = sharedPreferences.getInt(SCORE, 0);
    }

    private void saveScore(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SCORE, score);

        editor.apply();
    }

    private void setScore(){
        TextView scoreText = (TextView) findViewById(R.id.textGameScore);
        scoreText.setText("Score: " + score);
    }


    private String[] shuffleArray(String[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }


    private void addView(LinearLayout viewParent, final String text, final EditText editText) {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        linearLayoutParams.rightMargin = 20;

        final TextView textView = new TextView(this);

        textView.setLayoutParams(linearLayoutParams);
        textView.setBackground(this.getResources().getDrawable(R.drawable.bgpink));
        textView.setTextColor(this.getResources().getColor(R.color.colorPurple));
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTextSize(32);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/FredokaOneRegular.ttf");

        textQuestion = (TextView) findViewById(R.id.textQuestion);
        textScreen = (TextView) findViewById(R.id.textGameScore);
        textTitle = (TextView) findViewById(R.id.textTitle);

        textQuestion.setTypeface(typeface);
        textScreen.setTypeface(typeface);
        textTitle.setTypeface(typeface);
        editText.setTypeface(typeface);
        textView.setTypeface(typeface);

        textView.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(presCounter < maxPresCounter) {
                    if (presCounter == 0)
                        editText.setText("");

                        editText.setText(editText.getText().toString() + text);
                        textView.startAnimation(smallbigforth);
                        textView.animate().alpha(0).setDuration(300);
                        presCounter++;

                        if (presCounter == maxPresCounter)
                            doValidate();
                }
            }
        });


        viewParent.addView(textView);


    }


    private void doValidate() {
        presCounter = 0;

        EditText editText = findViewById(R.id.editText);
        LinearLayout linearLayout = findViewById(R.id.layoutParent);

        textAnswer = getAnswer();

        if(editText.getText().toString().equals(textAnswer)) {
//            Toast.makeText(GameActivity.this, "Correct", Toast.LENGTH_SHORT).show();

            score += 1;

            saveScore();

            Intent a = new Intent(GameActivity.this, GameActivity.class);
            startActivity(a);

            editText.setText("");
        } else {
           // Toast.makeText(GameActivity.this, "Wrong", Toast.LENGTH_SHORT).show();

            Intent a = new Intent(GameActivity.this,GameOverActivity.class);
            startActivity(a);

            editText.setText("");
        }

        /*keys = shuffleArray(keys);
        linearLayout.removeAllViews();
        for (String key : keys) {
            addView(linearLayout, key, editText);
        }*/

    }


}
