package com.oreilly.android.otweet.activities;

import com.oreilly.android.otweet.OTweetApplication;
import com.oreilly.android.otweet.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AuthorizationActivity extends Activity {

  private OTweetApplication app;
  private WebView webView;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (OTweetApplication)getApplication();
    setContentView(R.layout.authorization_view);
    setUpViews();
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    String authURL = app.beginAuthorization();
    webView.loadUrl(authURL);
  }

  private void setUpViews() {
    webView = (WebView)findViewById(R.id.web_view);
  }

}
