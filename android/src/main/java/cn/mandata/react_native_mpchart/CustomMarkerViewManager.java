package cn.mandata.react_native_mpchart;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.content.Context;
import android.graphics.Canvas;
import android.util.TypedValue;
import java.lang.Math;
import android.graphics.Typeface;

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
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.highlight.Highlight;
import com.facebook.react.bridge.UnexpectedNativeTypeException;

import android.view.View;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import java.util.ArrayList;
import java.util.Random;
import android.util.Log;

/**
 * Created by Administrator on 2015/11/6.
 */
public class CustomMarkerViewManager extends MarkerView {
    private static final boolean ENABLE_LOG = true;

    private TextView markerTextView;
    private RelativeLayout markerViewWrapper;
    private float xoffset = 0;
    private float yoffset = 0;
    private String textStructure = "{}";
    private String logStr;
    private float midX, midY;
    private boolean isPieChart = false;
    private int backgroundColor = Color.WHITE;
    private int paddingVal;

    private boolean isAngularOffset = false;
    private float angularOffset = 0;

    public CustomMarkerViewManager (Context context, int layoutResource, ReadableMap markerMap) {
        super(context, layoutResource);

        // this markerview only displays a textview
        markerTextView = (TextView) findViewById(R.id.markerViewId);
        markerViewWrapper = (RelativeLayout) findViewById(R.id.markViewWrapper);

        setupOptions(markerMap);

        if(markerMap.hasKey("borderWidth")){
            // Adding the background border
            if(markerMap.getInt("borderWidth") > 0){
                markerViewWrapper.setBackground(getResources().getDrawable(R.drawable.background_border));
            }
        }
    }

    public void setupOptions(ReadableMap markerMap){
        // Setting things according to the markerMap
        if(markerMap.hasKey("backgroundColor")){
            try{
                this.setBackgroundColor(markerMap.getString("backgroundColor"));
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("SetupOptions", e.toString());
                }
            }
        }

