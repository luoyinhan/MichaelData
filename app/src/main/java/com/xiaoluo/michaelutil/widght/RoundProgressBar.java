package com.xiaoluo.michaelutil.widght;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.xiaoluo.michaelutil.R;


/**
 * ??iphone?????????????????????View?????????????我??????
 *
 * @author michaelluo
 */
public class RoundProgressBar extends View {
    /**
     * ????????????
     */
    private Paint paint;

    private Paint smallcicrlPaint;

    /**
     * ??????
     */
    private static final int[] SECTION_COLORS = {Color.BLUE, Color.YELLOW, Color.RED};

    /**
     * ????????
     */
    private int roundColor;

    /**
     * ???????????
     */
    private int roundProgressColor;

    /**
     * ?技??????????????????
     */
    private int textColor;

    /**
     * ?技???????????????????
     */
    private float textSize;

    /**
     * ???????
     */
    private float roundWidth;

    /**
     * ?????????
     */
    private int max = 100;

    /**
     * ???????
     */
    private int progress;

    /**
     * ??????????
     */
    private int round = 0;

    /**
     * ???????技?????
     */
    private boolean textIsDisplayable;

    /**
     * ????????????????
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    private boolean isDrawBG = false;

    private Context mContext;

    public boolean isDrawBG() {
        return isDrawBG;
    }

    public void setDrawBG(boolean isDrawBG) {
        this.isDrawBG = isDrawBG;
    }

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        paint = new Paint();
        smallcicrlPaint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        // ?????????????????
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.BLUE);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 20);
        roundWidth = 30;
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);//??????????
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
        mTypedArray.recycle();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * ????????????
         */
        int centre = getWidth() / 2; // ???????x????
        int radius = (int) (centre - roundWidth / 2 - 20); // ??????
        paint.setColor(Color.parseColor("#DDDDDD")); // ????????????
        paint.setStyle(Paint.Style.STROKE); // ???????
        paint.setStrokeWidth(roundWidth); // ???????????
        paint.setAntiAlias(true); // ???????
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        paint.setPathEffect(effects);
        paint.setShader(null);
        RectF oval = new RectF(centre - radius, centre - radius, (centre + radius), centre + radius); // ???????????????????妊?????
        if (isDrawBG) {
            // canvas.drawCircle(centre, centre, radius, paint); // ???????
            canvas.drawArc(oval, 150, 240 * 1, false, paint); // ???????????
        }
        /**
         * ??妊?????
         */
        smallcicrlPaint.setColor(Color.parseColor("#DDDDDD")); // ????????????
        smallcicrlPaint.setStyle(Paint.Style.STROKE); // ???????
        smallcicrlPaint.setStrokeWidth(5); // ???????????
        smallcicrlPaint.setAntiAlias(true); // ???????
        int width = 800;
        //?????????????
        if (width < 720) {
            width = 10;
        } else {
            width = 25;
        }
        RectF smalloval = new RectF(centre - radius + width, centre - radius + width, (centre + radius - width), centre + radius - width);
        // canvas.drawCircle(centre, centre, radius - 25, smallcicrlPaint);
        // ???????
        canvas.drawArc(smalloval, 150, 240 * 1, false, smallcicrlPaint); // ???????????
        /**
         * ?????????
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); // ????????
        int percent = (int) (((float) progress / (float) max) * 100);
//        ?技????????????????float????忍?????????????0
        float textWidth = paint.measureText(percent + "%");
//        ???????????????????????????????????????技?
        if (textIsDisplayable && percent != 0 && style == STROKE) {
            paint.setTextSize(80);
            canvas.drawText(percent + "%", centre - textWidth * 2,
                    centre + textSize / 2, paint); // ???????????
        }
        /**
         * ????? ????????????
         */
        // ???????完??
        SweepGradient lg = null;
        // float[] positions = new float[]{0.2f, 0.5f, 0.8f, 1.0f};
        // lg = new LinearGradient(0, 0, 100, 100, SECTION_COLORS, null,
        // Shader.TileMode.MIRROR);
        lg = new SweepGradient(centre, centre, SECTION_COLORS, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(90, centre, centre);
        lg.setLocalMatrix(matrix);
        paint.setTextSize(textSize);
        if (lg != null) {
            paint.setShader(lg);
        }
        // ??????????????????
        paint.setStrokeWidth(roundWidth); // ???????????
        // paint.setColor(roundProgressColor); // ???????????
        paint.setColor(Color.BLUE);//
        paint.setPathEffect(null);
        // ???????????
        // RectF oval = new RectF(centre - radius, centre - radius,
        // (centre + radius), centre + radius); // ???????????????????妊?????

        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 150, 240 * progress / max, false, paint); // ???????????
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0) {
                    canvas.drawArc(oval, -90, 360 * progress / max, true, paint); // ???????????
                }
                break;
            }
        }
    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * ????????????
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * ???????.??????
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * ?????????????????????????????????????????? ?????????postInvalidate()?????UI??????
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();//????????????
        }
    }

    /**
     * ???loading ????????快???
     */
    public void startProgress(final int lProgress) {
        round = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (round < lProgress) {
                    round++;
                    setProgress(round);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //?????????
    public int getCricleColor() {
        return roundColor;
    }

    public void setTextShow(boolean isShow) {
        textIsDisplayable = isShow;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }
}
