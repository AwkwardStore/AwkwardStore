package com.za.awkwardstore;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Recyclerview_config
{
    private Context mContext;
    private ProduksAdapter mProdukAdapter;
    public List<Produk> produk;
    private OnItemClickListener mListener;

    public void setConfig(RecyclerView recyclerView, Context context, List<Produk> produks, List<String> keys, OnItemClickListener listener)
    {
        mContext = context;
        mProdukAdapter = new ProduksAdapter(produks, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mProdukAdapter);
        mListener = listener;
        this.produk = produks;
    }

    class ProdukItemView extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener
    {
        private TextView nameText;
        private TextView stokText;
        private ImageView imageView;

        private String key,imageUrl;


        public ProdukItemView(ViewGroup parent)
        {
            super(LayoutInflater.from(mContext).inflate(R.layout.produk_list_item, parent, false));

            nameText = (TextView)itemView.findViewById(R.id.txt_name);
            stokText = (TextView)itemView.findViewById(R.id.txt_stok);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null)
            {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    Intent i = new Intent(mContext,NewDetailProdukActivity.class);
                    i.putExtra("key",key);
                    i.putExtra("name",nameText.getText());
                    i.putExtra("stok",stokText.getText());
                    i.putExtra("image",imageUrl);
                    mContext.startActivity(i);
                    mListener.onItemClick(position);
                }
            }
        }

        public void onUpdateClick(){
            if (mListener != null)
            {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    Intent i = new Intent(mContext,NewUpdateProdukActivity.class);
                    i.putExtra("key",key);
                    i.putExtra("name",nameText.getText());
                    i.putExtra("stok",stokText.getText());
                    i.putExtra("image",imageUrl);
                    mContext.startActivity(i);
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doUpdate = menu.add(menu.NONE, 1, 1, "Do Update");
            MenuItem delete = menu.add(menu.NONE, 2, 2, "Delete");

            doUpdate.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item)
        {
            if (mListener != null)
            {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    switch (item.getItemId())
                    {
                        case 1:
//                            mListener.onUpdateClick(position);
                            onUpdateClick();
                            return true;

                        case 2:
                            mListener.onDeleteClick(position,key,imageUrl);
                            return true;
                    }
                }
            }
            return false;
        }

        public void Bind (Produk produk, String key)
        {
            nameText.setText(produk.getName());
            stokText.setText(produk.getStok());
            Picasso.with(mContext).load(produk.getmImageurl()).resize(100, 100).centerCrop().into(imageView);
            this.key = key;
            this.imageUrl = produk.getmImageurl();
        }
    }
    class ProduksAdapter extends RecyclerView.Adapter<ProdukItemView>
    {
        private List<Produk> mProdukList;
        private List<String> mKeys;

        public ProduksAdapter(List<Produk> mProdukList, List<String> mKeys) {
            this.mProdukList = mProdukList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public ProdukItemView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ProdukItemView(viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull ProdukItemView produkItemView, int i) {
            produkItemView.Bind(mProdukList.get(i), mKeys.get(i));
        }

        @Override
        public int getItemCount() {
            return mProdukList.size();
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick (int position);

        void onUpdateClick (int positiom);

        void onDeleteClick (int position,String key,String image);
    }
}