        if(markerMap.hasKey("textColor")) {
            try{
                this.setTextColor(markerMap.getString("textColor"));
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("SetupOptions", e.toString());
                }
            }
        }

        if(markerMap.hasKey("textSize")){
            try{
                this.setTextSize((int)markerMap.getDouble("textSize"));
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("SetupOptions", e.toString());
                }
            }
        }

        if(markerMap.hasKey("height")){
            try{
                this.setLayoutHeight((int)markerMap.getDouble("height"));
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("SetupOptions", e.toString());
                }
            }
        }

        if(markerMap.hasKey("padding")){
            try{
                this.setParentPadding((int)markerMap.getDouble("padding"));
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("SetupOptions", e.toString());
                }
            }
        }

        if(markerMap.hasKey("xOffset")){
            try{
                this.setXOffset((float)markerMap.getDouble("xOffset"));
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("SetupOptions", e.toString());
                }
            }
        }

        if(markerMap.hasKey("yOffset")){
            try{
                this.setYOffset((float)markerMap.getDouble("yOffset"));
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("SetupOptions", e.toString());
                }
            }
        }

        if(markerMap.hasKey("textStructure")){
            try{
                this.setTextStructure(markerMap.getString("textStructure"));
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("SetupOptions", e.toString());
                }
            }
        }

        if(markerMap.hasKey("angularOffset")){
            try{
                this.angularOffset = ((float)markerMap.getDouble("angularOffset"));
                this.isAngularOffset = true;
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("SetupOptions", e.toString());
                }
            }
        }

        if(markerMap.hasKey("borderRadius")){
            try{
                this.setBorderRadius((float)markerMap.getDouble("borderRadius"));
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("SetupOptions", e.toString());
                }
            }
        }
        
        if(markerMap.hasKey("setBold")){
            try{
                if(markerMap.getBoolean("setBold")){
                    this.setTypeface(Typeface.BOLD);
                }
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("SetupOptions", e.toString());
                }
            }
        }

        if(markerMap.hasKey("setItalics")){
            try{
                if(markerMap.getBoolean("setItalics")){
                    this.setTypeface(Typeface.ITALIC);
                }
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("SetupOptions", e.toString());
                }
            }
        }
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String unformattedStr = String.valueOf(e.getVal());

        // The entry data needs to be populated here.
        Object data = e.getData();
        if(data != null){
            try{
                unformattedStr = String.valueOf(data);
            } catch(Exception formatStrFetchErr){
                if(ENABLE_LOG){
                    Log.d(this.getClass().getName(), formatStrFetchErr.toString());
                }
            }
        }

        String displayText = this.textStructure.replace("{}", unformattedStr);
        markerTextView.setText(displayText);
    }

    @Override
    public void preDraw(ChartData chartData, Highlight highlight){
        // Empty stub.
    }

    @Override
    public int getXOffset(float xpos) {
        float offset = this.xoffset;
        if(xpos < this.midX){
            offset = -offset;
        }
        return (int)(-(getWidth() / 2) + offset);
    }

    @Override
    public int getYOffset(float ypos) {
        // -getHeight() will cause the marker-view to be above the selected value
        float offset = this.yoffset;
        if(ypos < this.midY){
            offset = -offset;
        }
        return (int)(-(getHeight() / 2) + offset);
    }

    /*
     * PS: This is taken from the source code directly, because I needed a simple
     * way to fetch the dimensions of the screen for all different types of charts 
     * without changing too much in the rest of the charts.
     */
    /**
     * Draws the MarkerView on the given position on the screen with the given Canvas object.
     *
     * @param canvas
     * @param posx
     * @param posy
     */
    public void draw(Canvas canvas, float posx, float posy) {
        float originalX = posx;
        float originalY = posy;

        if(this.isPieChart){
            int height = canvas.getHeight();
            int width = canvas.getWidth();

            this.midX = width/2.0f;
            this.midY = height/2.0f;
        }

        if(this.isPieChart && this.isAngularOffset){
            // Calculate angular displacement
            float offset = this.angularOffset;
            float ydiff = posy - midY;
            float xdiff = posx - midX;

            double slope = Math.atan2(ydiff, xdiff);

            posx += offset * (Math.cos(slope));
            posy += offset * (Math.sin(slope));
        }

        // take offsets into consideration
        posx += getXOffset(posx);
        posy += getYOffset(posy);

        this.markerTextView.measure(0, 0);
        this.markerTextView.getMeasuredHeight();
        this.markerTextView.getMeasuredWidth();
        
        this.markerViewWrapper.measure(0, 0);
        int textViewHeight = this.markerViewWrapper.getMeasuredHeight();
        int textViewWidth = this.markerViewWrapper.getMeasuredWidth() + (2 * paddingVal);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        if((textViewHeight + posy) > canvasHeight){
            posy = canvasHeight - textViewHeight;
        } else if(posy < 0){
            posy = 0;
        }

        if((textViewWidth + posx) > canvasWidth) {
            if(this.isPieChart){
                posx = canvasWidth - textViewWidth;
            } else{
                float newOffset = posx - originalX;
                posx = posx - (2*newOffset) - textViewWidth;
            }
        }else if(posx < 0) {
            posx = 0;
        }

        // Invalidating the root view drawn
        // NOTE: This is necessary because without this the contents of a markerview are not updated
        // properly, even though you've added setText and the rest of the changes. This call is
        // absolutely important, do NOT remove this. It does not seem to be a requirement in non-
        // react-native environments. Only in a RN app this is needed
        this.markerViewWrapper.invalidate();

        // translate to the correct position and draw
        canvas.translate(posx, posy);
        draw(canvas);
        canvas.translate(-posx, -posy);
    }

    public void setXOffset(float xoffset){
        this.xoffset = xoffset;
    }

    public void setYOffset(float ypos){
        this.yoffset = ypos;
    }

    public void setBackgroundColor(String colorString){
        this.backgroundColor = Color.parseColor(colorString);
        markerViewWrapper.setBackgroundColor(this.backgroundColor);
    }

    public void setParentPadding(int paddingVal){
        this.paddingVal = paddingVal;
        this.markerViewWrapper.setPadding(paddingVal, paddingVal, paddingVal, paddingVal);
    }

    public void setTextColor(String colorString ) {
        markerTextView.setTextColor(Color.parseColor(colorString));
    }

    public void setTextStructure(String textStructure){
        this.textStructure = textStructure;
    }

    public void setTextSize(int textSize){
        this.markerTextView.setTextSize(
                TypedValue.COMPLEX_UNIT_SP, 
                textSize
        );
    }

    public void setIsPieChart(boolean isPieChart){
        this.isPieChart = isPieChart;
        this.xoffset = 0;
        this.yoffset = 0;
    }

    public void setLayoutHeight(int height){
        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                height
        );

        this.markerViewWrapper.setLayoutParams(layout);
    } 

    public void setBorderRadius(float borderRadius){
        ShapeDrawable markerViewDrawable = new ShapeDrawable();
        this.markerViewWrapper.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        int backgroundColor = this.backgroundColor;

        int size = 8;
        float[] radii = new float[8];
        for(int i=0;i<size;i++) {
            radii[i] = borderRadius;
        }

        markerViewDrawable.setShape(new RoundRectShape(radii, null, null));
        markerViewDrawable.getPaint().setColor(backgroundColor);

        this.markerViewWrapper.setBackgroundDrawable(markerViewDrawable);
    }

    public void setTypeface(int attr){
        this.markerTextView.setTypeface(null, attr);
    }
}

