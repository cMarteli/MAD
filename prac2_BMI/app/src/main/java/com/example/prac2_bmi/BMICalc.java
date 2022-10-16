package com.example.prac2_bmi;

import android.os.Parcel;
import android.os.Parcelable;

public class BMICalc implements Parcelable {

    //class variables
    private String[] classification = {"Underweight", "Healthy Weight", "Overweight but not obese",
        "Obese class I", "Obese class II", "Obese class III"};
    private double maxWeight, maxHeight, usrWeight, usrHeight;
    private final String pref;
    private String[] symbols = new String[2];

    public BMICalc(String p)
    {
        pref = p;
        usrWeight = 0.0;
        usrHeight = 0.0;
        if(pref.equals("imperial")){
            initImperial();
        }
        else{
            initMetric();
        }
    }
    private void initMetric()
    {
        maxHeight = 300.0; //cms
        maxWeight = 300.0; //kgs
        symbols[0] = "KG";
        symbols[1] = "CM";
    }
    private void initImperial()
    {
        maxHeight = 118.11; //inches
        maxWeight = 661.387; //lbs
        symbols[0] = "lb";
        symbols[1] = "Inch";
    }

    //formula for BMI in cm
    public double generateBMI() {
        if(pref.equals("imperial")) { //case imperial
            return ((usrWeight*703)/(usrHeight*usrHeight));
        }//metric
        return (usrWeight/usrHeight/usrHeight) * 10000;
    }

    //returns the index for the correct classification
    public int getCategory()
    {
        double bmi = round(generateBMI());
        if(bmi < 18.5){
            return 0;
        }
        else if(bmi >= 18.5 && bmi <= 24.9){
            return 1;
        }
        else if(bmi >= 25 && bmi <= 29.9){
            return 2;
        }
        else if(bmi >= 30 && bmi <= 34.9){
            return 3;
        }
        else if(bmi >= 35 && bmi <= 39.9){
            return 4;
        }
        else if(bmi > 40){
            return 5;
        }
        else{
            throw new IllegalArgumentException("Invalid BMI: " + bmi);
        }
    }
    //helper method for getCategory - rounds doubles to 1 dp
    private static double round (double value) {
        int scale = (int)Math.pow(10, 1);
        return (double)Math.round(value * scale) / scale;
    }
    public String generateCategory(int cat)
    {
        return classification[cat];
    }
    public String getPref() { return pref; }
    //returns symbol at a set index from 0 for weight 1 for height
    public String getSymbols(int index) { return symbols[index]; }
    public double getMaxWeight() {
        return maxWeight;
    }
    public double getMaxHeight() {
        return maxHeight;
    }
    public double getUsrWeight() {
        return usrWeight;
    }
    public double getUsrHeight() {
        return usrHeight;
    }

    public void setUsrWeight(double inUsrWeight){
        usrWeight = inUsrWeight;
    }
    public void setUsrHeight(double inUsrHeight){
        usrHeight = inUsrHeight;
    }

    //parcelable methods
    protected BMICalc(Parcel in) {
        classification = in.createStringArray();
        maxWeight = in.readDouble();
        maxHeight = in.readDouble();
        usrWeight = in.readDouble();
        usrHeight = in.readDouble();
        pref = in.readString();
        symbols = in.createStringArray();
    }

    public static final Creator<BMICalc> CREATOR = new Creator<BMICalc>() {
        @Override
        public BMICalc createFromParcel(Parcel in) {
            return new BMICalc(in);
        }

        @Override
        public BMICalc[] newArray(int size) {
            return new BMICalc[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(classification);
        parcel.writeDouble(maxWeight);
        parcel.writeDouble(maxHeight);
        parcel.writeDouble(usrWeight);
        parcel.writeDouble(usrHeight);
        parcel.writeString(pref);
        parcel.writeStringArray(symbols);
    }
}
