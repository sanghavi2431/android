package in.woloo.www.period_tracker.circular_calender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import in.woloo.www.R;

public class PeriodCalendarView extends View {

    private int height,width = 0;
    private int padding = 0;
    private int fontSize = 0;
    private int radius = 0;
    private Paint paint;
    private boolean isInit;
    private List<Integer> days = new ArrayList<Integer>();
    private Rect rect = new Rect();
    private RectF buttonRect = new RectF();
    private int boxPadding = 40;
    private PERIOD_TYPES periodType = PERIOD_TYPES.PERIOD;
    private int TWO = 2;
   private int[] todayColors = {Color.parseColor("#F7EB30"), Color.parseColor("#F7C330")};
    // Following line added By Aarati to Change color of Today dot @woloo on 22 July 2024
   private int[] todayColorsNew = {Color.parseColor("#414042"), Color.parseColor("#414042")};

    private List<Integer> menstruationDays = Arrays.asList();
    private List<Integer> ovulationDays = Arrays.asList();
    private List<Integer> pregnancyDays = Arrays.asList();
    private int currentMonth = 0;
    private int currentYear = 0;
    private int currentDay = 0;
    private int periodDays = 3;

    private PeriodCalendarViewListener periodCalendarViewListener = null;

    List<Integer> colors = new ArrayList<Integer>();

    public PeriodCalendarView(Context context){
        super(context);
    }

    public PeriodCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PeriodCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PeriodCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void initPeriodCalendar(){
        height = getHeight();
        width = getWidth();
        padding = (int)(width * 0.05); //50;
        int fontSizeOffset = (int)(width * 0.0125); //13;
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,fontSizeOffset,getResources().getDisplayMetrics());
        int min = Math.min(height, width);
        radius = min / TWO - padding;
        paint = new Paint();

        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);
        currentDay = c.get(Calendar.DATE);

        resetCalendar();

        isInit = true;
    }

    private void resetCalendar(){
        colors.clear();
        days.clear();

        Calendar c = new GregorianCalendar(currentYear, currentMonth, 1);
        int daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);


        for(int i = 1; i <= daysInMonth; i++) {
            days.add(i);
        }

        for(int number : days){
            int color = 0;

            if(menstruationDays.indexOf(number) != -1){
                color = PERIOD_TYPES.MENSTRUATION.color();
            }
            else if(pregnancyDays.indexOf(number) != -1){
                color = PERIOD_TYPES.PREGNANCY.color();
            }
            else if(ovulationDays.indexOf(number) != -1){
                color = PERIOD_TYPES.OVULATION.color();
            }else{
                color = PERIOD_TYPES.PERIOD.color();
            }

            colors.add(color);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float cx = event.getX();
        float cy = event.getY();

        if(buttonRect.contains(cx,cy)){
            if(periodCalendarViewListener != null){
                periodCalendarViewListener.onEdit();
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isInit){
            initPeriodCalendar();
        }

        int innerCircleOffset = (int)(width * 0.0833); //90

        canvas.drawColor(Color.WHITE);
        drawArcs(canvas);
        drawCircle(canvas,6,boxPadding - 1,getResources().getColor(android.R.color.white));
        drawCircle(canvas,7,innerCircleOffset + boxPadding,getResources().getColor(android.R.color.white));
        drawCenter(canvas);
        drawDays(canvas);
        drawPeriodCircle(canvas,periodType);
        drawTitle(canvas);
        drawDaysTitle(canvas);
//        drawPeriodTitles(canvas); //TODO: need to implement it properly
        drawButton(canvas);

//        postInvalidateDelayed(500); //TODO: Do not uncomment it
        invalidate();
    }

    @Deprecated
    private void drawPeriodTitles(Canvas canvas) {
        int fontSizeOffset = (int)(width * 0.0125); //12;
        int fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,fontSizeOffset,getResources().getDisplayMetrics());
        paint.reset();
        paint.setTextSize(fontSize);
//        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        try {
            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.inter_semibold);
            paint.setTypeface(typeface);
        }catch(Exception e){

        }
        paint.setColor(getResources().getColor(android.R.color.black));
        paint.setStyle(Paint.Style.STROKE);
        float offset = 150f;
        float sweepAngle = 64.28f;
        RectF enclosingRect = new RectF(offset, offset, width - offset, height - offset);

        //TODO: only for debugging purpose
