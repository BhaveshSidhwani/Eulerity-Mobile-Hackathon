package com.sidhwanibhavesh.euleritymobilehackathon;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class HttpRequestExecutor {

   private static final String LOG_TAG = HttpRequestExecutor.class.getSimpleName();
   static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
   static Handler handler = new Handler(Looper.getMainLooper());

   public static void getJsonData (MainActivity activity) {
      singleThreadExecutor.execute(() -> {
         URL requestUrl = prepareUrl(Constants.IMAGE_JSON_URL);
         if (requestUrl != null) {
            JSONArray responseData = makeHttpRequest(requestUrl);

            handler.post(() -> {
               if (responseData!= null) {
                  extractUrlFromJson(responseData, activity);
               }
            });
         }
      });
   }

   public static void getUploadImageUrl (Image image) {
      singleThreadExecutor.execute(() -> {
         URL uploadUrl = prepareUrl(Constants.UPLOAD_IMAGE_URL);
         if (uploadUrl != null) {
            JSONObject responseData = makeHttpRequestForUpload(uploadUrl);
            Log.d(LOG_TAG, "getUploadImageUrl: responseData: " + responseData.toString());
            try {
               String url = responseData.getString(Constants.JSON_URL_KEY);
               Log.d(LOG_TAG, "getUploadImageUrl: url: " + url);
               uploadImage(url, image);
            } catch (JSONException e) {
               e.printStackTrace();
            }
         }
      });
   }

   private static URL prepareUrl (String url) {
      URL requestUrl = null;
      try {
         requestUrl = new URL(url);
      } catch (MalformedURLException e) {
         e.printStackTrace();
      }
      return requestUrl;
   }

   private static JSONArray makeHttpRequest (URL requestUrl) {
      JSONArray responseJson = null;

      HttpURLConnection conn = null;
      StringBuilder builder = null;
      try {
         conn = (HttpURLConnection) requestUrl.openConnection();
         conn.setRequestProperty("Content-Type", "application/json");
         conn.setRequestMethod("GET");
         conn.connect();

         if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

            InputStreamReader inputStreamReader = new InputStreamReader(
                    conn.getInputStream(),
                    StandardCharsets.UTF_8);

            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            builder = new StringBuilder();
            while ( (line=reader.readLine()) != null ) {
               builder.append(line);
            }
            reader.close();
         } else {
            Log.e (LOG_TAG, "Error Response Code: " + conn.getResponseCode());
         }

         if (builder != null) {
            responseJson = new JSONArray(builder.toString());
         }
      } catch (IOException | JSONException e) {
         e.printStackTrace();
      } finally {
         if (conn != null) {
            conn.disconnect();
         }
      }
      return responseJson;
   }

   private static JSONObject makeHttpRequestForUpload (URL requestUrl) {
      JSONObject responseJson = null;

      HttpURLConnection conn = null;
      StringBuilder builder = null;
      try {
         conn = (HttpURLConnection) requestUrl.openConnection();
         conn.setRequestProperty("Content-Type", "application/json");
         conn.setRequestMethod("GET");
         conn.connect();

         if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

            InputStreamReader inputStreamReader = new InputStreamReader(
                    conn.getInputStream(),
                    StandardCharsets.UTF_8);

            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            builder = new StringBuilder();
            while ( (line=reader.readLine()) != null ) {
               builder.append(line);
            }
            reader.close();
         } else {
            Log.e (LOG_TAG, "Error Response Code: " + conn.getResponseCode());
         }

         if (builder != null) {
            responseJson = new JSONObject(builder.toString());
         }
      } catch (IOException | JSONException e) {
         e.printStackTrace();
      } finally {
         if (conn != null) {
            conn.disconnect();
         }
      }
      return responseJson;
   }

   private static void extractUrlFromJson(JSONArray jsonData, MainActivity activity) {
      Constants.imageArray = new ArrayList<>();

      try {
         for (int i=0; i<jsonData.length(); i++) {
            JSONObject row = (JSONObject) jsonData.get(i);
            String imageUrl = row.getString(Constants.JSON_URL_KEY);

            Picasso.get()
                    .load(imageUrl)
                    .into(new Target() {
                       @Override
                       public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                          Constants.imageArray.add(new Image(imageUrl, bitmap));
                          activity.initAdapter();
                       }

                       @Override
                       public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                          e.printStackTrace();
                       }

                       @Override
                       public void onPrepareLoad(Drawable placeHolderDrawable) {

                       }
                    });
         }
      } catch (JSONException e) {
         e.printStackTrace();
      }
   }

   private static void uploadImage (String url, Image image) {
      HttpURLConnection conn = null;
      DataOutputStream dos = null;

      URL uploadUrl = prepareUrl(url);

      String originalUrl = image.url;
      Bitmap bitmap = image.imageBitmap;

      if (uploadUrl != null) {
         try {
            conn = (HttpURLConnection) uploadUrl.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + UUID.randomUUID().toString());

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes("--" + UUID.randomUUID().toString() + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"" + Constants.APP_ID_KEY + "\"\r\n\r\n");
            dos.writeBytes(Constants.APP_ID + "\r\n");
            Log.d(LOG_TAG, "uploadImage: APP_ID: " + Constants.APP_ID);

            dos.writeBytes("--" + UUID.randomUUID().toString() + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"" + Constants.ORIGINAL_KEY + "\"\r\n\r\n");
            dos.writeBytes(originalUrl + "\r\n");
            Log.d(LOG_TAG, "uploadImage: originalUrl: " + originalUrl);

            dos.writeBytes("--" + UUID.randomUUID().toString() + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"" + Constants.FILE_KEY + "\"; filename=\"image.jpeg\"\r\n");
            dos.writeBytes("Content-Type: image/jpeg\r\n\r\n");

            File tempFile = File.createTempFile("image", ".jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();

            // Read the temporary file and write its contents to the output stream
            FileInputStream fileInputStream = new FileInputStream(tempFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
               dos.write(buffer, 0, bytesRead);
            }
            fileInputStream.close();

            dos.writeBytes("\r\n");
            dos.writeBytes("--" + UUID.randomUUID().toString() + "--\r\n");

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
               Log.d(LOG_TAG, "uploadImage: Success");
            } else {
               Log.d(LOG_TAG, "uploadImage: Fail: responseCode: " + responseCode);
            }

         } catch (IOException e) {
            throw new RuntimeException(e);
         } finally {
            if (conn != null) {
               conn.disconnect();
            }
            try {
               if (dos != null) {
                  dos.close();
               }
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }
}
