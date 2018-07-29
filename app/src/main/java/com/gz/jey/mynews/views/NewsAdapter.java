package com.gz.jey.mynews.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.gz.jey.mynews.models.Result;
import com.gz.jey.mynews.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    public interface Listener {}

    // FOR COMMUNICATION
    private final Listener callback;

    // FOR DATA
    private List<Result> results;
    private RequestManager glide;

    // CONSTRUCTOR
    public NewsAdapter(List<Result> res, RequestManager glide, Listener callback) {
        this.results = res;
        this.glide = glide;
        this.callback = callback;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_main_item, parent, false);
        return new NewsViewHolder(view);
    }

    // UPDATE VIEW HOLDER WITH A NEWS
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder viewHolder, int position) {
        viewHolder.updateNews(this.results.get(position), this.glide, this.callback);
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.results.size();
    }

    public Result getNews(int position){
        return this.results.get(position);
    }
}
