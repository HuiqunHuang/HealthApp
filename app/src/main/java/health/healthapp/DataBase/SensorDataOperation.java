package health.healthapp.DataBase;

import health.healthapp.Service.SensorRequestService;

import static android.os.SystemClock.sleep;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class SensorDataOperation{
    public int step=0;
    public float walk_distance=0,run_distance=0,ride_distance=0,climb_distance=0,walk_speed=0,run_speed=0,ride_speed=0,climb_speed=0;
    public float walk_step_length=0,run_step_length=0,climb_step_length=0;
    public float walk_stride_rate=0,run_stride_rate=0,climb_striderate=0;
    public int climb_step=0,walk_step=0,run_step=0;
    public double former_min_accvector=0;//上一个波谷值
    public double former_max_accvector=0;//上一个波峰值
    public double former_accvector=0;//上一个峰值
   // public int former_time=0;//上一个波谷时间
    // public int upordown=1;//0：上一组加速度值为止加速度矢量持续增长  aq：上一组加速度值为止加速度矢量持续递减
    //步行时第一个动作加速度向持续增加
    public  SensorDataOperation(){}

    public double former_value=0;//上一个波值
    public double this_value=0;//此次计步的波峰值
    public double former_max_dif=0;//上一次有效计步中波峰值与两个波谷值的最大差
    public long former_min_time=0;//上一个波谷值的时间
    public double former_min_value=0;//上一次波谷值
    public int upordown=1;//数据变化趋势，初始化为变大
    public int max_upordown=1;//最大峰谷差值的变化趋势
    public long former_time=0;//上一次波峰或波谷值时间
    public double former_maxormin_value=0;//上一次的波峰或者波谷值
    public double f_value=0;//上一个数值
    public long f_time=0;//上一个数值的收集时间
    public int still=0;


    public double last_max_value;//上一次检测到的波峰
    public double last_value;//上一次的值
    public double last_min_value;//上一次检测到的波谷
    public int hh=1;//上升
    public int[] motion_array= new int[15];
    public int array_length=0;

    public void step(float x,float y,float z){
        fff();
        calculate_step_length();
        double acc=Math.sqrt(x*x+y*y+z*z);
        judge_motion_state(x,y,z);
        long now=System.currentTimeMillis();//当前时间（毫秒）
        if(still<=3) {
            if ((acc >= f_value && upordown == 1) || (acc <= f_value && upordown == 0)) {//变化趋势相同时
                f_value = acc;//将此次的数据和时间保留，更新f_value和f_time
                f_time = now;//不做其他操作
            } else if (acc >= f_value && upordown == 0) {//出现波谷值
                switch (ifstill(f_value)) {//判断是否为静止
                    case 0://是
                        f_value = acc;//将此次的数据和时间保留，更新f_value和f_time
                        f_time = now;
                        upordown = 0;
                        still++;
                    case 1://否  一次完整波形
                        if ((now - former_min_time <= 500)) {//一步的时间{
                            upordown = 1;
                            former_value = f_value;
                            former_time = f_time;
                            former_min_time = f_time;
                            former_min_value = f_value;
                            //f_value=acc;//将此次的数据和时间保留，更新f_value和f_time
                            //f_time=now;
                            still++;
                        } else {
                            //判断此次完整波形的波峰与两个波谷的差之间是否相差超过对方的一半
                            if ((((former_value - f_value) / (former_value - former_min_value)+(former_value - f_value) % (former_value - former_min_value)) >= 0.5) || (((former_value - f_value) / (former_value - former_min_value)+(former_value - f_value) % (former_value - former_min_value)) >= 2)) {
                                int upord = 0;
                                double max = 0;
                                if (former_value - f_value >= former_value - former_min_value)//下降的
                                    max = former_value - f_value;
                                else {
                                    max = former_value - former_min_value;
                                    upord = 1;
                                }

                                if ((max / former_max_dif+max % former_max_dif) >= 0.7 || (former_max_dif / max+former_max_dif % max) >= 0.7) {
                                    upordown = 1;
                                    former_value = f_value;
                                    former_time = f_time;
                                    former_min_value = f_value;
                                    former_min_time = f_time;
                                    still ++;
                                } else {
                                    switch (upord) {
                                        case 0:
                                            upordown = 1;
                                            former_min_time = former_time = f_time;
                                            former_min_value = former_value = f_value;
                                            f_value = acc;
                                            f_time = now;
                                            former_max_dif = max;
                                            still = 0;
                                            add_step(return_motion_state());
                                        case 1:
                                            upordown = 1;
                                            former_value = former_min_value;
                                            former_time = former_min_time;
                                            f_value = acc;
                                            f_time = now;
                                            still = 0;
                                    }
                                }

                            } else {
                                double max = 0;
                                if (former_value - f_value >= former_value - former_min_value)//下降的
                                    max = former_value - f_value;
                                else {
                                    max = former_value - former_min_value;
                                }

                                if ((max / former_max_dif+max % former_max_dif) >= 0.7 || (former_max_dif / max+former_max_dif % max) >= 0.7) {
                                    upordown = 1;
                                    former_value = f_value;
                                    former_time = f_time;
                                    former_min_value = f_value;
                                    former_min_time = f_time;
                                    still = 0;
                                } else {
                                    upordown = 1;
                                    former_min_time = former_time = f_time;
                                    former_min_value = former_value = f_value;
                                    f_value = acc;
                                    f_time = now;
                                    former_max_dif = max;
                                    still = 0;
                                    add_step(return_motion_state());
                                }
                            }
                        }
                    default:
                        break;
                }
            } else if (acc <= f_value && upordown == 1) {//出现波峰
                switch (ifstill(f_value)) {
                    case 0:
                        f_value = acc;//将此次的数据和时间保留，更新f_value和f_time
                        f_time = now;
                        upordown = 1;
                        still++;
                    case 1:
                        former_value = f_value;
                        former_time = f_time;
                        upordown = 0;
                        f_value = acc;
                        f_time = now;
                        still=0;
                    default:
                        break;
                }
            }
        }
    }

    public void add_step(int type){
        switch (type){
            case 1:
                walk_step++;
            case 2:
                run_step++;
            default:
                break;
        }
    }

    //判断检测到的趋势变化值与上一次的趋势变化值之差是否小于0.2g，是则返回0
    public int ifstill(double value){
        if(((value-former_maxormin_value)<(0.2*9.8))||((former_maxormin_value-value)<(0.2*9.8)))
            return 0;
        else
            return 1;
    }


    public void judge_motion_state(float x,float y,float z){
        double acc=Math.sqrt(x*x+y*y+z*z);
        if((acc>=last_value&&hh==1)||(acc<=last_value&&hh==0)){
            last_value=acc;
        }else if(acc>last_value&&hh==0){//检测到波谷
            motion_state(1);
            last_min_value=last_value;
            last_value=acc;
        }else if(acc<last_value&&hh==1){
            motion_state(0);
            last_max_value=last_value;
            last_value=acc;
        }
    }

    public void motion_state(int type){//type表示检测到的是波峰还是波谷
        if(SensorRequestService.return_dis()!=-999&&SensorRequestService.return_speed()>=3) {
            arrayAdd(3);
            ride_distance+=SensorRequestService.return_dis();
            ride_speed=SensorRequestService.return_speed();
        }else{
            switch (type) {
                case 0://波峰
                    if (last_value - last_min_value < 5 && last_value - last_min_value >= 0) {//静止
                        arrayAdd(0);
                    }
                    else if (last_value - last_min_value <= 15 && last_value - last_min_value >= 5) {//步行
                        arrayAdd(1);
                        if(SensorRequestService.return_dis()!=-999) {
                            walk_distance += SensorRequestService.return_dis();
                            walk_speed=SensorRequestService.return_speed();
                        }
                    }
                    else if (last_value - last_min_value <= 30 && last_value - last_min_value > 15) {//跑步
                        arrayAdd(2);
                        if(SensorRequestService.return_dis()!=-999) {
                            run_distance += SensorRequestService.return_dis();
                            run_speed=SensorRequestService.return_speed();
                        }
                    }
                    break;
                case 1:
                    if (last_max_value - last_value <= 1 && last_max_value - last_value >= 0)
                        arrayAdd(0);
                    else if (last_max_value - last_value <= 10 && last_max_value - last_value >= 2) {
                        arrayAdd(1);
                        if(SensorRequestService.return_dis()!=-999) {
                            walk_distance += SensorRequestService.return_dis();
                            walk_speed=SensorRequestService.return_speed();
                        }
                    }
                    else if (last_max_value - last_value <= 30 && last_max_value - last_value >= 10){
                        arrayAdd(2);
                        if(SensorRequestService.return_dis()!=-999) {
                            run_distance += SensorRequestService.return_dis();
                            run_speed=SensorRequestService.return_speed();
                        }
                    }
                    break;
            }
        }
    }

    //保存最近几个加速度变化值的DAA归属运动状态
    public void arrayAdd(int type){
        if(array_length<15&&array_length>=0) {
            motion_array[array_length] = type;
            array_length++;
        }
        else if(array_length>=15){
            for(int i=0;i<14;i++)
                motion_array[i]=motion_array[i+1];
            motion_array[14]=type;
        }
    }


    public int return_motion_state(){
        int max=0,motion_still=0,motion_walk=0,motion_run=0,max_type=0,motion_ride=0;
        if(array_length>=15)
            for(int i=0;i<15;i++)
                switch (motion_array[i]){
                    case 0:
                        motion_still++;
                    case 1:
                        motion_walk++;
                    case 2:
                        motion_walk++;
                    case 3:
                        motion_ride++;
                }
       if(motion_still>motion_walk) {
           max = motion_still;
           max_type=0;
       }
        else {
           max = motion_walk;
           max_type=1;
       }
        if (max<motion_run) {
            max = motion_run;
            max_type=2;
        }
        if(max<motion_ride){
            max=motion_ride;
            max_type=3;
        }
        if(max<=8) {
            max_type = 0;
        }
        return max_type;
    }

    public void operateAcc(float x,float y,float z){//统一在波谷增加步数
        int now=(int)System.currentTimeMillis();//当前时间（毫秒）
        double accvector=Math.sqrt(x*x+y*y+z*z);
        if(former_accvector==0){
            former_accvector=accvector;
            former_max_accvector=0;
            former_min_accvector=accvector;
            former_time=now;
        }else {
            switch (upordown) {
                case 0:
                    if (former_accvector > accvector) {//开始递减
                        upordown = 1;
                        former_max_accvector = former_accvector;//上一个值为峰值
                    }
                    former_accvector = accvector;
                    break;
                case 1:
                    if (former_accvector < accvector) {//开始增长
                        former_min_accvector = former_accvector;
                        upordown =  0;
                        if (now - former_time > 24) this.step++; //相邻波谷相差小于0.2秒，不计步  12
                        former_time = now;
                    }
                    former_accvector = accvector;
                    break;
                default:
                    break;
            }
        }
    }

    //public void step(){
       // this.step++;
    //}

    public void oo(){
        this.step++;
    }

    public double calculate_calorie(int type,int step,float weight,float speed,float distance){
        switch(type){
            case 0:
                return weight*distance*1.036;
            case 1:
                return weight*distance*1.036;//跑步热量
            case 2:
                return weight*distance*1.036;
            case 3:
                return weight*distance*1.036;
            default:
                return  0.0;

        }
    }



    public float get_walk_distance(){
        return walk_distance=walk_step*50/100;
    }
    public float get_walk_speed(){
        return walk_speed;
    }
    public float get_walk_step_length(){
        return walk_step_length;
    }
    public float get_walk_stride_rate(){ return walk_stride_rate;}
    public float get_run_distance(){
        return run_distance=run_step*50/100;
    }
    public float get_run_speed(){
        return run_speed;
    }
    public float get_run_step_length(){
        return run_step_length;
    }
    public float get_run_stride_rate(){ return run_stride_rate;}
    public float get_ride_distance(){
        return ride_distance;
    }
    public float get_ride_speed(){
        return ride_speed;
    }
    public float get_climb_distance(){
        return climb_distance=climb_step*30/100;
    }
    public float get_climb_speed(){
        return climb_speed;
    }
    public float get_climb_step_length(){return climb_step_length;}
    public float get_climb_stride_rate(){return climb_striderate;}

    public void calculate_striderate(){

    }
    public void calculate_step_length(){
        walk_step_length=walk_distance/walk_step+walk_distance%walk_step;
        run_step_length=run_distance/run_step+run_distance%run_step;
        climb_step_length=climb_distance/climb_step+climb_distance%climb_step;
    }

    public void fff(){
        walk_distance=walk_step*50/100;
        run_distance=run_step*50/100;
        climb_distance=climb_step*30/100;
    }

}
