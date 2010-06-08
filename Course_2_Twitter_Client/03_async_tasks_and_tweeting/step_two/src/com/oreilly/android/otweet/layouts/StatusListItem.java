package com.oreilly.android.otweet.layouts;

import java.net.URL;

import com.oreilly.android.otweet.R;
import com.oreilly.android.otweet.tasks.LoadImageAsyncTask;
import com.oreilly.android.otweet.tasks.LoadImageAsyncTask.LoadImageAsyncTaskResponder;

import twitter4j.Status;
import twitter4j.User;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StatusListItem extends RelativeLayout implements LoadImageAsyncTaskResponder {

  private ImageView     avatarView;
  private TextView      screenName;
  private TextView      statusText;
  private AsyncTask<URL, Void, Drawable> latestLoadTask;

  public StatusListItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void setStatus(Status status) {
    final User user = status.getUser();
    findViews();
    screenName.setText(user.getScreenName());
    statusText.setText(status.getText());
    // cancel old task
    if (null != latestLoadTask) {
      latestLoadTask.cancel(true);
    }
    latestLoadTask = new LoadImageAsyncTask(this).execute(user.getProfileImageURL());
  }

  public void imageLoading() {
    avatarView.setImageDrawable(null);
  }

  public void imageLoadCancelled() {
    // do nothing
  }

  public void imageLoaded(Drawable drawable) {
    avatarView.setImageDrawable(drawable);
  }

  private void findViews() {
    avatarView = (ImageView) findViewById(R.id.user_avatar);
    screenName = (TextView) findViewById(R.id.status_user_name_text);
    statusText = (TextView) findViewById(R.id.status_text);
  }

}
