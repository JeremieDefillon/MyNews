package com.gz.jey.mynews.Controllers.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
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

import com.gz.jey.mynews.Controllers.Activities.MainActivity;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.Utils.DatesCalculator;
import com.gz.jey.mynews.Utils.ItemClickSupport;
import com.gz.jey.mynews.Views.CheckBoxsAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gz.jey.mynews.Controllers.Activities.MainActivity.BEGIN_DATE;
import static com.gz.jey.mynews.Controllers.Activities.MainActivity.END_DATE;
import static com.gz.jey.mynews.Controllers.Activities.MainActivity.FILTERQUERY;
import static com.gz.jey.mynews.Controllers.Activities.MainActivity.QUERY;

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

    ProgressDialog progressDialog;
    //FOR DATA
    private String query;
    private Calendar begin_date, end_date;
    Calendar minbegin, maxbegin, minend, maxend;
    private List<String> filters;
    private List<Boolean> checked;
    private CheckBoxsAdapter checkBoxsAdapter;
    private int beginOrEnd = 0;

    // Start & Initializing
    public NewsQueryFragment(){}

    public static NewsQueryFragment newInstance(){
        return (new NewsQueryFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_query, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity)getActivity()).ProgressLoad();
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

        if(!QUERY.isEmpty())
            query=QUERY;

        if(!FILTERQUERY.isEmpty()){
            for (int i=0; i<checked.size(); i++ ) {
                if(FILTERQUERY.contains(filters.get(i)))
                    checked.set(i,true);
                else
                    checked.set(i,false);
            }
        }

        if(!BEGIN_DATE.isEmpty()) {
            begin_date = DatesCalculator
                    .SetUpCustomDateFromString(DatesCalculator.strDateFromStrReq(BEGIN_DATE));
            beginDateBTN.setText(DatesCalculator.strDateFormat(begin_date));
        }

        if(!END_DATE.isEmpty()) {
            end_date = DatesCalculator
                    .SetUpCustomDateFromString(DatesCalculator.strDateFromStrReq(END_DATE));
            endDateBTN.setText(DatesCalculator.strDateFormat(end_date));
        }

        searchQuery.setText(query);

        minbegin = Calendar.getInstance();
        minbegin.set(1800, 0,1);

        maxbegin = Calendar.getInstance();
        maxbegin.add(Calendar.DAY_OF_MONTH, -1);

        minend = Calendar.getInstance();
        minbegin.add(Calendar.YEAR, -200);

        maxend = Calendar.getInstance();
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

        ((MainActivity)getActivity()).TerminateLoad();
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
                int[] dtb = {datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth()};
                begin_date = DatesCalculator.SetUpCustomDateFromIntArray(dtb);
                beginDateBTN.setText(DatesCalculator.strDateFormat(begin_date));
                break;
            case 1 :
                int[] dte = {datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth()};
                end_date = DatesCalculator.SetUpCustomDateFromIntArray(dte);
                endDateBTN.setText(DatesCalculator.strDateFormat(end_date));
                break;
        }

        minbegin = Calendar.getInstance();
        minbegin.set(1800, 0,1);

        maxbegin = end_date!=null?end_date:Calendar.getInstance();
        maxbegin.add(Calendar.DAY_OF_MONTH, -1);

        Calendar ifnobegin = Calendar.getInstance();
        ifnobegin.add(Calendar.YEAR, -200);
        minend = begin_date!=null?begin_date:ifnobegin;
        minend.add(Calendar.DAY_OF_MONTH ,1);

        maxend = Calendar.getInstance();

        datePicker.setVisibility(View.GONE);
        artSearch.setVisibility(View.VISIBLE);
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
        query = searchQuery.getText().toString();
        boolean searchCondition = !query.isEmpty();
        boolean filterCondition = false;
        for (boolean b : checked)
            if(b){
                filterCondition=true;
                break;
            }

        if(searchCondition && filterCondition){
            QUERY = query;
            StringBuilder fq = new StringBuilder();
            for (int i=0; i<filters.size(); i++) {
                String comma = i==(filters.size()-1)?"":",";
                if(checked.get(i))
                    fq.append(filters.get(i)).append(comma);
            }
            FILTERQUERY = fq.toString();
            if(begin_date!=null)
                BEGIN_DATE = DatesCalculator.strDateForReq(begin_date);

            if(end_date!=null)
                END_DATE = DatesCalculator.strDateForReq(end_date);

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
        if(begin_date==null) {
            begin_date = DatesCalculator.StartingDates()[0];
        }

        Log.d("BEGIN DATE", DatesCalculator.strDateFormat(begin_date));
        datePicker.setVisibility(View.VISIBLE);
        datePicker.setMinDate(minbegin.getTimeInMillis());
        datePicker.setMaxDate(maxbegin.getTimeInMillis());
        UpdateDatePicker(begin_date);
        artSearch.setVisibility(View.GONE);
    }

    public void OpenUpEndDatePicker(){
        if(end_date==null)
            end_date=DatesCalculator.StartingDates()[1];

        Log.d("END DATE", DatesCalculator.strDateFormat(end_date));
        datePicker.setVisibility(View.VISIBLE);
        datePicker.setMinDate(minend.getTimeInMillis());
        datePicker.setMaxDate(maxend.getTimeInMillis());
        UpdateDatePicker(end_date);
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
