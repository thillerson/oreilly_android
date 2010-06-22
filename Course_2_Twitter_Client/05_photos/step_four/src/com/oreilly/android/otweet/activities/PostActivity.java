package com.oreilly.android.otweet.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.Status;

import com.harrison.lee.twitpic4j.TwitPic;
import com.harrison.lee.twitpic4j.TwitPicResponse;
import com.oreilly.android.otweet.OTweetApplication;
import com.oreilly.android.otweet.R;
import com.oreilly.android.otweet.tasks.PostPhotoAsyncTask;
import com.oreilly.android.otweet.tasks.PostTweetAsyncTask;
import com.oreilly.android.otweet.tasks.PostPhotoAsyncTask.PostPhotoResponder;
import com.oreilly.android.otweet.tasks.PostTweetAsyncTask.PostTweetResponder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PostActivity extends Activity implements PostTweetResponder, PostPhotoResponder {

  private static final int REQUEST_CHOOSE_PHOTO_FROM_LIBRARY = 0;
  private static final int REQUEST_CHOOSE_PHOTO_FROM_CAMERA = 1;
  private static final String PHOTO_URI_BUNDLE_KEY = "photoURI";

  private OTweetApplication app;
  private TextView counterText;
  private EditText tweetContent;
  private AlertDialog alertDialog;
  private ProgressDialog progressDialog;
  private Button photoButton;
  private Uri photoUri;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (OTweetApplication)getApplication();
    setContentView(R.layout.post_view);
    setUpViews();
  }
    
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (null != photoUri) {
      outState.putString(PHOTO_URI_BUNDLE_KEY, photoUri.toString());
    }
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    String photoURIString = savedInstanceState.getString(PHOTO_URI_BUNDLE_KEY);
    if (null != photoURIString) {
      photoUri = Uri.parse(photoURIString);
      photoToPostChosen(photoUri);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (REQUEST_CHOOSE_PHOTO_FROM_LIBRARY == requestCode && resultCode == RESULT_OK) {
      photoToPostChosen(intent.getData());
    } else if (REQUEST_CHOOSE_PHOTO_FROM_CAMERA == requestCode && resultCode == RESULT_OK) {
      photoToPostChosen(photoUri);
    } else {
      super.onActivityResult(requestCode, resultCode, intent);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    configurePhotoButton();
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
  
  protected void photoToPostChosen(Uri uri) {
    photoUri = uri;
    String id = photoUri.getLastPathSegment();
    if (null != id) {
      photoButton.setText(null);
      Bitmap thumbBitmap = MediaStore.Images.Thumbnails.getThumbnail(
          getContentResolver(),
          Long.parseLong(id),
          MediaStore.Images.Thumbnails.MICRO_KIND,
          null);
      photoButton.setBackgroundDrawable(new BitmapDrawable(thumbBitmap));
    }
  }
  
  public void photoPosting() {
    tweetPosting();
  }
  
  public void tweetPosting() {
    progressDialog = ProgressDialog.show(
        this,
        getResources().getString(R.string.posting_title),
        getResources().getString(R.string.posting_description)
      );
  }

  public void tweetPosted(Status tweet) {
    progressDialog.dismiss();
    Toast.makeText(this, R.string.tweet_posted, Toast.LENGTH_LONG).show();
    finish();
  }

  public void photoPosted(TwitPicResponse result) {
    tweetPosted(null);
  }

  private void postValidTweetOrWarn() {
    String postText = tweetContent.getText().toString();
    int postLength = postText.length();
    if (140 < postLength) {
      alertDialog = new AlertDialog.Builder(this).
        setTitle(R.string.too_many_characters).
        setMessage(R.string.too_many_characters_description).
        setPositiveButton(android.R.string.ok, new OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            alertDialog.dismiss(); alertDialog = null;
          }
        }).
        show();
    } else if (0 == postLength) {
      alertDialog = new AlertDialog.Builder(this).
      setTitle(R.string.tweet_is_blank).
      setMessage(R.string.blank_tweet_description).
      setPositiveButton(android.R.string.ok, new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          alertDialog.dismiss(); alertDialog = null;
        }
      }).
      show();
    } else {
      if (null == photoUri) {
        new PostTweetAsyncTask(this, app.getTwitter()).execute(postText);
      } else {
        TwitPic twitpic = new TwitPic(app.getTwitPicUsername(), app.getTwitPicPassword());
        new PostPhotoAsyncTask(this, this, twitpic).execute(postText, photoUri);
      }
    }
  }

  // called when post button on view is clicked
  public void postButtonClicked(View view) {
    postValidTweetOrWarn();
  }
  
  // called when #androidjava link clicked
  public void hashButtonClicked(View view) {
    String currentText = tweetContent.getText().toString();
    tweetContent.setText(currentText.concat(" #androidjava"));
  }
  
  protected void openCamera() {
    SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
    String filenameTimestamp = timeStampFormat.format(new Date());
    ContentValues values = new ContentValues();
    values.put(Media.TITLE, String.format("otweet_photo_%s.jpg", filenameTimestamp));
    values.put(Media.DESCRIPTION, getString(R.string.image_posted_from_o_tweet));
    photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
    startActivityForResult(intent, REQUEST_CHOOSE_PHOTO_FROM_CAMERA);
  }

  protected void openPhotoLibrary() {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
    intent.setType("image/*");
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    startActivityForResult(intent, REQUEST_CHOOSE_PHOTO_FROM_LIBRARY);
  }

  // called when post button on view is clicked
  public void photoButtonClicked(View view) {
    if (app.hasTwitPicCredentials()) {
      new AlertDialog.Builder(this).
        setTitle(R.string.attach_photo).
        setMessage(R.string.choose_a_photo_source).
        setPositiveButton(R.string.camera, cameraButtonClickListener).
        setNeutralButton(R.string.photo_library, libraryButtonClickListener).
        setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        }).
        show();
    } else {
      Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
    }
  }
  
  private void configurePhotoButton() {
    if (null != photoUri) {
      photoButton.setText(null);
    } else if (app.hasTwitPicCredentials()) {
      photoButton.setText(R.string.attach_photo);
    } else {
      photoButton.setText(R.string.sign_in_to_twitpic);
    }
  }

  private void setUpViews() {
    counterText = (TextView)findViewById(R.id.counter_text);
    photoButton = (Button)findViewById(R.id.photo_button);
    configurePhotoButton();
    tweetContent = (EditText)findViewById(R.id.tweet_contents);
    tweetContent.addTextChangedListener(new TextWatcher() {
      public void afterTextChanged(Editable text) { }
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

      public void onTextChanged(CharSequence s, int start, int before, int count) {
        int charsLeft = 140 - s.length();
        counterText.setText(String.valueOf(charsLeft));
      }
    });
  }
  
  private OnClickListener cameraButtonClickListener = new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int which) {
      openCamera();
    }
  };
  private OnClickListener libraryButtonClickListener = new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int which) {
      openPhotoLibrary();
    }
  };

}
