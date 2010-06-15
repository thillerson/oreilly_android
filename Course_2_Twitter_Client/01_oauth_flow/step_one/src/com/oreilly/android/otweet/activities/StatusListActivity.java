package com.oreilly.android.otweet.activities;

import com.oreilly.android.otweet.R;

import android.app.ListActivity;
import android.os.Bundle;

public class StatusListActivity extends ListActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.main);
  }
  
}