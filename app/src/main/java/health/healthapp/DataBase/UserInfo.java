package health.healthapp.DataBase;

public class UserInfo {
    private float Height,Weight;
    private int BirthYear,BirthMonth,BirthDay;
    private String UserId,Password;

    public float getHeight() {
        return Height;
    }
    public void setHeight(int Height) {
        this.Height = Height;
    }
    public float getWeight() {
        return Weight;
    }
    public void setWeight(int Weight) {
        this.Weight = Weight;
    }
    public int getBirthYear(){
        return BirthYear;
    }
    public void setBirthYear(int BirthYear){
        this.BirthYear=BirthYear;
    }
    public int getBirthMonth(){
        return BirthMonth;
    }
    public void setBirthMonth(int BirthMonth){
        this.BirthMonth=BirthMonth;
    }
    public int getBirthDay(){
        return BirthDay;
    }
    public void setBirthDay(int BirthDay){
        this.BirthDay=BirthDay;
    }
    public String getUserId(){
        return UserId;
    }
    public void setUserId(String UserId){
        this.UserId=UserId;
    }
    public String getPassword(){
        return Password;
    }
    public void setPassword(String Password) {
        this.Password=Password;
    }
}
