package com.gz.jey.mynews.Controllers.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gz.jey.mynews.Controllers.Activities.MainActivity;
import com.gz.jey.mynews.R;

import java.util.ArrayList;
import java.util.List;

public class NewsQueryFragment extends Fragment {

    private MainActivity mainact;
    Context context;

    Spinner beginDate, endDate;

    public NewsQueryFragment(){}

    public static NewsQueryFragment newInstance(){
        return (new NewsQueryFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_query, container, false);
        mainact = (MainActivity)getActivity();
        context = getContext();
        SetSpinner(view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // -----------------
    // ACTION
    // -----------------


    // -----------------
    // CONFIGURATION
    // -----------------

    public void SetSpinner(View view){

        beginDate = view.findViewById(R.id.input_begin_date);
        endDate = view.findViewById(R.id.input_end_date);

        // Set Begin Block
        ArrayAdapter<String> beginAdapter;
        List<String> begin= new ArrayList<>();
        //Fill begin arraylist..
        begin.add("01/01/1998");

        beginAdapter = new ArrayAdapter<String>(context,R.layout.date_text, begin);

        beginDate.setAdapter(beginAdapter);
        beginDate.setSelection(0);


        // Set End Block
        ArrayAdapter<String> endAdapter;
        List<String> end= new ArrayList<>();
        //Fill end arraylist..
        end.add("31/12/2018");

        endAdapter = new ArrayAdapter<>(context, R.layout.date_text, end);

        endDate.setAdapter(endAdapter);
        endDate.setSelection(0);
    }
}
