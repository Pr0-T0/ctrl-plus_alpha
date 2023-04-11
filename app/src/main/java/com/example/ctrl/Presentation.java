package com.example.ctrl;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class Presentation extends AppCompatActivity {
    private  long mTimeStartInMillis;

    private TextView mTextViewCountDown;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;

    Button start, left, right,set;
    TextView time;
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

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonReset = findViewById(R.id.button_reset);

        mTextViewCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the dialog layout
                View dialogView = getLayoutInflater().inflate(R.layout.set_time, null);

                // Find the EditText view in the dialog layout
                EditText timeEditText = dialogView.findViewById(R.id.edit_text_time);
                Button button_set = dialogView.findViewById(R.id.button_set);
                Button button_cancel = dialogView.findViewById(R.id.button_cancel);
                // Create the dialog and set its content view
                AlertDialog.Builder builder = new AlertDialog.Builder(Presentation.this);
                builder.setView(dialogView);

                AlertDialog dialog = builder.create();
                dialog.show();
                // Add "OK" button to the dialog
                button_set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get the time entered by the user
                        String timeString = timeEditText.getText().toString().trim();
                        if(timeString == null){
                            timeEditText.setError("Cannot be Empty");
                        }else {
                            int timeInSeconds = Integer.parseInt(timeString) * 60;

                            // Set the timer duration
                            mTimeStartInMillis = timeInSeconds * 1000;
                            resetTimer();

                            dialog.cancel();
                        }
                    }
                });

                // Add "Cancel" button to the dialog
                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar3);
        toolbar.setTitle(R.string.activity_presentation);

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        updateCountDownText();
        start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    currentKeyCode = 116;
                    isSendingCode = true;
                    handler.post(sendCodeRunnable);
                    if(mTimerRunning){
                        pauseTimer();
                    }else{
                        startTimer();
                    }
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
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    isSendingCode = false;
                }
                return true;
            }
        });
    }
    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();
        mTimerRunning = true;
        start.setText("exit");
        mButtonReset.setVisibility(View.INVISIBLE);
    }
    private void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;
        start.setText("start");
        mButtonReset.setVisibility(View.VISIBLE);
    }
    private void resetTimer(){

        mTimeLeftInMillis = mTimeStartInMillis;
        Log.d("Presentation", "Time entered by user: " + mTimeLeftInMillis);

        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
    }
    private void updateCountDownText(){
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }
}
