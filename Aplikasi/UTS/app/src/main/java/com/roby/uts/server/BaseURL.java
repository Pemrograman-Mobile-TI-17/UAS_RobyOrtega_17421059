package com.roby.uts.server;

public class BaseURL {

  public  static String baseUrl = "http://192.168.43.146:5050/";

  public static String login = baseUrl+"user/login";

  public static  String register = baseUrl+"user/registrasi";

//  buku
  public static String dataBarang = baseUrl+"barang/databarang";

  //edit data barang
  public static String editdataBarang = baseUrl+"barang/ubah/";

  //hapus data
  public static String hapusData = baseUrl+"barang/hapus/";


  //inputBuku
  public static String inputBarang = baseUrl+"barang/input";

}
