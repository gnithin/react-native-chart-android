package cn.mandata.react_native_mpchart;

import android.graphics.Color;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.ThemedReactContext;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.components.MarkerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;


public class MPPieChartManager extends MPPieRadarChartManager {
    private String CLASS_NAME="MPPieChart";
    private ThemedReactContext rContext;

    @Override
    public String getName() {
        return this.CLASS_NAME;
    }

    @Override
    protected PieChart createViewInstance(ThemedReactContext reactContext) {
        PieChart chart=new PieChart(reactContext);
        rContext = reactContext;

        // initialise event listener to bind to chart
        new MPPieChartSelectionEventListener(chart);

        return  chart;
    }

//    @Override
//    protected PieChart createViewInstance(ThemedReactContext reactContext) {
//        PieChart chart= new PieChart(reactContext);
//        return chart;
//    }

    @ReactProp(name = "holeRadius", defaultFloat = 50f)
    public void setHoleRadius(PieChart chart, float holeRadius){
        chart.setHoleRadius(holeRadius);
        chart.invalidate();
    }

    @ReactProp(name = "transparentCircleRadius", defaultFloat = 0f)
    public void setTransparentCircleRadius(PieChart chart, float transparentCircleRadius){
        chart.setTransparentCircleRadius(transparentCircleRadius);
        chart.invalidate();
    }

    @ReactProp(name="backgroundColor", defaultInt = Color.WHITE)
    public void setBackgroundColor(PieChart chart, int backgroundColor){
        chart.setBackgroundColor(backgroundColor);
        chart.invalidate();
    }

    @ReactProp(name="drawSliceText", defaultBoolean = false)
    public void setDrawSliceText(PieChart chart, boolean enabled){
        chart.setDrawSliceText(enabled);
        chart.invalidate();
    }

    @ReactProp(name="usePercentValues", defaultBoolean = true)
    public void setUsePercentValues(PieChart chart, boolean enabled){
        chart.setUsePercentValues(enabled);
        chart.invalidate();
    }
    @ReactProp(name="centerText")
    public void setCenterText(PieChart chart, String v){
        chart.setCenterText(v);
        chart.invalidate();
    }

    @ReactProp(name = "centerTextRadiusPercent", defaultFloat = 1.f)
    public void setCenterTextRadiusPercent(PieChart chart, float percent){
        chart.setCenterTextRadiusPercent(percent);
        chart.invalidate();
    }

    @ReactProp(name="data")
    public void setData(PieChart chart,ReadableMap rm){
        if(rm.hasKey("marker")){
            /*
             * This is for configuring the marker view
             */
            ReadableMap markerMap = rm.getMap("marker");

            if(
                (!markerMap.hasKey("display")) ||
                (markerMap.hasKey("display") && markerMap.getBoolean("display"))
            ){
                chart.setDrawMarkerViews(true);

                CustomMarkerViewManager markerViewObj = new CustomMarkerViewManager(
                        chart.getContext(),
                        R.layout.marker_view,
                        markerMap
                );

                // Need to set it for proper rendering of piechart
                markerViewObj.setIsPieChart(true);

                chart.setMarkerView(markerViewObj);
            }
        }

        ReadableArray xArray=rm.getArray("xValues");
        ReadableArray ra=rm.getArray("yValues");

        ArrayList<String> xVals=new ArrayList<String>();
        for(int m=0;m<xArray.size();m++){
            xVals.add(xArray.getString(m));
        }

        int yLen = 0;

        if(ra.size() > 0){
            ReadableMap sampleMap = ra.getMap(0);

            if(sampleMap.hasKey("data")){
                ReadableArray yArray = sampleMap.getArray("data");
                yLen = yArray.size();
            }
        }
        
        /*
         * This fix is for when the piechart xVal have been kept empty,
         * while the yVal is kept empty.
         */
        // Make sure that both the xValues is atleast as big as yValues
        int len_diff = yLen - xVals.size();

        if(len_diff > 0){
            // If the xvals is less, then make them equal by adding dummy vals.
            for(int m=0; m < len_diff; m++){
                xVals.add("");
            }
        }

        PieData pieData=new PieData(xVals);
        pieData.setValueTextSize(16f);
        pieData.setValueTextColor(Color.WHITE);
        for(int i=0;i<ra.size();i++){
            ReadableMap map=ra.getMap(i);
            ReadableArray data=map.getArray("data");
            String label=map.getString("label");
            float[] vals=new float[data.size()];
            ArrayList<Entry> entries=new ArrayList<Entry>();
            for (int j=0;j<data.size();j++){
                vals[j]=(float)data.getDouble(j);
                Entry be=new Entry((float)data.getDouble(j),j);
                entries.add(be);
            }
            PieDataSet dataSet=new PieDataSet(entries,label);
            dataSet.setValueFormatter(new PercentFormatter());
            dataSet.setValueTextSize(14f);
            dataSet.setValueTextColor(Color.WHITE);
            ReadableMap config= map.getMap("config");
            if(config.hasKey("colors")){
                ReadableArray colorsArray = config.getArray("colors");
                ArrayList<Integer> colors = new ArrayList<>();
                for(int c = 0; c < colorsArray.size(); c++){
                    colors.add(Color.parseColor(colorsArray.getString(c)));
                }
                dataSet.setColors(colors);
            }else
            if(config.hasKey("color")) {
                int[] colors=new int[]{Color.parseColor(config.getString("color"))};
                dataSet.setColors(colors);
            }
            if(config.hasKey("drawValues")) dataSet.setDrawValues(config.getBoolean("drawValues"));
            if(config.hasKey("valueTextColor")) dataSet.setValueTextColor(Color.parseColor(config.getString("valueTextColor")));
            dataSet.setSliceSpace(3f);
            pieData.addDataSet(dataSet);

        }
        chart.setUsePercentValues(true);
        chart.setData(pieData);
        chart.invalidate();
    }
}

