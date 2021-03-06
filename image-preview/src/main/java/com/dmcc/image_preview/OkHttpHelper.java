package com.dmcc.image_preview;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by sll on 2015/3/10.
 */
public class OkHttpHelper {
  private OkHttpClient mOkHttpClient;

  public OkHttpHelper(OkHttpClient mOkHttpClient) {
    this.mOkHttpClient = mOkHttpClient;
  }

  public Response execute(Request request) throws IOException {
    return mOkHttpClient.newCall(request).execute();
  }

  public void enqueue(Request request, Callback responseCallback) {
    mOkHttpClient.newCall(request).enqueue(responseCallback);
  }

  public void enqueue(Request request) {
    mOkHttpClient.newCall(request).enqueue(new Callback() {

      @Override public void onFailure(Call call, IOException e) {

      }

      @Override public void onResponse(Call call, Response response) throws IOException {

      }
    });
  }

  public String getStringFromServer(String url) throws IOException {
    Request request = new Request.Builder().url(url).build();
    Response response = execute(request);
    if (response.isSuccessful()) {
      return response.body().string();
    } else {
      throw new IOException("Unexpected code " + response);
    }
  }

  public void httpDownload(String url, File target) throws Exception {
    Request request = new Request.Builder().url(url).build();
    Response response = mOkHttpClient.newCall(request).execute();
    if (response.isSuccessful()) {
      BufferedSink sink = Okio.buffer(Okio.sink(target));
      sink.writeAll(response.body().source());
      sink.close();
    } else {
      throw new IOException("Unexpected code " + response);
    }
  }
}
