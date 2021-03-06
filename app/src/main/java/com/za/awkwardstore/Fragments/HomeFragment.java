package com.za.awkwardstore.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.za.awkwardstore.FirebaseDatabaseHelper;
import com.za.awkwardstore.NewProdukActivity;
import com.za.awkwardstore.NewUpdateProdukActivity;
import com.za.awkwardstore.Produk;
import com.za.awkwardstore.R;
import com.za.awkwardstore.Recyclerview_config;

import java.util.List;

public class HomeFragment extends Fragment implements Recyclerview_config.OnItemClickListener
{
    View v;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_home, container, false);

        // Inflate the layout for this fragment
        FloatingActionButton floatingActionButton = v.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),NewProdukActivity.class));
            }
        });
        recyclerView = v.findViewById(R.id.recycler_view_id);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        new FirebaseDatabaseHelper().readProduks(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Produk> produks, List<String> keys)
            {
                new Recyclerview_config().setConfig(recyclerView, getContext(),
                        produks, keys, HomeFragment.this);

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
        return v;
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getActivity(), "Normal Click at Position:" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateClick(int positiom) {
//        Toast.makeText(getActivity(), "Update Click at Position:" + positiom, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position,String key,String image) {
//        Toast.makeText(getActivity(), "Delete Click at position:" + position, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(),"Data Kamu telah dihapus",Toast.LENGTH_SHORT).show();
                return;
            }
        },image);
    }
}
