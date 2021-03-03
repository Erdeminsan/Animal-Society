package com.coderdemm.idea.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.coderdemm.idea.Fragment.HomeFragment;
import com.coderdemm.idea.Fragment.NotificationFragment;
import com.coderdemm.idea.Fragment.ProfileFragment;
import com.coderdemm.idea.Fragment.SearchFragment;
import com.coderdemm.idea.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
   // com.coderdemm.idea.Fragment selectedFragment = null;
    Fragment selectedFragment=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView=findViewById(R.id.bottom_navigation);

       bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedLintener);


       Bundle intent=getIntent().getExtras();
       if(intent!=null){
           String publisher=intent.getString("publisherid");

           SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
           editor.putString("profileid",publisher);
           editor.apply();
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
       }else{
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
       }


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedLintener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
          //  com.coderdemm.idea.Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.nav_home:
                   selectedFragment= new HomeFragment();

                    break;
                case R.id.nav_search:
                    selectedFragment=new SearchFragment();
                    break;
                case R.id.nav_add:
                    selectedFragment=null;
                    startActivity(new Intent(MainActivity.this, PostActicity.class));
                    break;
                case R.id.nav_heart:
                    selectedFragment=new NotificationFragment();
                    break;
                case R.id.nav_profile:
                    SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                    editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    editor.apply();
                    selectedFragment=new ProfileFragment();
                    break;
            }
            if(selectedFragment!=null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            }


            return true;
        }
    };
}