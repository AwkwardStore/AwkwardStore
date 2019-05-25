package com.za.awkwardstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.za.awkwardstore.Fragments.HomeFragment;

import java.util.List;

public class NewDetailProdukActivity extends AppCompatActivity {

    private TextView namaProduk_Txt;
    private TextView stokProduk_Txt;
    private ImageView imageView2;
    private Button update_btn;
    private Button delete_btn;

    private String key;
    private String name;
    private String stok ;
    private String images ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail_produk);
        update_btn = findViewById(R.id.update_btn);
        delete_btn = findViewById(R.id.delete_btn);
        imageView2 = findViewById(R.id.imageView2);
        key = getIntent().getStringExtra("key");
        name = getIntent().getStringExtra("name");
        stok = getIntent().getStringExtra("stok");
        images = getIntent().getStringExtra("image");
        namaProduk_Txt = (TextView) findViewById(R.id.nama_produk_txt);
        namaProduk_Txt.setText(name);
        stokProduk_Txt = (TextView) findViewById(R.id.stok_produk_txt);
        stokProduk_Txt.setText(stok);
        Picasso.with(NewDetailProdukActivity.this).load(images).resize(100, 100).centerCrop().into(imageView2);

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewDetailProdukActivity.this,NewUpdateProdukActivity.class);
                intent.putExtra("key",key);
                intent.putExtra("name",name);
                intent.putExtra("stok",stok);
                intent.putExtra("image",images);
                startActivity(intent);
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
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
                },images);
            }
        });
    }
}
