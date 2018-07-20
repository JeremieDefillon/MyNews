package com.gz.jey.mynews.Controllers.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.gz.jey.mynews.Controllers.Activities.MainActivity;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.Utils.DatesCalculator;
import com.gz.jey.mynews.Views.CheckBoxsAdapter;
import com.gz.jey.mynews.Views.NewsAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsQueryFragment extends Fragment implements CheckBoxsAdapter.Listener{

    // FOR DESIGN
    @BindView(R.id.checkbox_recyclerview)
    RecyclerView recyclerView;

    //FOR DATA
    private Calendar begin_date, end_date;
    private ArrayList<String> categs;
    private CheckBoxsAdapter checkBoxsAdapter;

    // Activity & Context
    private MainActivity mainact;
    Context context;

    // Views & Layouts
    DatePicker datePicker;

    // Datas

    // Start & Initializing
    public NewsQueryFragment(){}

    public static NewsQueryFragment newInstance(){
        return (new NewsQueryFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_query, container, false);
        ButterKnife.bind(this, view);
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
        SetCheckboxCategorys();
    }

    public void SetCheckboxCategorys(){
        categs = new ArrayList<>();
        for (String t : getResources().getStringArray(R.array.as_category)) {
            categs.add(t);
        }

        // Create newsAdapter passing in the sample user data
        checkBoxsAdapter = new CheckBoxsAdapter(categs, this);
        // Attach the newsAdapter to the recyclerview to populate items
        recyclerView.setAdapter(checkBoxsAdapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        checkBoxsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // -----------------
    // ACTION
    // -----------------

    public void OpenUpBeginDatePicker(){

    }

    public void OpenUpEndDatePicker(){

    }
    public void UpdateDatePicker(Calendar date){
        // Insert Date into DatePicker
        int[] intDates = DatesCalculator.intDateFormat(date);
        datePicker.updateDate(intDates[2],intDates[1],intDates[0]);
    }


    @Override
    public void onClickDeleteButton(int position) {
    }
}
