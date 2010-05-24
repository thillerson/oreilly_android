package com.oreilly.android.otweet;

import com.oreilly.android.otweet.authorization.OAuthHelper;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.RequestToken;
import android.app.Application;

public class OTweetApplication extends Application {

  private Twitter twitter;
  private RequestToken currentRequestToken;
  private OAuthHelper oAuthHelper;

  @Override
  public void onCreate() {
    super.onCreate();
    oAuthHelper = new OAuthHelper(this);
    twitter = new TwitterFactory().getInstance();
    oAuthHelper.configureOAuth(twitter);
  }

  public Twitter getTwitter() {
    return twitter;
  }

  public boolean isAuthorized() {
    return oAuthHelper.hasAccessToken();
  }
  
  public String beginAuthorization() {
    try {
      if (null == currentRequestToken) {
        currentRequestToken = twitter.getOAuthRequestToken();
      }
      return currentRequestToken.getAuthorizationURL();
    } catch (TwitterException e) {
      e.printStackTrace();
    }
    return null;
  }
  
}
