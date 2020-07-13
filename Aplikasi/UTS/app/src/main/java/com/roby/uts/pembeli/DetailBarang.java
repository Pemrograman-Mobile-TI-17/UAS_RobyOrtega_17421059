package com.roby.uts.pembeli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.roby.uts.R;
import com.roby.uts.server.BaseURL;
import com.squareup.picasso.Picasso;

public class DetailBarang extends AppCompatActivity {

  EditText edtkodeBarang, edtjenisBarang, edtnamaBarang, edtstockBarang, edthargBarang;
  ImageView imgGambarBuku;
  String strkodeBarang, strjenisBarang, strnamaBaranag, strstockBarang, strhargaBarang, strGamabr, _id;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_barang);

    edtkodeBarang = (EditText) findViewById(R.id.edtkodeBarang);
    edtjenisBarang = (EditText) findViewById(R.id.edtjenisBarang);
    edtnamaBarang = (EditText) findViewById(R.id.edtnamaBarang);
    edtstockBarang = (EditText) findViewById(R.id.edtstockBarang);
    edthargBarang = (EditText) findViewById(R.id.edthargaBarang);

    imgGambarBuku = (ImageView) findViewById(R.id.gambar);

    Intent i = getIntent();
    strkodeBarang = i.getStringExtra("kodeBarang");
    strjenisBarang = i.getStringExtra("jenisBarang");
    strnamaBaranag = i.getStringExtra("namaBarang");
    strstockBarang = i.getStringExtra("stockBarang");
    strhargaBarang = i.getStringExtra("hargaBarang");
    strGamabr = i.getStringExtra("gambar");
    _id = i.getStringExtra("_id");

    edtkodeBarang.setText(strkodeBarang);
    edtjenisBarang.setText(strjenisBarang);
    edtnamaBarang.setText(strnamaBaranag);
    edtstockBarang.setText(strstockBarang);
    edthargBarang.setText(strhargaBarang);
    Picasso.get().load(BaseURL.baseUrl + "gambar/" + strGamabr)
      .into(imgGambarBuku);
  }
}
