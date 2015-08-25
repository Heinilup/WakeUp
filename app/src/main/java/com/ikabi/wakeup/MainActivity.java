package com.ikabi.wakeup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    Toast mToast;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView showtime = (TextView) findViewById(R.id.showtime);

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.StarRepeating);
        button.setOnClickListener(mStartRepeatingListener);
        button = (Button)findViewById(R.id.StopRepeating);
        button.setOnClickListener(mStopRepeatingListener);
    }

    public void click(View v){
        EditText et = (EditText) findViewById(R.id.et);
        SharedPreferences sp = getSharedPreferences("sec", MODE_PRIVATE);
        sp.edit().putString("setNumber", et.getText().toString()).apply();
        mToast = Toast.makeText(MainActivity.this, R.string.saved,
                Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();

    }

    public void click1(View v){
        SharedPreferences settings = getSharedPreferences("count", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("count");
        editor.apply();
        mToast = Toast.makeText(MainActivity.this, R.string.ClearShare,
                Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();

    }
    public void click2(View v){
        SharedPreferences showtime = getSharedPreferences("count", MODE_PRIVATE);
        String timeValue = showtime.getString("count", String.valueOf(1));
        resultText.setText("次数：" + String.valueOf(timeValue));

    }


    private View.OnClickListener mStartRepeatingListener = new View.OnClickListener() {
        public void onClick(View v) {

            // When the alarm goes off, we want to broadcast an Intent to our
            // BroadcastReceiver.  Here we make an Intent with an explicit class
            // name to have our own receiver (which has been published in
            // AndroidManifest.xml) instantiated and called, and then create an
            // IntentSender to have the intent executed as a broadcast.
            // Note that unlike above, this IntentSender is configured to
            // allow itself to be sent multiple times.
            Intent intent = new Intent(MainActivity.this, RepeatingAlarm.class);
            PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this,
                    0, intent, 0);
            /*Get the setTime from SharedPreference*/
            SharedPreferences sp = getSharedPreferences("sec", MODE_PRIVATE);
            String setTime = sp.getString("setNumber", "");

            //Set first Wake Up time 10sec.
            long firstTime = SystemClock.elapsedRealtime();
            firstTime += 10*1000;


            // Schedule the alarm!
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP,
                    firstTime, Long.parseLong(setTime)*1000, sender);
            // Tell the user about what we did.
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(MainActivity.this, R.string.repeating_scheduled,
                    Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        }
    };

    private View.OnClickListener mStopRepeatingListener;


    {
        mStopRepeatingListener = new View.OnClickListener() {
            public void onClick(View v) {
                // Create the same intent, and thus a matching IntentSender, for
                // the one that was scheduled.
                Intent intent = new Intent(MainActivity.this, RepeatingAlarm.class);
                PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this,
                        0, intent, 0);

                // And cancel the alarm.
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.cancel(sender);

                // Tell the user about what we did.
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(MainActivity.this, R.string.repeating_unscheduled,
                        Toast.LENGTH_LONG);
                mToast.show();
            }
        };
    }


}

