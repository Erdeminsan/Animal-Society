package com.coderdemm.society.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coderdemm.society.Activity.CommentActivity;
import com.coderdemm.society.Activity.FollowersActivity;
import com.coderdemm.society.Fragment.ProfileFragment;
import com.coderdemm.society.Model.Kesfet;
import com.coderdemm.society.Model.Post;
import com.coderdemm.society.Model.User;
import com.coderdemm.society.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class KesfetAdapter extends RecyclerView.Adapter<KesfetAdapter.ViewHolder> {

    public Context mContext;
    public List<Kesfet> mkesfet;

    private FirebaseUser firebaseUser;

    public KesfetAdapter(Context mContext, List<Kesfet> mkesfet) {
        this.mContext = mContext;
        this.mkesfet = mkesfet;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.kesfet_item,parent,false);
        return new KesfetAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        Kesfet kesfet=mkesfet.get(position);

        Glide.with(mContext).load(kesfet.getPostimage()).into(holder.post_image);



        if(kesfet.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        }else{
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(kesfet.getDescription());
        }




        publisherInfo(holder.image_profile,holder.username,holder.publisher,kesfet.getPublisher());
        isLiked(kesfet.getPostid(),holder.like);
        nrLikes(holder.likes,kesfet.getPostid());
        getComment(kesfet.getPostid(),holder.comments);
        isSaved(kesfet.getPostid(),holder.save);

        holder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor=mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",kesfet.getPublisher());
                editor.apply();
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_kesfet,new ProfileFragment()).commit();


            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor=mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",kesfet.getPublisher());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_kesfet,new ProfileFragment()).commit();
            }
        });
        holder.publisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor=mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",kesfet.getPublisher());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_kesfet,new ProfileFragment()).commit();

            }
        });
       /* holder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor=mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("postid",kesfet.getPostid());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_kesfet,new PostDetailKesfetFragment()).commit();
            }
        });*/

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.save.getTag().equals("save")){
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid()).child(kesfet.getPostid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid()).child(kesfet.getPostid()).removeValue();
                }
            }
        });


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(kesfet.getPostid()).child(firebaseUser.getUid()).setValue(true);
                    addNotifications(kesfet.getPublisher(),kesfet.getPostid());
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(kesfet.getPostid()).child(firebaseUser.getUid()).removeValue();
                }

            }
        });


        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, CommentActivity.class);
                intent.putExtra("postid",kesfet.getPostid());
                intent.putExtra("publisherid",kesfet.getPublisher());
                mContext.startActivity(intent);
            }
        });
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, CommentActivity.class);
                intent.putExtra("postid",kesfet.getPostid());
                intent.putExtra("publisherid",kesfet.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, FollowersActivity.class);
                intent.putExtra("id",kesfet.getPostid());
                intent.putExtra("title","likes");
                mContext.startActivity(intent);
            }
        });


/*holder.moree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(mContext,view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.edit:
                                editPost(kesfet.getPostid());
                                return true;
                            case R.id.delete:
                                FirebaseDatabase.getInstance().getReference("Posts").child(kesfet.getPostid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                return true;
                            case R.id.report:
                                Toast.makeText(mContext, "Report clicked", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.post_menu);
                if(!kesfet.getPublisher().equals(firebaseUser.getUid())){
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                }
                popupMenu.show();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mkesfet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile,post_image,like,comment,save,moree;
        public TextView username,likes,publisher,description,comments;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile=itemView.findViewById(com.coderdemm.society.R.id.image_profile);
            post_image=itemView.findViewById(com.coderdemm.society.R.id.post_image);
            like=itemView.findViewById(com.coderdemm.society.R.id.like);
            comment=itemView.findViewById(com.coderdemm.society.R.id.comment);
            save=itemView.findViewById(com.coderdemm.society.R.id.save);
            username=itemView.findViewById(com.coderdemm.society.R.id.username);
            likes=itemView.findViewById(com.coderdemm.society.R.id.likes);
            publisher=itemView.findViewById(com.coderdemm.society.R.id.publisher);
            description=itemView.findViewById(com.coderdemm.society.R.id.description);
            comments=itemView.findViewById(com.coderdemm.society.R.id.commnets);
            //moree=itemView.findViewById(R.id.moree);
        }
    }
    private  void getComment(String postid,TextView comments){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.setText("View All "+snapshot.getChildrenCount()+" comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isLiked(String postid,ImageView imageView){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(com.coderdemm.society.R.drawable.ic_liked);
                    imageView.setTag("liked");
                }else{
                    imageView.setImageResource(com.coderdemm.society.R.drawable.ic_like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addNotifications(String userid,String  postid){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        HashMap<String ,Object> hashMap=new HashMap<>();
        hashMap.put("userid",firebaseUser.getUid());
        hashMap.put("text","liked your post");
        hashMap.put("postid",postid);
        hashMap.put("isposts",true);

        reference.push().setValue(hashMap);

    }

    private void nrLikes(TextView likes,String postid){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount()+" likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void publisherInfo(ImageView image_profile,TextView username,TextView publisher,String userid){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageurl()).into(image_profile);
                username.setText(user.getUsername());
                publisher.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void isSaved(String postid,ImageView imageView){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postid).exists()){
                    imageView.setImageResource(com.coderdemm.society.R.drawable.ic_save_dark);
                    imageView.setTag("saved");
                }else{
                    imageView.setImageResource(com.coderdemm.society.R.drawable.ic_save_black);
                    imageView.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void editPost(String postid){
        AlertDialog.Builder alerdialog=new AlertDialog.Builder(mContext);
        alerdialog.setTitle("Edit Post");

        EditText editText=new EditText(mContext);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(layoutParams);
        alerdialog.setView(editText);

        getText(postid,editText);
        alerdialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HashMap<String ,Object>hashMap=new HashMap<>();
                hashMap.put("description",editText.getText().toString());

                FirebaseDatabase.getInstance().getReference("Posts").child(postid).updateChildren(hashMap);
            }
        });
        alerdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        alerdialog.show();
    }
    private void getText(String postid,final EditText editText){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Posts").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                editText.setText(snapshot.getValue(Post.class).getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

