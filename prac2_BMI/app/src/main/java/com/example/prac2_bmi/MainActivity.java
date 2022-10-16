package com.example.prac2_bmi;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
public class MainActivity extends AppCompatActivity {

    Button yesBtn, noBtn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //buttons
        yesBtn = findViewById(R.id.lay01btnYes);
        noBtn = findViewById(R.id.lay01btnNo);

        final String snackBarMessage = "Only ages 20 and over are supported"; //Hardcoded

        //displays message to user
        Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main), snackBarMessage, Snackbar.LENGTH_SHORT);

        /* No button click */
        noBtn.setOnClickListener(view -> snackbar.show());
        /* Yes button click */
        yesBtn.setOnClickListener(view -> switchActivities());
    }

    private void switchActivities() {
        intent = new Intent(MainActivity.this, UnitChoiceActivity.class);
        startActivity(intent);
    }
}