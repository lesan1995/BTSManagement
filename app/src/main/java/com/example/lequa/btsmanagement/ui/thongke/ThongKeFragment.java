package com.example.lequa.btsmanagement.ui.thongke;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.databinding.FragmentThongKeBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
//import com.github.mikephil.charting.charts.BarChart;

import butterknife.ButterKnife;

//import android.view.MotionEvent;
//import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
//import com.github.mikephil.charting.listener.ChartTouchListener;
//import com.github.mikephil.charting.listener.OnChartGestureListener;
//import com.github.mikephil.charting.utils.ColorTemplate;
//
//import java.util.ArrayList;

public class ThongKeFragment extends Fragment implements Injectable{
//        ,OnChartGestureListener {
    AutoClearedValue<FragmentThongKeBinding> thongKeBinding;
//    private BarChart mChart;
//    private Typeface tff;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);
    }
//    protected BarData generateBarData(int dataSets, float range, int count) {
//        tff = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
//        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();
//
//        for(int i = 0; i < dataSets; i++) {
//
//            ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
//
////            entries = FileUtils.loadEntriesFromAssets(getActivity().getAssets(), "stacked_bars.txt");
//
//            for(int j = 0; j < count; j++) {
//                entries.add(new BarEntry(j, (float) (Math.random() * range) + range / 4));
//            }
//
//            BarDataSet ds = new BarDataSet(entries, getLabel(i));
//            ds.setColors(ColorTemplate.VORDIPLOM_COLORS);
//            sets.add(ds);
//        }
//
//        BarData d = new BarData(sets);
//        d.setValueTypeface(tff);
//        return d;
//    }
//    private String[] mLabels = new String[] { "Company A", "Company B", "Company C", "Company D", "Company E", "Company F" };
////    private String[] mXVals = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec" };
//
//    private String getLabel(int i) {
//        return mLabels[i];
//    }
    public void setBack(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                } else {
                    getActivity().onBackPressed();
                }
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentThongKeBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_thong_ke, container, false);
        thongKeBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());

//        // create a new chart object
//        mChart = new BarChart(getActivity());
//        mChart.getDescription().setEnabled(false);
//        mChart.setOnChartGestureListener(this);
//
//        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv);
//
//        mChart.setDrawGridBackground(false);
//        mChart.setDrawBarShadow(false);
//
//        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"OpenSans-Light.ttf");
//
//        mChart.setData(generateBarData(1, 20000, 7));
//
//        Legend l = mChart.getLegend();
//        l.setTypeface(tf);
//
//        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setTypeface(tf);
//        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//        mChart.getAxisRight().setEnabled(false);
//
//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setEnabled(false);
//
//        thongKeBinding.get().parentLayout.addView(mChart);

        return dataBinding.getRoot();
    }
    public static ThongKeFragment create() {
        ThongKeFragment thongKeFragment = new ThongKeFragment();
        return thongKeFragment;
    }
//
//    @Override
//    public void onChartGestureStart(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {
//
//    }
//
//    @Override
//    public void onChartGestureEnd(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {
//        mChart.highlightValues(null);
//    }
//
//    @Override
//    public void onChartLongPressed(MotionEvent motionEvent) {
//
//    }
//
//    @Override
//    public void onChartDoubleTapped(MotionEvent motionEvent) {
//
//    }
//
//    @Override
//    public void onChartSingleTapped(MotionEvent motionEvent) {
//
//    }
//
//    @Override
//    public void onChartFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//
//    }
//
//    @Override
//    public void onChartScale(MotionEvent motionEvent, float v, float v1) {
//
//    }
//
//    @Override
//    public void onChartTranslate(MotionEvent motionEvent, float v, float v1) {
//
//    }
}

