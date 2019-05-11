package com.za.awkwardstore;

public class Produk
{
    private String name;
    private String stok;

    private String mImageurl;

    public Produk() {

    }

    public Produk(String name, String stok, String mImageurl) {
        this.name = name;
        this.stok = stok;
        this.mImageurl = mImageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getmImageurl() { return mImageurl; }

    public void setmImageurl(String mImageurl) { this.mImageurl = mImageurl; }
}


