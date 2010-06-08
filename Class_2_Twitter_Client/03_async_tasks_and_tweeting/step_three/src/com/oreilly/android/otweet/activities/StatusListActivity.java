package com.oreilly.android.otweet.activities;

import java.util.ArrayList;

import com.oreilly.android.otweet.OTweetApplication;
import com.oreilly.android.otweet.R;
import com.oreilly.android.otweet.layouts.LoadMoreListItem;
import com.oreilly.android.otweet.tasks.LoadMoreAsyncTask;
import com.oreilly.android.otweet.tasks.LoadMoreAsyncTask.LoadMoreStatusesResponder;
import com.oreilly.android.otweet.tasks.LoadMoreAsyncTask.LoadMoreStatusesResult;
import com.oreilly.android.otweet.adapters.*;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

public class StatusListActivity extends ListActivity implements LoadMoreStatusesResponder {

  final private Handler handler = new Handler();
  private OTweetApplication app;
  private Twitter twitter;
  private LoadMoreListItem headerView;
  private LoadMoreListItem footerView;
  private StatusListAdapter adapter;
  protected ProgressDialog progressDialog;

  private Runnable finishedLoadingListTask = new Runnable() {
    public void run() {
      finishedLoadingList();
    }
  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (OTweetApplication) getApplication();
    twitter = app.getTwitter();
    
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    if (v.equals(headerView)) {
      headerView.showProgress();
      loadNewerTweets();
    } else if (v.equals(footerView)) {
      footerView.showProgress();
      loadOlderTweets();
    } else {
      // Watch out! Doesn't account for header/footer! -> Status status = adapter.getItem(position);
      Status status = (Status)getListView().getItemAtPosition(position);
      Intent intent = new Intent(this, StatusDetailActivity.class);
      intent.putExtra(StatusDetailActivity.STATUS, status);
      startActivity(intent);
    }
  }

  private void loadTimelineIfNotLoaded() {
    if (null == getListAdapter()) {
      progressDialog = ProgressDialog.show(
          StatusListActivity.this,
          getResources().getString(R.string.loading_title),
          getResources().getString(R.string.loading_home_timeline_description)
        );
      Thread loadHomeTimelineThread = new Thread() {
        public void run() {
          loadHomeTimeline();
          handler.post(finishedLoadingListTask);
        }
      };
      loadHomeTimelineThread.start();
    }
  }

  private void beginAuthorization() {
    Intent intent = new Intent(this, AuthorizationActivity.class);
    startActivity(intent);
  }

  private void loadHomeTimeline() {
    try {
      ArrayList<Status> statii = twitter.getHomeTimeline();
      adapter = new StatusListAdapter(StatusListActivity.this, statii);
    } catch (TwitterException e) {
      throw new RuntimeException("Unable to load home timeline",e);
    }
  }

  protected void finishedLoadingList() {
    setLoadMoreViews();
    setListAdapter(adapter);
    getListView().setSelection(1);
    progressDialog.dismiss();
  }

  public void loadingMoreStatuses() {
    setProgressBarIndeterminateVisibility(true);
  }

  public void statusesLoaded(LoadMoreStatusesResult result) {
    setProgressBarIndeterminateVisibility(false);
    if (result.newer) {
      headerView.hideProgress();
      adapter.appendNewer(result.statuses);
      getListView().setSelection(1);
    } else {
      footerView.hideProgress();
      adapter.appendOlder(result.statuses);
    }
  }

  private void loadNewerTweets() {
    headerView.showProgress();
    new LoadMoreAsyncTask(this, twitter, adapter.getFirstId(), true).execute();
  }

  private void loadOlderTweets() {
    footerView.showProgress();
    new LoadMoreAsyncTask(this, twitter, adapter.getLastId()-1, false).execute();
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