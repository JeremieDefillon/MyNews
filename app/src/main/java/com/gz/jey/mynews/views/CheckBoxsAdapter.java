package com.gz.jey.mynews.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gz.jey.mynews.R;

import java.util.List;

public class CheckBoxsAdapter extends RecyclerView.Adapter<CheckBoxsViewHolder>{


    public interface Listener {
        void onClickDeleteButton(int position);
    }

    // FOR COMMUNICATION
    private final CheckBoxsAdapter.Listener callback;

    // FOR DATA
    private List<String> categorys;
    private List<Boolean> check;

    // CONSTRUCTOR
    public CheckBoxsAdapter(List<String> categs, List<Boolean> checked, CheckBoxsAdapter.Listener callback) {
        this.categorys = categs;
        this.check = checked;
        this.callback = callback;
    }

    @Override
    public CheckBoxsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.checkbox_item, parent, false);
        return new CheckBoxsViewHolder(view);
    }

    // UPDATE VIEW HOLDER WITH A NEWS
    @Override
    public void onBindViewHolder(CheckBoxsViewHolder viewHolder, int position) {
        viewHolder.updateCheckBoxs(this.categorys.get(position),this.callback,this.check.get(position));
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.categorys.size();
    }

    public String getCheckBoxs(int position){
        return this.categorys.get(position);
    }
}

