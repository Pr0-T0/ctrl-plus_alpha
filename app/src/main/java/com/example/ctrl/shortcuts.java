package com.example.ctrl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class shortcuts extends AppCompatActivity {
    Button shut_down,restart,sleep,lock;
    SharedPreferences preferences;
    String ipAddress;
    int port;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcuts);
        Toolbar toolbar = findViewById(R.id.tbar1);
        toolbar.setTitle(R.string.activity_shortcuts);
        shut_down = findViewById(R.id.shut_down);
        restart = findViewById(R.id.restart);
        sleep = findViewById(R.id.sleep);
        lock = findViewById(R.id.lock);

        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        ipAddress = preferences.getString("ipAddress", "");
        port =  preferences.getInt("port", 0);

        shut_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send signals
                SignalSender.SendShortcuts(ipAddress,port,404);
                //yeah i set the siganl to 404
            }
        });
    }
}