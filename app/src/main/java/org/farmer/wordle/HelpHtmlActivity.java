package org.farmer.wordle;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.hardgusol.sol.R;



public class HelpHtmlActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setTitle("Guide");

        WebView webView  = findViewById(R.id.helpWebView);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl("file:///android_res/raw/android.html");


    }
}