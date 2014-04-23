package com.neatocode.gyroimageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
    Paint paint = new Paint();
    float startX, startY, stopX, stopY;
    
    public void setXY(float x_start, float y_start, float x_stop, float y_stop) {
    	startX = x_start;
    	startY = y_start;
    	stopX = x_stop;
    	stopY = y_stop;
    }
    
    public DrawView(Context context) {
        super(context);
        setBackgroundColor(Color.TRANSPARENT);
        paint.setColor(Color.GREEN);
    }
    
    
    @Override
    public void onDraw(Canvas canvas) {
    		canvas.drawLine(0, 0, stopX, stopY, paint);
            canvas.drawLine(startX, 0, 0, stopY, paint);
    }

}