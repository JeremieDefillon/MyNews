package com.gz.jey.mynews.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.gz.jey.mynews.R;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckBoxsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    @BindView(R.id.checkBox)CheckBox mCheckBox;

    private WeakReference<CheckBoxsAdapter.Listener> callbackWeakRef;

    CheckBoxsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateCheckBoxs(String category, CheckBoxsAdapter.Listener callback, boolean isChecked){
        this.mCheckBox.setText(category);
        this.mCheckBox.setChecked(isChecked);
        callbackWeakRef = new WeakReference<>(callback);
    }


    @Override
    public void onClick(View view) {
        callbackWeakRef.get();
    }
}