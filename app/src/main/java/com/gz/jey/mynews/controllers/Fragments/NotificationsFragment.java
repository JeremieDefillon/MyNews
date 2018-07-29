package com.gz.jey.mynews.controllers.Fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gz.jey.mynews.controllers.activities.MainActivity;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.models.Data;
import com.gz.jey.mynews.utils.DatesCalculator;
import com.gz.jey.mynews.utils.ItemClickSupport;
import com.gz.jey.mynews.views.CheckBoxsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsFragment  extends Fragment implements CheckBoxsAdapter.Listener {

    // FOR DESIGN
    @BindView(R.id.notifications)
    LinearLayout notifications;
    @BindView(R.id.search_query)
    EditText searchQuery;
    @BindView(R.id.checkbox_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.switch_enable_notification)
    Switch enableNotif;
    @BindView(R.id.time_setter)
    LinearLayout timeSetter;
    @BindView(R.id.set_time_btn)
    Button setTime;
    @BindView(R.id.cancel_time_btn)
    Button cancelBtn;
    @BindView(R.id.input_time)
    TextView inputTime;
    @BindView(R.id.time_picker)
    TimePicker timePicker;


    //FOR DATA
    @SuppressLint("StaticFieldLeak")
    static MainActivity mact;
    private String query;
    private List<String> filters;
    private List<Boolean> checked;

    // Start & Initializing
    public NotificationsFragment(){ }

    public static NotificationsFragment newInstance(MainActivity mainActivity){
        mact = mainActivity;
        return (new NotificationsFragment());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
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

    private void InitDatas(){
        notifications.setVisibility(View.VISIBLE);
        timePicker.setVisibility(View.GONE);
        timePicker.setIs24HourView(true);
        timeSetter.setVisibility(View.GONE);

        SetTimeOnButton();

        filters = new ArrayList<>();
        checked = new ArrayList<>();
        Collections.addAll(filters, getResources().getStringArray(R.array.as_category));

        for (String ignored :filters)
            checked.add(false);

        if(!Data.getNotifQuery().isEmpty())
            query=Data.getNotifQuery();

        if(!Data.getNotifFilters().isEmpty()){
            for (int i=0; i<checked.size(); i++ ) {
                if(Data.getNotifFilters().contains(filters.get(i)))
                    checked.set(i,true);
                else
                    checked.set(i,false);
            }
        }

        searchQuery.setText(query);
    }


    private void SetOnClickButtons(){
        enableNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enableNotif.isChecked())
                    ActivateNotif();
                else
                    UnactivateNotif();
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

        inputTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnOpenTimePicker();
            }
        });

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnChangedTime();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnEraseTime();
            }
        });

        mact.TerminateLoad();
    }

    private void SetCheckboxCategorys(){
        // Create newsAdapter passing in the sample user data
        CheckBoxsAdapter checkBoxsAdapter = new CheckBoxsAdapter(filters, checked, this);
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

    public void ActivateNotif(){
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
            Data.setNotifQuery(query);
            StringBuilder fq = new StringBuilder();
            for (int i=0; i<filters.size(); i++) {
                String comma = i==(filters.size()-1)?"":",";
                if(checked.get(i))
                    fq.append(filters.get(i)).append(comma);
            }
            Data.setNotifFilters(fq.toString());
            String msg = getResources().getString(R.string.enabledNotif);
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                timeSetter.setVisibility(View.VISIBLE);
            else{
                Data.setHour(7);
                Data.setMinutes(0);
            }
            assert mact != null;
            mact.SetNotification();
        }else{

            String[] message = getResources().getStringArray(R.array.as_messages);
            String msg = !searchCondition && filterCondition ? message[0]:
                        searchCondition? message[1]:message[2];

            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            enableNotif.setChecked(false);
            timeSetter.setVisibility(View.GONE);
        }
    }

    private void OnOpenTimePicker(){
        timePicker.setVisibility(View.VISIBLE);
        notifications.setVisibility(View.GONE);
    }

    private void OnChangedTime(){
        timePicker.setVisibility(View.GONE);
        notifications.setVisibility(View.VISIBLE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Data.setHour(timePicker.getHour());
            Data.setMinutes(timePicker.getMinute());
        }
        SetTimeOnButton();
        mact.SetNotification();
    }

    private void OnEraseTime(){
        timePicker.setVisibility(View.GONE);
        notifications.setVisibility(View.VISIBLE);
        Data.setHour(7);
        Data.setMinutes(0);
        SetTimeOnButton();
        mact.SetNotification();
    }

    private void UnactivateNotif(){
        String msg = getResources().getString(R.string.disabledNotif);
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        enableNotif.setChecked(false);
        timeSetter.setVisibility(View.GONE);

        mact.CancelNotification();
    }

    private void SetTimeOnButton(){
        int[] times = {Data.getHour(),Data.getMinutes()};
        String time = DatesCalculator.StandardStringTimeFormat(times);
        inputTime.setText(time);
    }

}
