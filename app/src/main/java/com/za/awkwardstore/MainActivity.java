package com.za.awkwardstore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.za.awkwardstore.Fragments.HomeFragment;
import com.za.awkwardstore.Fragments.TransaksiFragment;

public class MainActivity extends AppCompatActivity /*implements Recyclerview_config.OnItemClickListener */{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.navigation_bot);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        //new Recyclerview_config().setOnItemClickListener(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.keranjang, menu);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_transaksi:
                    selectedFragment = new TransaksiFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            return true;
        }
    };

//    @Override
//    public void onItemClick(int position) {
//        Toast.makeText(this, "Normal Click at Position:" + position, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onUpdateClick(int positiom) {
//        Toast.makeText(this, "Update Click at Position:" + positiom, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onDeleteClick(int position) {
//        Toast.makeText(this, "Delete Click at position:" + position, Toast.LENGTH_SHORT).show();
//    }
}
