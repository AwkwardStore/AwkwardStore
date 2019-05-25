package com.za.awkwardstore;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewUpdateProdukActivity extends AppCompatActivity
{
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button batalBtn ;
    private Button updateBtn ;
    private ImageView imageView3;

    private EditText namaProduk_edtTxt;
    private EditText stokProduk_edtTxt;

    private String key;
    private String name;
    private String stok ;
    private String images;
    private Uri imagesUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_update_produk);
        batalBtn = (Button)findViewById(R.id.btnBatal);
        updateBtn = (Button)findViewById(R.id.update_btn2);
        imageView3 = findViewById(R.id.imageView3);
        key = getIntent().getStringExtra("key");
        name = getIntent().getStringExtra("name");
        stok = getIntent().getStringExtra("stok");
        images = getIntent().getStringExtra("image");
        namaProduk_edtTxt = (EditText) findViewById(R.id.namaProduk_edtTxt);
        namaProduk_edtTxt.setText(name);
        stokProduk_edtTxt = (EditText) findViewById(R.id.stokProduk_edtTxt);
        stokProduk_edtTxt.setText(stok);
        Picasso.with(NewUpdateProdukActivity.this).load(images).resize(100, 100).centerCrop().into(imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFileChooser();
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Produk produk = new Produk();
                produk.setName(namaProduk_edtTxt.getText().toString());
                produk.setStok(stokProduk_edtTxt.getText().toString());
                produk.setmImageurl(images);
                new FirebaseDatabaseHelper().updateProduk(key, produk, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Produk> produks, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {
                        Toast.makeText(NewUpdateProdukActivity.this,"Produk kamu telah diupdate",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewUpdateProdukActivity.this,MainActivity.class);
                        startActivity(intent);                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                },getFileExtension(imagesUri),imagesUri,images);
            }
        });
        batalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();return;
            }
        });
    }
    private void OpenFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            imagesUri = data.getData();

            Picasso.with(this).load(imagesUri).into(imageView3);
        }
    }

    private String getFileExtension(Uri uri){
        if (uri != null) {
            ContentResolver cR = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cR.getType(uri));
        }else{
            return null;
        }
    }

}
