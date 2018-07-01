package com.example.android.quizzapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignIn extends AppCompatActivity {

    private String username;
    private String greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

    }

    public void startQuiz(View view) {

        //get the user name
        EditText usernameText = findViewById(R.id.name_edit_text);
        username = usernameText.getText().toString();

        //check if the EditText if left unfilled
        if (username.isEmpty()) {
            //ask the user to enter their name
            Toast toast = Toast.makeText(SignIn.this, getString(R.string.user_with_no_name), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            return;
        }

        //send sign in detail to main activity
        Intent intent = new Intent(SignIn.this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);

        //display a message in toast
        greeting = getString(R.string.greeting) + username + "\n" + getString(R.string.on_user_sign_in);
        Toast toast = Toast.makeText(SignIn.this, greeting, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }


}
