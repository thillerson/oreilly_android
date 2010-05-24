package com.oreilly.android.otweet.activities;

import java.util.ArrayList;

import com.oreilly.android.otweet.OTweetApplication;
import com.oreilly.android.otweet.R;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class StatusListActivity extends ListActivity {

  private OTweetApplication app;
  private Twitter twitter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (OTweetApplication)getApplication();
    twitter = app.getTwitter();
    
    setContentView(R.layout.main);
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    if (!app.isAuthorized()) {
      beginAuthorization();
    } else {
      loadTimelineIfNotLoaded();
    }
  }
  
  private void loadTimelineIfNotLoaded() {
    loadHomeTimeline();
  }

  private void beginAuthorization() {
    Intent intent = new Intent(this, AuthorizationActivity.class);
    startActivity(intent);
  }

  private void loadHomeTimeline() {
    try {
      ArrayList<Status> statii = twitter.getHomeTimeline();
      StatusListAdapter adapter = new StatusListAdapter(this, statii);
      setListAdapter(adapter);
    } catch (TwitterException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private class StatusListAdapter extends ArrayAdapter<Status> {

    public StatusListAdapter(Context context, ArrayList<Status> statii) {
      super(context, android.R.layout.simple_list_item_1, statii);
    }

  }
}