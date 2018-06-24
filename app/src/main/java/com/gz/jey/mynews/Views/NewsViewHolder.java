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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    @BindView(R.id.fragment_main_item_title)
    TextView Title;
    @BindView(R.id.fragment_main_item_date) TextView dat;
    @BindView(R.id.fragment_main_item_image)
    ImageView Image;
    @BindView(R.id.fragment_main_item_from) TextView From;

    WeakReference<NewsAdapter.Listener> callbackWeakRef;

    private SimpleDateFormat[] dateFormat = {
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"),
            new SimpleDateFormat("yyyy-MM-dd")
    };
    // ex date string 2018-04-25T05:00:13-04:00

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
        this.dat.setText(getDateFormated(res.getPublishedDate()));

        callbackWeakRef = new WeakReference<NewsAdapter.Listener>(callback);
    }

    @Override
    public void onClick(View view) {
        NewsAdapter.Listener callback = callbackWeakRef.get();
    }

    private String getDateFormated(String dateunformatted){
        Date dd = null;
        String d = "";
        for(SimpleDateFormat df : dateFormat ){
            try {
                dd = df.parse(dateunformatted);
                d = DateFormat.getDateInstance(DateFormat.SHORT).format(dd);
                break;
            } catch (ParseException e) {
                d= "";
            }
        }
        return d;
    }
}
