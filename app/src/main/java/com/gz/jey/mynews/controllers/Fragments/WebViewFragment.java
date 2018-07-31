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

    /**
     * Start & Initialize
     */
    public WebViewFragment() {
    }

    /**
     * @param mainActivity MainActivity
     * @return new WebViewFragment()
     */
    public static WebViewFragment newInstance(MainActivity mainActivity){
        mact = mainActivity;
        return (new WebViewFragment());
    }

    /**
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
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

        /**
         * Start & Initialize WebView
         */
        MyWebViewClient(){ }

        /**
         * @param view WebView
         * @param url String
         * @param favicon Bitmap
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        /**
         * @param view WebVIew
         * @param url String
         * @return true
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        /**
         * @param view WebView
         * @param request WebResourceRequest
         * @return true
         */
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        /**
         * @param view WebView
         * @param url String
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mact.CloseLoad();
        }
    }
}
