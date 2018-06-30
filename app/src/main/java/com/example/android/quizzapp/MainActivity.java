package com.example.android.quizzapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.OffsetDateTime;

public class MainActivity extends AppCompatActivity {


    private TextView timerDisplay;
    private String duration;

    private int quizTotalScore = 5;
    private int quizScore = 0;
    private String userResponse;
    private String username;
    private String finalAnswer;
    private String ansQuestion1, ansQuestion2, ansQuestion3, ansQuestion4, ansQuestion5;
    private String displayResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast toast = Toast.makeText(MainActivity.this, getString(R.string.oncreate_toast_message), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        duration = displayTimer();

        setTitle("Quiz App");
        getSupportActionBar().setIcon(R.drawable.icon);


    }

    /**
     * @param answerQuestion1 get correct option for question number 1
     * @param answerQuestion2 get correct option for question number 2
     * @param answerQuestion5 get correct option for question number 5
     * @param answerQuestion4 get correct option for question number 4
     * @param answerQuestion3 get correct option for question number 3
     * @param score           gets total correct score by the user
     * @param total           store total score for the quiz
     * @return a string
     */
    public String createQuizSummary(String answerQuestion1, String answerQuestion2, String answerQuestion3, String answerQuestion4, String answerQuestion5, int score, int total, String user) {

        //stop timer once user click submit button
        TimerTextHelper timerTextHelper = new TimerTextHelper(timerDisplay);
        timerTextHelper.stop();

        finalAnswer = String.format("Dear Mr/Mrs " + user.toUpperCase());
        finalAnswer += "\n" + getString(R.string.score) + " " + score + getString(R.string.outof) + " " + total +"question";
        finalAnswer += "\n" + getString(R.string.question1_label) + "\t" + answerQuestion1;
        finalAnswer += "\n" + getString(R.string.question2_label) + "\t" + answerQuestion2;
        finalAnswer += "\n" + getString(R.string.question3_label) + "\t" + answerQuestion3;
        finalAnswer += "\n" + getString(R.string.question4_label) + "\t" + answerQuestion4;
        finalAnswer += "\n" + getString(R.string.question5_label) + "\t" + answerQuestion5;
        return finalAnswer;

    }

    public String displayTimer() {
        timerDisplay = findViewById(R.id.timer_text_view);

        TimerTextHelper timerTextHelper = new TimerTextHelper(timerDisplay);
        timerTextHelper.start();
        return timerTextHelper.toString();

    }

    /**
     * This method get called when user click on submit button
     * and process user answers
     *
     * @param view display the score view to the user
     */
    public void quizResult(View view) {

        //get the user name
        EditText usernameText = (EditText) findViewById(R.id.name_edit_text);
        username = usernameText.getText().toString();
        //check if the EditText if left unfilled
        if (username.isEmpty()) {
            //ask the user to enter their name
            Toast toast = Toast.makeText(MainActivity.this, getString(R.string.user_with_no_name), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            return;
        }

        //confirm if user check any of the checkbox in question number 1
        CheckBox question1_optionA = findViewById(R.id.question1_optionA_check_box);
        CheckBox question1_optionB = findViewById(R.id.question1_optionB_check_box);
        CheckBox question1_optionD = findViewById(R.id.question1_optionD_check_box);
        CheckBox question1_optionC = findViewById(R.id.question1_optionC_check_box);

        //if user check the correct answer and not the incorrect answer add 1 to his scores
        if (question1_optionA.isChecked() && question1_optionB.isChecked()
                && question1_optionD.isChecked() && !question1_optionC.isChecked())
            quizScore = quizScore + 1;

        //checks user response to question number 4
        EditText questionFour = findViewById(R.id.question4_edit_text);
        userResponse = questionFour.getText().toString().toLowerCase().trim();

        //if EditText is filled and answer is correct add 1 to his scores
        if (!userResponse.isEmpty() && userResponse.equals(getString(R.string.question4_ans)))
            quizScore = quizScore + 1;

        //Checks all the options and pick the correct answer to question number 5
        CheckBox question5_optionA = findViewById(R.id.question5_optionA_check_box);
        CheckBox question5_optionB = findViewById(R.id.question5_optionB_check_box);
        CheckBox question5_optionC = findViewById(R.id.question5_optionC_check_box);
        CheckBox question5_optionD = findViewById(R.id.question5_optionD_check_box);
        if (question5_optionA.isChecked() && question5_optionB.isChecked()
                && !question5_optionC.isChecked() && !question5_optionD.isChecked())
            quizScore = quizScore + 1;

        //get the correct answers to all questions from string.xml
        // and append to a toast for user view
        ansQuestion1 = getString(R.string.question_1_option_A_check_box) + "\n";
        ansQuestion1 += getString(R.string.question_1_option_B_check_box) + "\n";
        ansQuestion1 += getString(R.string.question_1_option_D_check_box);

        ansQuestion5 = getString(R.string.question_5_option_A_check_box);
        ansQuestion5 += "\n" + getString(R.string.question_5_option_B_check_box);

        ansQuestion2 = getString(R.string.question_2_option_D_radio_button);

        ansQuestion3 = getString(R.string.question_3_option_A_radio_button);
        ansQuestion4 = getString(R.string.question4_ans);

        displayResult = createQuizSummary(ansQuestion1, ansQuestion2, ansQuestion3,
                ansQuestion4, ansQuestion5, quizScore, quizTotalScore, username);

        Toast toast = Toast.makeText(MainActivity.this, displayResult, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        //displace submit button so that user do not have ability to change
        Button submit = (Button) findViewById(R.id.submit_button);
        submit.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(Intent.ACTION_SEND);
        //intent.setData(Uri.parse("mailto"));
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, "Test@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Quiz Result");
        intent.putExtra(Intent.EXTRA_TEXT,displayResult);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    public class TimerTextHelper implements Runnable {
        private final Handler handler = new Handler();
        private final TextView textView;
        private volatile long startTime;
        private volatile long elapsedTime;

        public TimerTextHelper(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            textView.setText(String.format("%d:%02d", minutes, seconds));

            if (elapsedTime == -1) {
                handler.postDelayed(this, 500);
            }
        }

        public void start() {
            this.startTime = System.currentTimeMillis();
            this.elapsedTime = -1;
            handler.post(this);
        }

        public void stop() {
            this.elapsedTime = System.currentTimeMillis() - startTime;
            handler.removeCallbacks(this);
        }

        public long getElapsedTime() {
            return elapsedTime;
        }
    }

    /**
     * This methods check if user answer for question number 2 is correct
     *
     * @param view
     */
    public void onRadioButtonQuestion2Clicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.question2_optionB_radio_button:
                if (checked)
                    quizScore = quizScore + 1;
                break;
        }
    }

    /**
     * This methods check if user answer for question number 3 is correct
     *
     * @param view
     */
    public void onRadioButtonQuestion3Clicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.question3_optionA_radio_button:
                if (checked)
                    quizScore = quizScore + 1;
                break;
        }
    }


}