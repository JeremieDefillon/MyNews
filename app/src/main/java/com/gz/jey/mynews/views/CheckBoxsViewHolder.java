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

    /**
     * @param itemView View
     * CONSTRUCTOR
     */
    CheckBoxsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * @param category String
     * @param callback CheckBoxsAdapter.Listener
     * @param isChecked boolean
     * Build Checkboxs
     */
    public void updateCheckBoxs(String category, CheckBoxsAdapter.Listener callback, boolean isChecked){
        this.mCheckBox.setText(category);
        this.mCheckBox.setChecked(isChecked);
        callbackWeakRef = new WeakReference<>(callback);
    }


    /**
     * @param view View
     * OnClick callback
     */
    @Override
    public void onClick(View view)
    {
        callbackWeakRef.get();
    }
}