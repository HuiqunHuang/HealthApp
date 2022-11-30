package health.healthapp.DataBase;

public class Step {
    private int Step,Date;
    private double Calorie;
    private float distance,step_length,speed,stride_rate;
    private int time;

    public int getStep(){
        return Step;
    }
    public void setStep(int Step){
        this.Step=Step;
    }
    public int getDate() {
        return Date;
    }
    public void setDate(int Date){
        this.Date=Date;
    }
    public double getCalorie(){
        return Calorie;
    }
    public void setCalorie(double Calorie){
        this.Calorie=Calorie;
    }
    public int getTime(){
        return time;
    }
    public void setTime(int time){
        this.time=time;
    }
    public void setDistance(float distance){this.distance=distance;}
    public float getDistance(){return distance;}
    public void setStep_length(float step_length){this.step_length=step_length;}
    public  float getStep_length(){return step_length;}
    public void setSpeed(float speed){this.speed=speed;}
    public float getSpeed(){return speed;}
    public  void setStride_rate(float stride_rate){this.stride_rate=stride_rate;}
    public float getStride_rate(){return stride_rate;}
}
