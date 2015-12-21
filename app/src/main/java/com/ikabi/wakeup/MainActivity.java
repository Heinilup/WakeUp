package com.ikabi.wakeup;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
<<<<<<< HEAD
import android.os.PowerManager;
import android.util.Log;
=======
>>>>>>> origin/master
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
<<<<<<< HEAD
    private static final int DELAY = 0;
    TextView textView;
    Toast mToast;
    private String setTime;
    private PendingIntent sender;
    private Intent intent;

=======
    TextView textView;
    Toast mToast;
>>>>>>> origin/master
    private static final int msgKey1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        intent = new Intent(MainActivity.this, RepeatingAlarm.class);
         sender = PendingIntent.getBroadcast(MainActivity.this,
                0, intent, 0);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.showtime);
//        SharedPreferences preferences = getSharedPreferences("count", MODE_PRIVATE);
//        int count = preferences.getInt("count", 0);
//        textView.setText("唤醒总次数：" + count);
        Button button = (Button)findViewById(R.id.StarRepeating);

        button.setOnClickListener(mStartRepeatingListener);
        button = (Button)findViewById(R.id.StopRepeating);
        button.setOnClickListener(mStopRepeatingListener);
        new TimeThread().start();
    }
    public class TimeThread extends Thread {
        @Override
        public void run () {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(true);
        }
    }


    public void click(View v){
        EditText et = (EditText) findViewById(R.id.et);
        SharedPreferences sp = getSharedPreferences("sec", MODE_MULTI_PROCESS);
        sp.edit().putString("setNumber", et.getText().toString()).apply();
        mToast = Toast.makeText(MainActivity.this, R.string.saved,
                Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();

    }

    public void click1(View v){
        SharedPreferences settings = getSharedPreferences("count", MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("count");
        editor.apply();
        mToast = Toast.makeText(MainActivity.this, R.string.ClearShare,
                Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
<<<<<<< HEAD

    }
    public void click2(View v){
        SharedPreferences preferences = getSharedPreferences("count", MODE_MULTI_PROCESS);

        int count = preferences.getInt("count", 0);
        Toast.makeText(MainActivity.this, "唤醒已经被使用了" + count + "次。"
               , Toast.LENGTH_LONG).show();
=======

    }
    public void click2(View v){
        SharedPreferences preferences = getSharedPreferences("count", MODE_MULTI_PROCESS);

        int count = preferences.getInt("count", 0);
        Toast.makeText(MainActivity.this, "唤醒已经被使用了" + count + "次。"
               , Toast.LENGTH_LONG).show();

    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    SharedPreferences sp = getSharedPreferences("count", MODE_MULTI_PROCESS);
                    int count = sp.getInt("count", 0);
                    textView.setText("唤醒次数：" + count);
                    break;

                default:
                    break;
            }
        }
    };
>>>>>>> origin/master

    }
    private Runnable clock = new Runnable() {
        @Override
        public void run() {
            SharedPreferences preferences = getSharedPreferences("count", MODE_MULTI_PROCESS);
            preferences.edit().putInt("count", Integer.parseInt("0"));
            int count = preferences.getInt("count", 0);
//        Toast.makeText(context, "唤醒已经被使用了" + count + "次。"
//                , Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = preferences.edit();
            // 存入数据
            editor.putInt("count", ++count);
            // 提交修改
            editor.apply();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
            Log.i("sj", "pm =" + wl);
            //点亮屏幕
            wl.acquire();

            //释放
            wl.release();
            Log.d("times", "clock");
            mHandler.postDelayed(this,Long.parseLong(setTime) * 1000);
        }
    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    SharedPreferences sp = getSharedPreferences("count", MODE_MULTI_PROCESS);
                    int count = sp.getInt("count", 0);
                    textView.setText("唤醒次数：" + count);
                    break;
                case DELAY:



                default:
                    break;
            }
        }
    };



    public View.OnClickListener mStartRepeatingListener = new View.OnClickListener() {
        public void onClick(View v) {

            // When the alarm goes off, we want to broadcast an Intent to our
            // BroadcastReceiver.  Here we make an Intent with an explicit class
            // name to have our own receiver (which has been published in
            // AndroidManifest.xml) instantiated and called, and then create an
            // IntentSender to have the intent executed as a broadcast.
            // Note that unlike above, this IntentSender is configured to
            // allow itself to be sent multiple times.
            /*Get the setTime from SharedPreference*/
<<<<<<< HEAD
=======
            SharedPreferences sp = getSharedPreferences("sec", MODE_PRIVATE);
            String setTime = sp.getString("setNumber", "");
>>>>>>> origin/master


            SharedPreferences sp = getSharedPreferences("sec", MODE_PRIVATE);
            setTime = sp.getString("setNumber", "");

            /*// Schedule the alarm!
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP,
<<<<<<< HEAD
                    (System.currentTimeMillis() + Long.parseLong(setTime) * 1000), Long.parseLong(setTime) * 1000, sender);
            // Tell the user about what we did.*/
            mHandler.postDelayed(clock,Long.parseLong(setTime) * 1000);
=======
                    (System.currentTimeMillis() + Long.parseLong(setTime)*1000), Long.parseLong(setTime)*1000, sender);
            // Tell the user about what we did.
>>>>>>> origin/master
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


                // And cancel the alarm.
                /*AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.cancel(sender);*/
                mHandler.removeCallbacks(clock);

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

