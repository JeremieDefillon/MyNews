package com.gz.jey.mynews.Controllers.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gz.jey.mynews.Controllers.Activities.MainActivity;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.Utils.DatesCalculator;
import com.gz.jey.mynews.Utils.ItemClickSupport;
import com.gz.jey.mynews.Views.CheckBoxsAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsQueryFragment extends Fragment implements CheckBoxsAdapter.Listener{

    // FOR DESIGN
    @BindView(R.id.search_query)
    EditText searchQuery;
    @BindView(R.id.input_date)
    DatePicker datePicker;
    @BindView(R.id.article_search)
    LinearLayout artSearch;
    @BindView(R.id.checkbox_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.click_begin_date)
    Button beginDateBTN;
    @BindView(R.id.click_end_date)
    Button endDateBTN;
    @BindView(R.id.click_send_search)
    Button searchBTN;

    //FOR DATA
    private String query;
    private Calendar begin_date, end_date;
    private List<String> filters;
    private ArrayList<String> categs;
    private CheckBoxsAdapter checkBoxsAdapter;

    // Start & Initializing
    public NewsQueryFragment(){}

    public static NewsQueryFragment newInstance(){
        return (new NewsQueryFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_query, container, false);
        ButterKnife.bind(this, view);
        SetCheckboxCategorys();
        SetOnClickButtons(view);
        return view;
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    public void SetOnClickButtons(View view){
        filters = new ArrayList<>();

        beginDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenUpBeginDatePicker();
            }
        });

        endDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenUpEndDatePicker();
            }
        });

        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchArticles();
            }
        });

        ItemClickSupport.addTo(recyclerView, R.layout.checkbox_item)
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            filters.add(buttonView.getText().toString());
                        }else {
                            int index = filters.indexOf(buttonView.getText().toString());
                            filters.remove(index);
                        }
                    }
        });
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

    public void SearchArticles(){
        MainActivity mact = (MainActivity)getActivity();
        if(searchQuery.getText().toString()!=null && !searchQuery.getText().toString().isEmpty() && filters.size()!=0){
            mact.QUERY = searchQuery.getText().toString();
            String fq = "";
            for (String s : filters) {
                fq+=s+",";
            }
            mact.FILTERQUERY = fq;
            if(begin_date!=null)
                mact.BEGIN_DATE = DatesCalculator.strDateForReq(begin_date);

            if(end_date!=null)
                mact.END_DATE = DatesCalculator.strDateForReq(end_date);

            mact.ChangeData();
        }else{

        }
    }

    public void OpenUpBeginDatePicker(){
        if(begin_date==null)
            begin_date=DatesCalculator.StartingDates()[0];

        UpdateDatePicker(begin_date);
        datePicker.setVisibility(View.VISIBLE);
        artSearch.setVisibility(View.GONE);
    }

    public void OpenUpEndDatePicker(){
        if(end_date==null)
            end_date=DatesCalculator.StartingDates()[1];

        UpdateDatePicker(end_date);
        datePicker.setVisibility(View.VISIBLE);
        artSearch.setVisibility(View.GONE);
    }

    public void UpdateDatePicker(Calendar date){
        // Insert Date into DatePicker
        int[] intDates = DatesCalculator.intDateFormat(date);
        datePicker.updateDate(intDates[0],intDates[1]-1,intDates[2]);
    }


    @Override
    public void onClickDeleteButton(int position) {
    }
}
