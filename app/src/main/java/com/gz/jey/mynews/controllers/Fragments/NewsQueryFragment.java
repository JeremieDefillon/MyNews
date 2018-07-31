package com.gz.jey.mynews.controllers.Fragments;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.gz.jey.mynews.controllers.activities.MainActivity;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.models.Data;
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
import static com.gz.jey.mynews.utils.DatesCalculator.StandardStringDateFormat;

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
    @BindView(R.id.cancel_date_btn)
    Button cancelDate;

    //FOR DATA
    @SuppressLint("StaticFieldLeak")
    static MainActivity mact;
    private String query;
    private Calendar begin_date, end_date;
    Calendar minbegin, maxbegin, minend, maxend;
    private List<String> filters;
    private List<Boolean> checked;
    private int beginOrEnd = 0;

    /**
     * Start & Initializing
     */
    public NewsQueryFragment(){ }

    /**
     * @param mainActivity MainActivity
     * @return new NewsQueryFragment()
     */
    public static NewsQueryFragment newInstance(MainActivity mainActivity){
        mact = mainActivity;
        return (new NewsQueryFragment());
    }

    /**
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_query, container, false);
        ButterKnife.bind(this, view);
        mact.ProgressLoad();
        InitDatas();
        SetCheckboxCategorys();
        SetOnClickButtons();
        return view;
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    /**
     * Initialize Datas
     */
    private void InitDatas(){
        filters = new ArrayList<>();
        checked = new ArrayList<>();
        Collections.addAll(filters, getResources().getStringArray(R.array.as_category));

        for (String ignored :filters)
            checked.add(false);

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


    /**
     * set all the on clicks
     */
    private void SetOnClickButtons(){
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
        cancelDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnDateErased();
            }
        });

        mact.TerminateLoad();
    }

    /**
     * Set the checkboxs
     */
    private void SetCheckboxCategorys(){
        // Create newsAdapter passing in the sample user data
        CheckBoxsAdapter checkBoxsAdapter = new CheckBoxsAdapter(filters, checked, this);
        // Attach the newsAdapter to the recyclerview to populate items
        recyclerView.setAdapter(checkBoxsAdapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        checkBoxsAdapter.notifyDataSetChanged();
    }

    /**
     * when Date Changed
     */
    private void OnDateChanged(){
        switch (beginOrEnd){
            case 0 :
                int[] dtb = {datePicker.getDayOfMonth(), datePicker.getMonth()+1, datePicker.getYear()};
                begin_date = DatesCalculator.SetupCustomDateInt(dtb);
                break;
            case 1 :
                int[] dte = {datePicker.getDayOfMonth(), datePicker.getMonth()+1, datePicker.getYear()};
                end_date = DatesCalculator.SetupCustomDateInt(dte);
                break;
        }
        SetupMinMaxDates();

        datePicker.setVisibility(View.GONE);
        artSearch.setVisibility(View.VISIBLE);
    }

    /**
     * to erase Date datas
     */
    private void OnDateErased(){
        switch (beginOrEnd){
            case 0 :
                begin_date = null;
                beginDateBTN.setText("");
                Data.setBeginDate("");
                break;
            case 1 :
                end_date = null;
                endDateBTN.setText("");
                Data.setEndDate("");
                break;
        }
        SetupMinMaxDates();

        mact.SaveDatas();

        datePicker.setVisibility(View.GONE);
        artSearch.setVisibility(View.VISIBLE);
    }

    /**
     * to set Min and Max dates for the date picker
     */
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

        if(begin_date!=null){
            if(begin_date.after(maxbegin))
                begin_date=maxbegin;

            beginDateBTN.setText(DatesCalculator.StandardStringDateFormat(begin_date));
            Data.setBeginDate(RequestDateFormat(begin_date));
        }

        if(end_date!= null){
            if(end_date.after(maxend))
                end_date=maxend;

            endDateBTN.setText(DatesCalculator.StandardStringDateFormat(end_date));
            Data.setEndDate(RequestDateFormat(end_date));
        }

        mact.SaveDatas();

        Log.d("MIN BEGIN", StandardStringDateFormat(minbegin));
        Log.d("MAX BEGIN", StandardStringDateFormat(maxbegin));
        Log.d("MIN END", StandardStringDateFormat(minend));
        Log.d("MAX END", StandardStringDateFormat(maxend));
    }

    /**
     * Destroy this Fragment
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // -----------------
    // ACTION
    // -----------------

    /**
     * to send search with query term dates and filters choosen
     */
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
            String msg = !searchCondition && filterCondition ? message[0]:
                            searchCondition ? message[1]:message[2];

            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Open the datePicker from Begin Date button
     */
    public void OpenUpBeginDatePicker(){
        if(begin_date==null){
            Calendar cal = Calendar.getInstance();
            begin_date = GetOneWeekAgo(cal);
        }

        SetupMinMaxDates();
        UpdateDatePicker(begin_date, minbegin, maxbegin);
    }

    /**
     * Open the datePicker from End Date button
     */
    public void OpenUpEndDatePicker(){
        if(end_date==null)
            end_date = Calendar.getInstance();

        SetupMinMaxDates();
        UpdateDatePicker(end_date, minend, maxend);
    }

    /**
     * @param date Calendar
     * @param min Calendar
     * @param max Calendar
     *            to set and init datepicker
     */
    public void UpdateDatePicker(Calendar date, Calendar min, Calendar max){
        // Insert Date into DatePicker
        long mini = min.getTimeInMillis();
        long maxi = max.getTimeInMillis();
        datePicker.setMinDate(mini);
        datePicker.setMaxDate(maxi);
        int[] intDates = DatesCalculator.IntDateFormat(date);
        datePicker.updateDate(intDates[2],intDates[1]-1,intDates[0]);
        artSearch.setVisibility(View.GONE);
        datePicker.setVisibility(View.VISIBLE);
    }


}
