package com.example.prac2_bmi;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ResultsActivity extends AppCompatActivity {

    Button minusBtn, plusBtn;
    TextView height, weight, bmi, classification, yourBmi;
    BMICalc bmiCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        //layout
        yourBmi = findViewById(R.id.lay04txtYourBMI);
        height = findViewById(R.id.lay04txtYourHeight);
        weight = findViewById(R.id.lay04txtYourWeight);
        bmi = findViewById(R.id.lay04txtBMI);
        classification = findViewById(R.id.lay04txtClassification);
        //buttons
        minusBtn = findViewById(R.id.lay04btnMinus);
        plusBtn = findViewById(R.id.lay04btnPlus);

        //gets object
        bmiCalc = (BMICalc) getIntent().getParcelableExtra("bmi_calc");
        DecimalFormat numberFormat = new DecimalFormat("#.00");

        weight.setText(String.format("Your weight: %s %s", bmiCalc.getUsrWeight(), bmiCalc.getSymbols(0)));
        height.setText(String.format("Your height: %s %s", bmiCalc.getUsrHeight(), bmiCalc.getSymbols(1)));
        bmi.setText(numberFormat.format(bmiCalc.generateBMI()));

        /* displays result */
        int cat = bmiCalc.getCategory();
        classification.setText(bmiCalc.generateCategory(cat));
        /* Sets background colour of result */
        int clr = Color.BLACK;
        switch (cat) {
            case 0:
                clr = Color.GRAY;
                break;
            case 1:
                clr = Color.GREEN;
                break;
            case 2:
                clr = Color.CYAN;
                break;
            case 3:
                clr = Color.YELLOW;
                break;
            case 4:
                clr = Color.MAGENTA;
                break;
            case 5:
                clr = Color.RED;
                break;
        }
        classification.setBackgroundColor(clr);

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseText(classification);
                decreaseText(bmi);
                decreaseText(height);
                decreaseText(weight);
                decreaseText(yourBmi);
            }
        });
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseText(classification);
                increaseText(bmi);
                increaseText(height);
                increaseText(weight);
                increaseText(yourBmi);
            }
        });
    }
    //TODO: Add fragment so buttons only show up in table mode

    private void increaseText(TextView tv){
        float size= tv.getTextSize();
        Log.d("size", "increaseText: "+size);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size*1.1F);
    }

    private void decreaseText(TextView tv){
        float size= tv.getTextSize();
        Log.d("size", "decreaseText: "+size);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size*0.9F);
    }
}