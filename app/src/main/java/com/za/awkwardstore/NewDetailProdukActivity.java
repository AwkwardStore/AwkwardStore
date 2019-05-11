package com.za.awkwardstore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class NewDetailProdukActivity extends AppCompatActivity {

    private EditText namaProduk_edtTxt;
    private EditText stokProduk_edtTxt;

    private Button deleteBtn;
    private Button updateBtn;

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
        namaProduk_edtTxt = (EditText)findViewById(R.id.nama_produk_txt);
        namaProduk_edtTxt.setText(name);
        stokProduk_edtTxt = (EditText)findViewById(R.id.stok_produk_txt);
        stokProduk_edtTxt.setText(stok);

        deleteBtn = (Button)findViewById(R.id.delete_btn);
        updateBtn = (Button)findViewById(R.id.update_btn);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Produk produk = new Produk();
                produk.setName(namaProduk_edtTxt.getText().toString());
                produk.setStok(stokProduk_edtTxt.getText().toString());
                new FirebaseDatabaseHelper().updateProduk(key, produk, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Produk> produks, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {
                        Toast.makeText(NewDetailProdukActivity.this,"Produk kamu telah diupdate",Toast.LENGTH_SHORT).show();
                        finish();return;
                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseDatabaseHelper().deleteProduk(key, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Produk> produks, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(NewDetailProdukActivity.this,"Data Kamu telah dihapus",Toast.LENGTH_SHORT).show();
                        finish();return;
                    }
                });
            }
        });
    }
}
