package com.za.awkwardstore;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper
{
    private FirebaseDatabase mDatabase;
    private StorageReference mReferenceStorage;
    private DatabaseReference mReference;

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

    public void addProduks(final Produk produk, final DataStatus dataStatus, final String mime, Uri imageUri)
    {
        StorageReference fileReference = mReferenceStorage.child(produk.getName()+"."+mime);
        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String key = mReference.push().getKey();
                produk.setmImageurl("https://firebasestorage.googleapis.com/v0/b/" + mReferenceStorage.getBucket() + "/o/produks%2F" + produk.getName()+"."+mime+"?alt=media" );
                mReference.child(key).setValue(produk).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
            }
        });
    }
    public void updateProduk(String key, Produk produk, final DataStatus dataStatus)
    {
        mReference.child(key).setValue(produk).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }
    public void deleteProduk(String key, final DataStatus dataStatus)
    {
         mReference.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 dataStatus.DataIsDeleted();
             }
         });
    }
}
