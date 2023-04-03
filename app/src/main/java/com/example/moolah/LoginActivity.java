package com.example.moolah;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moolah.model.ErrorResponse;
import com.example.moolah.model.SharedPrefManager;
import com.example.moolah.model.User;
import com.example.moolah.remote.ApiUtils;
import com.example.moolah.remote.UserService;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText loginEmail;
    EditText loginPassword;
    Button loginButton;
    Button signupButton;

    UserService userService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //if the user is already logged in we will start directly

        //the main activity
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,HomepageActivity.class));
            return;
        }

        //get references to form elements
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);

        //get UserService instance
        userService = ApiUtils.getUserService();

        // set onClick action to loginButton
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get email and password entered by user
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                //validate form, make sure it is not empty
                if(validateLogin(email,password)){
                    //do login
                    doLogin(email,password);
                }
            }
        });
    }
    /**
     * Validate value of email and password entered. Client side validation.
     * @param email
     * @param password
     * @return
     */
    private boolean validateLogin(String email, String password)
    {
        if(email==null || email.trim().length()==0)
        {
            displayToast("Username is required");
            return false;
        }
        if(password == null || password.trim().length()==0)
        {
            displayToast("Password is required");
            return false;
        }
        return true;
    }

    /**
     * Call REST API to login
     * @param email
     * @param password
     */
    private void doLogin(String email, String password) {
        Call call = userService.login(email, password);

        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {

                // received reply from REST API
                if (response.isSuccessful()) {
                    // parse response to POJO
                    User user = (User) response.body();
                    if (user.getToken() != null) {
                        // successful login. server replies a token value
                        displayToast("Login successful");
                        displayToast("Token: " + user.getToken());

                        // store value in Shared Preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //forward user to MainActivity
                        finish();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                    }
                }
                else if (response.errorBody() != null){
                    // parse response to POJO
                    String errorResp = null;
                    try {
                        errorResp = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ErrorResponse e = new Gson().fromJson( errorResp, ErrorResponse.class);
                    displayToast(e.getError().getMessage());
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                displayToast("Error connecting to server.");
                displayToast(t.getMessage());
            }
        });
    }

    /**
     * Display a Toast message
     * @param message
     */
    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}