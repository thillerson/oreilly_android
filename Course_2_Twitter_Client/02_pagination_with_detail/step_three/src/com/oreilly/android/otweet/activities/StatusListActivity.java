package com.oreilly.android.otweet.activities;

import java.util.ArrayList;

import com.oreilly.android.otweet.OTweetApplication;
import com.oreilly.android.otweet.R;
import com.oreilly.android.otweet.adapters.StatusListAdapter;
import com.oreilly.android.otweet.layouts.LoadMoreListItem;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class StatusListActivity extends ListActivity {

  private OTweetApplication app;
  private Twitter twitter;
  private LoadMoreListItem headerView;
  private LoadMoreListItem footerView;
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
      setLoadMoreViews();
      setListAdapter(adapter);
      getListView().setSelection(1);
    } catch (TwitterException e) {
      throw new RuntimeException("Unable to load home timeline",e);
    }
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    if (v.equals(headerView)) {
      headerView.showProgress();
      loadNewerTweets();
    } else if (v.equals(footerView)) {
      footerView.showProgress();
      loadOlderTweets();
    }
  }

  private void loadNewerTweets() {
    try {
      headerView.hideProgress();
      ArrayList<Status> statii = twitter.getHomeTimeline(new Paging().sinceId(adapter.getFirstId()));
      adapter.appendNewer(statii);
      getListView().setSelection(1);
    } catch (TwitterException e) {
      throw new RuntimeException("Unable to load home timeline",e);
    }
  }

  private void loadOlderTweets() {
    try {
      footerView.hideProgress();
      ArrayList<Status> statii = twitter.getHomeTimeline(new Paging().maxId(adapter.getLastId()-1));
      adapter.appendOlder(statii);
    } catch (TwitterException e) {
      throw new RuntimeException("Unable to load home timeline",e);
    }
  }

  private void setLoadMoreViews() {
    headerView = (LoadMoreListItem) getLayoutInflater().inflate(R.layout.load_more, null);
    headerView.showHeaderText();
    footerView = (LoadMoreListItem) getLayoutInflater().inflate(R.layout.load_more, null);
    footerView.showFooterText();
    getListView().addHeaderView(headerView);
    getListView().addFooterView(footerView);
  }
}