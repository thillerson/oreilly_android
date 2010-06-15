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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

public class StatusListActivity extends ListActivity {

  final private Handler handler = new Handler();
  private OTweetApplication app;
  private Twitter twitter;
  private LoadMoreListItem headerView;
  private LoadMoreListItem footerView;
  private StatusListAdapter adapter;
  protected ProgressDialog progressDialog;

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
      adapter = new StatusListAdapter(this, statii);
    } catch (TwitterException e) {
      throw new RuntimeException("Unable to load home timeline", e);
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
      // Watch out! Doesn't account for header/footer! -> Status status =
      // adapter.getItem(position);
      Status status = (Status) getListView().getItemAtPosition(position);
      Intent intent = new Intent(this, StatusDetailActivity.class);
      intent.putExtra(StatusDetailActivity.STATUS, status);
      startActivity(intent);
    }
  }

  protected void finishedLoadingList() {
    setLoadMoreViews();
    setListAdapter(adapter);
    getListView().setSelection(1);
    progressDialog.dismiss();
  }

  private void loadNewerTweets() {
    headerView.showProgress();
    setProgressBarIndeterminateVisibility(true);
    Thread loadMoreThread = new Thread() {
      public void run() {
        try {
          ArrayList<Status> statii = twitter.getHomeTimeline(new Paging().sinceId(adapter.getFirstId()));
          Runnable finishedLoadingNewerTask = new LoadNewerResult(statii);;
          handler.post(finishedLoadingNewerTask);
        } catch (TwitterException e) {
          throw new RuntimeException("Unable to load home timeline", e);
        }
      }
    };
    loadMoreThread.start();
  }

  protected void finishedLoadingNewer(ArrayList<Status> statii) {
    adapter.appendNewer(statii);
    headerView.hideProgress();
    setProgressBarIndeterminateVisibility(false);
    getListView().setSelection(1);
  }

  private void loadOlderTweets() {
    footerView.showProgress();
    setProgressBarIndeterminateVisibility(true);
    Thread loadMoreThread = new Thread() {
      public void run() {
        try {
          ArrayList<Status> statii = twitter.getHomeTimeline(new Paging().maxId(adapter.getLastId() - 1));
          Runnable finishedLoadingOlderTask = new LoadOlderResult(statii);
          handler.post(finishedLoadingOlderTask);
        } catch (TwitterException e) {
          throw new RuntimeException("Unable to load home timeline", e);
        }
      }
    };
    loadMoreThread.start();
  }

  protected void finishedLoadingOlder(ArrayList<Status> statii) {
    adapter.appendOlder(statii);
    footerView.hideProgress();
    setProgressBarIndeterminateVisibility(false);
  }

  private void setLoadMoreViews() {
    headerView = (LoadMoreListItem) getLayoutInflater().inflate(R.layout.load_more, null);
    headerView.showHeaderText();
    footerView = (LoadMoreListItem) getLayoutInflater().inflate(R.layout.load_more, null);
    footerView.showFooterText();
    getListView().addHeaderView(headerView);
    getListView().addFooterView(footerView);
  }

  private Runnable finishedLoadingListTask = new Runnable() {
    public void run() {
      finishedLoadingList();
    }
  };

  private class LoadNewerResult implements Runnable {
    private ArrayList<Status> statii;

    public LoadNewerResult(ArrayList<Status> statii) {
      super();
      this.statii = statii;
    }

    public void run() {
      finishedLoadingNewer(statii);
    }
  };

  private class LoadOlderResult implements Runnable {
    private ArrayList<Status> statii;

    public LoadOlderResult(ArrayList<Status> statii) {
      super();
      this.statii = statii;
    }

    public void run() {
      finishedLoadingOlder(statii);
    }
  };

}