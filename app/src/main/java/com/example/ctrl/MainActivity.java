package com.example.ctrl;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {
    Button btn;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.connect);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle(R.string.activity_main);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });
    }
    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to turn on flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),result -> {
        String pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?):([1-9]|[1-9][0-9]{1,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
       if(result.getContents().matches(pattern) ) {
           String[] parts = result.getContents().split(":");
           String ipAddress = parts[0];
           int port = Integer.parseInt(parts[1]);

           //save ip address and port in shared preferences
           SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
           SharedPreferences.Editor editor = preferences.edit();
           editor.putString("ipAddress", ipAddress);
           editor.putInt("port", port);
           editor.apply();

           //menu Intent
           Intent i = new Intent(MainActivity.this, Menu.class);
           startActivity(i);
       }
       else{

           LayoutInflater inflater = getLayoutInflater();

            // Inflate the custom layout for the dialog
           View dialogView = inflater.inflate(R.layout.error_popup, null);

           AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
           builder.setView(dialogView);

            // Get references to the views in the custom layout
           TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
           TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
           Button yesButton = dialogView.findViewById(R.id.dialog_yes_button);

            // Set the text for the title and message
           titleTextView.setText("Error");
           messageTextView.setText("It looks like you are trying to scan other qr codes");
           // Create and show the dialog
           AlertDialog dialog = builder.create();
           dialog.show();
            // Set the listener for the button
           yesButton.setText("OK");
           yesButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dialog.dismiss();
               }
           });
           }

    });
}