package com.oreilly.android.otweet.layouts;

import java.io.IOException;

import com.oreilly.android.otweet.R;

import twitter4j.Status;
import twitter4j.User;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StatusListItem extends RelativeLayout {

  final private Handler handler = new Handler();
  private ImageView avatarView;
	private TextView screenName;
	private TextView statusText;
  protected Drawable avatarDrawable;

  private Runnable finishedLoadingDrawable = new Runnable() {
    public void run() {
      finishedLoadingUserAvatar();
    }
  };

	public StatusListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setStatus(Status status) {
		final User user = status.getUser();
		findViews();
		screenName.setText(user.getScreenName());
		statusText.setText(status.getText());
		avatarView.setImageDrawable(null);
    Thread loadUserAvatarThread = new Thread() {
      @Override
      public void run() {
        try {
          avatarDrawable = Drawable.createFromStream(
              user.getProfileImageURL().openStream(),
              user.getName()
          );
        } catch (IOException e) {
          Log.e(getClass().getName(), "Could not load image.", e);
        }
        handler.post(finishedLoadingDrawable);
      }
    };
    loadUserAvatarThread.start();
	}

  protected void finishedLoadingUserAvatar() {
    avatarView.setImageDrawable(avatarDrawable);
  }

	private void findViews() {
    avatarView = (ImageView)findViewById(R.id.user_avatar);
		screenName = (TextView)findViewById(R.id.status_user_name_text);
		statusText = (TextView)findViewById(R.id.status_text);
	}


}
