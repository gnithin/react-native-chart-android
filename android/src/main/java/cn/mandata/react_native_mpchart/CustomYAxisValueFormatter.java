package cn.mandata.react_native_mpchart;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

/**
 * Created by Administrator on 2015/11/7.
 */
public class CustomYAxisValueFormatter implements YAxisValueFormatter {
   private String format="";
   public CustomYAxisValueFormatter(String format){
       this.format = format;
   }
    @Override
    public String getFormattedValue(float v, YAxis yAxis) {
        if(format.equalsIgnoreCase("custom")){
            return customFormatter(v);
        } else{
            return String.format(format, v);
        }
    }

    public String customFormatter(float v){
        String finalStr = "";

        String prefix = "$ ";
        String suffix = "";
        String numStr = "";

        if(v < 1000){
            int numVal = (int)(Math.round(v));
            numStr = String.format("%d", numVal);
        } else{
            String []expSuffixArr = {"K", "M", "B"};
            int expVal = (int)((Math.floor(Math.log10((double)v)))/3);
            int expIndex = expVal - 1;

            if (expIndex >= expSuffixArr.length){
                expIndex = expSuffixArr.length - 1;
            }

            suffix += expSuffixArr[expIndex];

            int modifiedVal = (int)(Math.round(v/(Math.pow(10, expVal*3))));
            numStr = String.format("%d", modifiedVal);
        }

        finalStr = prefix + numStr + suffix;

        return finalStr;
    }
}
