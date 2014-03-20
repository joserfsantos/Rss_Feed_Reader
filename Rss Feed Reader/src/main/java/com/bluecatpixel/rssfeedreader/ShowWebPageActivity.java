package com.bluecatpixel.rssfeedreader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bluecatpixel.rssfeedreader.model.Constants;

/**
 * @author josericardosantos (Blue Cat Pixel)
 */
@SuppressLint("SetJavaScriptEnabled")
public class ShowWebPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webpage_layout);

        String url = null;

        if (getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString(Constants.EXTRA_WEBPAGE_URL);
        }

        WebView webView = (WebView) findViewById(R.id.wv_webpage);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new CustomWebViewClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
