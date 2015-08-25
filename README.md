#Repeating Screen On by scheduled
* use system API Alarm Repeat.
* Set how long to repeat the scheduled.
* Some notes below:

---------------------------

###AlarmManager 
1. RTC_WAKEUP
2. RTC
3. ELAPSED_REALTIME_WAKEUP
4. ELAPSED_REALTIME
 

----------------------------
### Set Wake up by ScreenLock state

	win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

	PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        Toast.makeText(context, R.string.repeating_wake,
                Toast.LENGTH_LONG).show();
        //释放
        wl.release();



###关于这个屏幕唤醒

+ 使用系统Alarm API调用循环唤醒
+ 设定AlarmManager的启动方式，看文档4种启动方式的不同，在这就不说明具体使用方法了。
+ 设置休眠状态唤醒有以下2点第一点就是在

        setContentView(R.layout.activity_main);
+ 之前插入
+ 
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
+ 第二就是在RepeatingAlarm里面调用 PowerManager方法
+ 
		 PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        Toast.makeText(context, R.string.repeating_wake,
                Toast.LENGTH_LONG).show();
        //释放
        wl.release();
+ 在清单文件加入标注

	
		<receiver android:name=".RepeatingAlarm" android:process=":remote">
            <intent-filter>
                <action android:name=".AlarmActivity"/>
            </intent-filter>
        </receiver>
+ 目前有BUG问题，停止广播失效 ，保存时间待添加
+ 08/24 已经解决这个问题由于广播接受者Recevie里面TOAST引起的错误。