//        float cx = width / TWO; //Center of the circle
//        float cy = height / TWO; //Center of the circle
//
//        canvas.drawCircle(cx, cy, (radius + padding - offset) , paint);

        paint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        path.addArc(enclosingRect, 106.28f, -sweepAngle);
        canvas.drawTextOnPath(PERIOD_TYPES.OVULATION.toString(), path, 0f, 0f, paint);

        offset = 175;
        enclosingRect = new RectF(offset, offset, width - offset, height - offset);

        //TODO: only for debugging purpose
//        canvas.drawCircle(cx, cy, (radius + padding - offset) , paint);

        path.reset();
        path.addArc(enclosingRect, 174.28f, sweepAngle);
        canvas.drawTextOnPath(PERIOD_TYPES.PREGNANCY.toString(), path, 0f, 0f, paint);

        path.reset();
        path.addArc(enclosingRect, -83.28f, sweepAngle);
        canvas.drawTextOnPath(PERIOD_TYPES.MENSTRUATION.toString(), path, 0f, 0f, paint);

    }

    private void drawPeriodCircle(Canvas canvas,PERIOD_TYPES type) {
        int circleOffset = (int)(width * 0.085);
        int innerCircleOffset = (int)(width * 0.070);
        int paddingOffset = circleOffset + boxPadding;//94
        paint.reset();

        paint.setColor(type.color());
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawCircle(width / TWO, height/ TWO, radius + padding - paddingOffset, paint);

        paint.setColor(type.innerColor());
        canvas.drawCircle(width / TWO, height / TWO, radius + padding - (paddingOffset + innerCircleOffset), paint);


        //Draw background
        BitmapFactory.Options bg_options = new BitmapFactory.Options();
        bg_options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.pt_background, bg_options);

        int bitmapWidth = bg_options.outWidth;
        int bitmapHeight = bg_options.outHeight;

        int topOffset = (int)(width * 0.01);
        int widthOffset = (int)(width * 0.07);
        int heightOffset = (int)(height * 0.05);

        int left = (width - bitmapWidth ) / TWO;
        int top = (height - bitmapHeight ) / TWO;

        left -= widthOffset;
        top += topOffset; //50

        int right = left + bitmapWidth + (widthOffset * 2);
        int bottom = top + bitmapHeight + (heightOffset * 2);

        Bitmap bg_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pt_background);
        canvas.drawBitmap(bg_bitmap,null,new Rect(left ,top,right,bottom),paint);
        //Draw background

        //Draw icon
        int icon = 0;
        BitmapFactory.Options icon_options = new BitmapFactory.Options();
        icon_options.inJustDecodeBounds = true;
        switch (type){
            case MENSTRUATION:
                icon = R.drawable.menstrual_cup;
                break;
            case OVULATION:
                icon = R.drawable.fertilization;
                break;
            case PREGNANCY:
                icon = R.drawable.fetus;
                break;
                //Commented by Aarati to hide icon @Woloo on 22 July 24
          /*  case PERIOD:
                icon = R.drawable.ic_normal_cycle;
                break;*/
        }

        if(icon > 0){
            BitmapFactory.decodeResource(getResources(), icon, icon_options);

            int iconWidthOffset = (int)(width * 0.04); //10
            double offset = 0.05;
            if(type == PERIOD_TYPES.PREGNANCY || type == PERIOD_TYPES.PERIOD){
                offset = 0.04;
            }
            int iconHeightOffset = (int)(height * offset); //10
            int iconTopOffset = (int)(height * 0.17); //100
            int iconWidth = icon_options.outWidth + iconWidthOffset;
            int iconHeight = icon_options.outHeight + iconHeightOffset;

            left = (width - iconWidth ) / TWO;
            top = radius + padding - (paddingOffset + iconTopOffset);

            right = left + iconWidth;
            bottom = top + iconHeight;

            Bitmap icon_bitmap = BitmapFactory.decodeResource(getResources(), icon);
            canvas.drawBitmap(icon_bitmap,null,new Rect(left ,top,right,bottom),paint);
        }

        //Draw icon

    }

    private void drawArcs(Canvas canvas) {
        RectF box = new RectF(boxPadding,boxPadding,width - boxPadding,height - boxPadding);

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);

        float start = -90;
        float angle = (360.0f / (float) days.size()) * (float) 1;
        int index = 0;

        float menstruationStartAngle = 0f;
        float ovulationStartAngle = 0f;
        float pregnancyStartAngle = 0f;

        if(days.size() > 0){
            for(int number : days){
                paint.setColor(colors.get(index));
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawArc(box,start,angle ,true,paint);
                paint.setColor(getResources().getColor(android.R.color.white));
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(box,start,angle ,true,paint);

                if(menstruationDays.indexOf(number) != -1 && menstruationStartAngle == 0f){
                    menstruationStartAngle = start;
                }
                else if(pregnancyDays.indexOf(number) != -1 && pregnancyStartAngle == 0f){
                    pregnancyStartAngle = start;
                }
                else if(ovulationDays.indexOf(number) != -1 && ovulationStartAngle == 0f){
                    ovulationStartAngle = start;
                }

                start += angle;
                index++;
            }
        }

