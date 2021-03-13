package com.coderdemm.society.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.coderdemm.society.Fragment.KesfetFragment;
import com.coderdemm.society.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class KesfetActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    // com.coderdemm.idea.Fragment selectedFragment = null;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kesfet);

        /*Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });*/


        KesfetFragment kesfetFragment=new KesfetFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();

        FragmentTransaction transaction=fragmentManager.beginTransaction();

        transaction.add(R.id.fragment_container_kesfet,kesfetFragment);
        transaction.commit();

    }
}

