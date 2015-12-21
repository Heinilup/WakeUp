package com.ikabi.wakeup;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;


/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:shuangjun.zou@sim.com
 * @ Data:2015/12/2
 */
public class ScreenoffWakeup extends Activity {
    private static final String TAG = "CIT_AwakeNoSleep";
    Toast mToast;
    private int mSavedTimeout;
    private PowerManager mPwrMgr;
    private PowerManager.WakeLock mWakelock;
    private PowerManager.WakeLock mTurnBackOn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screenoff_wakeup);
        Log.d(TAG, " onCreate(");

    }

    @Override
    protected void onResume() {
        Log.d(TAG, " onResume");

        //mgr.registerListener((SensorEventListener) this, light, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
        mPwrMgr = (PowerManager) getSystemService(POWER_SERVICE);
        mPwrMgr.goToSleep(SystemClock.uptimeMillis());
        //   mWakelock = mPwrMgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AwakeNoSleep");
        // mWakelock.acquire();

        // Save the current value of the screen timeout, then set it always on
        Log.d(TAG, " mPwrMgr.goToSleep(0)");


        Settings.System.putInt(getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT, -1); // always on
        mToast = Toast.makeText(ScreenoffWakeup.this, R.string.screenOff,
                Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }



    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                Log.d(TAG, " The screen has turned off");
                // Turn the screen back on again, from the main thread
                if (mTurnBackOn != null)
                    mTurnBackOn.release();

                mTurnBackOn = mPwrMgr.newWakeLock(
                        PowerManager.PARTIAL_WAKE_LOCK
                                | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                        "ScreenOff");
                mTurnBackOn.acquire();
            }
        }
    };

    @Override
    protected void onStart() {
        Log.d(TAG, " onStart()");
        super.onStart();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);

    }

    @Override
    protected void onStop() {
        Log.d(TAG, " onStop()");
        super.onStop();
        unregisterReceiver(mReceiver);
        // mWakelock.release();
        if (mTurnBackOn != null)
            mTurnBackOn.release();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, " onPause() ");
        //mgr.unregisterListener((SensorEventListener) this, light);
        super.onPause();
    }
}