//        Log.v("Start Menstruation",String.valueOf(menstruationStartAngle));
//        Log.v("Start Ovulation",String.valueOf(ovulationStartAngle));
//        Log.v("Start Pregnancy",String.valueOf(pregnancyStartAngle));
    }

    private void drawDays(Canvas canvas) {
        paint.reset();
        paint.setTextSize(fontSize);
        try {
            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.inter_semibold);
            paint.setTypeface(typeface);
        }catch(Exception e){

        }
//        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        paint.setColor(getResources().getColor(android.R.color.black));

        float gapBetween = (float) ((float) days.size() / TWO);

//        28 = + 0.10
//        29 = + 0.05
//        30 = - 0.01
//        31 = - 0.05

        float offset = 0.0f;
        switch (days.size()){
            case 28:
                offset = 0.10f;
                break;
            case 29:
                offset = 0.05f;
                break;
            case 30:
                offset = - 0.01f;
                break;
            case 31:
                offset = - 0.05f;
                break;
        }

        int min = Math.min(height, width);
        int lineRadius = min / TWO - 10;

        float cx = 0f;
        float cy = 0f;

        for(int number : days){
            String tmp = String.valueOf(number);
            paint.getTextBounds(tmp,0,tmp.length(), rect);
            double angle = (Math.PI / gapBetween * (number - 8)) + offset;
            int x = (int) (width / TWO + Math.cos(angle) * (radius - boxPadding + 5) - rect.width() / TWO);
            int y = (int) (height / TWO + Math.sin(angle) * (radius - boxPadding + 5) + rect.height() / TWO);
            paint.setColor(getResources().getColor(android.R.color.black));
            canvas.drawText(tmp,x,y,paint);

            if(number == currentDay){
                cx =(float) (width / TWO + Math.cos(angle) * lineRadius);
                cy = (float) (height / TWO + Math.sin(angle) * lineRadius);
            }
        }

        //Draw Today
        if(cx > 0 && cy > 0){
            drawToday(canvas,10,todayColorsNew[0],todayColorsNew[1],cx,cy);
        }

    }

    private void drawCenter(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / TWO,height / TWO,12, paint);
    }

    private void drawCircle(Canvas canvas,int strokeWidth, int paddingOffset,int color) {
        paint.reset();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(width / TWO, height / TWO, radius + padding - paddingOffset, paint);
    }

    private void drawToday(Canvas canvas,int radius,int startColor,int endColor,float cx, float cy){
        Shader shader = new LinearGradient(0, cy, 0, radius, startColor, endColor, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        canvas.drawCircle(cx,cy,radius,paint);
    }

    private void drawTitle(Canvas canvas){
        int fontSizeOffset = (int)(width * 0.0125); //12;
        int fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,fontSizeOffset,getResources().getDisplayMetrics());
        paint.reset();
        paint.setTextSize(fontSize);
        try {
            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.inter_regular);
            paint.setTypeface(typeface);
        }catch(Exception e){

        }
        paint.setColor(getResources().getColor(android.R.color.black));

        String title = String.format("%s Cycle",periodType.toString());

        paint.getTextBounds(title,0,title.length(), rect);

        int cx = (width - rect.width()) / TWO;
        int cy = (height - rect.height()) / TWO;
        int topOffset = (int)(height * 0.06); //65
        canvas.drawText(title,cx,cy - topOffset,paint);

    }

    private void drawDaysTitle(Canvas canvas){
        int fontSizeOffset = (int)(width * 0.028); //30;
        int fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,fontSizeOffset,getResources().getDisplayMetrics());
        paint.reset();
        paint.setTextSize(fontSize);
       try{
           Typeface typeface = ResourcesCompat.getFont(getContext(),R.font.inter_bold);
           paint.setTypeface(typeface);
       }catch (Exception e){
//             CommonUtils.printStackTrace(e)
       }
