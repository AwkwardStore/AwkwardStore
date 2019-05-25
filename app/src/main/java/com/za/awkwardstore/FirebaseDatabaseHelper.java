package com.za.awkwardstore;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

public class FirebaseDatabaseHelper
{
    private FirebaseDatabase mDatabase;
    private StorageReference mReferenceStorage;
    private StorageReference ref2;
    private DatabaseReference mReference;
    private SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmssms");

    private List<Produk> produks = new ArrayList<>();

    public interface DataStatus
    {
        void DataIsLoaded (List<Produk> produks, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper()
    {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("produks");
        mReferenceStorage = FirebaseStorage.getInstance().getReference("produks");
    }

    public void readProduks (final  DataStatus dataStatus)
    {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                produks.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren())
                {
                    keys.add(keyNode.getKey());
                    Produk produk = keyNode.getValue(Produk.class);
                    produks.add(produk);
                }
                dataStatus.DataIsLoaded(produks, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addProduks(final Produk produk, final DataStatus dataStatus, final String mime, Uri imageUri, final ProgressBar progressBar)
    {
        final Date date = new Date();
        StorageReference fileReference = mReferenceStorage.child(produk.getName().replaceAll("\\s","")+"-"+sdf.format(date)+"."+mime);
        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                android.os.Handler handler = new android.os.Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        progressBar.setProgress(0);
                    }
                }, 500);

                String key = mReference.push().getKey();
                produk.setmImageurl("https://firebasestorage.googleapis.com/v0/b/" + mReferenceStorage.getBucket() + "/o/produks%2F" + produk.getName().replaceAll("\\s","")+"-"+sdf.format(date)+"."+mime+"?alt=media" );
                mReference.child(key).setValue(produk).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);
            }
        });
    }
    public void updateProduk(final String key, final Produk produk, final DataStatus dataStatus, final String mime, Uri imageUri,String image)
    {
        if(mime!=null){
            final Date date = new Date();
            FirebaseStorage.getInstance().getReferenceFromUrl(image).delete();
            StorageReference fileReference = mReferenceStorage.child(produk.getName().replaceAll("\\s","")+"-"+sdf.format(date)+"."+mime);
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    produk.setmImageurl("https://firebasestorage.googleapis.com/v0/b/" + mReferenceStorage.getBucket() + "/o/produks%2F" + produk.getName().replaceAll("\\s","")+"-"+sdf.format(date)+"."+mime+"?alt=media" );
                    mReference.child(key).setValue(produk).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dataStatus.DataIsUpdated();
                        }
                    });
                }
            });
        }
        else{
            mReference.child(key).setValue(produk).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    dataStatus.DataIsUpdated();
                }
            });
        }

    }
    public void deleteProduk(String key, final DataStatus dataStatus,String image)
    {
        FirebaseStorage.getInstance().getReferenceFromUrl(image).delete();
        mReference.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();

            }
        });
    }
}
