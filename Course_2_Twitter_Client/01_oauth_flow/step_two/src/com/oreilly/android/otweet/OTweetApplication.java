package com.oreilly.android.otweet;

import com.oreilly.android.otweet.authorization.OAuthHelper;

import android.app.Application;

public class OTweetApplication extends Application {

  private OAuthHelper oAuthHelper;

  @Override
  public void onCreate() {
    super.onCreate();
    oAuthHelper = new OAuthHelper(this);
  }

  public boolean isAuthorized() {
    return oAuthHelper.hasAccessToken();
  }
  
}
