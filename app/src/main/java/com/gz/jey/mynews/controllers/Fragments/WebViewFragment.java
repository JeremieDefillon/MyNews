package com.gz.jey.mynews.controllers.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gz.jey.mynews.R;
import com.gz.jey.mynews.controllers.activities.MainActivity;
import com.gz.jey.mynews.models.Data;


public class WebViewFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    static MainActivity mact;
    WebView wvPage;

    public WebViewFragment() {
    }

    public static WebViewFragment newInstance(MainActivity mainActivity){
        mact = mainActivity;
        return (new WebViewFragment());
    }

   @SuppressLint("SetJavaScriptEnabled")
   @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mact.ProgressLoad();
        View v = inflater.inflate(R.layout.fragment_webview, container, false);
        wvPage = v.findViewById(R.id.webview);
        wvPage.loadUrl(Data.getUrl());
        WebSettings settings = wvPage.getSettings();
        settings.setJavaScriptEnabled(true);

       wvPage.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

       wvPage.getSettings().setBuiltInZoomControls(true);
       wvPage.getSettings().setUseWideViewPort(true);
       wvPage.getSettings().setLoadWithOverviewMode(true);


        wvPage.setWebViewClient(new MyWebViewClient());
        return v;
    }

    private class MyWebViewClient extends WebViewClient {

        MyWebViewClient(){ }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mact.CloseLoad();
        }
    }
}
