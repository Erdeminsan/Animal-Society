package com.coderdemm.idea;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.coderdemm.idea.Activity.LoginActivity;
import com.coderdemm.idea.Activity.MainActivity;
import com.coderdemm.idea.Activity.StartActivity;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, StartActivity.class));
        finish();
    }
}
