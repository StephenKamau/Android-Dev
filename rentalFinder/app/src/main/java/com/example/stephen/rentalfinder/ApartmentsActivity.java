package com.example.stephen.rentalfinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ApartmentsActivity extends AppCompatActivity {
    Connection connection = new Connection();
    String urlAddress = connection.getUrlAddApartment();
    EditText apartmentTxt, locationTxt, descriptionTxt, rentTxt, contactTxt;
    CheckBox statusCheck;
    TextView showImage;
    Button getImage;

    private static int RESULT_LOAD_IMG = 1;

    //holds the entered values
    String apartmentValue, locationValue, descriptionValue, contactValue, apartmentImage;
    int rentValue;

    boolean status, isImageSelected;
    SharedPreferences sharedPreferences;

    //Creating Volley RequestQueue.
    RequestQueue requestQueue;

    // Creating Progress dialog.
    ProgressDialog progressDialog;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartments);

        //get shared preferences
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);

        //handles drawer click events
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.view_apartment:
                                Intent viewApartment = new Intent(ApartmentsActivity.this, ViewApartmentsActivity.class);
                                startActivity(viewApartment);
                                break;

                            case R.id.view_responsibility:
                                Toast.makeText(ApartmentsActivity.this, "Coming soon", Toast.LENGTH_LONG).show();
                                break;

                            case R.id.view_users:
                                Toast.makeText(ApartmentsActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.add_apartment:
                                Intent addApartment = new Intent(ApartmentsActivity.this, ApartmentsActivity.class);
                                startActivity(addApartment);
                                break;

                            case R.id.add_responsibility:
                                Toast.makeText(ApartmentsActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.logout:
                                //set the shared preferences
                                sharedPreferences.edit().putString("user", "").apply();
                                sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();

                                Intent logout = new Intent(ApartmentsActivity.this, LoginActivity.class);
                                logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(logout);
                                break;


                            default:
                                return true;
                        }
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        return true;
                    }
                });

        //handles the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menu);

        Button saveBtn = findViewById(R.id.btnApartmentAdd);
        getImage = findViewById(R.id.btnAddImage);
        showImage = findViewById(R.id.imageViewApartment);

        saveBtn.setOnClickListener(new View.OnClickListener() {
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
                    if (!(apartmentTxt.getText().toString().equals("")) || isImageSelected || !(locationTxt.getText().toString().equals("")) || !(descriptionTxt.getText().toString().equals(""))) {

                        apartmentValue = apartmentTxt.getText().toString().trim();
                        locationValue = locationTxt.getText().toString().trim();
                        descriptionValue = descriptionTxt.getText().toString().trim();
                        contactValue = contactTxt.getText().toString().trim();
                        rentValue = Integer.parseInt(rentTxt.getText().toString().trim());
                        status = statusCheck.isChecked();

                        // Creating string request with post method.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlAddress,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String ServerResponse) {

                                        // Hiding the progress dialog after all task complete.
                                        progressDialog.dismiss();

                                        // Showing response message coming from server.
                                        Toast.makeText(ApartmentsActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {

                                        // Hiding the progress dialog after all task complete.
                                        progressDialog.dismiss();

                                        // Showing error message if something goes wrong.
                                        Toast.makeText(ApartmentsActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {

                                // Creating Map String Params.
                                Map<String, String> params = new HashMap<String, String>();

                                // Adding All values to Params.
                                params.put("apartment", apartmentValue);
                                params.put("location", locationValue);
                                params.put("description", descriptionValue);
                                params.put("rent_per_month", String.valueOf(rentValue));
                                params.put("principal_contact", contactValue);
                                params.put("status", String.valueOf(status));
                                params.put("apartmentImage", apartmentImage);

                                return params;
                            }

                        };

                        // Creating RequestQueue.
                        RequestQueue requestQueue = Volley.newRequestQueue(ApartmentsActivity.this);

                        // Adding the StringRequest object into requestQueue.
                        requestQueue.add(stringRequest);

                        //returns the values to empty

                        apartmentTxt.setText("");
                        locationTxt.setText("");
                        descriptionTxt.setText("");
                        statusCheck.setChecked(false);
                        rentTxt.setText("");
                        contactTxt.setText("");
                        showImage.setVisibility(View.GONE);
                        getImage.setText(R.string.add_image);

                        //set focus to first field
                        apartmentTxt.setFocusable(true);
                        apartmentTxt.requestFocus();

                    } else {
                        progressDialog.dismiss();
                        // Showing error message if fields are empty
                        Toast.makeText(ApartmentsActivity.this, "Fill in all the fields", Toast.LENGTH_LONG).show();
                    }
                } else {
                    progressDialog.dismiss();
                    // Showing error message if fields are empty
                    Toast.makeText(ApartmentsActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        //pick image from gallery
        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

            }
        });

        //assigning the editText fields
        apartmentTxt = findViewById(R.id.apartmentName);
        locationTxt = findViewById(R.id.location);
        descriptionTxt = findViewById(R.id.description);
        statusCheck = findViewById(R.id.checkBoxStatus);
        rentTxt = findViewById(R.id.rentAmount);
        contactTxt = findViewById(R.id.principalContact);

        // Creating Volley newRequestQueue .
        requestQueue = Volley.newRequestQueue(ApartmentsActivity.this);

        progressDialog = new

                ProgressDialog(ApartmentsActivity.this);

    }

    //Activity result
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                getImage.setText(R.string.change_image);
                showImage.setVisibility(View.VISIBLE);
                String path = "Image Selected";
                apartmentImage = getStringImage(selectedImage);
                isImageSelected = true;
                showImage.setText(path);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ApartmentsActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        } else {
            isImageSelected = false;
            Toast.makeText(ApartmentsActivity.this, "You haven't picked an image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //encode image to string
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
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

    //new code
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
