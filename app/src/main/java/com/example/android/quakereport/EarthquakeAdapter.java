package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alejandroalfaro on 16/04/17.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    Activity mContext;

    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes) {
        super(context, R.layout.list_item, earthquakes);

        mContext = context;


    }

    public View getView(int position, View convertView, ViewGroup parent){


        LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null, true);

        Earthquake currentEarthquake = getItem(position);


        TextView magnitudeTextView = (TextView) rowView.findViewById(R.id.magnitude);

        double currentMag = currentEarthquake.getMagnitude();

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());
        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        magnitudeTextView.setText(formatMagnitude(currentMag));

        TextView placeTextView = (TextView) rowView.findViewById(R.id.city);

        TextView offsetTextView = (TextView) rowView.findViewById(R.id.offset);

        String fullPlace = currentEarthquake.getPlace();
        if (fullPlace.contains("km") == true) {
            offsetTextView.setText(fullPlace.substring(0, fullPlace.indexOf("f") + 1));
            placeTextView.setText(fullPlace.substring(fullPlace.indexOf("f") + 2));

        }
        else {
            offsetTextView.setText(getContext().getString(R.string.near_the));
            placeTextView.setText(currentEarthquake.getPlace());
        }


        //Create a Date object passing it the long variable
        Date dateObject = new Date(currentEarthquake.getDate());
        //Create a SimpleDateFormat object to handle the format wanted on the Date object
        TextView dateTextView = (TextView) rowView.findViewById(R.id.date);
        String dateToDisplay = formatDate(dateObject);
        dateTextView.setText(dateToDisplay);

        TextView timeTextView = (TextView) rowView.findViewById(R.id.time);
        String timeToDisplay = formatTime(dateObject);
        timeTextView.setText(timeToDisplay);



        return rowView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
