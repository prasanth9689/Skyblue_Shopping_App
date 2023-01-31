package com.skyblue.shop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.skyblue.shop.AppConstants;
import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;
import com.skyblue.shop.model.User;


import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostReviewActivity extends AppCompatActivity {
    private SessionHandler session;
    User user;
    ImageView backBtn, reviewImage;
    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    Uri selectedImageUri;
    Context context = this;
    Button postReviewButton;
    Bitmap bitmap;
    private static final int MAX_IMAGE_SIZE = 10;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    int id = 1;
    public static String id3 = "test_channel_03";
    NotificationManager nm;
    EditText reviewEditText;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_READ_PERMISSION = 687;
    String timeString , dateString , timeDateString , reviewTextHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_review);

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        initializeVariable();
        setOnClickListener();
    }

    private void setOnClickListener() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        reviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

        postReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null && !selectedImageUri.equals(Uri.EMPTY)) {
                 //   showMessageInSnackbar(getString(R.string.review_image_selected_ok));
                    UploadAsyncTask();
                } else {
                //    showMessageInSnackbar(getString(R.string.please_select_review_image));
                }
            }
        });
    }

    private void initializeVariable() {
        backBtn = findViewById(R.id.back_button_review);
        reviewImage = findViewById(R.id.review_image_upload);
        postReviewButton = findViewById(R.id.post_review_btn);
        reviewEditText = findViewById(R.id.review_edit_text);
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
        }else {
            if(ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
               // showMessageInSnackbar("Permission False or True");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    //    imageChooser();
    }

    private void UploadAsyncTask() {

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context,id3);
        mBuilder.setContentTitle("Picture Upload")
                .setContentText("Upload in progress")
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        reviewTextHolder = reviewEditText.getText().toString();

//        UploadAsyncTask uploadAsyncTask = new UploadAsyncTask(context);
//        uploadAsyncTask.execute();
    }

//    private class UploadAsyncTask extends AsyncTask<Void, Integer, String> {

//        HttpClient httpClient = new DefaultHttpClient();
//        private final Context context;
//        private Exception exception;

//        private UploadAsyncTask(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            HttpResponse httpResponse = null;
//            HttpEntity httpEntity = null;
//            String responseString = null;
//
//            try {
//                HttpPost httpPost = new HttpPost(AppConstants.REVIEW_SEND_POST);
//                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
//
//                                String filename = "hiiii";
//                File f = new File(context.getCacheDir(), filename);
//                f.createNewFile();
//
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 60 /*ignored for PNG*/, bos);
//                byte[] bitmapdata = bos.toByteArray();
//
//                //write the bytes in file
//                FileOutputStream fos = new FileOutputStream(f);
//                fos.write(bitmapdata);
//
//                FileBody fileBody = new FileBody(f);
//
//                String fileNameImagePlaceholder = "placeHolderImage";
//                File filePlaceHolderImage = new File(context.getCacheDir(), fileNameImagePlaceholder);
//                filePlaceHolderImage.createNewFile();
//
//                Bitmap scaledBitmapPlaceholder = scaleDown(bitmap, MAX_IMAGE_SIZE, true);
//
//                //Convert bitmap to byte array
//                // Bitmap bitmap = ((BitmapDrawable)ShowSelectedImagePrimary.getDrawable()).getBitmap();
//                ByteArrayOutputStream bosPlaceholder = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bosPlaceholder);
//                byte[] bitmapdataPlaceholder = bosPlaceholder.toByteArray();
//
//                //write the bytes in file
//                FileOutputStream fosPlaceholder = new FileOutputStream(filePlaceHolderImage);
//                fosPlaceholder.write(bitmapdataPlaceholder);
//
//                dateString = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//                timeString = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//                timeDateString  = dateString +" "+timeString;
//
//                multipartEntityBuilder.addPart("file_image", fileBody);
//                multipartEntityBuilder.addTextBody("review_text", reviewTextHolder);
//                multipartEntityBuilder.addTextBody("user_id", user.getId());
//                multipartEntityBuilder.addTextBody("time_date", timeDateString);

//                // Progress listener - updates task's progress
//                MyHttpEntity.ProgressListener progressListener =
//                        new MyHttpEntity.ProgressListener() {
//                            @Override
//                            public void transferred(float progress) {
//                                publishProgress((int) progress);
//                            }
//                        };

                // POST
//                httpPost.setEntity(new MyHttpEntity(multipartEntityBuilder.build(),
//                        progressListener));
//

//                httpResponse = httpClient.execute(httpPost);
//                httpEntity = httpResponse.getEntity();
//
//                int statusCode = httpResponse.getStatusLine().getStatusCode();
//                if (statusCode == 200) {
//                    // Server response
//                    responseString = EntityUtils.toString(httpEntity);
//
//                } else {
//                    responseString = "Error occurred! Http Status Code: "
//                            + statusCode;
//                }
//            } catch (UnsupportedEncodingException | ClientProtocolException e) {
//                e.printStackTrace();
//                Log.e("UPLOAD", e.getMessage());
//                this.exception = e;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return responseString;
//        }