//        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        paint.setColor(periodType.color());

        String title = "Day " + periodDays;

        paint.getTextBounds(title,0,title.length(), rect);

        int cx = (width - rect.width()) / TWO;
        int cy = (height - rect.height()) / TWO;
        int topOffset = (int)(height * 0.0555); //60
        canvas.drawText(title,cx,cy  + topOffset,paint);
    }

    private void drawButton(Canvas canvas){
        int buttonSize = (int)(width * 0.09);
        int topOffset = (int)(height * 0.09);
        int buttonRadius = 25;
        int cx = (width - buttonSize) / TWO;
        int cy = (height - buttonSize) / TWO;

        cy+=topOffset;

        int right = cx + buttonSize;
        int bottom = cy + buttonSize;

        buttonRect = new RectF(cx, cy, right, bottom);

        Shader shader = new LinearGradient(0, buttonRect.bottom, 0, buttonRect.top, todayColors[0], todayColors[1], Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        canvas.drawRoundRect(buttonRect,buttonRadius,buttonRadius,paint);

        BitmapFactory.Options bg_options = new BitmapFactory.Options();
        bg_options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.pencil, bg_options);

        int bitmapWidth = bg_options.outWidth;
        int bitmapHeight = bg_options.outHeight;
        int offset = bitmapWidth;
        if(bitmapHeight > bitmapWidth){
            offset = bitmapHeight;
        }

        int iconOffset = (int)(offset * 0.7);

        RectF buttonIconRect = new RectF(cx + iconOffset, cy + iconOffset, right - iconOffset, bottom - iconOffset);

        Bitmap bg_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pencil);
        canvas.drawBitmap(bg_bitmap,null,buttonIconRect,paint);
    }

    public void setCalendar(int day, int month, int year){
        currentMonth = month;
        currentYear = year;
        currentDay = day;

        resetCalendar();

        invalidate();
        requestLayout();

    }

    public void setPeriodType(PERIOD_TYPES type){

        periodType = type;

        invalidate();
        requestLayout();
    }

    public PERIOD_TYPES getPeriodType(){
        return periodType;
    }

    public void setPeriodCycle(List<Integer> menstruation, List<Integer> ovulation, List<Integer> pregnancy){
        menstruationDays = menstruation;
        ovulationDays = ovulation;
        pregnancyDays = pregnancy;

        invalidate();
        requestLayout();
    }

    public void setPeriodCalendarViewListener(PeriodCalendarViewListener listener){
        periodCalendarViewListener = listener;
    }

    public void setPeriodDays(int days){
        periodDays = days;
        invalidate();
        requestLayout();
    }
}
