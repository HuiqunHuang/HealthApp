package health.healthapp.DataBase;

public class Gps {
    private float Longitude,Latitude,Altitude,Speed;
    private int Gps_AddTime;

    public float getLongitude(){
        return Longitude;
    }
    public void setLongitude(float Longitude){
        this.Longitude=Longitude;
    }
    public float getLatitude(){
        return Latitude;
    }
    public void setLatitude(float Latitude){
        this.Latitude=Latitude;
    }
    public float getAltitude(){
        return Altitude;
    }
    public void setAltitude(float Altitude){
        this.Altitude=Altitude;
    }
    public float getSpeed(){
        return Speed;
    }
    public void setSpeed(float Speed){
        this.Speed=Speed;
    }
    public int getAddTime(){
        return Gps_AddTime;
    }
    public void setAddTime(int Gps_AddTime){
        this.Gps_AddTime=Gps_AddTime;
    }
}
