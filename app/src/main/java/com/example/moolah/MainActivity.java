package com.example.moolah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.moolah.model.SharedPrefManager;
import com.example.moolah.model.User;

public class MainActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        //get reference to the textview
        TextView txtHello = findViewById(R.id.txtHello);

        //get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        //set the textview to display username
        txtHello.setText("Hello "+user.getUsername()+"!");

    }
}