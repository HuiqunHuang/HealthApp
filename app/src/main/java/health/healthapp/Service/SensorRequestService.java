package health.healthapp.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import health.healthapp.Activity.ClimbSubcoatActivity;
import health.healthapp.Activity.MainActivity;
import health.healthapp.Activity.RideSubcoatActivity;
import health.healthapp.Activity.RunSubcoatActivity;
import health.healthapp.Activity.SensorRequestActivity;
import health.healthapp.Activity.WalkSubcoatActivity;
import health.healthapp.DataBase.SensorDataOperation;
import health.healthapp.Fragment.HealthFragment;
import health.healthapp.R;


public class SensorRequestService extends Service implements SensorEventListener{
    private MyBinder mBinder = new MyBinder();
    NotificationManager mNotificationManager;
    Intent mIntent;
    PendingIntent mPendingIntent;
    Notification notification;
    SensorManager sm;
    private LocationManager lm;
    public static Location lc;
    public static float values[];
    public static int type=6;
    public double[] last_location;
    private static float[] result=new float[1];
    public static long last_location_time=0;
    //private MainActivity mainActivity;
    private SensorDataOperation sensorDataOperation;

    public void onCreate() {
        super.onCreate();
        if(last_location==null)
        {
            last_location=new double[2];
            last_location[0]=0;
            last_location[1]=0;
        }
        result[0]=-999;
        if(sm==null)
          sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorDataOperation=MainActivity.returnSensorDataOperation();

        //传感器接口的声明和调用
        Sensor accS = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); //加速度
        sm.registerListener(this, accS, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor gyroS = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);//陀螺仪
        sm.registerListener(this, gyroS, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor lightS = sm.getDefaultSensor(Sensor.TYPE_LIGHT);//光线
        sm.registerListener(this, lightS, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor distanceS = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);//距离
        sm.registerListener(this, distanceS, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor gravityS = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);//重力
        sm.registerListener(this, gravityS, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor RotationS = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);//旋转矢量
        sm.registerListener(this, RotationS, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor step = sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sm.registerListener(this, RotationS, SensorManager.SENSOR_DELAY_NORMAL);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!isGpsAble(lm)) {
            Toast.makeText(SensorRequestService.this, "请打开GPS", Toast.LENGTH_SHORT).show();
        }
        //从GPS获取最近的定位信息
        try {
            //需要权限的操作
            //从GPS获取最近的定位信息
            lc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //updateShow(lc);
            if(lc!=null) {
                last_location[0] = lc.getLatitude();
                last_location[1] = lc.getLongitude();
                last_location_time = System.currentTimeMillis();
            }
            //设置间隔五秒获得一次GPS定位信息
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 8, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // 当GPS定位信息发生改变时，更新定位
                    //updateShow(location);
                    lc=location;
                    if(lc!=null) {
                        calculate_dis(location);
                        last_location[0] = lc.getLatitude();
                        last_location[1] = lc.getLongitude();
                        last_location_time = System.currentTimeMillis();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {
                    try {
                        lc=lm.getLastKnownLocation(provider);
                        if(lc!=null) {
                            calculate_dis(lc);
                            last_location[0] = lc.getLatitude();
                            last_location[1] = lc.getLongitude();
                            last_location_time = System.currentTimeMillis();
                        }
                       // updateShow(lm.getLastKnownLocation(provider));
                    }catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onProviderDisabled(String provider) {
                    lc=null;
                }
            });

        } catch (SecurityException e) {
            e.printStackTrace();
        }

        mNotificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        mIntent = new Intent(this, MainActivity.class);

        mPendingIntent = PendingIntent.getActivity(this, 0,
                mIntent, 0);

        Notification.Builder builder = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("通知")
                .setContentText("正在后台收集传感器数据~")
                .setContentIntent(mPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true);
        notification=builder.getNotification();
       // mNotificationManager.notify(0, notification);
        startForeground(1, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(false);
        if(sm!=null) {
           sm.unregisterListener(this);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
        //return START_STICKY;
    }

    private boolean isGpsAble(LocationManager lm) {
        return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ? true : false;
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS ");
        String date = sDateFormat.format(new java.util.Date());
        values = event.values;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            sensorDataOperation.step(values[0],values[1],values[2]);
            type=0;
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            type=1;
        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            type=2;
        } else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            type=3;
        } else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            type=4;
        }
    }


    public void calculate_dis(Location location){
       // float[] result=new float[3];
        result[0]=-999;
        if(location!=null)
           Location.distanceBetween(last_location[0],last_location[1],lc.getLatitude(),lc.getLongitude(),result);
    }

    public static float return_dis(){
        return  result[0];
    }

    public static float return_speed(){
        long now = System.currentTimeMillis();
        return result[0]/((now-last_location_time)/1000+(now-last_location_time)%1000)+result[0]%((now-last_location_time)/1000+(now-last_location_time)%1000);
    }
    public  static float[] returnvalue(){
        return values;
    }

    public static int  sensorType(){
        return type;
    }

    public static Location return_location(){
        return lc;
    }

}

