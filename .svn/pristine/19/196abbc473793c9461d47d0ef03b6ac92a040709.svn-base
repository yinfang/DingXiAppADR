package com.clubank.device;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.clubank.device.op.GetShopArena;
import com.clubank.device.op.ReportCheckin;
import com.clubank.device.op.ReportCheckinHour;
import com.clubank.device.op.ReportIncome;
import com.clubank.dingxi.R;
import com.clubank.domain.BC;
import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 营业统计
 */
public class BussinessStatisticsActivity extends BaseActivity {
    private MyData arenas;//场馆
    private PopupWindow ArenaPop;
    private ListView list;
    private int ArenaID = 100;
    private String startdate, enddate;
    private int[] colors;
    private LineChart lineChart;
    private PieChart pieChart;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness_statistics);
        initView();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE, 1);

        startdate = C.df_yMd.format(new Date());//今天
        enddate = C.df_yMd.format(calendar.getTime());//明天
        LinearLayout chartll = findViewById(R.id.chart_ll);
        chartll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screen.getHeight() / 3));
        chartll.setPadding(30, 0, 30, 40);
        lineChart = findViewById(R.id.lineChart);
        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.barChart);
        pieChart.setLayoutParams(new LinearLayout.LayoutParams(screen.getWidth() / 3, screen.getWidth() / 3));
        arenas = new MyData();
        new MyAsyncTask(this, GetShopArena.class).run();
        refreshAllData();
    }

    public void refreshAllData() {
        super.refreshData();
        new MyAsyncTask(this, ReportIncome.class).run(startdate, enddate, ArenaID);
        new MyAsyncTask(this, ReportCheckin.class).run(startdate, enddate, ArenaID);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        new MyAsyncTask(this, ReportIncome.class).run(startdate, enddate, ArenaID);
        new MyAsyncTask(this, ReportCheckinHour.class).run(startdate, enddate, ArenaID);
    }

    private void initView() {
        FrameLayout photoBg = findViewById(R.id.photo_bg);
        photoBg.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screen.getHeight() / 3));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//状态栏透明
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            int statusBarHeight = -1;
            //获取status_bar_height资源的ID
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
                findViewById(R.id.status_view).setMinimumHeight(statusBarHeight);
            } else {
                statusBarHeight = UI.toPixel(this, 24);
                findViewById(R.id.status_view).setMinimumHeight(statusBarHeight);
            }
        }
        colors = new int[]{getResources().getColor(R.color.tab_active), getResources().getColor(R.color.status2),
                getResources().getColor(R.color.status3), getResources().getColor(R.color.status01), getResources().getColor(R.color.yellow)};
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == GetShopArena.class) {
            if (result.code == BC.SUCCESS) {
                MyRow rr = new MyRow();
                rr.put("ArenaName", "全部场馆");
                rr.put("ArenaID", "100");
                arenas.add(rr);
                MyData data = (MyData) result.obj;
                for (MyRow r : data) {
                    arenas.add(r);
                }
            }
        } else if (op == ReportIncome.class) {
            if (result.code == BC.SUCCESS) {
                MyData datas = (MyData) result.obj;
                float total = 0;//
                for (MyRow ro : datas) {
                    total += ro.getFloat("TotalAmount");
                }
                setEText(R.id.all_integral, getString(R.string.yuan) + "  " + C.nf_a.format(total));//总金额
                if(datas.size()>0){
                    show(R.id.pie_ll);
                    initPieChart(pieChart, datas);
                }else{
                    hide(R.id.pie_ll);
                }
            }
        } else if (op == ReportCheckin.class) {//全部场馆登记统计
            if (result.code == BC.SUCCESS) {
                MyData datas = (MyData) result.obj;
                int total = (int) result.obj2;
                setEText(R.id.all_incomes, String.format(getString(R.string.tody_incomes), total + ""));//今日客流量
                show(R.id.barChart);
                hide(R.id.lineChart);
                int amount = 0;
                for (MyRow ro : datas) {
                    amount += ro.getInt("CheckinNum");
                }
                setEText(R.id.second_num, String.format(getString(R.string.booking_num), amount + ""));//总计 单
                if(datas.size()>0){
                    show(R.id.chart_ll);
                    initBarChart(barChart, datas);
                }else{
                    hide(R.id.chart_ll);
                }
            }
        } else if (op == ReportCheckinHour.class) {//分场馆登记统计
            if (result.code == BC.SUCCESS) {
                MyData datas = (MyData) result.obj;
                int total = 0;
                for (MyRow ro : datas) {
                    total += ro.getInt("CheckinNum");
                }
                setEText(R.id.all_incomes, String.format(getString(R.string.tody_bookings), total + ""));//今日定场数
                setEText(R.id.second_num, String.format(getString(R.string.booking_num), total + ""));//总计 单
                hide(R.id.barChart);
                show(R.id.lineChart);
                if(datas.size()>0){
                    show(R.id.chart_ll);
                    initLineChart(lineChart, datas);
                }else{
                    hide(R.id.chart_ll);
                }
            }
        }
    }

    public void doWork(View v) {
        switch (v.getId()) {
            case R.id.all_arenas://场馆选择
                String[] ss = new String[]{};
                ss = new String[arenas.size()];
                for (int i = 0; i < arenas.size(); i++) {
                    ss[i] = arenas.get(i).getString("ArenaName");
                }
                showListDialog(v, ss);
                break;
        }
    }

    @Override
    public void listSelected(View view, int index) {
        super.listSelected(view, index);
        ArenaID = arenas.get(index).getInt("ArenaID");
        MyRow ro = arenas.get(index);
        setEText(R.id.all_arenas, ro.getString("ArenaName"));
        if (ArenaID == 100) {//全部场馆
            setEText(R.id.second_title, getString(R.string.tody_book_sort));
            refreshAllData();
        } else {
            setEText(R.id.second_title, String.format(getString(R.string.time_book__sort), ro.getString("ArenaName")));//分时定场数
            refreshData();
        }
    }

    public void initPieChart(PieChart pieChart, MyData datas) {//type 0全部场馆  1 分场馆
        //饼状图数据
        ArrayList<PieEntry> entries = new ArrayList<>();
        int n = datas.size();
        for (int i = 0; i < n; i++) {
            float va = 0;
            String text = datas.get(i).getString("Proportion");
            text = text.substring(0, text.length() - 1);
            va = Float.parseFloat(text);
            entries.add(new PieEntry(va, i));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(0f);//设置饼状Item被选中时变化的距离
        dataSet.setValueTextSize(12);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setDrawValues(false);//不显示文字
        pieChart.setData(data);
        pieChart.setNoDataText("暂无数据");
        pieChart.setDrawHoleEnabled(true);  //是否显示PieChart内部圆环(true:下面属性才有意义)
        pieChart.setHoleRadius(30f); //设置PieChart内部圆的半径(这里设置28.0f)
        pieChart.setTransparentCircleRadius(31f);//设置PieChart内部透明圆的半径(这里设置31.0f)
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(255);
        pieChart.setDrawEntryLabels(true);//显示图例方框
        pieChart.setRotation(0);//设置绘制的起始角度　Y轴正方向是0度
        pieChart.setRotationEnabled(false);//设置pieChart图表是否可以手动旋转
//        pieChart.animateY(1000, Easing.EasingOption.EaseInOutQuad); //设置动画
        pieChart.setUsePercentValues(true);//显示为百分数
        pieChart.setDrawCenterText(false);
        pieChart.getDescription().setEnabled(false);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);//设置显示图例
        legend.setDrawInside(false);

        LinearLayout ll = findViewById(R.id.barss);
        showBarss(ll, datas);
     /*   pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                setYuanScale((int) h.getX());
            }
            @Override
            public void onNothingSelected() {
            }
        });*/
        pieChart.invalidate();
    }

    private void setYuanScale(int xIndex) { //设置barss第index的圆圈动画开启
        LinearLayout ll = this.findViewById(R.id.barss);
        LinearLayout childAt = (LinearLayout) ll.getChildAt(xIndex);
        final TextView tv_yuan = childAt.findViewById(R.id.yuan);
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //3秒完成动画
        scaleAnimation.setDuration(700);
        //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
        animationSet.addAnimation(scaleAnimation);
        //启动动画
        tv_yuan.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet animationSet2 = new AnimationSet(true);
                ScaleAnimation scaleAnimation2 = new ScaleAnimation(1, 1.3f, 1, 1.3f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //3秒完成动画
                scaleAnimation2.setDuration(300);
                //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
                animationSet2.addAnimation(scaleAnimation2);
                //启动动画
                tv_yuan.startAnimation(animationSet2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 展示饼状图右侧的列表数据
     * Common method to draw content bars,
     */
    public void showBarss(LinearLayout container, MyData data) {
        container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < data.size(); i++) {
            MyRow row = data.get(i);
            View v = inflater.inflate(R.layout.item_barss, container, false);
            TextView rectangle = v.findViewById(R.id.rectangle);
            TextView tt = v.findViewById(R.id.yuan);
            rectangle.setBackgroundColor(colors[i]);
            String typeName = row.getString("ArenaName");
            tt.setText(typeName + ": " + getString(R.string.yuan) + data.get(i).getFloat("TotalAmount") + " " + data.get(i).getString("Proportion"));
            container.addView(v);
        }
        if (data.size() > 0) {
            displayIt(true);
        }
    }

    protected void displayIt(boolean display) {
        LinearLayout ll = this.findViewById(R.id.barss);
        if (display) {
            ll.setVisibility(View.VISIBLE);
        } else {
            ll.setVisibility(View.GONE);
        }
    }

    void initBarChart(BarChart barChart, final MyData datas) {
        /*//TODO test
        for (int i = 0; i < 10; i++) {
            MyRow ro = new MyRow();
            ro.put("CheckinNum", 12);
            ro.put("ArenaName", "羽毛球场");
            datas.add(ro);
        }*/

        ArrayList<BarEntry> yValues1 = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            yValues1.add(new BarEntry(i, datas.get(i).getInt("CheckinNum")));
        }
        BarDataSet set1 = new BarDataSet(yValues1, "");
        set1.setColors(colors);
        set1.setValueTextSize(12f);
        final BarData data = new BarData(set1);
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                            ViewPortHandler viewPortHandler) {
                return String.valueOf((int) value);//设置每个柱子上面显示的字符的样式
            }
        });
        data.setBarWidth(0.7f);
        barChart.setData(data);
        barChart.setNoDataText("暂无数据");
        barChart.getDescription().setEnabled(false);
        barChart.setScaleXEnabled(false);
        barChart.setScaleYEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setHighlightPerTapEnabled(false);//点击没阴影
        barChart.setHighlightPerDragEnabled(false);//滑动无阴影
        barChart.setFitBars(true);     //设置X轴范围两侧柱形条是否显示一半
        barChart.setVisibleXRange(4, 6);
        Legend l = barChart.getLegend();
        l.setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
