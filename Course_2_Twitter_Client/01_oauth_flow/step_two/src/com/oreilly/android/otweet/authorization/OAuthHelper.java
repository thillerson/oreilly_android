package com.oreilly.android.otweet.authorization;

import java.io.InputStream;
import java.util.Properties;

import com.oreilly.android.otweet.R;

import android.content.Context;

public class OAuthHelper {

  private String consumerSecretKey;
  private String consumerKey;
  private Context context;

  public OAuthHelper(Context context) {
    this.context = context;
    loadConsumerKeys();
  }
  
  public boolean hasAccessToken() {
    return false;
  }

  private void loadConsumerKeys() {
    try {
      Properties props = new Properties();
      InputStream stream = context.getResources().openRawResource(R.raw.oauth);
      props.load(stream);
      consumerKey = (String)props.get("consumer_key");
      consumerSecretKey = (String)props.get("consumer_secret_key");
    } catch (Exception e) {
      throw new RuntimeException("Unable to load consumer keys from oauth.properties", e);
    }
  }
}
