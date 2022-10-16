package com.example.prac2_bmi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

public class CalculatorActivity extends AppCompatActivity {

    BMICalc bmiCalc;
    TextView unitDisplay, symbolW, symbolH;
    EditText weightDisplay, heightDisplay;
    SeekBar heightBar, weightBar;
    Button nextBtn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        intent = new Intent(CalculatorActivity.this, ResultsActivity.class);
        //gets calc object
        bmiCalc = (BMICalc) getIntent().getParcelableExtra("bmi_calc");

        //text
        unitDisplay = findViewById(R.id.lay03txtUnitsDisplay);
        symbolW = findViewById(R.id.lay03txtSymbW);
        symbolH = findViewById(R.id.lay03txtSymbH);
        weightDisplay = findViewById(R.id.lay03txtWeightDisplay);
        heightDisplay = findViewById(R.id.lay03txtHeightDisplay);
        //sliders
        heightBar = findViewById(R.id.lay03seekBarHeight);
        weightBar = findViewById(R.id.lay03seekBarWeight);
        //button
        nextBtn = findViewById(R.id.lay03btnNext);
        nextBtn.setEnabled(false); //disables button when no user input
        //set max values
        heightBar.setMax((int) bmiCalc.getMaxHeight());
        weightBar.setMax((int) bmiCalc.getMaxWeight());

        //set progress
        heightBar.setProgress((int)(bmiCalc.getMaxHeight()/2));
        weightBar.setProgress((int)(bmiCalc.getMaxWeight()/2));

        //sets text to chosen unit
        unitDisplay.setText(String.format("You have selected: %s system", bmiCalc.getPref()));
        symbolW.setText(bmiCalc.getSymbols(0));
        symbolH.setText(bmiCalc.getSymbols(1));

        heightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                heightDisplay.setText(String.format(Locale.getDefault(),"%d", i));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        /* Weight slider */
        weightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                weightDisplay.setText(String.format(Locale.getDefault(),"%d", i));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        /* Next Button Click */
        nextBtn.setOnClickListener(view -> {
            bmiCalc.setUsrHeight(Double.parseDouble(String.valueOf(heightDisplay.getText())));
            bmiCalc.setUsrWeight(Double.parseDouble(String.valueOf(weightDisplay.getText())));
            intent.putExtra("bmi_calc", bmiCalc); //sends bmi_calc object to other activity
            startActivity(intent);
        });

        /* Checks if height editText has changed */
        heightDisplay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                enableNext();
            }
        });
        /* Checks if weight editText has changed */
        weightDisplay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                enableNext();
            }
        });
    }

    /* Enables next button if EditBoxes aren't empty are at least one and smaller than max values */
    public void enableNext()
    {
        nextBtn.setEnabled(false);
        if (!TextUtils.isEmpty(weightDisplay.getText().toString()) &&
                !TextUtils.isEmpty(heightDisplay.getText().toString())){
            double currHeight = Double.parseDouble(String.valueOf(heightDisplay.getText()));
            double currWeight = Double.parseDouble(String.valueOf(weightDisplay.getText()));

            if(currHeight >= 1 && currHeight <= bmiCalc.getMaxHeight() &&
                    currWeight >= 1 && currWeight <= bmiCalc.getMaxWeight()) {
                nextBtn.setEnabled(true);
            }
        }
    }
}