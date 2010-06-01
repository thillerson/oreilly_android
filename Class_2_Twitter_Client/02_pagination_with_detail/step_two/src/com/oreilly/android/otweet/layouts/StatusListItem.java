package com.oreilly.android.otweet.layouts;

import com.oreilly.android.otweet.R;

import twitter4j.Status;
import twitter4j.User;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StatusListItem extends RelativeLayout {

	private TextView screenName;
	private TextView statusText;
  protected Drawable avatarDrawable;

	public StatusListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setStatus(Status status) {
		final User user = status.getUser();
		findViews();
		screenName.setText(user.getScreenName());
		statusText.setText(status.getText());
	}

	private void findViews() {
		screenName = (TextView)findViewById(R.id.status_user_name_text);
		statusText = (TextView)findViewById(R.id.status_text);
	}


}
