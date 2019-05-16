package com.za.awkwardstore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class NewDetailProdukActivity extends AppCompatActivity {

    private TextView namaProduk_Txt;
    private TextView stokProduk_Txt;

    private String key;
    private String name;
    private String stok ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail_produk);

        key = getIntent().getStringExtra("key");
        name = getIntent().getStringExtra("name");
        stok = getIntent().getStringExtra("stok");
        namaProduk_Txt = (TextView) findViewById(R.id.nama_produk_txt);
        namaProduk_Txt.setText(name);
        stokProduk_Txt = (TextView) findViewById(R.id.stok_produk_txt);
        stokProduk_Txt.setText(stok);
    }
}
