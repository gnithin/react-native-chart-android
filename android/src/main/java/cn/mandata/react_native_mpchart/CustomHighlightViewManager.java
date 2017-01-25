package cn.mandata.react_native_mpchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UnexpectedNativeTypeException;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * Created by Administrator on 2015/11/6.
 */
public class CustomHighlightViewManager extends MarkerView {
    private static final boolean ENABLE_LOG = true;

    private TextView markerTextView;
    private RelativeLayout markerViewWrapper;
    private float xoffset = 0;
    private float yoffset = 0;
    private boolean dynHighlightColor = true;

    private int outerColor = Color.WHITE;
    private int innerColor = Color.BLACK;

    private int outerRadius = 20;
    private int innerRadius = 10;

    public CustomHighlightViewManager(Context context, int layoutResource, ReadableMap markerMap) {
        super(context, layoutResource);

        // The custom highlightView basically just draws the highlightView directly.
        // As of now, no resource is needed.
        setupOptions(markerMap);
    }

    public void setupOptions(ReadableMap markerMap){
        Log.d("HighlightCircle", "Setting up options");

        // Setting things according to the markerMap
        /*
        outerColor
        innerColor
        outerRadius
        innerRadius
        dynChangeHighlightColor
         */
        if(markerMap.hasKey("outerColor")){
            try {
                // Set the outerCOlor here
                this.setOuterColor(markerMap.getString("outerColor"));
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("CustomHighlightView - ", e.toString());
                }
            }
        }

        if(markerMap.hasKey("innerColor")){
            try {
                // Set the innerColor here
                this.setInnerColor(markerMap.getString("innerColor"));
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("CustomHighlightView - ", e.toString());
                }
            }
        }

        if(markerMap.hasKey("outerRadius")){
            try {
                // Set the outerRadius here
                this.setOuterRadius(markerMap.getInt("outerRadius"));
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("CustomHighlightView - ", e.toString());
                }
            }
        }

        if(markerMap.hasKey("innerRadius")){
            try {
                // Set the innerRadius here
                this.setInnerRadius(markerMap.getInt("innerRadius"));
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("CustomHighlightView - ", e.toString());
                }
            }
        }

        if(markerMap.hasKey("dynChangeHighlightColor")){
            try {
                this.setDynHighlightColor(markerMap.getBoolean("dynChangeHighlightColor"));
            }catch(UnexpectedNativeTypeException e){
                if(ENABLE_LOG){
                    Log.d("CustomHighlightView - ", e.toString());
                }
            }
        }


    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        // Empty Stub
    }

    @Override
    public void preDraw(ChartData chartData, Highlight highlight){
        if(this.dynHighlightColor){
            int currentIndex = highlight.getDataSetIndex();
            int[] chartColors = chartData.getColors();
            int chartColorsLen = chartColors.length;

            if(chartColors != null && chartColorsLen > 0 && currentIndex < chartColorsLen && currentIndex > 0){
                this.outerColor= chartColors[currentIndex];
            }

        }
    }

    @Override
    public int getXOffset(float xpos) {
        return (int)xpos;
    }

    @Override
    public int getYOffset(float ypos) {
        return (int)ypos;
    }

    public void setXOffset(float xoffset){
        this.xoffset = xoffset;
    }

    public void setYOffset(float ypos){
        this.yoffset = ypos;
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
    @Override
    public void draw(Canvas canvas, float posx, float posy) {
        Log.d("HighlightCircle", "Drawing the circle");


        // Draw the custom view over here at the point.
        this.drawCircles(canvas, posx, posy);
    }

    protected void drawCircles(Canvas c, float x, float y) {
        Paint mRenderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // Outer Circle
        mRenderPaint.setStyle(Paint.Style.FILL);

        mRenderPaint.setColor(this.outerColor);
        c.drawCircle(x, y, this.outerRadius, mRenderPaint);

        // Inner Circle
        mRenderPaint.setStyle(Paint.Style.FILL);
        mRenderPaint.setColor(this.innerColor);
        c.drawCircle(x, y, this.innerRadius, mRenderPaint);
    }

    public void setOuterColor(String outerColor){
        this.outerColor = Color.parseColor(outerColor);
    }

    public void setInnerColor(String innerColor){
        this.innerColor = Color.parseColor(innerColor);
    }

    public void setOuterRadius (int outerRadius){
        this.outerRadius = outerRadius;
    }

    public void setInnerRadius (int innerRadius){
        this.innerRadius = innerRadius;
    }

    public void setDynHighlightColor (boolean isEnabled) { this.dynHighlightColor = isEnabled; }
}

