package com.oreilly.android.otweet.tasks;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.os.AsyncTask;

public class PostTweetAsyncTask extends AsyncTask<String, Void, twitter4j.Status> {

  private Twitter twitter;
  private PostTweetResponder responder;
  
  public PostTweetAsyncTask(PostTweetResponder responder, Twitter twitter) {
    super();
    this.twitter = twitter;
    this.responder = responder;
  }

  public interface PostTweetResponder {
    public void tweetPosting();
    public void tweetPosted(twitter4j.Status tweet);
  }
  
  @Override
  protected twitter4j.Status doInBackground(String... params) {
    String tweet = params[0];
    try {
      return twitter.updateStatus(tweet);
    } catch (TwitterException e) {
      throw new RuntimeException("Couldn't post status", e);
    }
  }

  @Override
  protected void onPostExecute(twitter4j.Status result) {
    super.onPostExecute(result);
    responder.tweetPosted(result);
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    responder.tweetPosting();
  }

}
