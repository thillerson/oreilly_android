package com.oreilly.android.otweet.activities;

import com.oreilly.android.otweet.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

public class MenuHelper {

  @SuppressWarnings("unchecked")
  public static boolean openActivityFromMenuItem(Context context, MenuItem item) {
    Intent intent;
    Class<? extends Activity> requestingClass = (Class<? extends Activity>) context.getClass();
    switch (item.getItemId()) {
    case R.id.menu_item_home:
      if (requestingClass != StatusListActivity.class) {
        intent = new Intent(context, StatusListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        return true;
      }
    break;
    case R.id.menu_item_post:
      if (requestingClass != PostActivity.class) {
        intent = new Intent(context, PostActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        return true;
      }
    break;
    }
    return false;
  }
  
  

}
