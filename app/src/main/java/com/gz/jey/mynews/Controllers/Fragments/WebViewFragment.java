package com.gz.jey.mynews.Controllers.Fragments;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gz.jey.mynews.Controllers.Activities.MainActivity;
import com.gz.jey.mynews.R;

import static com.gz.jey.mynews.Controllers.Activities.MainActivity.URLI;

public class WebViewFragment extends Fragment {

    MainActivity mact;
    WebView wvPage;

    public WebViewFragment(MainActivity mainActivity) {
        mact = mainActivity;
    }

    public static WebViewFragment newInstance(MainActivity mainActivity){
        return (new WebViewFragment(mainActivity));
    }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       mact.ProgressLoad();
        View v = inflater.inflate(R.layout.fragment_webview, container, false);
        wvPage = v.findViewById(R.id.webview);
        wvPage.loadUrl(URLI);
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

        public MyWebViewClient(){ }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.O)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            mact.TerminateLoad();
        }
    }
}
