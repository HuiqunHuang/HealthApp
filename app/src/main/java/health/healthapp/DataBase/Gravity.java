package health.healthapp.DataBase;

public class Gravity {
    private float Gravity_X,Gravity_Y,Gravity_Z;
    private int AddTime;

    public float getGravity_X(){
        return Gravity_X;
    }
    public void setGravity_X(float Gravity_X){
        this.Gravity_X=Gravity_X;
    }
    public float getGravity_Y(){
        return Gravity_Y;
    }
    public void setGravity_Y(float Gravity_Y){
        this.Gravity_Y=Gravity_Y;
    }
    public float getGravity_Z(){
        return Gravity_Z;
    }
    public void setGravity_Z(float Gravity_Z){
        this.Gravity_Z=Gravity_Z;
    }
    public int getAddTime(){
        return AddTime;
    }
    public void setAddTime(int AddTime){
        this.AddTime=AddTime;
    }
}
