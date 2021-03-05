package com.coderdemm.idea.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.coderdemm.idea.Adapter.KesfetAdapter;
import com.coderdemm.idea.Adapter.NotificationAdapter;
import com.coderdemm.idea.Model.Kesfet;
import com.coderdemm.idea.Model.Notification;
import com.coderdemm.idea.Model.Post;
import com.coderdemm.idea.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KesfetActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private KesfetAdapter kesfetAdapter;
    private List<Kesfet> kesfetlist;
    private List<String> followingList;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kesfet);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        kesfetlist=new ArrayList<>();
        kesfetAdapter=new KesfetAdapter(this,kesfetlist);
        recyclerView.setAdapter(kesfetAdapter);

        checkFollowing();

    }



    private  void  checkFollowing(){
        followingList=new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    followingList.add(dataSnapshot.getKey());
                }
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private  void readPosts(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kesfetlist.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Kesfet kesfet=dataSnapshot.getValue(Kesfet.class);
                        kesfetlist.add(kesfet);
                }
                Collections.reverse(kesfetlist);
                kesfetAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    }

