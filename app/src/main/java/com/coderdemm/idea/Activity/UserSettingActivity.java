package com.coderdemm.idea.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coderdemm.idea.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserSettingActivity extends AppCompatActivity {

    TextView delete,repassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        delete=findViewById(R.id.delete_account);
        repassword=findViewById(R.id.repassword);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccountFB();
            }
        });
        repassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePasswords();
            }
        });
    }
    private void updatePasswords() {
        AlertDialog.Builder updateAlert=new AlertDialog.Builder(this);
        updateAlert.setCancelable(false);
        updateAlert.setTitle("Password Update");
        LinearLayout linearLayout=new LinearLayout(this);
        final EditText pasword=new EditText(this);
        pasword.setHint("New Password");
        pasword.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        pasword.setMinEms(16);
        linearLayout.addView(pasword);
        linearLayout.setPadding(10,10,10,10);
        updateAlert.setView(linearLayout);
        updateAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                firebaseAuth=FirebaseAuth.getInstance();
                String pass=pasword.getText().toString().trim();
                beginUpdate(pass);

            }
        });
        updateAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(UserSettingActivity.this, "Update failed", Toast.LENGTH_SHORT).show();

            }
        });
        updateAlert.create().show();
    }
    private void beginUpdate(String pass) {
        firebaseUser.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UserSettingActivity.this, "Password update", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UserSettingActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserSettingActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteAccountFB() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Are you sure delete account?");
        // builder.setIcon(R.drawable.info); ikon eklemek istersek
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                firebaseAuth=FirebaseAuth.getInstance();
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(UserSettingActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(UserSettingActivity.this,StartActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(UserSettingActivity.this, "Not delete", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserSettingActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Toast.makeText(UserSettingActivity.this, "Not delete", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}