package com.ikabi.wakeup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

//import com.socks.library.KLog;


public class MainActivity extends Activity implements SensorEventListener{

    private static final String TAG = "Wakeup";
    private static final int DELAY = 0;
    TextView textView;
    TextView psensortime;
    TextView gsersontime;
    TextView ecomtime;
    Toast mToast;
    private String setTime;
    private PendingIntent sender;
    private Intent intent;
    private byte[] light_on = {'4','0'};
    private byte[] light_off = {'0'};
    private String red_node = "/sys/class/leds/red/brightness";
    private String green_node = "/sys/class/leds/green/brightness";
    private String blue_node = "/sys/class/leds/blue/brightness";

    private int mtimes ;
    private Timer mCalculateTimeTimer = new Timer();

    class CalculateTimeTask extends TimerTask {
        public void run() {
            mtimes++;
            if (mtimes%3 ==0) {
                write_node(light_on, red_node);
                write_node(light_off, green_node);
                write_node(light_off, blue_node);

            }else if (mtimes%3 ==1) {
                write_node(light_off, red_node);
                write_node(light_on, green_node);
                write_node(light_off, blue_node);
            }else if (mtimes%3 ==2) {
                write_node(light_off, red_node);
                write_node(light_off, green_node);
                write_node(light_on, blue_node);
            }
        }
    }

    // 定义Sensor管理器
    private SensorManager mSensorManager;
    EditText etOrientation;
    EditText etGyro;
    EditText etMagnetic;
    EditText etGravity;
    EditText etLinearAcc;
    EditText etTemerature;
    EditText etLight;
    EditText psensor_add;
    EditText etPressure;
    EditText et;
    String file=null;
    private boolean isSaved = false;
    private boolean isSuccesSaved = false;
    private boolean isGsensorSaved = false;
    private boolean isGSuccesSaved = false;
    private boolean isPsensorSaved = false;
    private boolean isPSuccesSaved = false;
    static int wakeupcount=0;
    PowerManager pm;

    public static float[] opV={0,0,0};
    public static float[] npV={0,0,0};
    public static float[] ogV={0,0,0};
    public static float[] ngV={0,0,0};
    public static float[] oeV={0,0,0};
    public static float[] neV={0,0,0};
    //public static Context context=null;

