package health.healthapp.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Choreographer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.GraphicalView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import health.healthapp.DataBase.DataBaseOperation;
import health.healthapp.DataBase.MyDataBaseHelper;
import health.healthapp.DataBase.SensorDataOperation;
import health.healthapp.Fragment.HealthFragment;
import health.healthapp.Fragment.MainFragment;
import health.healthapp.Fragment.MineFragment;
import health.healthapp.R;
import health.healthapp.Service.SensorRequestService;

public class MainActivity extends FragmentActivity implements OnClickListener{
    private MainFragment frag_main;
    private HealthFragment frag_health;
    private MineFragment frag_mine;
    private Button btn_main;
    private Button btn_health;
    private Button btn_mine;
    private SensorRequestService.MyBinder myBinder;
    SensorManager sm;
    private LocationManager lm;
    public static SensorDataOperation sensorDataOperation;
    public DataBaseOperation dataBaseOperation;
    private Intent startIntent;
    Fragment frag;
    //private SensorRequestService sensorRequestService;
    private Thread mThread;
    private Thread sensorThread;
    private int timer = 0;
    private Handler handler;
    private MyDataBaseHelper myDataBaseHelper;
    float value[];
    int type;
    public static double walkCalorie=0,runCalorie=0,rideCalorie=0,climbCalorie=0;
    public int walk_step=0,run_step=0,climb_step=0;
    public float walk_dis=0,walk_steplength=0,walk_speed=0,walk_striderate=0;
    public float run_dis=0,run_steplength=0,run_speed=0,run_striderate;
    public float ride_dis=0,ride_speed=0;
    public float climb_speed=0,climb_striderate=0,climb_steplength=0,climb_dis=0;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(sensorDataOperation==null)
            sensorDataOperation=new SensorDataOperation();
        if(dataBaseOperation==null)
            dataBaseOperation=new DataBaseOperation(MainActivity.this);
        btn_main = (Button) findViewById(R.id.btn_main);
        btn_health = (Button) findViewById(R.id.btn_health);
        btn_mine = (Button) findViewById(R.id.btn_mine);
        btn_main.setOnClickListener(this);
        btn_health.setOnClickListener(this);
        btn_mine.setOnClickListener(this);
        calendar = Calendar.getInstance();
        setDefaultFragment();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!isGpsAble(lm)) {
            Toast.makeText(MainActivity.this, "请打开GPS,否则该应用将无法正常收集运动数据。", Toast.LENGTH_SHORT).show();
            openGPS2();
        }
        start_sensor_service();//开启前台服务，手机传感器数据

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String s=msg.obj+"";
                switch (msg.what) {
                    case 0:
                        frag_main.setText_walkstep(s+"步");
                        break;
                    case 2:
                        frag_main.setText_walkcalorie(s+"cal");
                        break;
                    case 3:
                        frag_main.setText_rundis(s+"m");
                        break;
                    case 4:
                        frag_main.setText_runcalorie(s+"cal");
                        break;
                    case 5:
                        frag_main.setText_ridedis(s+"m");
                        break;
                    case 6:
                        frag_main.setText_ridecalorie(s+"cal");
                        break;
                    case 7:
                        frag_main.setText_climbdis(s+"m");
                        break;
                    case 8:
                        frag_main.setText_climbcalorie(s+"cal");
                        break;
                    default:
                        break;
                }
            }
        };
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        walk_step_operate();
                        walk_calorie_operate();
                        getdata();
                        run_dis_operate();
                        run_calorie_operate();
                        ride_dis_operate();
                        ride_calorie_operate();
                        climb_dis_operate();
                        climb_calorie_operate();
                        Thread.currentThread().sleep(2000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        mThread.start();

        sensorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        type=SensorRequestService.sensorType();
                        value=SensorRequestService.returnvalue();
                        Thread.currentThread().sleep(5000);
                        Message message = new Message();
                        message.obj = type;
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        //sensorThread.start();
    }



    private boolean isGpsAble(LocationManager lm) {
        return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ? true : false;
    }


    //打开设置页面让用户自己设置
    private void openGPS2() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, 0);
    }

    //设置默认fragment为MainFragment
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        frag_main = new MainFragment();
        frag=frag_main;
        ft.replace(R.id.fragment_main, frag_main);//首页
        ft.commit();

    }

    //根据按钮下标切换fragment
    @Override
    public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();//开启fragment事务
        restartButton();
        switch (v.getId()) {
            case R.id.btn_main://首页
                if (frag_main == null)
                    frag_main = new MainFragment();
                frag=frag_main;
                btn_main.setTextColor(getResources().getColor(R.color.dimgray));
                ft.replace(R.id.fragment_main, frag_main);
                break;
            case R.id.btn_health://健康
                if (frag_health == null)
                    frag_health = new HealthFragment();
                frag=frag_health;
                //DatabaseOperate databaseOperate=new DatabaseOperate(this.getBaseContext());
                //int y=databaseOperate.Insert_Single(3,6);
                btn_health.setTextColor(getResources().getColor(R.color.dimgray));
                ft.replace(R.id.fragment_main, frag_health);
                break;
            case R.id.btn_mine://我的
                if (frag_mine == null)
                    frag_mine = new MineFragment();
                frag=frag_mine;
                btn_mine.setTextColor(getResources().getColor(R.color.dimgray));
                ft.replace(R.id.fragment_main, frag_mine);
                break;
        }
        ft.commit();//提交事务
    }

    //将三个按钮颜色变为黑色
    private void restartButton() {
        btn_main.setTextColor(getResources().getColor(R.color.darkgray));
        btn_health.setTextColor(getResources().getColor(R.color.darkgray));
        btn_mine.setTextColor(getResources().getColor(R.color.darkgray));
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (SensorRequestService.MyBinder) service;
             //Step_SetText();
        }
    };

    private void start_sensor_service() {
        if(startIntent==null) {
            startIntent = new Intent(MainActivity.this, SensorRequestService.class);
            startService(startIntent);
            bindService(startIntent, connection, BIND_AUTO_CREATE);
            //Toast.makeText(MainActivity.this, "开始收集数据~", Toast.LENGTH_SHORT).show();
        }
    }

    private void stop_sensor_service(){
        Intent stopIntent = new Intent(MainActivity.this, SensorRequestService.class);
        stopService(stopIntent);
        unbindService(connection);
        Toast.makeText(MainActivity.this, "已停止收集数据", Toast.LENGTH_SHORT).show();
    }

    private void Step_SetText(int sensor_type,float sensor_values[]){
        switch(sensor_type){
            case 0://acc
                //Toast.makeText(MainActivity.this, "jiasudu", Toast.LENGTH_SHORT).show();
                break;
            case 1://gyro
                break;
            case 2://light
                break;
            case 3://proximity
                break;
            case 4://rotation
                break;
            case 6:
                //Toast.makeText(MainActivity.this, "6", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        Location lc = SensorRequestService.return_location();
    }


    //步行步数文本框操作
    public void walk_step_operate(){
        int type=SensorRequestService.sensorType();
        float value[]=SensorRequestService.returnvalue();
        Message message = new Message();
        int t=calendar.get(Calendar.YEAR)*10000+calendar.get(Calendar.MONTH)*100+calendar.get(Calendar.DAY_OF_MONTH);
        //int valuetype,int step,int type,int date,int calorie,float distance,float step_length,float speed,float stride_rate
        //dataBaseOperation.Update_Tab_Step(0,walk_step,0,t,sensorDataOperation.calculate_calorie(0,walk_step,78,walk_speed,walk_dis),walk_dis,walk_steplength,walk_speed,walk_striderate);
       // if(dataBaseOperation.find_Step(t,0)!=null)
           // message.obj=dataBaseOperation.find_Step(t,0).getStep();//根据日期查询步数
        //else
           // message.obj=0;
        message.obj=walk_step;
        message.what = 0;
        handler.sendMessage(message);
    }

    //步行消耗热量文本框操作
    public void walk_calorie_operate(){
        Message message = new Message();
        int t=calendar.get(Calendar.YEAR)*10000+calendar.get(Calendar.MONTH)*100+calendar.get(Calendar.DAY_OF_MONTH);
        //int valuetype,int step,int type,int date,int calorie,float distance,float step_length,float speed,float stride_rate
       // dataBaseOperation.Update_Tab_Step(1,sensorDataOperation.step,0,t,walkCalorie,walk_dis,walk_steplength,walk_speed,walk_striderate);
       // if(dataBaseOperation.find_Step(t,0)!=null)
            //message.obj=dataBaseOperation.find_Step(t,0).getCalorie();//根据日期查询热量
        //else
            //message.obj=0;
        message.obj=runCalorie;
        message.what = 2;
        handler.sendMessage(message);
    }

    //跑步距离文本框操作
    public void run_dis_operate(){
        Message message = new Message();
        int t=calendar.get(Calendar.YEAR)*10000+calendar.get(Calendar.MONTH)*100+calendar.get(Calendar.DAY_OF_MONTH);
        message.obj=sensorDataOperation.run_distance;
        message.what=3;
        handler.sendMessage(message);
    }

    //跑步消耗热量文本框操作
    public void run_calorie_operate(){
        Message message = new Message();
        int t=calendar.get(Calendar.YEAR)*10000+calendar.get(Calendar.MONTH)*100+calendar.get(Calendar.DAY_OF_MONTH);
        message.obj=runCalorie;
        message.what=4;
        handler.sendMessage(message);
    }

    //骑行距离文本框操作
    public void ride_dis_operate(){
        Message message = new Message();
        int t=calendar.get(Calendar.YEAR)*10000+calendar.get(Calendar.MONTH)*100+calendar.get(Calendar.DAY_OF_MONTH);
        message.obj=ride_dis;
        message.what=5;
        handler.sendMessage(message);
    }

    //骑行消耗热量文本框操作
    public void ride_calorie_operate(){
        Message message = new Message();
        int t=calendar.get(Calendar.YEAR)*10000+calendar.get(Calendar.MONTH)*100+calendar.get(Calendar.DAY_OF_MONTH);
        message.obj=rideCalorie;
        message.what=6;
        handler.sendMessage(message);
    }

    //爬高距离文本框操作
    public void climb_dis_operate(){
        Message message = new Message();
        int t=calendar.get(Calendar.YEAR)*10000+calendar.get(Calendar.MONTH)*100+calendar.get(Calendar.DAY_OF_MONTH);
        message.obj=climb_dis;
        message.what=7;
        handler.sendMessage(message);
    }

    //爬高消耗热量文本框操作
    public void climb_calorie_operate(){
        Message message = new Message();
        int t=calendar.get(Calendar.YEAR)*10000+calendar.get(Calendar.MONTH)*100+calendar.get(Calendar.DAY_OF_MONTH);
        message.obj=climbCalorie;
        message.what=8;
        handler.sendMessage(message);
    }

    private void getdata(){
        walk_speed=sensorDataOperation.walk_speed;
        walk_dis=sensorDataOperation.walk_distance;
        walk_steplength=sensorDataOperation.walk_step_length;
        walk_striderate=sensorDataOperation.walk_stride_rate;
        walk_step=sensorDataOperation.walk_step;
        walkCalorie=(int)sensorDataOperation.calculate_calorie(0,walk_step,78,walk_speed,walk_dis);
        run_step=sensorDataOperation.run_step;
        run_speed=sensorDataOperation.run_speed;
        run_dis=sensorDataOperation.run_distance;
        run_steplength=sensorDataOperation.run_step_length;
        run_striderate=sensorDataOperation.run_stride_rate;
        runCalorie=(int)sensorDataOperation.calculate_calorie(1,run_step,23,run_speed,run_dis);
        ride_dis=sensorDataOperation.ride_distance;
        ride_speed=sensorDataOperation.ride_speed;
        rideCalorie=(int)sensorDataOperation.calculate_calorie(2,sensorDataOperation.step,23,ride_speed,ride_dis);
        climb_speed=sensorDataOperation.climb_speed;
        climb_step=sensorDataOperation.climb_step;
        climb_steplength=sensorDataOperation.climb_step_length;
        climb_striderate=sensorDataOperation.climb_striderate;
        climb_dis=sensorDataOperation.climb_distance;
        climbCalorie=(int)sensorDataOperation.calculate_calorie(3,climb_step,23,climb_speed,77);
    }
    public static SensorDataOperation returnSensorDataOperation(){
        return sensorDataOperation;
    }

    public static double returnCalorie(int type){
        switch (type){
            case 0:
                return walkCalorie;
            case 1:
                return runCalorie;
            case 2:
                return rideCalorie;
            case 3:
                return climbCalorie;
            default:
                return 0;
        }
    }
}

