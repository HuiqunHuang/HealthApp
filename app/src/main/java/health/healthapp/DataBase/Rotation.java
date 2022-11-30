package health.healthapp.DataBase;

public class Rotation {
    private float Rotation_X,Rotation_Y,Rotation_Z;
    private int Rotation_AddTime;


    public float getRotation_X(){
        return Rotation_X;
    }
    public void setRotation_X(float Rotation_X){
        this.Rotation_X=Rotation_X;
    }
    public float getRotation_Y(){
        return Rotation_Y;
    }
    public void setRotation_Y(float Rotation_Y){
        this.Rotation_Y=Rotation_Y;
    }
    public float getRotation_Z(){
        return Rotation_Z;
    }
    public void setRotation_Z(float Rotation_Z){
        this.Rotation_Z=Rotation_Z;
    }
    public int getAddTime(){
        return Rotation_AddTime;
    }
    public void setAddTime(int Rotation_AddTime){
        this.Rotation_AddTime=Rotation_AddTime;
    }
}
