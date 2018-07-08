package com.gz.jey.mynews.Views;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.gz.jey.mynews.Models.Result;
import com.gz.jey.mynews.R;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.gz.jey.mynews.Utils.Formatter;

public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    @BindView(R.id.fragment_main_item_title)
    TextView Title;
    @BindView(R.id.fragment_main_item_date) TextView dat;
    @BindView(R.id.fragment_main_item_image)
    ImageView Image;
    @BindView(R.id.fragment_main_item_from) TextView From;

    WeakReference<NewsAdapter.Listener> callbackWeakRef;



    public NewsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateNews(Result res, RequestManager glide, NewsAdapter.Listener callback){
        String Sec = res.getSection();
        String Sub = res.getSubsection();
        String und = (TextUtils.isEmpty(Sec)||TextUtils.isEmpty(Sub))?"":" > ";
        this.From.setText(Sec + und + Sub);
        if(res.getImageUrl() != "")
            // Replace .into(Image); -> .apply(RequestOptions.circleCropTransform()).into(Image);
            // For an image circle cropped !
            glide.load(res.getImageUrl()).into(Image);
        else
            this.Image.setAdjustViewBounds(false);

        this.Title.setText(res.getTitle());
        this.dat.setText(res.getPublishedDate());

        callbackWeakRef = new WeakReference<NewsAdapter.Listener>(callback);
    }

    @Override
    public void onClick(View view) {
        NewsAdapter.Listener callback = callbackWeakRef.get();
    }


}
