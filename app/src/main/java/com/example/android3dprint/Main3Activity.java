package com.example.android3dprint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.android3dprint.databinding.ActivityMain3Binding;

public class Main3Activity extends AppCompatActivity {
    private static final String TAG = "Main3Activity";
    User user=new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main3);


        ActivityMain3Binding binding = DataBindingUtil.setContentView(this, R.layout.activity_main3);
         binding.setUser(user);
    }


    public void Logit(View view) {
        // Do something in response to button
        Log.d(TAG,"Hello Michael");
//        user.setLastName("cong");
        Log.d(TAG,user.getLastName().toString());
    }
}
