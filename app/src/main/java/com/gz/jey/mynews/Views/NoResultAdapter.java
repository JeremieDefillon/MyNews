package com.gz.jey.mynews.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.gz.jey.mynews.Models.Result;
import com.gz.jey.mynews.R;

import java.util.List;

public class NoResultAdapter extends RecyclerView.Adapter<NoResultViewHolder> {

    TextView no_r;

    public interface Listener {
        void onClickDeleteButton(int position);
    }

    // FOR COMMUNICATION
    private final Listener callback;

    // FOR DATA
    private RequestManager glide;

    // CONSTRUCTOR
    public NoResultAdapter(Listener callback) {
      //  this.glide = glide;
        this.callback = callback;
    }

    @Override
    public NoResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_no_result, parent, false);
        return new NoResultViewHolder(view);
    }

    // UPDATE VIEW HOLDER WITH A NEWS
    @Override
    public void onBindViewHolder(NoResultViewHolder viewHolder, int position) {
            viewHolder.noResult(this.callback);
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return 1;
    }

}
