/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ikabi.wakeup;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

/**
 * This is an example of implement an {@link BroadcastReceiver} for an alarm that
 * should occur once.
 */
public class RepeatingAlarm extends BroadcastReceiver {
    PowerManager pm;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences("count", context.MODE_MULTI_PROCESS);
        preferences.edit().putInt("count", Integer.parseInt("0"));
        int count = preferences.getInt("count", 0);
//        Toast.makeText(context, "唤醒已经被使用了" + count + "次。"
//                , Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = preferences.edit();
        // 存入数据
        editor.putInt("count", ++count);
        // 提交修改
        editor.apply();
        pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        Log.i("sj", "pm =" + wl);
        //点亮屏幕
        wl.acquire();

        //释放
        wl.release();

        startSleep();

    }
    private void startSleep(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(5000);
                    pm.goToSleep(SystemClock.uptimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    }

