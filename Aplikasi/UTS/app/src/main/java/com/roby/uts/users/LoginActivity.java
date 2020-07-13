package com.roby.uts.users;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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
import com.roby.uts.admin.HomeAdminActivity;
import com.roby.uts.pembeli.HomePembeli;
import com.roby.uts.server.BaseURL;
import com.roby.uts.session.PrefSetting;
import com.roby.uts.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

  Button btnregis;
  NoboButton btnlogin;
  EditText edtuserName, edtPassword;
  ProgressDialog pDialog;
  SessionManager session;
  SharedPreferences prefs;
  PrefSetting prefSetting;

  private RequestQueue mRequestQueue;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    getSupportActionBar().hide();

    mRequestQueue = Volley.newRequestQueue(this);

    pDialog = new ProgressDialog(this);
    pDialog.setCancelable(false);

 btnregis = (Button)findViewById(R.id.btnregis);
btnlogin  = (NoboButton) findViewById(R.id.btnlogin);

edtuserName = (EditText) findViewById(R.id.edtuserName);
edtPassword = (EditText) findViewById(R.id.edtPassword);

prefSetting = new PrefSetting(this);
prefs = prefSetting.getSharePreferences();

session = new SessionManager(this);

prefSetting.checkLogin(session,prefs);

btnregis.setOnClickListener(new View.OnClickListener() {
  @Override
  public void onClick(View v) {
    Intent i = new Intent(LoginActivity.this, RegistrasiActivity.class);
    startActivity(i);
    finish();
  }
});

btnlogin.setOnClickListener(new View.OnClickListener() {
  @Override
  public void onClick(View v) {
    String strUsername = edtuserName.getText().toString();
    String strPassword = edtPassword.getText().toString();


    if(strUsername.isEmpty()){
      Toast.makeText(getApplicationContext(), "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
    }else if(strPassword.isEmpty()){
      Toast.makeText(getApplicationContext(), "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
    }else{
      login(strUsername, strPassword);
    }
  }
});
  }


  public void login(String userName, String password){

// Post params to be sent to the server
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("userName", userName);
    params.put("password", password);

    pDialog.setMessage("Mohon Tunggu...");
    showDialog();


    JsonObjectRequest req = new JsonObjectRequest(BaseURL.login, new JSONObject(params),
      new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
          hideDialog();
          try {
            String strMsg = response.getString("msg");
            boolean status = response.getBoolean("error");
            if (status == false){
              Toast.makeText(getApplicationContext(),strMsg, Toast.LENGTH_SHORT).show();
              String data = response.getString("data");
              JSONObject jsonObject = new JSONObject(data);
              String role = jsonObject.getString("role");
              String _id =  jsonObject.getString("_id");
              String userName = jsonObject.getString("userName");
              String namaLengkap = jsonObject.getString("namaLengkap");
              String email = jsonObject.getString("email");
              String noTelp = jsonObject.getString("noTelp");

              session.setLogin(true);
            prefSetting.storeRegIdSharedPrefences(LoginActivity.this, _id, userName,namaLengkap, email, noTelp, role,prefs);

              if(role.equals("1")){
                Intent i = new Intent(LoginActivity.this , HomeAdminActivity.class);
                startActivity(i);
                finish();
              }else{
                Intent i = new Intent(LoginActivity.this, HomePembeli.class);
                startActivity(i);
                finish();
              }
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