//        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.GRAY);
        xAxis.setDrawLabels(true);//是否显示X坐标轴上的刻度，默认是true, x坐标轴默认显示的是1,2,3,4,5.....
        xAxis.setLabelCount(datas.size(), false);//第一个参数是轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(11f);
//        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setGridColor(Color.BLACK);
        //xAxis.setCenterAxisLabels(true);//设置居中 组类型数据有效

        //自定义x轴显示的数字格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String text = "";
                int poss;
                for (int i = 0; i < datas.size(); i++) {
                    if (value == i) {
                        int pos = (int) (value + datas.size()) % datas.size();
                        text = datas.get(pos).getString("ArenaName");
                        /*poss = pos;
                        if (pos > 4) {
                            poss = pos % 5;
                        }
                        axis.setTextColor(colors[poss]);*/
                    }
                }
                return text;
            }
        });

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });
        leftAxis.setAxisLineColor(Color.GRAY);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextSize(11f);
        //保证Y轴从0开始，不然会上移一点
//        leftAxis.setAxisMinimum(0f);//为这个轴设置一个自定义的最小值。如果设置,这个值不会自动根据所提供的数据计算
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//y轴的数值显示在外侧
//        leftAxis.setLabelCount(6, true);//第一个参数是轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
//        leftAxis.setAxisMaximum( numbers.get(numbers.size()-1) );//动态自定义y轴的最大值
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);
        barChart.postInvalidate();
    }

    void initLineChart(LineChart lineChart, MyData datas) {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            values.add(new Entry(i, datas.get(i).getInt("CheckinNum")));
        }
        LineDataSet set1 = new LineDataSet(values, "");
        set1.setColor(getResources().getColor(R.color.title_blue));
        set1.setCircleColor(getResources().getColor(R.color.title_blue));
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(true);
        set1.setValueTextSize(12f);
        set1.setFillColor(getResources().getColor(R.color.status01));
        set1.setDrawFilled(true);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);//设置显示为曲线还是折线

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                            ViewPortHandler viewPortHandler) {
                return String.valueOf((int) value);//设置每个柱子上面显示的字符的样式
            }
        });
        lineChart.setData(data);
        lineChart.setNoDataText("暂无数据");   // 没有数据时样式
        lineChart.setDrawGridBackground(false);
        lineChart.setVisibleXRange(6, 6);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDragEnabled(true);
        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setGridColor(getResources().getColor(R.color.white));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(11f);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(datas.size(), false);
        // xAxis.setGridDashedLine(new DashPathEffect());//画虚线
        // xAxis.setAvoidFirstLastClipping(true);
        final String[] xvalues = new String[datas.size()];
        for (int i = 0; i < datas.size(); i++) {
            xvalues[i] = datas.get(i).getString("CheckinHour");
        }
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xvalues[(int) value];
            }
        });
        YAxis leftAxis = lineChart.getAxisLeft();//y轴独有
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });
        leftAxis.setGridColor(getResources().getColor(R.color.white));
        //保证Y轴从0开始，不然会上移一点
//        leftAxis.setLabelCount(6,true);//第一个参数是轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        leftAxis.setGranularity(1f);//设置轴值之间最小间隔1
        leftAxis.setAxisMinimum(0f);//为这个轴设置一个自定义的最小值。如果设置,这个值不会自动根据所提供的数据计算
        leftAxis.setTextSize(11f);
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//y轴的数值显示在外侧

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);//不显示右侧线
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        lineChart.postInvalidate();
    }
}
