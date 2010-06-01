package com.oreilly.android.otweet.layouts;

import com.oreilly.android.otweet.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadMoreListItem extends RelativeLayout {

	private TextView loadMoreText;
	private ProgressBar progressIndicator;

	public LoadMoreListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onFinishInflate() {
		findViews();
	}
	
	public void showProgress() {
	  progressIndicator.setVisibility(View.VISIBLE);
	}

  public void hideProgress() {
    progressIndicator.setVisibility(View.INVISIBLE);
  }

	public void showHeaderText() {
	  String headerText = getResources().getString(R.string.load_more_header);
		loadMoreText.setText(headerText);
	}

  public void showFooterText() {
    String headerText = getResources().getString(R.string.load_more_footer);
    loadMoreText.setText(headerText);
  }

  private void findViews() {
		loadMoreText = (TextView)findViewById(R.id.load_more_text);
		progressIndicator = (ProgressBar)findViewById(R.id.load_more_progress);
	}

}
