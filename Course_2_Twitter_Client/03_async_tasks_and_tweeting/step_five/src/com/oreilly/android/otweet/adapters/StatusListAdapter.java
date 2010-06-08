/**
 * 
 */
package com.oreilly.android.otweet.adapters;

import java.util.ArrayList;

import twitter4j.Status;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.oreilly.android.otweet.R;
import com.oreilly.android.otweet.layouts.StatusListItem;

public class StatusListAdapter extends ArrayAdapter<Status> {

  private Context context;

  public StatusListAdapter(Context context, ArrayList<Status> statii) {
    super(context, android.R.layout.simple_list_item_1, statii);
    this.context = context;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    StatusListItem v;
    if (null == convertView) {
      v = (StatusListItem) View.inflate(context, R.layout.status_list_item, null);
    } else {
      v = (StatusListItem) convertView;
    }
    v.setStatus((Status) getItem(position));
    return v;
  }

  public long getFirstId() {
    Status firstStatus = getItem(0);
    if (null == firstStatus) {
      return 0;
    } else {
      return firstStatus.getId();
    }
  }

  public long getLastId() {
    Status lastStatus = getItem(getCount()-1);
    if (null == lastStatus) {
      return 0;
    } else {
      return lastStatus.getId();
    }
  }

  public void appendNewer(ArrayList<Status> statii) {
    setNotifyOnChange(false);
    for (Status status : statii) {
      insert(status, 0);
    }
    notifyDataSetChanged();
  }
  
  public void appendOlder(ArrayList<Status> statii) {
    setNotifyOnChange(false);
    for (Status status : statii) {
      add(status);
    }
    notifyDataSetChanged();
  }
}