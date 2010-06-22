package com.oreilly.android.otweet.tasks;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.harrison.lee.twitpic4j.TwitPic;
import com.harrison.lee.twitpic4j.TwitPicResponse;
import com.harrison.lee.twitpic4j.exception.TwitPicException;

public class PostPhotoAsyncTask extends AsyncTask<Object, Void, TwitPicResponse>  {
  
  private Context context;
  private PostPhotoResponder responder;
  private TwitPic twitpic;

  public PostPhotoAsyncTask(PostPhotoResponder responder, Context context, TwitPic twitPic) {
    super();
    this.context = context;
    this.responder = responder;
    this.twitpic = twitPic;
  }

  public interface PostPhotoResponder {
    public void photoPosting();
    public void photoPosted(TwitPicResponse result);
  }
  
  @Override
  protected TwitPicResponse doInBackground(Object... params) {
    String message = (String)params[0];
    Uri photoURI = (Uri)params[1];
    InputStream in;
    byte[] bytes;
    ContentResolver resolver = context.getContentResolver();
    TwitPicResponse tpResponse = null;

    try {
      in = resolver.openInputStream(photoURI);
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      byte[] tmp = new byte[4096];
      int l;
      while ((l = in.read(tmp)) != -1) {
          bout.write(tmp, 0, l);
      }
      bout.flush();
      bytes = bout.toByteArray();
      in.close();

      tpResponse = twitpic.uploadAndPost(bytes, message);
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TwitPicException e) {
      e.printStackTrace();
    } finally {
    }
    
    return tpResponse;
  }
  
  @Override
  protected void onPostExecute(TwitPicResponse result) {
    super.onPostExecute(result);
    responder.photoPosted(result);
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    responder.photoPosting();
  }

}
