package com.example.stephen.rentalfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewApartmentsActivity extends AppCompatActivity {

    Connection connection = new Connection();
    private DrawerLayout mDrawerLayout;
    private List<ViewApartmentDataHandler> apartmentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ViewApartmentRecyclerViewAdapter mAdapter;
    private ProgressBar progressBar;
    private ImageView errorIcon;
    private Button retry;
    private TextView errorText;
    private int requestCount = 1;
    String urlAddress = connection.getUrlViewApartments();
    String ipAddress = connection.getIpAddress();

    String apartmentName = "apartmentname";
    String locationName = "location";
    String contactName = "principalcontact";
    String rentAmount = "rentpermonth";
    String imageUrl = "imageurl";
    String description = "description";

    //access shared preferences
    SharedPreferences sharedPreferences;

    //nav

    JsonArrayRequest jsonArrayRequest;

    RequestQueue requestQueue;

    private static Context mAppContext;

    //main entrance
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_apartments);

        //handles drawer click events
        mDrawerLayout = findViewById(R.id.drawer_layout);

        //get shared preferences
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);

        //new code
        this.setAppContext(getApplicationContext());

        //find button,image
        errorIcon = findViewById(R.id.imgError);
        retry = findViewById(R.id.retry);
        errorText = findViewById(R.id.err);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        int id = menuItem.getItemId();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        switch (id) {
                            case R.id.view_apartment:
                                Intent viewApartment = new Intent(ViewApartmentsActivity.this, ViewApartmentsActivity.class);
                                startActivity(viewApartment);
                                break;

                            case R.id.view_responsibility:
                                Toast.makeText(ViewApartmentsActivity.this, "Coming soon", Toast.LENGTH_LONG).show();
                                break;

                            case R.id.view_users:
                                Toast.makeText(ViewApartmentsActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.add_apartment:
                                Intent addApartment = new Intent(ViewApartmentsActivity.this, ApartmentsActivity.class);
                                startActivity(addApartment);
                                // set item as selected to persist highlight
                                menuItem.setChecked(true);
                                break;

                            case R.id.add_responsibility:
                                Toast.makeText(ViewApartmentsActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.logout:
                                //set the shared preferences
                                sharedPreferences.edit().putString("user", "").apply();
                                sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();

                                Intent logout = new Intent(ViewApartmentsActivity.this, LoginActivity.class);
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

        //recycler view

        recyclerView = (RecyclerView) findViewById(R.id.apartmentsList);

        //Adding an scroll change listener to recycler view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (isLastItemDisplaying(recyclerView)) {
                        //Calling the method get data again
                        requestCount++;
                        getData();
                    }
                }
            });
        }

        mAdapter = new ViewApartmentRecyclerViewAdapter(apartmentList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //
        apartmentList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
        if (isInternetAvailable()) {
            getData();
        } else {
            progressBar.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
            errorIcon.setVisibility(View.VISIBLE);
            errorText.setVisibility(View.VISIBLE);
            Toast.makeText(ViewApartmentsActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
        }

        //retry click
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                errorIcon.setVisibility(View.GONE);
                retry.setVisibility(View.GONE);
                errorText.setVisibility(View.GONE);
                //retry the call again
                if (isInternetAvailable()) {
                    getData();
                } else {
                    progressBar.setVisibility(View.GONE);
                    retry.setVisibility(View.VISIBLE);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                    Toast.makeText(ViewApartmentsActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //mAdapter.notifyDataSetChanged();
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void getData() {

        jsonArrayRequest = new JsonArrayRequest(urlAddress + requestCount,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        progressBar.setVisibility(View.GONE);

                        parseJsonData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.GONE);
                        retry.setVisibility(View.VISIBLE);
                        errorIcon.setVisibility(View.VISIBLE);
                        errorText.setVisibility(View.VISIBLE);
                        //show error
                        // Showing error message if something goes wrong.
                        Toast.makeText(ViewApartmentsActivity.this, "Loaded all the records", Toast.LENGTH_LONG).show();

                    }
                });

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }

    public void parseJsonData(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {

            ViewApartmentDataHandler viewApartmentDataHandler = new ViewApartmentDataHandler();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                viewApartmentDataHandler.setApartmentName(json.getString(apartmentName));
                viewApartmentDataHandler.setLocation(json.getString(locationName));
                viewApartmentDataHandler.setContact(json.getString(contactName));
                viewApartmentDataHandler.setRent(json.getString(rentAmount));
                viewApartmentDataHandler.setImageUrl(ipAddress + json.getString(imageUrl));
                viewApartmentDataHandler.setDescription(json.getString(description));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            apartmentList.add(viewApartmentDataHandler);
        }

        mAdapter = new ViewApartmentRecyclerViewAdapter(apartmentList, this);

        recyclerView.setAdapter(mAdapter);

        //Notifying the adapter that data has been added or changed
        mAdapter.notifyDataSetChanged();

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

    //This method would check that the recycler view scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    //new code
    public static Context getAppContext() {
        return mAppContext;
    }

    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
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