//        @Override
//        protected void onPreExecute() {
//
//            mBuilder.setProgress(100, 0, false);
//            mNotifyManager.notify(id, mBuilder.build());
//
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//
//            mBuilder.setProgress(100, progress[0], false);
//            mNotifyManager.notify(id, mBuilder.build());
//            super.onProgressUpdate(progress);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            mBuilder.setProgress(0,0,false);
//            mBuilder.setContentText("Upload completed");
//            mNotifyManager.notify(id, mBuilder.build());
//
//            Toast.makeText(getApplicationContext(),
//                    result, Toast.LENGTH_LONG).show();
//        }
//    }

    private void createchannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel mChannel = new NotificationChannel(id3,
                    getString(R.string.channel_name),  //name of the channel
                    NotificationManager.IMPORTANCE_DEFAULT);   //importance level

            //a urgent level channel
            mChannel = new NotificationChannel(id3,
                    getString(R.string.channel_name2),  //name of the channel
                    NotificationManager.IMPORTANCE_HIGH);   //importance level
            // Configure the notification channel.
            mChannel.setDescription(getString(R.string.channel_description3));
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this channel, if the device supports this feature.
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            nm.createNotificationChannel(mChannel);
        }
    }


//    private class UploadAsyncTask extends AsyncTask<Void, Integer, String> {
//
//        HttpClient httpClient = new DefaultHttpClient();
//        private final Context context;
//        private Exception exception;

//        private UploadAsyncTask(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            HttpResponse httpResponse = null;
//            HttpEntity httpEntity = null;
//            String responseString = null;
//
//            try {
//                HttpPost httpPost = new HttpPost(AppConstants.REVIEW_SEND_POST);
//                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
//
//                String filename = "hiiii";
//                File f = new File(context.getCacheDir(), filename);
//                f.createNewFile();
//
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 60 /*ignored for PNG*/, bos);
//                byte[] bitmapdata = bos.toByteArray();
//
//                //write the bytes in file
//                FileOutputStream fos = new FileOutputStream(f);
//                fos.write(bitmapdata);
//
//                FileBody fileBody = new FileBody(f);
//
//                String fileNameImagePlaceholder = "placeHolderImage";
//                File filePlaceHolderImage = new File(context.getCacheDir(), fileNameImagePlaceholder);
//                filePlaceHolderImage.createNewFile();
//
//                Bitmap scaledBitmapPlaceholder = scaleDown(bitmap, MAX_IMAGE_SIZE, true);
//
//                //Convert bitmap to byte array
//                // Bitmap bitmap = ((BitmapDrawable)ShowSelectedImagePrimary.getDrawable()).getBitmap();
//                ByteArrayOutputStream bosPlaceholder = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bosPlaceholder);
//                byte[] bitmapdataPlaceholder = bosPlaceholder.toByteArray();
//
//                //write the bytes in file
//                FileOutputStream fosPlaceholder = new FileOutputStream(filePlaceHolderImage);
//                fosPlaceholder.write(bitmapdataPlaceholder);
//
//                FileBody fileBodyPlaceholder = new FileBody(filePlaceHolderImage);
//
//                multipartEntityBuilder.addPart("file_image", fileBody);
//                multipartEntityBuilder.addPart("file_image_placeholder", fileBodyPlaceholder);
//                multipartEntityBuilder.addTextBody("review_text", reviewTextHolder);
//
//                // Progress listener - updates task's progress
//                MyHttpEntity.ProgressListener progressListener =
//                        new MyHttpEntity.ProgressListener() {
//                            @Override
//                            public void transferred(float progress) {
//                                publishProgress((int) progress);
//                            }
//                        };
//
//                // POST
//                httpPost.setEntity(new MyHttpEntity(multipartEntityBuilder.build(),
//                        progressListener));
//
//
//                httpResponse = httpClient.execute(httpPost);
//                httpEntity = httpResponse.getEntity();
//
//                int statusCode = httpResponse.getStatusLine().getStatusCode();
//                if (statusCode == 200) {
//                    // Server response
//                    responseString = EntityUtils.toString(httpEntity);
//
//                } else {
//                    responseString = "Error occurred! Http Status Code: "
//                            + statusCode;
//                }
//            } catch (UnsupportedEncodingException | ClientProtocolException e) {
//                e.printStackTrace();
//                Log.e("UPLOAD", e.getMessage());
//                this.exception = e;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return responseString;
//        }
//
//        @Override
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//            super.onProgressUpdate(progress);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            Toast.makeText(getApplicationContext(),
//                    result, Toast.LENGTH_LONG).show();
//        }
//    }

//    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
//                                   boolean filter) {
//        float ratio = Math.min(
//                (float) maxImageSize / realImage.getWidth(),
//                (float) maxImageSize / realImage.getHeight());
//        int width = Math.round((float) ratio * realImage.getWidth());
//        int height = Math.round((float) ratio * realImage.getHeight());
//
//        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
//                height, filter);
//        return newBitmap;
//    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }


    // this function is triggered when user
    // selects the image from the imageChooser
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//
//            // compare the resultCode with the
//            // SELECT_PICTURE constant
//            if (requestCode == SELECT_PICTURE) {
//                // Get the url of the image from data
//                selectedImageUri = data.getData();
//                if (null != selectedImageUri) {
//                    // update the preview image in the layout
//                    reviewImage.setImageURI(selectedImageUri);
//
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_READ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            imageChooser();
//        }
//    }

//    private void showMessageInSnackbar(String message) {
//        Snackbar snack = Snackbar.make(
//                (((Activity) context).findViewById(android.R.id.content)),
//                message, Snackbar.LENGTH_SHORT);
//        snack.setDuration(Snackbar.LENGTH_SHORT);//change Duration as you need
//        //snack.setAction(actionButton, new View.OnClickListener());//add your own listener
//        View view = snack.getView();
//        TextView tv = (TextView) view
//                .findViewById(com.google.android.material.R.id.snackbar_text);
//        tv.setTextColor(Color.WHITE);//change textColor
//
//        TextView tvAction = (TextView) view
//                .findViewById(com.google.android.material.R.id.snackbar_action);
//        tvAction.setTextSize(16);
//        tvAction.setTextColor(Color.WHITE);
//
//        snack.show();
//    }
}