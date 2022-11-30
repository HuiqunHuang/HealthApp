package health.healthapp.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;
import android.widget.Toast;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    public  Context mcontext;
    public static final String CREATE_ACC = "CREATE TABLE Acc(Id integer primary key,Acc_X float,Acc_Y float,Acc_Z float,Acc_AddTime int)";
    public static final String CREATE_GPS = "CREATE TABLE  Gps (Id integer primary key,Longitude float,Latitude float,Altitude float,Speed float,Gps_AddTime int)";
    public static final String CREATE_GYRO = "CREATE TABLE Gyro (Id integer primary key,Gyro_X float,Gyro_Y float,Gyro_Z float,Gyro_AddTime int)";
    public static final String CREATE_LIGHT = "CREATE TABLE  Light (Id integer primary key,Light float,Light_AddTime int)";
    public static final String CREATE_ROTATION = "CREATE TABLE  Rotation (Id integer primary key,Rotation_X float,Rotation_Y float,Rotation_Z float,Rotation_AddTime int)";
    public static final String CREATE_PROXIMITY ="CREATE TABLE  Proximity(Id integer primary key,Distance float,AddTime int)";
    public static final String CREATE_GRAVITY ="CREATE TABLE Gravity(Id integer primary key,Gravity_X float,Gravity_Y float,Gravity_Z float,AddTime int)";
    public static final String CREATE_STEP="CREATE TABLE  Step(Id integer primary key,Step int,Date int,Calorie double,Type int,distance float,step_length float,speed float,stride_rate float)";
    public static final String CREATE_User_Info="CREATE TABLE  User_Info(Id integer primary key,Height float,Weight float,BirthYear int,BirthMonth int,BirthDay int,UserId varchar(32),Password varchar(32))";
    public static final String CREATE_HeartRate="CREATE TABLE HeartRate(Id integer primary key,HeartRate float,AddTime int)";
    public static final String CREATE_BloodPressure="CREATE TABLE BloodPressure(Id integer primary key,BloodPressure float,AddTime int)";

    private TextView ii;

    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version)
    {
        super(context, name, cursorFactory, version);
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //db.execSQL(CREATE_ACC);
        //db.execSQL(CREATE_GPS);
        //db.execSQL(CREATE_GYRO);
        //db.execSQL(CREATE_LIGHT);
        //db.execSQL(CREATE_ROTATION);
        //db.execSQL(CREATE_PROXIMITY);
        //db.execSQL(CREATE_GRAVITY);
        db.execSQL(CREATE_STEP);
        db.execSQL(CREATE_User_Info);
        db.execSQL(CREATE_HeartRate);
        db.execSQL(CREATE_BloodPressure);

        Toast.makeText(mcontext, "Create succeeded!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //更改版本操作
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //Toast.makeText(mcontext, "Create succeeded!", Toast.LENGTH_SHORT).show();
        // TODO 每次成功打开数据库后首先被执行
    }
}

