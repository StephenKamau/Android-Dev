package com.example.stephen.rentalfinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private String gender, firstName, lastName, emailAddress, phone, dateOfBirth, password,urlAddress;
    private EditText firstNameTxt, lastNameTxt, emailAddressTxt, phoneTxt, dateOfBirthTxt, passwordTxt, confirmPasswordTxt;
    private ImageButton backToLogin;
    private Button signUp;
    private ProgressDialog progressDialog;
    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        connection = new Connection();

        //list to new sign up request
        backToLogin = findViewById(R.id.imageButtonBack);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                //Called when the user taps the sign-up button

                Intent login = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });

        //get the fields
        firstNameTxt = findViewById(R.id.firstName);
        lastNameTxt = findViewById(R.id.lastName);
        emailAddressTxt = findViewById(R.id.emailAddress);
        phoneTxt = findViewById(R.id.phone);
        dateOfBirthTxt = findViewById(R.id.dateOfBirth);
        passwordTxt = findViewById(R.id.password);
        confirmPasswordTxt = findViewById(R.id.confirmPassword);
        signUp = findViewById(R.id.signUp);
        urlAddress = connection.getUrlSignUpUser();

        //create the progress dialog
        progressDialog = new ProgressDialog(SignUpActivity.this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check for internet connection before sending data
                if (isInternetAvailable()) {
                    // Showing progress dialog at user registration time.
                    progressDialog.setMessage("Sending Data, Please wait...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);

                    //check whether the fields are empty
                    if (!isBlank()) {

                        if (isPasswordMatching()) {

                            firstName = firstNameTxt.getText().toString().trim();
                            lastName = lastNameTxt.getText().toString().trim();
                            emailAddress = emailAddressTxt.getText().toString().trim();
                            phone = phoneTxt.getText().toString().trim();
                            dateOfBirth = dateOfBirthTxt.getText().toString().trim();
                            password = passwordTxt.getText().toString().trim();

                            // Creating string request with post method.
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlAddress,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String ServerResponse) {

                                            // Hiding the progress dialog after all task complete.
                                            progressDialog.dismiss();

                                            // Showing response message coming from server.
                                            Toast.makeText(SignUpActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {

                                            // Hiding the progress dialog after all task complete.
                                            progressDialog.dismiss();

                                            // Showing error message if something goes wrong.
                                            Toast.makeText(SignUpActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() {

                                    // Creating Map String Params.
                                    Map<String, String> params = new HashMap<String, String>();

                                    // Adding All values to Params.
                                    params.put("first_name", firstName);
                                    params.put("last_name", lastName);
                                    params.put("email", emailAddress);
                                    params.put("phone", phone);
                                    params.put("password", password);
                                    params.put("gender", gender);
                                    params.put("date_of_birth", dateOfBirth);

                                    return params;
                                }

                            };

                            // Creating RequestQueue.
                            RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);

                            // Adding the StringRequest object into requestQueue.
                            requestQueue.add(stringRequest);

                            //returns the values to empty

                            /*firstNameTxt.setText("");
                            lastNameTxt.setText("");
                            emailAddressTxt.setText("");
                            emailAddressTxt.setText("");
                            phoneTxt.setText("");
                            dateOfBirthTxt.setText("");
                            passwordTxt.setText("");
                            confirmPasswordTxt.setText("");*/


                            //set focus to first field
                            firstNameTxt.setFocusable(true);
                            firstNameTxt.requestFocus();

                        } else {
                            progressDialog.dismiss();
                            // Showing error message if fields are empty
                            Toast.makeText(SignUpActivity.this, "Your passwords are not matching", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        progressDialog.dismiss();
                        // Showing error message if fields are empty
                        Toast.makeText(SignUpActivity.this, "Fill in all the fields", Toast.LENGTH_LONG).show();
                    }
                } else {
                    progressDialog.dismiss();
                    // Showing error message if fields are empty
                    Toast.makeText(SignUpActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_female:
                if (checked) {
                    // assign gender
                    gender = "female";
                }
                break;
            case R.id.radio_male:
                if (checked) {
                    gender = "male";
                }
                break;
            case R.id.radio_others:
                if (checked) {
                    gender = "other";
                }
                break;
        }
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
        if (firstNameTxt.getText().toString().equals("") ||
                lastNameTxt.getText().toString().equals("") ||
                emailAddressTxt.getText().toString().equals("") ||
                phoneTxt.getText().toString().equals("") ||
                dateOfBirthTxt.getText().toString().equals("") ||
                passwordTxt.getText().toString().equals("") ||
                confirmPasswordTxt.getText().toString().equals("")
                ) {
            return true;

        } else
            return false;
    }

    //check whether passwords match
    public boolean isPasswordMatching() {
        if (!isBlank()) {
            return passwordTxt.getText().toString().equals(confirmPasswordTxt.getText().toString());
        } else {
            return false;
        }
    }
    @Override
    public void onBackPressed(){
        Intent backToLogin = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(backToLogin);
    }
}
