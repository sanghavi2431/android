package in.woloo.www.period_tracker.circular_calender;
import android.graphics.Color;

public enum PERIOD_TYPES {
    PERIOD("Normal",0,"#f7eb30","#F7C330"),
    MENSTRUATION("Menstruation",1,"#f8646b","#fee8e9"),
    OVULATION("Ovulation",2,"#2abdc4","#dff5f6"),
    PREGNANCY("High Fertility",3,"#ed8524","#fcedde");



    private String stringValue;
    private int intValue;
    private int intColor;
    private int intInnerColor;

    private PERIOD_TYPES(String toString, int value, String colorString, String innerColorString){
        stringValue = toString;
        intValue = value;
        intColor = Color.parseColor(colorString);
        intInnerColor = Color.parseColor(innerColorString);
    }

    @Override
    public String toString(){
        return stringValue;
    }

    public int color(){
        return intColor;
    }
    public int innerColor(){
        return intInnerColor;
    }
}
