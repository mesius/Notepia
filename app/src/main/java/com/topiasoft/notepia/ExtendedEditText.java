package com.topiasoft.notepia;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by fm on 29/11/2016.
 */

public class ExtendedEditText extends EditText {

    private Paint p1;
    private Paint p2;
    private Rect r;
    private float escala;
    private Rect mRect;
    private Paint mPaint;

    public ExtendedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inicialization();
    }
    public ExtendedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        inicialization();
    }

    public ExtendedEditText(Context context) {
        super(context);

        inicialization();
    }



    private void inicialization()
    {
        p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p1.setColor(Color.BLACK);
        p1.setStyle(Paint.Style.FILL_AND_STROKE);
        //p1.setColor(Color.parseColor("#D3D3D3"));
        p1.setColor(Color.parseColor("#CCCCCC"));
        //p1.setColor(Color.YELLOW);
        //p1.setStyle(Style.FILL);

        p2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p2.setColor(Color.YELLOW);
        p2.setTextSize(20);

        r = new Rect();


        //escala = getResources().getDisplayMetrics().density;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        //Llamamos al metodo de la clase base (EditText)
        super.onDraw(canvas);

        int height = canvas.getHeight();
        int line_height = getLineHeight();

        int count = height / line_height;

        if (getLineCount() > count)
            count = getLineCount();



        //Paint paint = mPaint;


        //mRect = new Rect;

        int baseline = getLineBounds(0, r);


        for (int i = 0; i < count; i++) {

            canvas.drawLine(r.left, baseline + 5, r.right, baseline + 5, p1);
            //canvas.drawLine(0, 0, 100, 50, p2);
            baseline += getLineHeight();
        }



    }
}
