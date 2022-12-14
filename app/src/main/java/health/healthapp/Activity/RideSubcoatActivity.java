package health.healthapp.Activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Date;
import java.util.Random;

import health.healthapp.R;

public class RideSubcoatActivity extends AppCompatActivity {
    private LinearLayout ride_subcoat_graph;
    private Context context;
    private GraphicalView ridechart;
    private XYMultipleSeriesRenderer ride_renderer;
    private XYMultipleSeriesDataset ride_dataset;
    private XYSeriesRenderer ride_xyRenderer;
    private static double calorie=0;
    private static float distance=0,speed=0;
    private static TextView ride_subcoat_calorie;
    private static TextView ride_subcoat_dis;
    private static TextView ride_subcoat_speed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_subcoat);
        ride_subcoat_calorie=(TextView)findViewById(R.id.ride_subcoat_calorie);
        ride_subcoat_dis=(TextView)findViewById(R.id.ride_subcoat_dis);
        ride_subcoat_speed=(TextView)findViewById(R.id.ride_subcoat_speed);
        setText();
        ridechart();
    }


    private void ridechart(){
        ride_subcoat_graph=(LinearLayout)findViewById(R.id.ride_subcoat_step_graph);
        ride_renderer = new XYMultipleSeriesRenderer();
        ride_dataset = new XYMultipleSeriesDataset();
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
        ride_dataset.addSeries(series);
        ride_xyRenderer = new XYSeriesRenderer();
        ride_xyRenderer.setColor(Color.WHITE);
        ride_xyRenderer.setPointStyle(PointStyle.CIRCLE);
        ride_xyRenderer.setLineWidth(5 );
        ride_renderer.addSeriesRenderer(ride_xyRenderer);

        setChartSettings(ride_renderer, "Weekday", "Steps", -2000, 20000);
        ridechart = ChartFactory.getLineChartView(this, ride_dataset, ride_renderer);///this??????context
        ride_subcoat_graph.removeAllViews();
        ride_subcoat_graph.addView(ridechart,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
        Calendar todayMax = Calendar.getInstance();    //??????  ??????
        todayMax.set(Calendar.YEAR, current.get(Calendar.YEAR));
        todayMax.set(Calendar.MONTH, current.get(Calendar.MONTH));
        todayMax.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH)+1);*/


        /*renderer.setPanLimits(new double[]{0,todayMax.getTimeInMillis(), 0,30000});

        //??????x????????????????????????????????????????????????
        renderer.setXAxisMax(Math.round(endtime));
        renderer.setXAxisMin(Math.round(startime));
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        */
        renderer.setFitLegend(true);// ?????????????????????
        renderer.setMarginsColor(getResources().getColor(
                R.color.TealA700));
        //renderer.setBarSpacing(3);
        // renderer.setBarWidth(7f);
        // renderer.setXAxisMax(0);
        //renderer.setXAxisMin(6);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setXLabelsAlign(Paint.Align.LEFT);
        renderer.setYAxisAlign(Paint.Align.LEFT, 0);//????????????Y??????????????????
        //renderer.setAxesColor(R.color.TealA700);//x???y?????????
        renderer.setLabelsColor(Color.WHITE);
        renderer.setShowGrid(false);//?????????
        renderer.setXLabels(0);//???????????????X????????????????????????0 ??????
        renderer.setYLabels(0);
        renderer.setLabelsTextSize(15);// ????????????????????????????????????
        renderer.setXLabelsColor(Color.WHITE);
        renderer.setYLabelsColor(0,Color.WHITE);
        //renderer.setYLabelsVerticalPadding(-5);
        // renderer.setPanEnabled(true, false);// ????????????????????????????????????????????????
        //renderer.setPanLimits(new double[] { 0, 31, 0, 0, });// ????????????????????????
        renderer.setChartTitleTextSize(25);
        renderer.setDisplayChartValues(true);
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        renderer.setAxisTitleTextSize(18);
        renderer.setPointSize((float) 1);
        renderer .setShowLegend(false);
        //renderer.setXLabelsAngle(-45);// ?????????????????????
        //renderer.setFitLegend(true);
        renderer.setMargins(new int[] { 5, 25, 5, 25 });// ????????????????????????(???/???/???/???)

        renderer.addXTextLabel(0,"???");
        renderer.addXTextLabel(1,"???");
        renderer.addXTextLabel(2,"???");
        renderer.addXTextLabel(3,"???");
        renderer.addXTextLabel(4,"???");
        renderer.addXTextLabel(5,"???");
        renderer.addXTextLabel(6,"???");
    }

    public static void setText(){
        calorie=MainActivity.returnCalorie(2);
        distance=MainActivity.sensorDataOperation.ride_distance;
        speed=MainActivity.sensorDataOperation.run_speed;
        ride_subcoat_calorie.setText((int)calorie+"???");
        ride_subcoat_dis.setText(distance+"m");
        ride_subcoat_speed.setText(speed+"km/h");
    }
}
