package com.gz.jey.mynews.Views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gz.jey.mynews.R;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    @BindView(R.id.no_result) TextView nr;

    WeakReference<NoResultAdapter.Listener> callbackWeakRef;

    public NoResultViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void noResult(NoResultAdapter.Listener callback){
        this.nr.setText("No results found under \r\n this Category");
        callbackWeakRef = new WeakReference<>(callback);
    }

    @Override
    public void onClick(View view) {
        NoResultAdapter.Listener callback = callbackWeakRef.get();
    }


}
