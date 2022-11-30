package health.healthapp.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import health.healthapp.DataBase.SensorDataOperation;
import health.healthapp.R;

public class SensorRequestActivity extends Activity implements SensorEventListener{
    SensorManager sm = null;
    private LocationManager lm;
    private SensorDataOperation sensorDataOperation;
    private TextView tx_step;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        setContentView(R.layout.activity_main);

        tx_step=(TextView)findViewById(R.id.stepp);
        sensorDataOperation=new SensorDataOperation();
        //传感器接口的声明和调用
        Sensor accS = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); //加速度
        sm.registerListener( SensorRequestActivity.this, accS, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor gyroS = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);//陀螺仪
        sm.registerListener(SensorRequestActivity.this, gyroS, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor lightS = sm.getDefaultSensor(Sensor.TYPE_LIGHT);//光线
        sm.registerListener(SensorRequestActivity.this, lightS, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor distanceS = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);//距离
        sm.registerListener(SensorRequestActivity.this, distanceS, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor gravityS = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);//重力
        sm.registerListener(SensorRequestActivity.this, gravityS, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor RotationS =sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);//旋转矢量
        sm.registerListener(SensorRequestActivity.this,RotationS,SensorManager.SENSOR_DELAY_NORMAL);
        Sensor step = sm.getDefaultSensor((Sensor.TYPE_STEP_COUNTER));

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!isGpsAble(lm)) {
            Toast.makeText(SensorRequestActivity.this, "请打开GPS", Toast.LENGTH_SHORT).show();
            openGPS2();
        }
        //从GPS获取最近的定位信息
        try {
            //需要权限的操作
            //从GPS获取最近的定位信息
            Location lc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            updateShow(lc);

            //设置间隔五秒获得一次GPS定位信息
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 8, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // 当GPS定位信息发生改变时，更新定位
                    updateShow(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {
                    try {
                        updateShow(lm.getLastKnownLocation(provider));
                    }catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onProviderDisabled(String provider) {
                    updateShow(null);
                }
            });

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    //更新gps信息
    private void updateShow(Location location) {
        if (location != null) {
            //locationgps.setText("\n当前的位置信息：\n" + "经度：" + location.getLongitude() + "\n" + "纬度：" + location.getLatitude() + "\n" + "高度：" + location.getAltitude() + "\n" + "速度：" + location.getSpeed() + "\n" + "方向：" + location.getBearing() + "\n" + "定位精度：" + location.getAccuracy() + "\n");
        }
    }


    private boolean isGpsAble(LocationManager lm) {
        return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ? true : false;
    }


    //打开设置页面让用户自己设置
    private void openGPS2() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, 0);
    }

    protected void onResume() {
        super.onResume();
    }

    //暂停捕获传感器信息
    protected void onPause() {
        super.onPause();
    }

    //重写变化
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        float mGravity = 9.80665F;
        //Log.d("linc", "value size: " + event.values.length);
        float xValue = event.values[0];// Acceleration minus Gx on the x-axis
        float yValue = event.values[1];//Acceleration minus Gy on the y-axis
        float zValue = event.values[2];//Acceleration minus Gz on the z-axis

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date(System.currentTimeMillis());
        String time = sDateFormat.format(now);

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            sensorDataOperation.step(xValue,yValue,zValue);
            //   tx_step.setText(sensorDataOperate.step);
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

        } else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {

        }else if(event.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR){

        }

    }
}
