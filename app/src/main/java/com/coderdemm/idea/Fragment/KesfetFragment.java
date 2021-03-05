package com.coderdemm.idea.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coderdemm.idea.Adapter.KesfetAdapter;
import com.coderdemm.idea.Model.Kesfet;
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

public class KesfetFragment extends Fragment {

    private RecyclerView recyclerView;
    private KesfetAdapter kesfetAdapter;
    private List<Kesfet> kesfetlist;
    private List<String> followingList;
    FirebaseUser firebaseUser;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_kesfet, container, false);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();




        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        kesfetlist=new ArrayList<>();
        kesfetAdapter=new KesfetAdapter(getContext(),kesfetlist);
        recyclerView.setAdapter(kesfetAdapter);

        checkFollowing();

        swipeRefreshLayout=view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                kesfetAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;


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