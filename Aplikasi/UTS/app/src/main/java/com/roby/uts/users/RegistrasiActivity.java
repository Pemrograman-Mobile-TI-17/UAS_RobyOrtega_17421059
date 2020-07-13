package com.roby.uts.users;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ornach.nobobutton.NoboButton;
import com.roby.uts.R;
import com.roby.uts.server.BaseURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class RegistrasiActivity extends AppCompatActivity {

Button btnlogin;
NoboButton btnregis;
EditText edtuserName, edtnamalengkap, edtemaill, edtnomortelp, edtpassword;
ProgressDialog pDialog;

  private RequestQueue mRequestQueue;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registrasi);

    getSupportActionBar().hide();

    mRequestQueue = Volley.newRequestQueue(this);

    edtuserName = (EditText) findViewById(R.id.edtuserName);
    edtnamalengkap = (EditText) findViewById(R.id.edtnamalengkap);
    edtemaill  = (EditText) findViewById(R.id.edtemail);
    edtnomortelp = (EditText) findViewById(R.id.edtnomortelp);
    edtpassword = (EditText) findViewById(R.id.edtpassword);

    btnlogin = (Button) findViewById(R.id.btnlogin);
    btnregis = (NoboButton) findViewById(R.id.btnregis);

    pDialog = new ProgressDialog(this);
    pDialog.setCancelable(false);

    btnlogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(RegistrasiActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
      }
    });

    btnregis.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String strUsername = edtuserName.getText().toString();
        String strNamaLengkap = edtnamalengkap.getText().toString();
        String strEmail = edtemaill.getText().toString();
        String strNoTelp = edtnomortelp.getText().toString();
        String strPassword = edtpassword.getText().toString();


        if(strUsername.isEmpty()){
          Toast.makeText(getApplicationContext(), "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }else if(strNamaLengkap.isEmpty()){
          Toast.makeText(getApplicationContext(), "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }else if(strEmail.isEmpty()){
          Toast.makeText(getApplicationContext(), "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }else if(strNoTelp.isEmpty()){
          Toast.makeText(getApplicationContext(), "Nomor Telephone tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }else if(strPassword.isEmpty()){
          Toast.makeText(getApplicationContext(), "Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        }else{
          registrasi(strUsername, strNamaLengkap,strEmail,strNoTelp,strPassword);
        }

      }
    });
  }

  public void registrasi(String userName, String namaLengkap, String email, String noTelp, String password){

// Post params to be sent to the server
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("userName", userName);
    params.put("namaLengkap", namaLengkap);
    params.put("email", email);
    params.put("noTelp", noTelp);
    params.put("role", "2");
    params.put("password", password);

    pDialog.setMessage("Mohon Tunggu...");
    showDialog();


    JsonObjectRequest req = new JsonObjectRequest(BaseURL.register, new JSONObject(params),
      new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
          hideDialog();
          try {
           String strMsg = response.getString("msg");
           boolean status = response.getBoolean("error");
           if (status == false){
             Toast.makeText(getApplicationContext(),strMsg, Toast.LENGTH_SHORT).show();
             Intent i = new Intent(RegistrasiActivity.this, LoginActivity.class);
             startActivity(i);
             finish();
           }else{
             Toast.makeText(getApplicationContext(),strMsg, Toast.LENGTH_SHORT).show();
           }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        VolleyLog.e("Error: ", error.getMessage());
        hideDialog();
      }
    });
    mRequestQueue.add(req);
  }


  @Override
  public void onBackPressed() {
    Intent i = new Intent(RegistrasiActivity.this, LoginActivity.class);
    startActivity(i);
    finish();
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
}
