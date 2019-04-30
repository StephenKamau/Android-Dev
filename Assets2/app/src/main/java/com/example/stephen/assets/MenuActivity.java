package com.example.stephen.assets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //code for handling the navigation
        final ImageButton addUsers = findViewById(R.id.btnAddUser);
        addUsers.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent addUsers = new Intent(v.getContext(),SignUpActivity.class);
                startActivity(addUsers);
            }
        });

        //code for navigating to location
        final ImageButton addLocation = findViewById(R.id.btnAddLocation);
        addLocation.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent addLocations = new Intent(v.getContext(),LocationActivity.class);
                startActivity(addLocations);
            }
        });

        //code for navigating to add items
        final ImageButton addItem = findViewById(R.id.btnAddItems);
        addItem.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent addItem = new Intent(v.getContext(),ItemActivity.class);
                startActivity(addItem);
            }
        });

        //code for navigating to add responsibility
        final ImageButton addResponsibility = findViewById(R.id.btnAddResponsibility);
        addResponsibility.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent addResponsibility = new Intent(v.getContext(),ResponsibilityActivity.class);
                startActivity(addResponsibility);
            }
        });
    }
}
