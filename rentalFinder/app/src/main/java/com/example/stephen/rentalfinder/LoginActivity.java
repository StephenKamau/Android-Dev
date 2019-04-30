package com.example.stephen.rentalfinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Connection connection = new Connection();
    private String urlAddress = connection.getUrlLogin();
    private TextView signUp;
    EditText emailAddressTxt, passwordTxt;
    ProgressDialog progressDialog;
    String emailValue, passwordValue, user;
    Button login;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //prepare shared preferences
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {

            //if user was logged in open the default home screen
            Intent home = new Intent(LoginActivity.this, ViewApartmentsActivity.class);
            startActivity(home);
        }

        //list to new sign up request
        signUp = findViewById(R.id.txtSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                //Called when the user taps the sign-up button

                Intent signUp = new Intent(v.getContext(), SignUpActivity.class);
                startActivity(signUp);
            }
        });

        //get the values
        emailAddressTxt = findViewById(R.id.emailAddress);
        passwordTxt = findViewById(R.id.password);

        //create the progress dialog
        progressDialog = new ProgressDialog(LoginActivity.this);

        //onclick listener for login
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check for internet connection
                //check for internet connection before sending data
                if (isInternetAvailable()) {

                    //check if the fields are blank
                    if (!isBlank()) {

                        //assign the values
                        emailValue = emailAddressTxt.getText().toString().trim();
                        passwordValue = passwordTxt.getText().toString().trim();

                        // Showing progress dialog at user registration time.
                        progressDialog.setMessage("Logging in, Please wait...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);

                        // Creating string request with post method.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlAddress,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String ServerResponse) {

                                        // Hiding the progress dialog after all task complete.
                                        progressDialog.dismiss();

                                        //get the server response
                                        if (ServerResponse.toString().equals("fail")) {

                                            // Showing response message coming from server.
                                            Toast.makeText(LoginActivity.this, "Invalid user details", Toast.LENGTH_LONG).show();

                                        } else if (!(ServerResponse.toString().equals("fail"))) {
                                            user = ServerResponse;
                                            // Showing response message coming from server.
                                            Toast.makeText(LoginActivity.this, "Logged in as " + user, Toast.LENGTH_SHORT).show();

                                            //set the shared preferences
                                            sharedPreferences.edit().putString("user", user).apply();
                                            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply();

                                            //open the default home screen
                                            Intent home = new Intent(LoginActivity.this, ViewApartmentsActivity.class);
                                            startActivity(home);
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {

                                        // Hiding the progress dialog after all task complete.
                                        progressDialog.dismiss();

                                        // Showing error message if something goes wrong.
                                        Toast.makeText(LoginActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {

                                // Creating Map String Params.
                                Map<String, String> params = new HashMap<String, String>();

                                // Adding All values to Params.
                                params.put("email_address", emailValue);
                                params.put("password", passwordValue);

                                return params;
                            }

                        };

                        // Creating RequestQueue.
                        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

                        // Adding the StringRequest object into requestQueue.
                        requestQueue.add(stringRequest);

                        //returns the values to empty

                            /*firstNameTxt.setText("");
                            lastNameTxt.setText("");
                           */

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Fill in all the fields", Toast.LENGTH_LONG).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    //check for internet connection
    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            return isConnected;
        }
        return false;
    }

    //check for blank fields
    public boolean isBlank() {
        if (emailAddressTxt.getText().toString().equals("") ||
                passwordTxt.getText().toString().equals("")) {
            return true;

        } else
            return false;
    }

    //new code
    @Override
    public void onBackPressed() {
        finish();
    }
}
