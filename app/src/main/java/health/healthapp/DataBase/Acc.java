package health.healthapp.DataBase;

public class Acc {
    private float Acc_X,Acc_Y,Acc_Z;
    private int Acc_AddTime;


    public float getAcc_X(){
        return Acc_X;
    }
    public void setAcc_X(float Acc_X){
        this.Acc_X=Acc_X;
    }
    public float getAcc_Y(){
        return Acc_Y;
    }
    public void setAcc_Y(float Acc_Y){
        this.Acc_Y=Acc_Y;
    }
    public float getAcc_Z(){
        return Acc_Z;
    }
    public void setAcc_Z(float Acc_Z){
        this.Acc_Z=Acc_Z;
    }
    public int getAddTime(){
        return Acc_AddTime;
    }
    public void setAddTime(int Acc_AddTime){
        this.Acc_AddTime=Acc_AddTime;
    }
}
