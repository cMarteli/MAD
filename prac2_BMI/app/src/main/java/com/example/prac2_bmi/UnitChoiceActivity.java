package com.example.prac2_bmi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UnitChoiceActivity extends AppCompatActivity {

    Button metricBtn;
    Button imperialBtn;
    BMICalc bmiCalc;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_choice);

        intent = new Intent(UnitChoiceActivity.this, CalculatorActivity.class);
        //buttons layout2
        metricBtn = findViewById(R.id.lay02btnMetric);
        imperialBtn = findViewById(R.id.lay02btnImperial);

        /**
         * Unit System choice
         */
        //metric
        metricBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bmiCalc = new BMICalc("metric");
                wrapUp();
            }
        });
        //imperial
        imperialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bmiCalc = new BMICalc("imperial");
                wrapUp();
            }
        });
    }
    public void wrapUp()
    {
        intent.putExtra("bmi_calc", bmiCalc); //sends bmi_calc object to other activity
        startActivity(intent);
    }
}