package com.oreilly.android.otweet.activities;

import com.oreilly.android.otweet.OTweetApplication;
import com.oreilly.android.otweet.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

  private EditText passwordText;
  private EditText usernameText;
  private OTweetApplication app;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (OTweetApplication)getApplication();
    setContentView(R.layout.settings);
    setUpViews();
    loadSettings();
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
  
  public void saveButtonClicked(View view) {
    saveSettings();
  }

  private void loadSettings() {
    usernameText.setText(app.getTwitPicUsername());
  }

  private void saveSettings() {
    String username = usernameText.getText().toString();
    String password = passwordText.getText().toString();
    app.saveTwitPicCredentials(username, password);
    Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_LONG).show();
    finish();
  }

  private void setUpViews() {
    usernameText = (EditText)findViewById(R.id.username);
    passwordText = (EditText)findViewById(R.id.password);
  }

}
