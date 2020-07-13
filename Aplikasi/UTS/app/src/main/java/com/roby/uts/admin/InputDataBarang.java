package com.roby.uts.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.roby.uts.R;
import com.roby.uts.server.BaseURL;
import com.roby.uts.server.VolleyMultipart;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputDataBarang extends AppCompatActivity {

  EditText edtkodeBarang, edtjenisBarang, edtnamaBarang, edtstockBarang, edthargaBarang;
  ImageView imgGambarBuku;

  Button btnTakeImg, simpanData;
  private RequestQueue mRequestQueue;

  Bitmap bitmap;

  private final int CameraR_PP = 1;
  String mCurrentPhotoPath;

  ProgressDialog pDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_input_data_barang);

    getSupportActionBar().hide();

    if (Build.VERSION.SDK_INT >= 24) {
      try {
        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
        m.invoke(null);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    mRequestQueue = Volley.newRequestQueue(this);
    pDialog = new ProgressDialog(this);
    pDialog.setCancelable(false);

    edtkodeBarang = (EditText) findViewById(R.id.edtkodeBarang);
    edtjenisBarang = (EditText) findViewById(R.id.edtjenisBarang);
    edtnamaBarang = (EditText) findViewById(R.id.edtnamaBarang);
    edtstockBarang = (EditText) findViewById(R.id.edtstockBarang);
    edthargaBarang = (EditText) findViewById(R.id.edthargaBarang);

    imgGambarBuku = (ImageView) findViewById(R.id.gambar);

    btnTakeImg = (Button) findViewById(R.id.btnTakeImage);
    simpanData = (Button) findViewById(R.id.simpanData);

    btnTakeImg.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        takeImage();
      }
    });
    simpanData.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        inputBarang(bitmap);
      }
    });
  }

  public void takeImage(){
    addPermission();
    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (cameraIntent.resolveActivity(InputDataBarang.this.getPackageManager()) != null) {
      // Create the File where the photo should go
      File photoFile = null;
      try {
        photoFile = createImageFile();
      } catch (IOException ex) {
        // Error occurred while creating the File
        Log.i("Tags", "IOException");
      }
      // Continue only if the File was successfully created
      if (photoFile != null) {
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(cameraIntent, CameraR_PP);
      }
    }
  }


  private File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = Environment.getExternalStoragePublicDirectory(
      Environment.DIRECTORY_PICTURES);
    File image = File.createTempFile(
      imageFileName,  // prefix
      ".jpg",         // suffix
      storageDir      // directory
    );

    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
    return image;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == EditBarangDanHapusActivity.RESULT_CANCELED) {
      return;
    }
    if (requestCode == CameraR_PP) {
      try {

        bitmap = MediaStore.Images.Media.getBitmap(InputDataBarang.this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
        imgGambarBuku.setImageBitmap(bitmap);
//                uploadPotoProfile(bitmap);
        if (imgGambarBuku.getDrawable() != null) {
          int newHeight = 300; // New height in pixels
          int newWidth = 300;
          imgGambarBuku.requestLayout();
          imgGambarBuku.getLayoutParams().height = newHeight;
          // Apply the new width for ImageView programmatically
          imgGambarBuku.getLayoutParams().width = newWidth;
          // Set the scale type for ImageView image scaling
          imgGambarBuku.setScaleType(ImageView.ScaleType.FIT_XY);
          ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) imgGambarBuku.getLayoutParams();
          marginParams.setMargins(0, 10, 0, 0);
        }
      } catch (IOException e) {
        e.printStackTrace();
        Toast.makeText(InputDataBarang.this, "Failed!", Toast.LENGTH_SHORT).show();
      }
    }
  }

  public void addPermission() {
    Dexter.withActivity(InputDataBarang.this)
      .withPermissions(

        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE)
      .withListener(new MultiplePermissionsListener() {
        @Override
        public void onPermissionsChecked(MultiplePermissionsReport report) {
          // check if all permissions are granted
          if (report.areAllPermissionsGranted()) {
            //Toast.makeText(getActivity(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
          }

          // check for permanent denial of any permission
          if (report.isAnyPermissionPermanentlyDenied()) {
            // show alert dialog navigating to Settings

          }
        }

        @Override
        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
          token.continuePermissionRequest();
        }
      }).
      withErrorListener(new PermissionRequestErrorListener() {
        @Override
        public void onError(DexterError error) {
          Toast.makeText(InputDataBarang.this, "Some Error! ", Toast.LENGTH_SHORT).show();
        }
      })
      .onSameThread()
      .check();
  }

  public byte[] getFileDataFromDrawable(Bitmap bitmap) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    if (bitmap != null){
      bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
    }
    return byteArrayOutputStream.toByteArray();
  }

  private void inputBarang(final Bitmap bitmap) {
    pDialog.setMessage("Mohon Tunggu .........");
    showDialog();
    VolleyMultipart volleyMultipartRequest = new VolleyMultipart(Request.Method.POST, BaseURL.inputBarang,
      new Response.Listener<NetworkResponse>() {
        @Override
        public void onResponse(NetworkResponse response) {
          mRequestQueue.getCache().clear();
          hideDialog();
          try {
            JSONObject jsonObject = new JSONObject(new String(response.data));
            System.out.println("ress = " + jsonObject.toString());
            String strMsg = jsonObject.getString("msg");
            boolean status= jsonObject.getBoolean("error");
            if(status == false){
              Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
              Intent i = new Intent(InputDataBarang.this, ActivityDataBarang.class);
              startActivity(i);
              finish();
            }else {
              Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      },
      new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          hideDialog();
          Toast.makeText(InputDataBarang.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
      }) {

      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("kodeBarang", edtkodeBarang.getText().toString());
        params.put("jenisBarang", edtjenisBarang.getText().toString());
        params.put("namaBarang", edtnamaBarang.getText().toString());
        params.put("stockBarang", edtstockBarang.getText().toString());
        params.put("hargaBarang", edthargaBarang.getText().toString());
        return params;
      }
      @Override
      protected Map<String, DataPart> getByteData() {
        Map<String, DataPart> params = new HashMap<>();
        long imagename = System.currentTimeMillis();
        params.put("gambar", new VolleyMultipart.DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
        return params;
      }
    };

    volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
      5000,
      DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
      DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    mRequestQueue = Volley.newRequestQueue(InputDataBarang.this);
    mRequestQueue.add(volleyMultipartRequest);
  }

  private void showDialog(){
    if(!pDialog.isShowing()){
      pDialog.show();
    }
  }

  private void hideDialog(){
    if(pDialog.isShowing()){
      pDialog.dismiss();
    }
  }

  @Override
  public void onBackPressed() {
    Intent i = new Intent(InputDataBarang.this, HomeAdminActivity.class);
    startActivity(i);
    finish();
  }
  }
