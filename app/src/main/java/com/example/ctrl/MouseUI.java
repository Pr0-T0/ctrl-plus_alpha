package com.example.ctrl;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MouseUI extends AppCompatActivity {
    TextView textView;
    Button leftButton;
    Button rightButton;
    String ipAddress;
    int port;
    private boolean isDragging = false; // To track if the finger is dragging or not
    private int lastX, lastY; // To track the last X and Y position of the finger

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse_ui);
        textView = (TextView) findViewById(R.id.TouchView);
        leftButton = (Button) findViewById(R.id.leftclick);
        rightButton = (Button) findViewById(R.id.rightclick);

        // Retrieve the IP address and port from shared preferences
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        ipAddress = preferences.getString("ipAddress", "");
        port =  preferences.getInt("port", 0);

        // Set an OnTouchListener on the textView to handle finger movements
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getActionMasked(); // Get the action of the event

                switch (action) {
                    case MotionEvent.ACTION_DOWN: // Finger touched the screen
                        isDragging = true; // Start dragging
                        lastX = (int) event.getX(); // Set the last X position
                        lastY = (int) event.getY(); // Set the last Y position
                        break;

                    case MotionEvent.ACTION_MOVE: // Finger moved on the screen
                        if (isDragging) {
                            int x = (int) event.getX(); // Get the new X position
                            int y = (int) event.getY(); // Get the new Y position
                            int deltaX = x - lastX; // Get the change in X position
                            int deltaY = y - lastY; // Get the change in Y position
                            lastX = x; // Update the last X position
                            lastY = y; // Update the last Y position
                            SignalSender.sendMouseData(ipAddress,port,deltaX, deltaY, false, false); // Send the mouse data to the computer
                        }
                        break;

                    case MotionEvent.ACTION_UP: // Finger lifted off the screen
                        isDragging = false; // Stop dragging
                        break;
                }
                return true;
            }

            public boolean performClick() {
                return false;
            }
        });
        // Set OnTouchListener on the leftButton to handle left clicks
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // Button pressed down
                        SignalSender.sendMouseData(ipAddress,port,0, 0, true, false); // Send left click data to the computer
                        break;

                    case MotionEvent.ACTION_UP: // Button released
                        SignalSender.sendMouseData(ipAddress,port,0, 0, false, false); // Send mouse data to stop left click
                        break;
                }
                return true;
            }
        });

// Set OnTouchListener on the rightButton to handle right clicks
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // Button pressed down
                        SignalSender.sendMouseData(ipAddress,port,0, 0, false, true); // Send right click data to the computer
                        break;

                    case MotionEvent.ACTION_UP: // Button released
                        SignalSender.sendMouseData(ipAddress,port,0, 0, false, false); // Send mouse data to stop right click
                        break;
                }
                return true;
            }
        });
    }
    /*@Override
    protected void onDestroy() {

        super.onDestroy();
        SignalSender.sendMouseData(ipAddress,port,0,0,true,true);
    }*/
}