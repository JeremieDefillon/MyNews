package com.gz.jey.mynews.controllers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gz.jey.mynews.controllers.activities.MainActivity;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.model.Data;
import com.gz.jey.mynews.utils.DatesCalculator;
import com.gz.jey.mynews.utils.ItemClickSupport;
import com.gz.jey.mynews.views.CheckBoxsAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gz.jey.mynews.utils.DatesCalculator.GetOneWeekAgo;
import static com.gz.jey.mynews.utils.DatesCalculator.IntDateFormat;
import static com.gz.jey.mynews.utils.DatesCalculator.RequestDateFormat;
import static com.gz.jey.mynews.utils.DatesCalculator.SetupCustomDateInt;

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
    @BindView(R.id.set_date_btn)
    Button setDate;

    //FOR DATA
    static MainActivity mact;
    private String query;
    private Calendar begin_date, end_date;
    Calendar minbegin, maxbegin, minend, maxend;
    private List<String> filters;
    private List<Boolean> checked;
    private CheckBoxsAdapter checkBoxsAdapter;
    private int beginOrEnd = 0;

    // Start & Initializing
    public NewsQueryFragment(){ }

    public static NewsQueryFragment newInstance(MainActivity mainActivity){
        mact = mainActivity;
        return (new NewsQueryFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_query, container, false);
        ButterKnife.bind(this, view);
        mact.ProgressLoad();
        InitDatas();
        SetCheckboxCategorys();
        SetOnClickButtons(view);
        return view;
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void InitDatas(){
        filters = new ArrayList<>();
        checked = new ArrayList<>();
        Collections.addAll(filters, getResources().getStringArray(R.array.as_category));

        for (String t:filters) {
            checked.add(false);
        }

        if(!Data.getSearchQuery().isEmpty())
            query=Data.getSearchQuery();

        if(!Data.getSearchFilters().isEmpty()){
            for (int i=0; i<checked.size(); i++ ) {
                if(Data.getSearchFilters().contains(filters.get(i)))
                    checked.set(i,true);
                else
                    checked.set(i,false);
            }
        }

        if(!Data.getBeginDate().isEmpty()) {
            begin_date = DatesCalculator
                    .ConvertRequestToCalendar(Data.getBeginDate());
            beginDateBTN.setText(DatesCalculator.StandardStringDateFormat(begin_date));
        }

        if(!Data.getEndDate().isEmpty()) {
            end_date = DatesCalculator
                    .ConvertRequestToCalendar(Data.getEndDate());
            endDateBTN.setText(DatesCalculator.StandardStringDateFormat(end_date));
        }

        searchQuery.setText(query);

        SetupMinMaxDates();

    }


    private void SetOnClickButtons(View view){
        beginDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginOrEnd=0;
                OpenUpBeginDatePicker();
            }
        });

        endDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginOrEnd=1;
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
                        int index = filters.indexOf(buttonView.getText().toString());
                        checked.set(index, isChecked);
                    }
        });

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnDateChanged();
            }
        });

        mact.TerminateLoad();
    }

    private void SetCheckboxCategorys(){
        // Create newsAdapter passing in the sample user data
        checkBoxsAdapter = new CheckBoxsAdapter(filters, checked,this);
        // Attach the newsAdapter to the recyclerview to populate items
        recyclerView.setAdapter(checkBoxsAdapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        checkBoxsAdapter.notifyDataSetChanged();
    }

    private void OnDateChanged(){
        switch (beginOrEnd){
            case 0 :
                int[] dtb = {datePicker.getDayOfMonth(), datePicker.getMonth()+1, datePicker.getYear()};
                begin_date = DatesCalculator.SetupCustomDateInt(dtb);
                beginDateBTN.setText(DatesCalculator.StandardStringDateFormat(begin_date));
                Data.setBeginDate(RequestDateFormat(begin_date));
                break;
            case 1 :
                int[] dte = {datePicker.getDayOfMonth(), datePicker.getMonth()+1, datePicker.getYear()};
                end_date = DatesCalculator.SetupCustomDateInt(dte);
                endDateBTN.setText(DatesCalculator.StandardStringDateFormat(end_date));
                Data.setEndDate(RequestDateFormat(end_date));
                break;
        }
        SetupMinMaxDates();

        mact.SaveDatas();

        datePicker.setVisibility(View.GONE);
        artSearch.setVisibility(View.VISIBLE);
    }

    private  void SetupMinMaxDates(){
        minbegin = Calendar.getInstance();
        maxbegin = Calendar.getInstance();
        minend = Calendar.getInstance();
        maxend = Calendar.getInstance();

        minbegin.add(Calendar.YEAR, -200);
        if(end_date!=null)
            maxbegin = SetupCustomDateInt(IntDateFormat(end_date));
        maxbegin.add(Calendar.DAY_OF_MONTH, -1);

        if(begin_date!=null)
            minend = SetupCustomDateInt(IntDateFormat(begin_date));
        else
            minend = SetupCustomDateInt(IntDateFormat(maxbegin));
        minend.add(Calendar.DAY_OF_MONTH,1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // -----------------
    // ACTION
    // -----------------

    public void SearchArticles(){
        query = searchQuery.getText().toString();
        boolean searchCondition = !query.isEmpty();
        boolean filterCondition = false;
        for (boolean b : checked)
            if(b){
                filterCondition=true;
                break;
            }

        if(searchCondition && filterCondition){
            Data.setSearchQuery(query);
            StringBuilder fq = new StringBuilder();
            for (int i=0; i<filters.size(); i++) {
                String comma = i==(filters.size()-1)?"":",";
                if(checked.get(i))
                    fq.append(filters.get(i)).append(comma);
            }
            Data.setSearchFilters(fq.toString());

            if(Data.getActualTab()!=2)
                mact.pager.setCurrentItem(2);
            else
                mact.ChangeData();
        }else{

            String[] message = getResources().getStringArray(R.array.as_messages);
            String msg =    !searchCondition && filterCondition ?   message[0]:
                            searchCondition && !filterCondition ?   message[1]:
                                                                    message[2];

            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void OpenUpBeginDatePicker(){
        if(begin_date==null)
            begin_date = Calendar.getInstance();

        datePicker.setVisibility(View.VISIBLE);
        datePicker.setMinDate(minbegin.getTimeInMillis());
        datePicker.setMaxDate(maxbegin.getTimeInMillis());
        UpdateDatePicker(begin_date);
        artSearch.setVisibility(View.GONE);
    }

    public void OpenUpEndDatePicker(){
        if(end_date==null){
            Calendar cal = Calendar.getInstance();
            end_date = GetOneWeekAgo(cal);
        }

        datePicker.setVisibility(View.VISIBLE);
        datePicker.setMinDate(minend.getTimeInMillis());
        datePicker.setMaxDate(maxend.getTimeInMillis());
        UpdateDatePicker(end_date);
        artSearch.setVisibility(View.GONE);
    }

    public void UpdateDatePicker(Calendar date){
        // Insert Date into DatePicker
        int[] intDates = DatesCalculator.IntDateFormat(date);
        datePicker.updateDate(intDates[2],intDates[1]-1,intDates[0]);
    }


    @Override
    public void onClickDeleteButton(int position) {}
}
