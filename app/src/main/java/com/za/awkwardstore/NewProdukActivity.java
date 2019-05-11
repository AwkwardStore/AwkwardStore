package com.za.awkwardstore;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewProdukActivity extends AppCompatActivity
{
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText namaProduk_edtTxt;
    private EditText stokProduk_edtTxt;

    private Button addBtn;

    private ImageView mImageView;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_produk);

        namaProduk_edtTxt = (EditText)findViewById(R.id.nama_produk_txt);
        stokProduk_edtTxt = (EditText)findViewById(R.id.stok_produk_txt);

        addBtn = (Button)findViewById(R.id.Add_btn);

        mImageView = findViewById(R.id.image_view);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                OpenFileChooser();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Produk produk = new Produk();
                produk.setName(namaProduk_edtTxt.getText().toString());
                produk.setStok(stokProduk_edtTxt.getText().toString());
                new FirebaseDatabaseHelper().addProduks(produk, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Produk> produks, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(NewProdukActivity.this, "Produk Kamu Telah Ditambahkan", Toast.LENGTH_LONG).show();
                        finish();return;
                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                },getFileExtension(mImageUri),mImageUri);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
