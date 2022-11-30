package health.healthapp.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataBaseOperation{
    public MyDataBaseHelper myDataBaseHelper;
    private UserInfo userInfo;
    private Acc acc;
    public Step step;
    HeartRate heartrate;
    private BloodPressure bloodpressure;
    private Context mContext;
    public DataBaseOperation(Context context) {
        myDataBaseHelper = new MyDataBaseHelper(context,"healthapp.db",null,2);
        mContext=context;
    }

    public void Insert_XYZ(int operation,float x,float y,float z) {// 插入记录
        SQLiteDatabase db= myDataBaseHelper.getWritableDatabase();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date(System.currentTimeMillis());
        //String time = sDateFormat.format(now);
        switch(operation)
        {
            case 0://插入一条acc数据
                db.execSQL("insert into Tab_Acc (Acc_X,Acc_Y,Acc_Z,Acc_AddTime) values('"+x+"','"+y+"','"+z+"','"+now+"')");
                break;
            case 1://插入一条gyro数据
                db.execSQL("insert into Tab_Gyro (Gyro_X,Gyro_Y,Gyro_Z,Gyro_AddTime)values('"+x+"','"+y+"','"+z+"','"+now+"')");
                break;
            case 2://插入一条rotation数据
                db.execSQL("insert into Tab_Rotation (Rotation_X,Rotation_Y,Rotation_Z,Rotation_AddTime) values('"+x+"','"+y+"','"+z+"','"+now+"')");
                break;
            case 3://插入一条gravity数据
                db.execSQL("insert into Tab_Gravity (Gravity_X,Gravity_Y,Gravity_Z,Gravity_AddTime) values('"+x+"','"+y+"','"+z+"','"+now+"')");
                break;
            default:
                break;
        }
        db.close();// 记得关闭数据库操作
    }

    public void Insert_Gps(float longitude,float latitude,float altitude,float speed){
        SQLiteDatabase db= myDataBaseHelper.getWritableDatabase();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date(System.currentTimeMillis());
        //String time = sDateFormat.format(now);
        db.execSQL("insert into Tab_Gps (Longitude,Latitude,Altitude,Speed,Gps_AddTime) values('"+longitude+"','"+latitude+"','"+altitude+"','"+speed+"','"+now+"')");
        db.close();
    }

    public void Insert_Single(int operation,float value){
        SQLiteDatabase db= myDataBaseHelper.getWritableDatabase();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date(System.currentTimeMillis());
       // String time = sDateFormat.format(now);

        switch(operation)
        {
            case 0://亮度
                db.execSQL("insert into Tab_Light (Light,Light_AddTime) values('"+value+"','"+now+"')");
                break;
            case 1://距离
                db.execSQL("insert into Tab_Proximity (Distance,AddTime) values('"+value+"','"+now+"')");
                break;
            case 2://心率
                db.execSQL("insert into Tab_HeartRate (HeartRate,AddTime) values('"+value+"','"+now+"')");
                break;
            case 3://血压
                db.execSQL("insert into Tab_BloodPressure (BloodPressure,AddTime) values('"+value+"','"+now+"')");
                break;
            default:
                break;
        }
        db.close();
    }

    //type  0：步行 aq：跑步 2：骑行 3：爬高
    public void Insert_Step(int step,int type,int calorie,float distance,float step_length,float speed,float stride_rate){
        SQLiteDatabase db= myDataBaseHelper.getWritableDatabase();
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int t=year*10000+month*100+day;
        if(find_Step(t,type)==null)
            db.execSQL("insert into Tab_Step (Step,Date,Calorie,Type,distance,step_length,speed,stride_rate) values('"+step+"','"+t+"','"+calorie+"','"+type+"','"+distance+"','"+step_length+"','"+speed+"','"+stride_rate+"')");
        else
           //Update_Step(step,type,t);

        db.close();
    }

    public void Update_UserInfo(float height,float weight,int birthyear,int birthmonth,int birthday,String UserId,String Password) {
        SQLiteDatabase db= myDataBaseHelper.getWritableDatabase();
        if(find_User_Info(UserId)==null){
            db.execSQL("insert into Tab_User_Info (Height,Weight,BirthYear,BirthMonth,BirthDay,UserId,Password) values('"+height+"','"+weight+"','"+birthyear+"','"+birthmonth+"','"+birthday+"','"+UserId+"','"+Password+"')");
        }else {
            db.execSQL("update Tab_User_Info set Height=?,Weight=?,BirthYear=?,BirthMonth=?,BirthDay=?,Password=?,where" + " UserId=?", new Object[]{height, weight, birthyear, birthmonth, birthday, Password, UserId});
        }
        db.close();
    }

    //根据valuetype确定要更新的Step表格信息
    public void Update_Tab_Step(int valuetype,int step,int type,int date,double calorie,float distance,float step_length,float speed,float stride_rate) {//约定在其他类调用更新数据库语句时，应取具体的数值，而非在数据库查询后的数值
        SQLiteDatabase db= myDataBaseHelper.getWritableDatabase();
        if(find_Step(date,type)==null)
            db.execSQL("insert into Tab_Step (Step,Date,Calorie,Type,distance,step_length,speed,stride_rate) values('"+step+"','"+date+"','"+calorie+"','"+type+"','"+distance+"','"+step_length+"','"+speed+"','"+stride_rate+"')");
        else {
            switch(valuetype) {
                case 0://更新步数
                    db.execSQL("update Tab_Step set Step=?,where" + " Type=? and Date=?", new Object[]{step, type, date});
                    break;
                case 1://更新大卡
                    db.execSQL("update Tab_Step set Calorie=?,where" + " Type=? and Date=?", new Object[] {calorie,type,date});
                    break;
                case 2:
                    db.execSQL("update Tab_Step set distance=?,step_length=?,speed=?,stride_rate=?,where" + " Type=? and Date=?", new Object[] {distance,step_length,speed,stride_rate,type,date});
                    break;
                default:
                    break;
            }
        }
        Toast.makeText(mContext, "Create succeeded!", Toast.LENGTH_SHORT).show();
        db.close();
    }


    public List<HeartRate> findAllHeartRate() {// 查询所有记录
        SQLiteDatabase db= myDataBaseHelper.getReadableDatabase();
        List<HeartRate> lists = new ArrayList<HeartRate>();
        Cursor cursor = db.rawQuery("select * from Tab_HeartRate ", null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            heartrate = new HeartRate();
            heartrate.setHeartRate(cursor.getFloat(cursor.getColumnIndex("HeartRate")));
            heartrate.setAddTime(cursor.getInt(cursor.getColumnIndex("AddTime")));
            lists.add(heartrate);
        }
        db.close();
        return lists;
    }

    public List<BloodPressure> findAllBloodPressure() {// 查询所有记录
        SQLiteDatabase db= myDataBaseHelper.getReadableDatabase();
        List<BloodPressure> lists = new ArrayList<BloodPressure>();
        Cursor cursor = db.rawQuery("select * from Tab_BloodPressure ", null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            bloodpressure = new BloodPressure();
            bloodpressure.setBloodPressure(cursor.getFloat(cursor.getColumnIndex("BloodPressure")));
            bloodpressure.setAddTime(cursor.getInt(cursor.getColumnIndex("AddTime")));
            lists.add(bloodpressure);
        }
        db.close();
        return lists;
    }

    public UserInfo find_User_Info(String id) {// 根据ID查找纪录
        SQLiteDatabase db= myDataBaseHelper.getReadableDatabase();
        // 用游标Cursor接收从数据库检索到的数据
        Cursor cursor = db.rawQuery("select * from Tab_User_Info where UserId=?", new String[] { id });
        if(cursor!=null)
        while (cursor.moveToFirst()) {// 依次取出数据
            userInfo = new UserInfo();
            userInfo.setBirthYear(cursor.getInt(cursor.getColumnIndex("BirthYear")));
            userInfo.setBirthMonth(cursor.getInt(cursor.getColumnIndex("BirthMonth")));
            userInfo.setBirthDay(cursor.getInt(cursor.getColumnIndex("BirthDay")));
            userInfo.setHeight(cursor.getInt(cursor.getColumnIndex("Height")));
            userInfo.setWeight(cursor.getInt(cursor.getColumnIndex("Weight")));
            userInfo.setPassword(cursor.getString(cursor.getColumnIndex("Password")));

        }
        db.close();
        return userInfo;
    }

    public Step find_Step(int date,int type) {
        SQLiteDatabase db= myDataBaseHelper.getReadableDatabase();
        // 用游标Cursor接收从数据库检索到的数据
        String t=date+"";
        String tt=type+"";
        Cursor cursor = db.rawQuery("select * from Tab_Step where Date=? and Type=?", new String[] { t,tt });
        if(cursor!=null)
        while (cursor.moveToFirst()) {// 依次取出数据
            step = new Step();
            step.setStep(cursor.getInt(cursor.getColumnIndex("Step")));
            step.setCalorie((cursor.getInt(cursor.getColumnIndex("Calorie"))));
            step.setDistance((cursor.getFloat(cursor.getColumnIndex("distance"))));
            step.setSpeed((cursor.getFloat(cursor.getColumnIndex("speed"))));
            step.setStep_length((cursor.getFloat(cursor.getColumnIndex("step_length"))));
            step.setStride_rate((cursor.getFloat(cursor.getColumnIndex("stride_rate"))));
        }
        db.close();
        return step;
    }
}
