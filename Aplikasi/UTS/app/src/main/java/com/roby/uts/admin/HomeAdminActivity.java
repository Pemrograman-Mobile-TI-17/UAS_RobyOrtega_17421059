package com.roby.uts.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import com.roby.uts.R;
import com.roby.uts.session.PrefSetting;
import com.roby.uts.session.SessionManager;
import com.roby.uts.users.LoginActivity;

import java.util.Calendar;

public class HomeAdminActivity extends AppCompatActivity {


SessionManager session;
SharedPreferences prefs;
CardView cardExit, carddatabuku, cardInputDataBarang, cardprofile;
PrefSetting prefSetting;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout .activity_home_admin);


    prefSetting  = new PrefSetting(this);

    prefs = prefSetting.getSharePreferences();

    session = new SessionManager(HomeAdminActivity.this);

      prefSetting.islogin(session, prefs);

      cardExit = (CardView) findViewById(R.id.cardExit);
    carddatabuku = (CardView) findViewById(R.id.carddatabuku);
    cardInputDataBarang = (CardView) findViewById(R.id.cardInputDataBarang);
    cardprofile = (CardView)  findViewById(R.id.cardprofile);

      cardExit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          session.setLogin(false);
          session.setSessid(0);
          Intent i = new Intent(HomeAdminActivity.this, LoginActivity.class);
          startActivity(i);
          finish();
        }
      });

    carddatabuku.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(HomeAdminActivity.this, ActivityDataBarang.class);
        startActivity(i);
        finish();
      }
    });

    cardInputDataBarang.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(HomeAdminActivity.this, InputDataBarang.class);
        startActivity(i);
        finish();
      }
    });

    cardprofile.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(HomeAdminActivity.this, Profile.class);
        startActivity(i);
        finish();
      }
    });

  }
}
