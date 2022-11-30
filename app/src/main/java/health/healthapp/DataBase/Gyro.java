package health.healthapp.DataBase;

public class Gyro {
    private float Gyro_X,Gyro_Y,Gyro_Z;
    private int Gyro_AddTime;

    public float getGyro_X(){
        return Gyro_X;
    }
    public void setGyro_X(float Gyro_X){
        this.Gyro_X=Gyro_X;
    }
    public float getGyro_Y(){
        return Gyro_Y;
    }
    public void setGyro_Y(float Gyro_Y){
        this.Gyro_Y=Gyro_Y;
    }
    public float getGyro_Z(){
        return Gyro_Z;
    }
    public void setGyro_Z(float Gyro_Z){
        this.Gyro_Z=Gyro_Z;
    }
    public int getAddTime(){
        return Gyro_AddTime;
    }
    public void setAddTime(int Gyro_AddTime){
        this.Gyro_AddTime=Gyro_AddTime;
    }
}
