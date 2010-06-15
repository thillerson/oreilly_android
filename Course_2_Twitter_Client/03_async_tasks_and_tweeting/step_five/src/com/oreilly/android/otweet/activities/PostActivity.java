package com.oreilly.android.otweet.activities;

import twitter4j.Status;

import com.oreilly.android.otweet.OTweetApplication;
import com.oreilly.android.otweet.R;
import com.oreilly.android.otweet.tasks.PostTweetAsyncTask;
import com.oreilly.android.otweet.tasks.PostTweetAsyncTask.PostTweetResponder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PostActivity extends Activity implements PostTweetResponder {

  private OTweetApplication app;
  private TextView counterText;
  private EditText tweetContent;
  private AlertDialog alertDialog;
  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (OTweetApplication)getApplication();
    setContentView(R.layout.post_view);
    setUpViews();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return MenuHelper.openActivityFromMenuItem(this, item);
  }
  
  public void tweetPosted(Status tweet) {
    progressDialog.dismiss();
    Toast.makeText(this, R.string.tweet_posted, Toast.LENGTH_LONG).show();
    finish();
  }

  public void tweetPosting() {
    progressDialog = ProgressDialog.show(
        this,
        getResources().getString(R.string.posting_title),
        getResources().getString(R.string.posting_description)
      );
  }

  private void postValidTweetOrWarn() {
    String postText = tweetContent.getText().toString();
    int postLength = postText.length();
    if (140 < postLength) {
      alertDialog = new AlertDialog.Builder(this).
        setTitle(R.string.too_many_characters).
        setMessage(R.string.too_many_characters_description).
        setPositiveButton(android.R.string.ok, new OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            alertDialog.dismiss(); alertDialog = null;
          }
        }).
        show();
    } else if (0 == postLength) {
      alertDialog = new AlertDialog.Builder(this).
      setTitle(R.string.tweet_is_blank).
      setMessage(R.string.blank_tweet_description).
      setPositiveButton(android.R.string.ok, new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          alertDialog.dismiss(); alertDialog = null;
        }
      }).
      show();
    } else {
      new PostTweetAsyncTask(this, app.getTwitter()).execute(postText);
    }
  }

  // called when post button on view is clicked
  public void postButtonClicked(View view) {
    postValidTweetOrWarn();
  }
  
  private void setUpViews() {
    counterText = (TextView)findViewById(R.id.counter_text);
    tweetContent = (EditText)findViewById(R.id.tweet_contents);
    tweetContent.addTextChangedListener(new TextWatcher() {
      public void afterTextChanged(Editable text) { }
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

      public void onTextChanged(CharSequence s, int start, int before, int count) {
        int charsLeft = 140 - s.length();
        counterText.setText(String.valueOf(charsLeft));
        if (charsLeft <= 0) {
          counterText.setTextColor(Color.RED);
        } else {
          counterText.setTextColor(Color.BLACK);
        }
      }
    });
  }

}
