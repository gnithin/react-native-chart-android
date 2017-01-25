package cn.mandata.react_native_mpchart;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.content.Context;
import android.widget.RelativeLayout.LayoutParams;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2015/11/6.
 */
public class MPLineChartManager extends MPBarLineChartManager {
    private String CLASS_NAME="MPLineChart";
    private Random random;//用于产生随机数

    private LineChart chart;
    private LineData data;
    private LineDataSet dataSet;
    private ThemedReactContext rContext;

    @Override
    public String getName() {
        return this.CLASS_NAME;
    }

    @Override
    protected LineChart createViewInstance(ThemedReactContext reactContext) {
        LineChart chart=new LineChart(reactContext);

        this.rContext = reactContext;

        // initialise event listener to bind to chart
        new MPChartSelectionEventListener(chart);

        return  chart;
    }

    //{XValues:[],YValues:[{Data:[],Label:""},{}]}
    @ReactProp(name="data")
    public void setData(LineChart chart,ReadableMap rm){
        if(rm.hasKey("marker")){
            /*
             * This is for configuring the marker view
             */
            ReadableMap markerMap = rm.getMap("marker");

            if(
                (!markerMap.hasKey("display")) ||
                (markerMap.hasKey("display") && markerMap.getBoolean("display"))
            ){
                CustomMarkerViewManager markerViewObj = new CustomMarkerViewManager(
                        this.rContext,
                        R.layout.marker_view,
                        markerMap
                );
                chart.setMarkerView(markerViewObj);
            }
        }

        if(rm.hasKey("highlightCircle")){
            ReadableMap highlightViewMap = rm.getMap("highlightCircle");

            if(
                    (!highlightViewMap.hasKey("display")) ||
                    (highlightViewMap.hasKey("display") && highlightViewMap.getBoolean("display"))
            ){
                chart.setDrawCustomHighlightViews(true);

                CustomHighlightViewManager highlightViewObj = new CustomHighlightViewManager(
                        this.rContext,
                        R.layout.marker_view,
                        highlightViewMap
                );

                // Sending all the objects to the drawer method.
                chart.setCustomHighlightView(highlightViewObj);
            }

        }

        ReadableArray xArray=rm.getArray("xValues");
        ArrayList<String> xVals=new ArrayList<String>();
        for(int m=0;m<xArray.size();m++){
            xVals.add(xArray.getString(m));
        }
        ReadableArray ra=rm.getArray("yValues");
        LineData chartData=new LineData(xVals);
        for(int i=0;i<ra.size();i++){
            ReadableMap map=ra.getMap(i);
            ReadableArray data=map.getArray("data");

            String label=map.getString("label");

            boolean isDisplayData = false;
            ReadableArray displayArr = null;

            if(map.hasKey("displayData")){
                displayArr = map.getArray("displayData");
                isDisplayData = true;
            }

            float[] vals=new float[data.size()];
            ArrayList<Entry> entries=new ArrayList<Entry>();
            for (int j=0;j<data.size();j++){
                if (!data.isNull(j)) {
                    vals[j]=(float)data.getDouble(j);

                    Entry be=new Entry((float)data.getDouble(j),j);
                    if(isDisplayData && displayArr != null){
                        be.setData(displayArr.getString(j));
                    }

                    entries.add(be);
                }
            }

            /*BarEntry be=new BarEntry(vals,i);
            entries.add(be);*/
            ReadableMap config= map.getMap("config");
            LineDataSet dataSet=new LineDataSet(entries,label);

            if(config.hasKey("drawCircles")) dataSet.setDrawCircles(config.getBoolean("drawCircles"));
            if(config.hasKey("circleSize")) dataSet.setCircleSize((float) config.getDouble("circleSize"));
            if(config.hasKey("lineWidth")) dataSet.setLineWidth((float) config.getDouble("lineWidth"));
            if(config.hasKey("drawValues")) dataSet.setDrawValues(config.getBoolean("drawValues"));
            if(config.hasKey("valueTextColor")) dataSet.setValueTextColor(Color.parseColor(config.getString("valueTextColor")));
            if(config.hasKey("drawHorizontalHighlightIndicator")){dataSet.setDrawHorizontalHighlightIndicator(config.getBoolean("drawHorizontalHighlightIndicator"));}
            if(config.hasKey("drawVerticalHighlightIndicator")){dataSet.setDrawVerticalHighlightIndicator(config.getBoolean("drawVerticalHighlightIndicator"));}

            // Text Size for bar value

            if(config.hasKey("valueTextSize")) dataSet.setValueTextSize((float)config.getDouble("valueTextSize"));

            if (config.hasKey("drawCircleHole")) dataSet.setDrawCircleHole(config.getBoolean("drawCircleHole"));
            if(config.hasKey("drawValues")) dataSet.setDrawValues(config.getBoolean("drawValues"));
            if(config.hasKey("drawStepped")) dataSet.setDrawStepped(config.getBoolean("drawStepped"));
            if(config.hasKey("colors")){
                ReadableArray colorsArray = config.getArray("colors");
                ArrayList<Integer> colors = new ArrayList<>();
                for(int c = 0; c < colorsArray.size(); c++){
                    colors.add(Color.parseColor(colorsArray.getString(c)));
                }
                dataSet.setColors(colors);
            }else if (config.hasKey("color")) {
                int[] colors=new int[]{Color.parseColor(config.getString("color"))};
                dataSet.setColors(colors);
            }
            if(config.hasKey("circleColors")){
                ReadableArray colorsArray = config.getArray("circleColors");
                ArrayList<Integer> colors = new ArrayList<>();
                for(int c = 0; c < colorsArray.size(); c++){
                    colors.add(Color.parseColor(colorsArray.getString(c)));
                }
                dataSet.setCircleColors(colors);
            }else if (config.hasKey("circleColor")) {
                int[] colors=new int[]{Color.parseColor(config.getString("circleColor"))};
                dataSet.setCircleColors(colors);
            }
            if (config.hasKey("dashedLine")) {
                String[] dashedLine = config.getString("dashedLine").split(" ");
                dataSet.enableDashedLine(Integer.parseInt(dashedLine[0]), Integer.parseInt(dashedLine[1]), Integer.parseInt(dashedLine[2]));
            }

            if (config.hasKey("drawFill")) dataSet.setDrawFilled(config.getBoolean("drawFill"));

            if (config.hasKey("fillGradient")){
                ReadableMap gradientProperties = config.getMap("fillGradient");
                GradientDrawable.Orientation defaultOrientation = GradientDrawable.Orientation.LEFT_RIGHT;
                GradientDrawable.Orientation orientation;
                if(gradientProperties.hasKey("angle")){
                    String angle = gradientProperties.getString("angle");

                    /*
                    Following the convention mentioned here -
                    https://developer.android.com/guide/topics/resources/drawable-resource.html#gradient-element
                     */
                    switch(angle){
                        case "0":
                            orientation = GradientDrawable.Orientation.LEFT_RIGHT;
                            break;
                        case "45":
                            orientation = GradientDrawable.Orientation.TL_BR;
                            break;
                        case "90":
                            orientation = GradientDrawable.Orientation.TOP_BOTTOM;
                            break;
                        case "135":
                            orientation = GradientDrawable.Orientation.TR_BL;
                            break;
                        case "180":
                            orientation = GradientDrawable.Orientation.RIGHT_LEFT;
                            break;
                        case "225":
                            orientation = GradientDrawable.Orientation.BR_TL;
                            break;
                        case "270":
                            orientation = GradientDrawable.Orientation.BOTTOM_TOP;
                            break;
                        case "315":
                            orientation = GradientDrawable.Orientation.BR_TL;
                            break;
                        default:
                            orientation = defaultOrientation;

                    }
                } else{
                    orientation = defaultOrientation;
                }

                // Note: This supports an array of colors. Just supporting two of them right now.
                int startColor;
                if(gradientProperties.hasKey("startColor")){
                    startColor = Color.parseColor(gradientProperties.getString("startColor"));
                }else{
                    // Setting a default value
                    startColor = 0x00000000;
                }

                int endColor;
                if(gradientProperties.hasKey("endColor")){
                    endColor = Color.parseColor(gradientProperties.getString("endColor"));
                }else{
                    // Setting a default value
                    endColor = 0xFFFFFF00;
                }

                GradientDrawable gd = new GradientDrawable(
                        orientation,
                        new int[] {startColor, endColor}
                );
                gd.setCornerRadius(0f);
                dataSet.setFillDrawable(gd);
            } else if (config.hasKey("fillColor")){
                dataSet.setFillColor(Color.parseColor(config.getString("fillColor")));
            }

            if (config.hasKey("fillAlpha")) dataSet.setFillAlpha((int)(255 * config.getDouble("fillAlpha")));
            if (config.hasKey("bezier")) dataSet.setDrawCubic(config.getBoolean("bezier"));

            chartData.addDataSet(dataSet);
        }

        chart.setData(chartData);

        /**
         * Graph animation configurations
         * If no animation config provided, call chart.invalidate()
         * animation configs are maps with the following keys
         * - duration or durationX/durationY in case of animateXY
         * - support for easeFunction yet to be given
         */
        if (rm.hasKey("animateX")) {
            chart.animateX(rm.getMap("animateX").getInt("duration"));
        } else if (rm.hasKey("animateY")) {
            chart.animateY(rm.getMap("animateY").getInt("duration"));
        } else if (rm.hasKey("animateXY")) {
            ReadableMap animationConfig = rm.getMap("animateXY");
            chart.animateXY(
                animationConfig.getInt("durationX"),
                animationConfig.getInt("durationY")
            );
        } else {
            chart.invalidate();
        }
    }
}

