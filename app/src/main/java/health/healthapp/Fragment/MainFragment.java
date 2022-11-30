package health.healthapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import health.healthapp.Activity.ClimbSubcoatActivity;
import health.healthapp.Activity.MainActivity;
import health.healthapp.Activity.RideSubcoatActivity;
import health.healthapp.Activity.RunSubcoatActivity;
import health.healthapp.Activity.WalkSubcoatActivity;
import health.healthapp.R;

public class MainFragment extends Fragment {
    private TextView walk_step;
    private TextView walkcalorie;
    private TextView run_dis;
    private TextView run_calorie;
    private TextView ride_dis;
    private TextView ride_calorie;
    private TextView climb_dis;
    private TextView climb_calorie;
    private View View;
    private String walkStepStr;
    private String walkCalorieStr;
    private String runDisStr;
    private String runCalorieStr;
    private String rideDisStr;
    private String rideCalorieStr;
    private String climbDisStr;
    private String climbCalorieStr;
    private LinearLayout walk_stepgraph;
    private LinearLayout run_stepgraph;
    private LinearLayout up_stepgraph;
    private LinearLayout ride_stepgraph;
    private Context context;
    private GraphicalView walkchart;
    private GraphicalView runchart;
    private GraphicalView ridechart;
    private GraphicalView upchart;
    private XYMultipleSeriesRenderer walk_renderer;
    private XYMultipleSeriesDataset walk_dataset;
    private XYSeriesRenderer walk_xyRenderer;
    private XYMultipleSeriesRenderer run_renderer;
    private XYMultipleSeriesDataset run_dataset;
    private XYSeriesRenderer run_xyRenderer;
    private XYMultipleSeriesRenderer ride_renderer;
    private XYMultipleSeriesDataset ride_dataset;
    private XYSeriesRenderer ride_xyRenderer;
    private XYMultipleSeriesRenderer up_renderer;
    private XYMultipleSeriesDataset up_dataset;
    private XYSeriesRenderer up_xyRenderer;
    private List<Date[]> xDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View = inflater.inflate(R.layout.fragment_main, null);
        context = getActivity().getApplicationContext();
        walk_stepgraph=(LinearLayout)View.findViewById(R.id.step_graph);
        run_stepgraph=(LinearLayout)View.findViewById(R.id.run_step_graph);
        ride_stepgraph=(LinearLayout)View.findViewById(R.id.ride_step_graph);
        up_stepgraph=(LinearLayout)View.findViewById(R.id.up_step_graph);
        walk_stepgraph.setOnClickListener(new View.OnClickListener() {//点击首页折线图进入二级页面
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), WalkSubcoatActivity.class);
                startActivity(intent);
            }
        });
        run_stepgraph.setOnClickListener(new View.OnClickListener() {//点击首页折线图进入二级页面
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), RunSubcoatActivity.class);
                startActivity(intent);
            }
        });
        ride_stepgraph.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//点击首页折线图进入二级页面
                Intent intent=new Intent(getActivity(), RideSubcoatActivity.class);
                startActivity(intent);
            }
        });
        up_stepgraph.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//点击首页折线图进入二级页面
                Intent intent=new Intent(getActivity(), ClimbSubcoatActivity.class);
                startActivity(intent);
            }
        });
        walkchart();
        runchart();
        ridechart();
        upchart();
        return View;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        walk_step = (TextView)View.findViewById(R.id.walksteptitle);
        walkcalorie=(TextView)View.findViewById(R.id.walkcalorietitle);
        run_dis=(TextView)View.findViewById(R.id.runsteptitle);
        run_calorie=(TextView)View.findViewById(R.id.runcalorietitle);
        ride_dis=(TextView)View.findViewById(R.id.ridesteptitle);
        ride_calorie=(TextView)View.findViewById(R.id.ridecalorietitle);
        climb_dis=(TextView)View.findViewById(R.id.upsteptitle);
        climb_calorie=(TextView)View.findViewById(R.id.upcalorietitle);
        if (!TextUtils.isEmpty(walkStepStr)){
            walk_step.setText(walkStepStr);
        }
        if(!TextUtils.isEmpty(walkCalorieStr)){
            walkcalorie.setText(walkCalorieStr);
        }
        if (!TextUtils.isEmpty(runDisStr)){
            run_dis.setText(runDisStr);
        }
        if(!TextUtils.isEmpty(runCalorieStr)){
            run_calorie.setText(runCalorieStr);
        }
        if (!TextUtils.isEmpty(rideDisStr)){
            ride_dis.setText(rideDisStr);
        }
        if(!TextUtils.isEmpty(rideCalorieStr)){
            ride_calorie.setText(rideCalorieStr);
        }
        if (!TextUtils.isEmpty(climbDisStr)){
            climb_dis.setText(climbDisStr);
        }
        if(!TextUtils.isEmpty(climbCalorieStr)){
            climb_calorie.setText(climbCalorieStr);
        }
    }
    //步行步数文本填充
    public void setText_walkstep(String text){
        if (walk_step == null){
            walkStepStr = text;
        }
        else {
            walk_step.setText(text);
        }
    }

    //步行热量文本填充
    public void setText_walkcalorie(String text){
        if (walkcalorie == null){
            walkCalorieStr = text;
        }
        else {
            walkcalorie.setText(text);
        }
    }

    //跑步距离文本填充
    public void setText_rundis(String text){
        if (run_dis == null){
            runDisStr = text;
        }
        else {
            run_dis.setText(text);
        }
    }

    //跑步热量文本填充
    public void setText_runcalorie(String text){
        if (run_calorie == null){
            runCalorieStr = text;
        }
        else {
            run_calorie.setText(text);
        }
    }


    //骑行距离文本填充
    public void setText_ridedis(String text){
        if (ride_dis == null){
            rideDisStr = text;
        }
        else {
            ride_dis.setText(text);
        }
    }

    //骑行热量文本填充
    public void setText_ridecalorie(String text){
        if (ride_calorie == null){
            rideCalorieStr = text;
        }
        else {
            ride_calorie.setText(text);
        }
    }

    //爬高距离文本填充
    public void setText_climbdis(String text){
        if (climb_dis == null){
            climbDisStr = text;
        }
        else {
            climb_dis.setText(text);
        }
    }

    //爬高热量文本填充
    public void setText_climbcalorie(String text){
        if (climb_calorie == null){
            climbCalorieStr = text;
        }
        else {
            climb_calorie.setText(text);
        }
    }


    //从文本框中获取步行消耗的能量
    public int getCalorieText(){
        if(walkcalorie.getText()==null||walkcalorie.getText()=="")
            return 0;
        else {
            int data=0;
            try{
                data=Integer.parseInt(walkcalorie.getText().toString());
            }catch (Exception e){
                e.printStackTrace();
            }
            return data;
        }
    }

    protected void setChartSettings(XYMultipleSeriesRenderer renderer,
                                    String xTitle, String yTitle,
                                    double yMin, double yMax) {
        //renderer.addXTextLabel(0,"");
        renderer.setBackgroundColor(getResources().getColor(
                R.color.TealA700));
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setZoomEnabled(false, false);
        renderer.setPanEnabled(false, false);
        /*double  startime = new Date().getTime();
        double endtime = new Date().getTime()+ TimeChart.DAY;

        Calendar current = Calendar.getInstance();
        Calendar todayMax = Calendar.getInstance();    //今天  最大
        todayMax.set(Calendar.YEAR, current.get(Calendar.YEAR));
        todayMax.set(Calendar.MONTH, current.get(Calendar.MONTH));
        todayMax.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH)+1);*/


        /*renderer.setPanLimits(new double[]{0,todayMax.getTimeInMillis(), 0,30000});

        //设置x轴开始值和结束值，有利于放大效果
        renderer.setXAxisMax(Math.round(endtime));
        renderer.setXAxisMin(Math.round(startime));
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        */
        //renderer.setFitLegend(true);// 调整合适的位置
        renderer.setMarginsColor(getResources().getColor(
                R.color.TealA700));
        //renderer.setBarSpacing(3);
       // renderer.setBarWidth(7f);
       // renderer.setXAxisMax(0);
       //renderer.setXAxisMin(6);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setXLabelsAlign(Paint.Align.LEFT);
        renderer.setYAxisAlign(Paint.Align.LEFT, 0);//用来调整Y轴放置的位置
        //renderer.setAxesColor(R.color.TealA700);//x、y轴颜色
        renderer.setLabelsColor(Color.WHITE);
        renderer.setShowGrid(false);//网络格
        renderer.setXLabels(0);//若不想显示X标签刻度，设置为0 即可
        renderer.setYLabels(0);
        renderer.setLabelsTextSize(15);// 设置坐标轴标签文字的大小
        renderer.setXLabelsColor(Color.WHITE);
        renderer.setYLabelsColor(0,Color.WHITE);
        //renderer.setYLabelsVerticalPadding(-5);
        //renderer.setPanEnabled(true, false);// 设置横坐标可以滑动，纵坐标不可以
        //renderer.setPanLimits(new double[] { 0, 31, 0, 0, });// 坐标滑动上、下限
        renderer.setChartTitleTextSize(25);
        renderer.setDisplayChartValues(true);
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        renderer.setAxisTitleTextSize(18);
        renderer.setPointSize((float) 1);
        renderer.setShowLegend(false);
        //renderer.setXLabelsAngle(-45);// 标签倾斜的角度
        //renderer.setFitLegend(true);
        renderer.setMargins(new int[] { 5, 25, 5, 25 });// 设置图表的外边框(上/左/下/右)
        //renderer.setXLabelsPadding(0);//设置标签的间距


        renderer.addXTextLabel(0,"一");
        renderer.addXTextLabel(1,"二");
        renderer.addXTextLabel(2,"三");
        renderer.addXTextLabel(3,"四");
        renderer.addXTextLabel(4,"五");
        renderer.addXTextLabel(5,"六");
        renderer.addXTextLabel(6,"日");


    }



    private void walkchart(){
        walk_renderer = new XYMultipleSeriesRenderer();
        walk_dataset = new XYMultipleSeriesDataset();
        Random r = new Random();
        long value = new Date().getTime() - 3 * TimeChart.DAY;
        XYSeries series = new XYSeries("test1");
        series.add(0,3000);
        series.add(1,7000);
        series.add(2,1000);
        series.add(3,2000);
        series.add(4,4000);
        series.add(5,9000);
        series.add(6,3000);
        walk_dataset.addSeries(series);
        walk_xyRenderer = new XYSeriesRenderer();
        walk_xyRenderer.setColor(Color.WHITE);
        walk_xyRenderer.setPointStyle(PointStyle.CIRCLE);
        walk_xyRenderer.setLineWidth(5 );
        walk_renderer.addSeriesRenderer(walk_xyRenderer);

        setChartSettings(walk_renderer, "Weekday", "Steps", -2000, 20000);
        walkchart = ChartFactory.getLineChartView(context, walk_dataset, walk_renderer);
        walk_stepgraph.removeAllViews();
        walk_stepgraph.addView(walkchart,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void runchart(){
        run_renderer = new XYMultipleSeriesRenderer();
        run_dataset = new XYMultipleSeriesDataset();
        Random r = new Random();
        long value = new Date().getTime() - 3 * TimeChart.DAY;
        XYSeries series2 = new XYSeries("test2");
       // for (int k = 0; k <= 6; k++) {
           // series2.add(value + k * TimeChart.DAY / 4,r.nextInt() % 30000);
       // }
        series2.add(0,4000);
        series2.add(1,2000);
        series2.add(2,6000);
        series2.add(3,1000);
        series2.add(4,8000);
        series2.add(5,4000);
        series2.add(6,7000);
        run_dataset.addSeries(series2);
        run_xyRenderer = new XYSeriesRenderer();
        run_xyRenderer.setColor(Color.WHITE);
        run_xyRenderer.setPointStyle(PointStyle.CIRCLE);
        run_xyRenderer.setLineWidth(5 );
        run_renderer.addSeriesRenderer( run_xyRenderer);
        setChartSettings( run_renderer, "Weekday", "Steps", -2000, 20000);
        runchart = ChartFactory.getLineChartView(context,  run_dataset,  run_renderer );
        run_stepgraph.removeAllViews();
        run_stepgraph.addView( runchart,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void ridechart(){
        ride_renderer = new XYMultipleSeriesRenderer();
        ride_dataset = new XYMultipleSeriesDataset();
        Random r = new Random();
        long value = new Date().getTime() - 3 * TimeChart.DAY;
        XYSeries series3 = new XYSeries("test3");
        series3.add(0,5000);
        series3.add(1,1000);
        series3.add(2,3000);
        series3.add(3,9000);
        series3.add(4,3000);
        series3.add(5,6000);
        series3.add(6,3000);
        ride_dataset.addSeries(series3);
        ride_xyRenderer = new XYSeriesRenderer();
        ride_xyRenderer.setColor(Color.WHITE);
        ride_xyRenderer.setPointStyle(PointStyle.CIRCLE);
        ride_xyRenderer.setLineWidth(5 );
        ride_renderer.addSeriesRenderer( ride_xyRenderer);
        setChartSettings( ride_renderer, "Weekday", "Steps", -2000, 20000);
        ridechart = ChartFactory.getLineChartView(context,  ride_dataset,  ride_renderer);
        ride_stepgraph.removeAllViews();
        ride_stepgraph.addView( ridechart,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void upchart(){
        up_renderer = new XYMultipleSeriesRenderer();
        up_dataset = new XYMultipleSeriesDataset();
        Random r = new Random();
        long value = new Date().getTime() - 3 * TimeChart.DAY;
        XYSeries series4 = new XYSeries("test4");
        series4.add(0,5000);
        series4.add(1,8000);
        series4.add(2,6000);
        series4.add(3,3000);
        series4.add(4,10000);
        series4.add(5,9000);
        series4.add(6,3000);
        up_dataset.addSeries(series4);
        up_xyRenderer = new XYSeriesRenderer();
        up_xyRenderer.setColor(Color.WHITE);
        up_xyRenderer.setPointStyle(PointStyle.CIRCLE);
        up_xyRenderer.setLineWidth(5 );
        up_renderer.addSeriesRenderer( up_xyRenderer);
        setChartSettings( up_renderer, "Weekday", "Steps", -2000, 20000);
        upchart = ChartFactory.getLineChartView(context,  up_dataset,  up_renderer);
        up_stepgraph.removeAllViews();
        up_stepgraph.addView( upchart,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
}
