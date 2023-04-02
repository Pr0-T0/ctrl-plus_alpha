package com.example.ctrl;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Menu extends AppCompatActivity {
    String ipAddress;
    int port;
    ImageButton img,img1,img2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_menu);

        // Retrieve the IP address and port from shared preferences
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        ipAddress = preferences.getString("ipAddress", "");
        port = preferences.getInt("port", 0);

        Toast.makeText(this, "IP Address: " + ipAddress + "\nPort: " + port, Toast.LENGTH_LONG).show();
        img = (ImageButton) findViewById(R.id.scrnshare);
        img1 = (ImageButton) findViewById(R.id.mouse);
        img2 = (ImageButton) findViewById(R.id.presentation);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, scrnshare.class);
                startActivity(i);
            }
        });
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, MouseUI.class);
                startActivity(i);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Presentation.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        // Inflate the custom layout for the dialog
        View dialogView = inflater.inflate(R.layout.pop_up, null);
        builder.setView(dialogView);

        // Get references to the views in the custom layout
        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
        Button yesButton = dialogView.findViewById(R.id.dialog_yes_button);
        Button noButton = dialogView.findViewById(R.id.dialog_no_button);

        // Set the text for the title and message
        titleTextView.setText("Alert");
        messageTextView.setText("Are you sure you want to leave the menu?this will reset the connection");

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Set the listeners for the buttons
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu.super.onBackPressed();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


}