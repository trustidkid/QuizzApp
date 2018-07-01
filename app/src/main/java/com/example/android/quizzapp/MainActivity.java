package com.example.android.quizzapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.OffsetDateTime;

public class MainActivity extends AppCompatActivity {

    private int quizTotalScore = 5;
    private int quizScore = 0;
    private String userResponse;
    private String username;
    private String finalAnswer;
    private String ansQuestion1, ansQuestion2, ansQuestion3, ansQuestion4, ansQuestion5;
    private String displayResult;
    private String cheerMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //display username on the Action bar
        TextView showUserInActionBar = findViewById(R.id.user_name_text_view);
        showUserInActionBar.setText(getIntent().getStringExtra("username"));

        //set up countdown for user
        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                TextView mTextField = findViewById(R.id.timer_text_view);
                mTextField.setText(getString(R.string.timer) + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //disable submit button when time elapse
                Button submit = findViewById(R.id.submit_button);
                submit.setEnabled(false);
                TextView mTextField = findViewById(R.id.timer_text_view);
                mTextField.setText(getString(R.string.time_up));

            }
        }.start();

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

        //get username from signin activity
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        if (quizScore <= 2) {
            cheerMessage = getString(R.string.cheer_message_fail);
        } else cheerMessage = getString(R.string.cheer_message_pass);

        finalAnswer = "Dear Awesome" + username + ",\n";
        finalAnswer += "\n" + cheerMessage;
        finalAnswer += "\n" + getString(R.string.score) + " " + score + "\n";
        finalAnswer += "\n" + getString(R.string.question1_label) + "\t" + answerQuestion1;
        finalAnswer += "\n" + getString(R.string.question2_label) + "\t" + answerQuestion2;
        finalAnswer += "\n" + getString(R.string.question3_label) + "\t" + answerQuestion3;
        finalAnswer += "\n" + getString(R.string.question4_label) + "\t" + answerQuestion4;
        finalAnswer += "\n" + getString(R.string.question5_label) + "\t" + answerQuestion5;
        return finalAnswer;

    }

    /**
     * This method get called when user click on submit button
     * and process user answers
     *
     * @param view display the score view to the user
     */
    public void quizResult(View view) {

        //confirm if user check any of the checkbox in question number 1
        CheckBox question1_optionA = findViewById(R.id.question1_optionA_check_box);
        CheckBox question1_optionB = findViewById(R.id.question1_optionB_check_box);
        CheckBox question1_optionD = findViewById(R.id.question1_optionD_check_box);
        CheckBox question1_optionC = findViewById(R.id.question1_optionC_check_box);

        //if user check the correct answer and not the incorrect answer add 1 quizScore
        if (question1_optionA.isChecked() && question1_optionB.isChecked()
                && question1_optionD.isChecked() && !question1_optionC.isChecked())
            quizScore = quizScore + 1;

        //checks user response to question number 4
        EditText questionFour = findViewById(R.id.question4_edit_text);
        userResponse = questionFour.getText().toString().toLowerCase().trim();

        //if EditText is filled and answer is correct add quizScore
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

        //display result in toast
        Toast toast = Toast.makeText(MainActivity.this, displayResult, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        //displace submit button so that user do not have ability to change
        Button submit = findViewById(R.id.submit_button);
        submit.setEnabled(false);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, username + ": Quiz Result");
        intent.putExtra(Intent.EXTRA_TEXT,displayResult);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    /**
     * This methods check if user answer for question number 2 is correct
     *
     * @param view get the group radio view
     */
    public void onRadioButtonQuestion2Clicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        RadioGroup radioSexGroup = findViewById(R.id.radioquestion2);

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.question2_optionB_radio_button:
                if (checked) {
                    quizScore = quizScore + 1;
                    radioSexGroup.setEnabled(false);
                }
                break;
        }
    }


    /**
     * This methods check if user answer for question number 3 is correct
     *
     * @param view gets group radio view
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

        RadioButton hss = findViewById(R.id.question3_optionA_radio_button);

    }

}