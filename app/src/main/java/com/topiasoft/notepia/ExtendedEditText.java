package com.topiasoft.notepia;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by fm on 29/11/2016.
 */


/***
 * Otra:  https://www.codeproject.com/Tips/524556/Android-customize-EditText-like-a-page-in-textbook
 */

public class ExtendedEditText extends EditText {

    private Paint p1;
    private Paint p2;
    private Rect r;
    private float escala;
    private Rect mRect;
    private Paint mPaint;

    private Rect rect;
    private Paint paint;

    public ExtendedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inicialization();
    }
    public ExtendedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        //this.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|
          //      InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        inicialization();

        /*
        setLineSpacing(10,1);
        mRect = new Rect();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // define the style of line
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // define the color of line
        mPaint.setColor(Color.parseColor("#CCCCCC"));
        //mPaint.setTextSize(30);
        */
    }

    public ExtendedEditText(Context context) {
        super(context);

        inicialization();
    }



    private void inicialization()
    {

        /*
        setLineSpacing(10,1);
        mRect = new Rect();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // define the style of line
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // define the color of line
        mPaint.setColor(Color.parseColor("#CCCCCC"));
        //mPaint.setTextSize(30);
        */


        /*
        setLineSpacing(10,1);
        p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p1.setColor(Color.BLACK);
        p1.setStyle(Paint.Style.FILL_AND_STROKE);
        //p1.setColor(Color.parseColor("#D3D3D3"));
        p1.setColor(Color.parseColor("#CCCCCC"));
        */

        //p1.setColor(Color.YELLOW);
        //p1.setStyle(Style.FILL);
/*
        p2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p2.setColor(Color.YELLOW);
        p2.setTextSize(20);
*/
        //r = new Rect();


        //escala = getResources().getDisplayMetrics().density;

        //setLineSpacing(10,1);
        rect = new Rect();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        //paint.setColor(Color.parseColor(context.getString((R.color.lined_edit_line))));
        paint.setColor(Color.parseColor("#CCCCCC"));

    }

    @Override
    public void onDraw(Canvas canvas) {
        //Llamamos al metodo de la clase base (EditText)
        //super.onDraw(canvas);
        //setLineSpacing(20,1);
/*
        int height = getHeight();
        int line_height = getLineHeight();

        int count = height / line_height;

        if (getLineCount() > count){
            count = getLineCount();
        }

        Rect r = mRect;
        Paint paint = mPaint;

        // first line
        int baseline = getLineBounds(0, r);

        // draw the remaining lines.
        for (int i = 0; i < count; i++) {
            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
            // next line
            baseline += getLineHeight();
        }
*/
        int height = getHeight();
        int line_height = getLineHeight();

        int count = height / line_height;

        if (getLineCount() > count) {
            count = getLineCount();
        }

        int baseline = getLineBounds(0, rect);

        for (int i = 0; i < count; i++) {

            canvas.drawLine(rect.left, baseline + 1, rect.right, baseline + 1, paint);
            baseline += getLineHeight();
        }

/*
        int baseline = getLineBounds(0, r);
        //Log.e("Linea ", toString(baseline) );

        for (int i = 0; i < count; i++) {

            //canvas.drawLine(r.left, baseline + 5, r.right, baseline + 5, p1);
            canvas.drawLine(r.left, baseline + 5, r.right, baseline + 5, p1);
            //canvas.drawLine(0, 0, 100, 50, p2);
            baseline += getLineHeight();

        }
*/
        super.onDraw(canvas);

    }


}
