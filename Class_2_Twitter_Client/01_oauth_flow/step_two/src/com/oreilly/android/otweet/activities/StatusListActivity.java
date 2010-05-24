package com.oreilly.android.otweet.activities;

import com.oreilly.android.otweet.OTweetApplication;
import com.oreilly.android.otweet.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

public class StatusListActivity extends ListActivity {

  private OTweetApplication app;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (OTweetApplication)getApplication();
    
    setContentView(R.layout.main);
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    if (!app.isAuthorized()) {
      beginAuthorization();
    } else {
      // load the user's home timeline
    }
  }
  
  private void beginAuthorization() {
    Intent intent = new Intent(this, AuthorizationActivity.class);
    startActivity(intent);
  }

}