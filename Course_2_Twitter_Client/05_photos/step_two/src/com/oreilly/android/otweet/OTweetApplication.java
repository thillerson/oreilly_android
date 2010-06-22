package com.oreilly.android.otweet;

import com.oreilly.android.otweet.authorization.SettingsHelper;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;
import android.app.Application;

public class OTweetApplication extends Application {

  private Twitter twitter;
  private RequestToken currentRequestToken;
  private SettingsHelper settingsHelper;
  private String username;
  private String password;

  @Override
  public void onCreate() {
    super.onCreate();
    settingsHelper = new SettingsHelper(this);
    twitter = new TwitterFactory().getInstance();
    settingsHelper.configureOAuth(twitter);
    
    String[] credentials = settingsHelper.getTwitPicCredentials();
    username = credentials[0];
    password = credentials[1];
  }

  public Twitter getTwitter() {
    return twitter;
  }

  public boolean isAuthorized() {
    return settingsHelper.hasAccessToken();
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

  public void authorized() {
    try {
      AccessToken accessToken = twitter.getOAuthAccessToken();
      settingsHelper.storeAccessToken(accessToken);
    } catch (TwitterException e) {
      throw new RuntimeException("Unable to authorize user", e); 
    }
    
  }

  public boolean authorize(String pin) {
    try {
      AccessToken accessToken = twitter.getOAuthAccessToken(currentRequestToken, pin);
      settingsHelper.storeAccessToken(accessToken);
      return true;
    } catch (TwitterException e) {
      throw new RuntimeException("Unable to authorize user", e); 
    }
  }

  public String getTwitPicUsername() {
    return username;
  }

  public String getTwitPicPassword() {
    return password;
  }
  
  public boolean hasTwitPicCredentials() {
    return (null != username && null != password);
  }

  public void saveTwitPicCredentials(String username, String password) {
    this.username = username;
    this.password = password;
    settingsHelper.saveTwitPicCredentials(username, password);
  }

}
