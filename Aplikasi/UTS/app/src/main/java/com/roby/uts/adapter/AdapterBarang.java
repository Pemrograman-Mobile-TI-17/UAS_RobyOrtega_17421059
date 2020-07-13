package com.roby.uts.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roby.uts.R;
import com.roby.uts.model.ModelBarang;
import com.roby.uts.server.BaseURL;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterBarang extends BaseAdapter {

  private Activity activity;
  private LayoutInflater inflater;
  private List<ModelBarang  > item;

  public AdapterBarang(Activity activity, List<ModelBarang
    > item) {
    this.activity = activity;
    this.item = item;
  }

  @Override
  public int getCount() {
    return item.size();
  }

  @Override
  public Object getItem(int position) {
    return item.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (inflater == null)
      inflater = (LayoutInflater) activity
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    if (convertView == null)
      convertView = inflater.inflate(R.layout.content_barang, null);


    TextView namaBarang = (TextView) convertView.findViewById(R.id.txtnamaBarang);
    TextView jenisBarang          = (TextView) convertView.findViewById(R.id.txtjenisBarang);
    TextView harga         = (TextView) convertView.findViewById(R.id.txthargaBarang);
    TextView stockBarang         = (TextView) convertView.findViewById(R.id.txtstockBarang);
    ImageView gambarBarang         = (ImageView) convertView.findViewById(R.id.gambarBarang);


    namaBarang.setText(item.get(position).getNamaBarang());
    jenisBarang.setText(item.get(position).getJenisBarang());
    harga.setText("Rp." + item.get(position).getHargaBarang());
    stockBarang.setText(item.get(position).getStockBarang());
    Picasso.get().load(BaseURL.baseUrl + "gambar/" + item.get(position).getGambar())
      .resize(40, 40)
      .centerCrop()
      .into(gambarBarang);
    return convertView;



  }
  }
