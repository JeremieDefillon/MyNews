package com.gz.jey.mynews.Controllers.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.gz.jey.mynews.Controllers.Activities.MainActivity;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.Utils.DatesCalculator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewsQueryFragment extends Fragment {

    // Activity & Context
    private MainActivity mainact;
    Context context;

    // Views & Layouts
    DatePicker datePicker;


    // Datas
    Calendar begin_date, end_date;


    // Start & Initializing
    public NewsQueryFragment(){}

    public static NewsQueryFragment newInstance(){
        return (new NewsQueryFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_query, container, false);
        InitDatas(view);
        return view;
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    public void InitDatas(View view){
        mainact = (MainActivity)getActivity();
        context = getContext();
        datePicker = view.findViewById(R.id.input_date);
        SetCheckboxCategorys(view);
    }

    public void SetCheckboxCategorys(View view){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // -----------------
    // ACTION
    // -----------------

    public void UpdateDatePicker(Calendar date){
        // Insert Date into DatePicker
        int[] intDates = DatesCalculator.intDateFormat(date);
        datePicker.updateDate(intDates[2],intDates[1],intDates[0]);
    }


    // --------------------------
    // Utils for all Dates Stuff
    // --------------------------


}
