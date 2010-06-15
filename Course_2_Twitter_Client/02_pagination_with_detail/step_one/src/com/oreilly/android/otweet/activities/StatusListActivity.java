package com.oreilly.android.otweet.activities;

import java.util.ArrayList;

import com.oreilly.android.otweet.OTweetApplication;
import com.oreilly.android.otweet.R;
import com.oreilly.android.otweet.adapters.StatusListAdapter;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

public class StatusListActivity extends ListActivity {

  private OTweetApplication app;
  private Twitter twitter;
  private StatusListAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (OTweetApplication) getApplication();
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
    if (null == getListAdapter()) {
      loadHomeTimeline();
    }
  }

  private void beginAuthorization() {
    Intent intent = new Intent(this, AuthorizationActivity.class);
    startActivity(intent);
  }

  private void loadHomeTimeline() {
    try {
      ArrayList<Status> statii = twitter.getHomeTimeline();
      adapter = new StatusListAdapter(this, statii);
      setListAdapter(adapter);
    } catch (TwitterException e) {
      throw new RuntimeException("Unable to load home timeline",e);
    }
  }

}