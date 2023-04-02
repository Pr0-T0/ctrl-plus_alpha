package com.example.ctrl;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class Presentation extends AppCompatActivity {
    Button start, left, right;
    SharedPreferences preferences;
    String ipAddress;
    int port;
    Handler handler = new Handler();
    boolean isSendingCode = false;
    int currentKeyCode = -1;
    Runnable sendCodeRunnable = new Runnable() {
        @Override
        public void run() {
            if (isSendingCode) {
                SignalSender.sendKeycode(ipAddress, port,currentKeyCode);
                handler.postDelayed(this, 50); // Adjust delay time as needed
            }
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        start = findViewById(R.id.start);
        left = findViewById(R.id.left_arrow);
        right = findViewById(R.id.right_arrow);
        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        ipAddress = preferences.getString("ipAddress", "");
        port =  preferences.getInt("port", 0);
        start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    currentKeyCode = 116;
                    isSendingCode = true;
                    handler.post(sendCodeRunnable);
                    System.out.println("116");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    isSendingCode = false;
                }
                return true;
            }
        });

        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    currentKeyCode = 37;
                    isSendingCode = true;

                    handler.post(sendCodeRunnable);
                    System.out.println("37");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    isSendingCode = false;
                }
                return true;
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    currentKeyCode = 39;
                    isSendingCode = true;

                    handler.post(sendCodeRunnable);
                    System.out.println("39");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    isSendingCode = false;
                }
                return true;
            }
        });
    }
}
