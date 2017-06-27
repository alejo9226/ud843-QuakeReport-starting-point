package com.example.android.quakereport;

/**
 * Created by alejandroalfaro on 16/04/17.
 */

public class Earthquake {

    private double mMagnitude;
    private String mPlace;
    private long mDate;
    private String mUrl;

    // variable constant con valor -1
    private static final int NO_IMAGE_PROVIDED = -1;
    //private Context wContext;
    // This is the constructor of the Earthquake object with three inputs: 2 Strings and 1 float
    public Earthquake (double magnitude, String place, long date, String url){

        mMagnitude = magnitude;
        mPlace = place;
        mDate = date;
        mUrl = url;

    }

    public double getMagnitude(){
        return mMagnitude;
    }
    public String getPlace(){
        return mPlace;
    }
    public long getDate(){
        return mDate;
    }
    public String getUrl(){
        return mUrl;
    }

    //This method represents the whole object as a string, usually for debugging purposes.
    @Override
    public String toString() {
        return "Earthquake{" +
                "mMagnitude='" + mMagnitude + '\'' +
                ", mPlace='" + mPlace + '\'' +
                ", mDate=" + mDate + '\'' +
                ", mUrl=" + mUrl +
                '}';
    }


}