    private static final int msgKey1 = 1;
    private static final int msgKey2 = 2;
    private startSleep mStartSleep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window win = getWindow();
       // context=this;
        mCalculateTimeTimer.schedule(new CalculateTimeTask(), 1, 1000);
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        intent = new Intent(MainActivity.this, RepeatingAlarm.class);
        sender = PendingIntent.getBroadcast(MainActivity.this,
                0, intent, 0);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.showtime);

        psensortime = (TextView) findViewById(R.id.psensortime);
        gsersontime = (TextView) findViewById(R.id.gsersontime);
        ecomtime = (TextView) findViewById(R.id.ecomtime);

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mStartSleep = new startSleep();


        //Sensor start
        etOrientation = (EditText) findViewById(R.id.etOrientation);
        etGyro = (EditText) findViewById(R.id.etGyro);
        etMagnetic = (EditText) findViewById(R.id.etMagnetic);
        etGravity = (EditText) findViewById(R.id.etGravity);
        etLinearAcc = (EditText) findViewById(R.id.etLinearAcc);
        etTemerature = (EditText) findViewById(R.id.etTemerature);
        etLight = (EditText) findViewById(R.id.etLight);
        psensor_add = (EditText) findViewById(R.id.psensor_add);
        etPressure = (EditText) findViewById(R.id.etPressure);

        /*InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        et.clearFocus();
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);*/
        // 获取传感器管理服务
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        //Sensor end

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
        et = (EditText) findViewById(R.id.et);
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

    }
    public void click2(View v){
        SharedPreferences preferences = getSharedPreferences("count", MODE_MULTI_PROCESS);

        int count = preferences.getInt("count", 0);
        Toast.makeText(MainActivity.this, "唤醒已经被使用了" + count + "次。"
                , Toast.LENGTH_LONG).show();

    }

    /*public void click_sensor(View v){
        Intent intent = new Intent(this, SensorTestActivity.class);
        startActivity(intent);

    }*/
    private Runnable clock = new Runnable() {
        @Override
        public void run() {
            Log.d("ZSJ","Runnable-Clock");
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
            //Log.d("ZSJ","Runnable");
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
            //Log.i("sj", "pm =" + wl);
            //点亮屏幕
            wl.acquire();


            //释放
            wl.release();
            /*if ( ngV == ogV){
                gslog(true);
            } else {
                gslog(false);
            }
            if (opV == npV){
                pslog(true);
            } else{
                pslog(false);
            }
            if( oeV == neV){
                eslog(true);
            } else {
                eslog(false);
            }*/

            //mStartSleep.run();
            mHandler.sendEmptyMessageDelayed(DELAY,60*1000);
            //mHandler.postDelayed(this,Long.parseLong(setTime) * 1000);

        }
    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    SharedPreferences sp = getSharedPreferences("count", MODE_MULTI_PROCESS);
                    wakeupcount = sp.getInt("count", 0);
                    textView.setText("唤醒次数：" + wakeupcount);
                    SharedPreferences sp2 = getSharedPreferences("EMsensor_count", MODE_MULTI_PROCESS);
                    int count2 = sp2.getInt("EMsensor_count", 0);
                    psensortime.setText("Psensor失败次数：" + count2);
                    SharedPreferences sp3 = getSharedPreferences("Gsensor_count", MODE_MULTI_PROCESS);
                    int count3 = sp3.getInt("gsersontime_count", 0);
                    gsersontime.setText("Gsensor失败次数：" + count3);
                    SharedPreferences sp4 = getSharedPreferences("ecomtime_count", MODE_MULTI_PROCESS);
                    int count4 = sp4.getInt("EMsensor_count", 0);
                    ecomtime.setText("重力传感器失败次数：" + count4);
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


            SharedPreferences sp = getSharedPreferences("sec", MODE_PRIVATE);
            setTime = sp.getString("setNumber", "");

            // Schedule the alarm!
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP,
                    (System.currentTimeMillis() + Long.parseLong(setTime) * 1000), Long.parseLong(setTime) * 1000, sender);
            // Tell the user about what we did.
            mHandler.postDelayed(clock,Long.parseLong(setTime) * 1000);
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(MainActivity.this, R.string.repeating_scheduled,
                    Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
            pm.goToSleep(SystemClock.uptimeMillis());
            if (!isSaved) {
                isSaved = true;
            }

        }
    };

    public class startSleep extends Thread {


        LogFile lfile=new LogFile(MainActivity.this);
        String time=lfile.cfile();
        @Override
        public void run () {
            do {
                try {
                    sleep(15000);
                    if(opV[0]==0&&npV[0]==0){
                        lfile.pslog(time,true);
                        SharedPreferences preferences = getSharedPreferences("Psensor_count", MODE_MULTI_PROCESS);
                        preferences.edit().putInt("Psensor_count", Integer.parseInt("0"));
                        int count = preferences.getInt("EMsensor_count", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("Psensor_count", ++count);
                        editor.apply();

                    }else{
                        lfile.pslog(time,false);
                    }
                    if(ogV[0]==ngV[0]&&ogV[1]==ngV[1]&&ogV[2]==ngV[2]){
                        lfile.gslog(time,true);
                        SharedPreferences preferences = getSharedPreferences("Gsensor_count", MODE_MULTI_PROCESS);
                        preferences.edit().putInt("Gsensor_count", Integer.parseInt("0"));
                        int count = preferences.getInt("Gsensor_count", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("Gsensor_count", ++count);
                        editor.apply();
                    }else{
                        lfile.gslog(time,false);
                    }
                    if(oeV[0]==neV[0]&&oeV[1]==neV[1]&&oeV[2]==neV[2]){
                        lfile.eslog(time,true);
                        SharedPreferences preferences = getSharedPreferences("Psensor_count", MODE_MULTI_PROCESS);
                        preferences.edit().putInt("Psensor_count", Integer.parseInt("0"));
                        int count = preferences.getInt("EMsensor_count", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("Psensor_count", ++count);
                        editor.apply();
                    }else{
                        lfile.eslog(time,false);
                    }

                    pm.goToSleep(SystemClock.uptimeMillis());
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(true);
        }
    }


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
                isSaved = false;
            }
        };
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        mtimes =0;
        for(int i=0;i<3;i++){
            ogV[i]=ngV[i];
        }
        for(int i=0;i<3;i++){
            oeV[i]=neV[i];
        }
        opV[0]=npV[0];
        //startLed();
        /*isSaved = false;
        isSuccesSaved = false;
        isGsensorSaved =false;
        isGSuccesSaved = false ;
        isPsensorSaved =false;
        isPSuccesSaved = false ;*/
        // 为系统的方向传感器注册监听器
        mSensorManager.registerListener((SensorEventListener) this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        // 为系统的陀螺仪传感器注册监听器
        mSensorManager.registerListener((SensorEventListener) this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_GAME);
        // 为系统的磁场传感器注册监听器
        mSensorManager.registerListener((SensorEventListener) this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        // 为系统的重力传感器注册监听器
        mSensorManager.registerListener((SensorEventListener) this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_GAME);
        // 为系统的线性加速度传感器注册监听器
        mSensorManager.registerListener((SensorEventListener) this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_GAME);
        // 为系统的温度传感器注册监听器
        mSensorManager.registerListener((SensorEventListener) this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_GAME);
        // 为系统的光传感器注册监听器
        mSensorManager.registerListener((SensorEventListener) this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_GAME);
        // 为系统的压力传感器注册监听器
        mSensorManager.registerListener((SensorEventListener) this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener((SensorEventListener) this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mCalculateTimeTimer.cancel();
    }

    @Override
    protected void onStop()
    {
        // 程序退出时取消注册传感器监听器
        mSensorManager.unregisterListener((SensorEventListener) this);
        super.onStop();
    }
    @Override
    protected void onPause()
    {
        //KLog.d();

        write_node(light_off, red_node);
        write_node(light_off, green_node);
        write_node(light_off, blue_node);
        // 程序暂停时取消注册传感器监听器
        mSensorManager.unregisterListener((SensorEventListener) this);
        super.onPause();
    }
    // 以下是实现SensorEventListener接口必须实现的方法
    @Override
    // 当传感器精度改变时回调该方法。
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        float[] values = event.values;
        // 获取触发event的传感器类型
        int sensorType = event.sensor.getType();
        StringBuilder sb = null;
       /* File f  = new File(file + "/ECompass.txt");
        File f1  = new File(file + "/Gsensor.txt");
        File f2  = new File(file + "/Psensor.txt");*/
        // 判断是哪个传感器发生改变
        switch (sensorType)
        {
            // 方向传感器
            /*case Sensor.TYPE_ORIENTATION:
                sb = new StringBuilder();
                sb.append("绕Z轴转过的角度：");
                sb.append(values[0]);
                sb.append("\n绕X轴转过的角度：");
                sb.append(values[1]);
                sb.append("\n绕Y轴转过的角度：");
                sb.append(values[2]);
                etOrientation.setText(sb.toString());
                break;*/
            // 陀螺仪传感器
            case Sensor.TYPE_GYROSCOPE:
                for(int i=0;i<3;i++){
                    ngV[i]=values[i];
                }
                sb = new StringBuilder();
                sb.append("绕X轴旋转的角速度：");
                sb.append(values[0]);
                sb.append("\n绕Y轴旋转的角速度：");
                sb.append(values[1]);
                sb.append("\n绕Z轴旋转的角速度：");
                sb.append(values[2]);
                etGyro.setText(sb.toString());

                /*if(!isGsensorSaved) {
                    float[] values_Gsensor = event.values;
                    Log.d("ZSJ","values_Gsensor"+values_Gsensor);

                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
                        file = Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + "/Sensor";
                    } else {// 如果SD卡不存在，就保存到本应用的目录下
                        file = MainActivity.this.getFilesDir().getAbsolutePath()
                                + "/Sensor";
                    }

                    File dir = new File(file);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    try {
                        if (!f1.exists()) {
                            f1.createNewFile();
                        }
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        return;
                    }
                    if (values_Gsensor == null) {
                        //错误Gsensor存入
                        SharedPreferences preferences = getSharedPreferences("Gsensor_count", MODE_MULTI_PROCESS);
                        preferences.edit().putInt("Gsensor_count", Integer.parseInt("0"));
                        int count = preferences.getInt("Gsensor_count", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("Gsensor_count", ++count);
                        editor.apply();
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date1 = format1.format(new Date(System.currentTimeMillis()));
                        FileWriter pr = null;
                        try {

                            pr = new FileWriter(f1, true);
                            String s = date1 + " Log.e(" + "EMsensor" + "," + "Gsensor fail" + "次数"+ ")" + wakeupcount + " \n";
                            pr.write(s);//, 0, s.getBytes().length);
                        } catch (FileNotFoundException e) {

                            return;
                        } catch (IOException e) {

                            return;
                        } finally {
                            try {
                                pr.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                    isSaved = true;
                } else if (!isGSuccesSaved){

                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date1 = format1.format(new Date(System.currentTimeMillis()));
                    FileWriter pr = null;
                    try {

                        pr = new FileWriter(f1, true);
                        String s = date1 + " Log.e(" + "Gsensor" + "," + "Gsensor 成功调取第"+ wakeupcount + "次数"+ ")" +" \n";
                        pr.write(s);//, 0, s.getBytes().length);
                    } catch (FileNotFoundException e) {

                        return;
                    } catch (IOException e) {

                        return;
                    } finally {
                        try {
                            pr.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    isSuccesSaved = true;
                }*/

                break;
            // 磁场传感器
            case Sensor.TYPE_MAGNETIC_FIELD:

                for(int i=0;i<3;i++){
                    neV[i]=values[i];
                }
                sb = new StringBuilder();
                sb.append("X轴方向上的磁场强度：");
                sb.append(values[0]);
                sb.append("\nY轴方向上的磁场强度：");
                sb.append(values[1]);
                sb.append("\nZ轴方向上的磁场强度：");
                sb.append(values[2]);
                etMagnetic.setText(sb.toString());

                /*if(!isSaved) {
                    float[] values_EMsensor = event.values;

                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
                        file = Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + "/Sensor";
                    } else {// 如果SD卡不存在，就保存到本应用的目录下
                        file = MainActivity.this.getFilesDir().getAbsolutePath()
                                + "/Sensor";
                    }

                    File dir = new File(file);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    try {
                        if (!f.exists()) {
                            f.createNewFile();
                        }
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        return;
                    }
                    if (values_EMsensor == null) {
                        //错误EMsensor存入
                        SharedPreferences preferences = getSharedPreferences("EMsensor_count", MODE_MULTI_PROCESS);
                        preferences.edit().putInt("EMsensor_count", Integer.parseInt("0"));
                        int count = preferences.getInt("EMsensor_count", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("EMsensor_count", ++count);
                        editor.apply();
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date1 = format1.format(new Date(System.currentTimeMillis()));
                        FileWriter pr = null;
                        try {

                            pr = new FileWriter(f, true);
                            String s = date1 + " Log.e(" + "ECompass" + "," + "ECompass fail" + "次数"+ ")" + wakeupcount + " \n";
                            pr.write(s);//, 0, s.getBytes().length);
                        } catch (FileNotFoundException e) {

                            return;
                        } catch (IOException e) {

                            return;
                        } finally {
                            try {
                                pr.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                isSaved = true;
                } else if (!isSuccesSaved){

                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date1 = format1.format(new Date(System.currentTimeMillis()));
                    FileWriter pr = null;
                    try {

                        pr = new FileWriter(f, true);
                        String s = date1 + " Log.e(" + "ECompass" + "," + "ECompass 成功调取第"+ wakeupcount + "次数"+ ")" +" \n";
                        pr.write(s);//, 0, s.getBytes().length);
                    } catch (FileNotFoundException e) {

                        return;
                    } catch (IOException e) {

                        return;
                    } finally {
                        try {
                            pr.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    isSuccesSaved = true;
                }*/


                break;
            // 重力传感器
            case Sensor.TYPE_GRAVITY:
                sb = new StringBuilder();
                sb.append("X轴方向上的重力：");
                sb.append(values[0]);
                sb.append("\nY轴方向上的重力：");
                sb.append(values[1]);
                sb.append("\nZ方向上的重力：");
                sb.append(values[2]);
                etGravity.setText(sb.toString());
                /*if(!isPsensorSaved) {
                    float[] values_EMsensor = event.values;
                    Log.d("ZSJ","values_EMsensor"+values_EMsensor);

                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
                        file = Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + "/Sensor";
                    } else {// 如果SD卡不存在，就保存到本应用的目录下
                        file = MainActivity.this.getFilesDir().getAbsolutePath()
                                + "/Sensor";
                    }

                    File dir = new File(file);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    try {
                        if (!f2.exists()) {
                            f2.createNewFile();
                        }
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        return;
                    }
                    if (values_EMsensor == null) {
                        //错误EMsensor存入
                        SharedPreferences preferences = getSharedPreferences("Psensor_count", MODE_MULTI_PROCESS);
                        preferences.edit().putInt("Psensor_count", Integer.parseInt("0"));
                        int count = preferences.getInt("EMsensor_count", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("Psensor_count", ++count);
                        editor.apply();
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date1 = format1.format(new Date(System.currentTimeMillis()));
                        FileWriter pr = null;
                        try {

                            pr = new FileWriter(f2, true);
                            String s = date1 + " Log.e(" + "Psensor" + "," + "Psensor fail" + "次数"+ ")" + wakeupcount + " \n";
                            pr.write(s);//, 0, s.getBytes().length);
                        } catch (FileNotFoundException e) {

                            return;
                        } catch (IOException e) {

                            return;
                        } finally {
                            try {
                                pr.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                    isPsensorSaved = true;
                } else if (!isPSuccesSaved){

                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date1 = format1.format(new Date(System.currentTimeMillis()));
                    FileWriter pr = null;
                    try {

                        pr = new FileWriter(f2, true);
                        String s = date1 + " Log.e(" + "Psensor" + "," + "Psensor 成功调取第"+ wakeupcount + "次数"+ ")" +" \n";
                        pr.write(s);//, 0, s.getBytes().length);
                    } catch (FileNotFoundException e) {

                        return;
                    } catch (IOException e) {

                        return;
                    } finally {
                        try {
                            pr.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    isPSuccesSaved = true;
                }*/
                break;
            // 线性加速度传感器
            /*case Sensor.TYPE_LINEAR_ACCELERATION:
                sb = new StringBuilder();
                sb.append("X轴方向上的线性加速度：");
                sb.append(values[0]);
                sb.append("\nY轴方向上的线性加速度：");
                sb.append(values[1]);
                sb.append("\nZ轴方向上的线性加速度：");
                sb.append(values[2]);
                etLinearAcc.setText(sb.toString());
                break;
            // 温度传感器
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                sb = new StringBuilder();
                sb.append("当前温度为：");
                sb.append(values[0]);
                etTemerature.setText(sb.toString());
                break;*/
            // 光传感器
            case Sensor.TYPE_LIGHT:
                sb = new StringBuilder();
                sb.append("当前光的强度为：");
                sb.append(values[0]);
                etLight.setText(sb.toString());

                break;
            // 压力传感器
            /*case Sensor.TYPE_PRESSURE:
                sb = new StringBuilder();
                sb.append("当前压力为：");
                sb.append(values[0]);
                etPressure.setText(sb.toString());
                break;*/
            /*psensor*/
            case Sensor.TYPE_PROXIMITY:
                npV[0]=values[0];
                sb = new StringBuilder();
                sb.append("");
                sb.append(values[0]);
                psensor_add.setText(sb.toString());
                break;
        }
    }


    private void write_node(byte[] values, String path) {
        try{
            FileOutputStream fosFL = new FileOutputStream(path);


            fosFL.write(values);

            fosFL.flush();

            fosFL.close();

        } catch (Exception e) {
            //KLog.e(e.toString());
            //Log.d("ZSJ","e"+e);
            ledlog(false);
        }
    }



    private void startLed(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < 3; i++) {
                    if (i == 0) {
                        write_node(light_on, red_node);
                        write_node(light_off, green_node);
                        write_node(light_off, blue_node);
                    } else if (i==1) {
                        write_node(light_off, red_node);
                        write_node(light_on, green_node);
                        write_node(light_off, blue_node);
                    } else if (i==2) {
                        write_node(light_off, red_node);
                        write_node(light_off, green_node);
                        write_node(light_on, blue_node);
                    }
                    try {
                        sleep(15000);
                        if (isSaved) {
                            pm.goToSleep(SystemClock.uptimeMillis());
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(i == 2){
                        i = -1;
                    }
                }
            }
        }.start();
    }
    private boolean gslog(boolean b){
        String file=null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sensor";
        }else{
            file = MainActivity.this.getFilesDir().getAbsolutePath()+ "/Sensor";
        }
        File dir = new File(file);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File f = new File(file+"/Gsensor.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
        } catch (IOException e1) {
            return false;
        }
        if(b){
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date1 = format1.format(new Date(System.currentTimeMillis()));
            FileWriter pr =null;
            try {
                pr = new FileWriter(f,true);
                String s = date1 + " Log.d(" + "Gsensor" + "," + "Gsensor fail在唤醒第"+ wakeupcount + "次失败"+ ")"  + " \n";
                pr.write(s);//, 0, s.getBytes().length);
            } catch (FileNotFoundException e) {
                return false;
            }catch (IOException e) {
                return false;
            }finally{
                try {
                    pr.close();
                }catch(IOException e) {
                    return false;
                }
            }
        }else{
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date1 = format1.format(new Date(System.currentTimeMillis()));
            FileWriter pr =null;
            try {
                pr = new FileWriter(f,true);
                //String s=date1+" Log.d("+"Gsensor"+","+"Gsensor pass"+")"+" \n";
                String s = date1 + " Log.d(" + "Gsensor" + "," + "Gsensor pass在唤醒第"+ wakeupcount + "次成功"+ ")"  + " \n";
                pr.write(s);//, 0, s.getBytes().length);
            } catch (FileNotFoundException e) {
                return false;
            }catch (IOException e) {
                return false;
            }finally{
                try{
                    pr.close();
                }catch(IOException e) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean pslog(boolean b){
        String file=null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sensor";
        }else{
            file = MainActivity.this.getFilesDir().getAbsolutePath()+ "/Sensor";
        }
        File dir = new File(file);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File f = new File(file+"/Psensor.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
        } catch (IOException e1) {
            return false;
        }
        if(b){
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date1 = format1.format(new Date(System.currentTimeMillis()));
            FileWriter pr =null;
            try {
                pr = new FileWriter(f,true);
                //String s=date1+" Log.e("+"Psensor"+","+"Psensor fail"+")"+" \n";
                String s = date1 + " Log.d(" + "Psensor" + "," + "Psensor fail在唤醒第"+ wakeupcount + "次失败"+ ")"  + " \n";
                pr.write(s);//, 0, s.getBytes().length);
            } catch (FileNotFoundException e) {
                return false;
            }catch (IOException e) {
                return false;
            }finally{
                try {
                    pr.close();
                }catch(IOException e) {
                    return false;
                }
            }
        }else{
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date1 = format1.format(new Date(System.currentTimeMillis()));
            FileWriter pr =null;
            try {
                pr = new FileWriter(f,true);
                //String s=date1+" Log.d("+"Psensor"+","+"Psensor pass"+")"+" \n";
                String s = date1 + " Log.d(" + "Psensor" + "," + "Psensor pass在唤醒第" + "次成功"+ ")" + wakeupcount + " \n";
                pr.write(s);//, 0, s.getBytes().length);
            } catch (FileNotFoundException e) {
                return false;
            }catch (IOException e) {
                return false;
            }finally{
                try{
                    pr.close();
                }catch(IOException e) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean eslog(boolean b){
        String file=null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sensor";
        }else{
            file = MainActivity.this.getFilesDir().getAbsolutePath()+ "/Sensor";
        }
        File dir = new File(file);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File f = new File(file+"/ECompass.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
        } catch (IOException e1) {
            return false;
        }
        if(b){
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date1 = format1.format(new Date(System.currentTimeMillis()));
            FileWriter pr =null;
            try {
                pr = new FileWriter(f,true);
                //String s=date1+" Log.e("+"ECompass"+","+"ECompass fail"+")"+" \n";
                String s = date1 + " Log.d(" + "ECompass" + "," + "ECompass fail在唤醒第" + wakeupcount + "次失败"+ ")"  + " \n";
                pr.write(s);//, 0, s.getBytes().length);
            } catch (FileNotFoundException e) {
                return false;
            }catch (IOException e) {
                return false;
            }finally{
                try {
                    pr.close();
                }catch(IOException e) {
                    return false;
                }
            }
        }else{
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date1 = format1.format(new Date(System.currentTimeMillis()));
            FileWriter pr =null;
            try {
                pr = new FileWriter(f,true);
                //String s=date1+" Log.d("+"ECompass"+","+"ECompass pass"+")"+" \n";
                String s = date1 + " Log.d(" + "ECompass" + "," + "ECompass pass在唤醒第" + wakeupcount+ "次成功"+ ")"  + " \n";
                pr.write(s);//, 0, s.getBytes().length);
            } catch (FileNotFoundException e) {
                return false;
            }catch (IOException e) {
                return false;
            }finally{
                try{
                    pr.close();
                }catch(IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean ledlog(boolean b){
        String led=null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            led = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Sensor/"+"Led/";
        }else{
            led = MainActivity.this.getFilesDir().getAbsolutePath()+ "/Sensor/"+"Led/";
        }
        led=led+"Led.txt";
        File f = new File(led);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
        } catch (IOException e1) {
            return false;
        }
        if(b){
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date1 = format1.format(new Date(System.currentTimeMillis()));
            FileWriter pr =null;
            try {
                pr = new FileWriter(f,true);
                String s = date1 + " Log.d(" + "LED" + "," + "Led fail在唤醒第"+ wakeupcount + "次失败"+ ")"  + " \n";
                pr.write(s);//, 0, s.getBytes().length);
            } catch (FileNotFoundException e) {
                return false;
            }catch (IOException e) {
                return false;
            }finally{
                try {
                    pr.close();
                }catch(IOException e) {
                    return false;
                }
            }
        }else{
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date1 = format1.format(new Date(System.currentTimeMillis()));
            FileWriter pr =null;
            try {
                pr = new FileWriter(f,true);
                //String s=date1+" Log.d("+"Gsensor"+","+"Gsensor pass"+")"+" \n";
                String s = date1 + " Log.d(" + "LED" + "," + "LED pass在唤醒第"+ wakeupcount + "次成功"+ ")"  + " \n";
                pr.write(s);//, 0, s.getBytes().length);
            } catch (FileNotFoundException e) {
                return false;
            }catch (IOException e) {
                return false;
            }finally{
                try{
                    pr.close();
                }catch(IOException e) {
                    return false;
                }
            }
        }
        return true;
    }
}
