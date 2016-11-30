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
 * ��iphone�����ȵĽ��������̰߳�ȫ��View����ֱ�����߳��и��½���
 *
 * @author michaelluo
 */
public class RoundProgressBar extends View {
    /**
     * ���ʶ��������
     */
    private Paint paint;

    private Paint smallcicrlPaint;

    /**
     * �ֶ���ɫ
     */
    private static final int[] SECTION_COLORS = {Color.BLUE, Color.YELLOW, Color.RED};

    /**
     * Բ������ɫ
     */
    private int roundColor;

    /**
     * Բ�����ȵ���ɫ
     */
    private int roundProgressColor;

    /**
     * �м���Ȱٷֱȵ��ַ�������ɫ
     */
    private int textColor;

    /**
     * �м���Ȱٷֱȵ��ַ���������
     */
    private float textSize;

    /**
     * Բ���Ŀ��
     */
    private float roundWidth;

    /**
     * Ĭ��������
     */
    private int max = 100;

    /**
     * ��ǰ����
     */
    private int progress;

    /**
     * ��ǰ���ý���
     */
    private int round = 0;

    /**
     * �Ƿ���ʾ�м�Ľ���
     */
    private boolean textIsDisplayable;

    /**
     * ���ȵķ��ʵ�Ļ��߿���
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
        // ��ȡ�Զ������Ժ�Ĭ��ֵ
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.BLUE);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 20);
        roundWidth = 30;
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);//Ĭ����ʾ����
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
        mTypedArray.recycle();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * �������Ĵ�Բ��
         */
        int centre = getWidth() / 2; // ��ȡԲ�ĵ�x����
        int radius = (int) (centre - roundWidth / 2 - 20); // Բ���İ뾶
        paint.setColor(Color.parseColor("#DDDDDD")); // ����Բ������ɫ
        paint.setStyle(Paint.Style.STROKE); // ���ÿ���
        paint.setStrokeWidth(roundWidth); // ����Բ���Ŀ��
        paint.setAntiAlias(true); // �������
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        paint.setPathEffect(effects);
        paint.setShader(null);
        RectF oval = new RectF(centre - radius, centre - radius, (centre + radius), centre + radius); // ���ڶ����Բ������״�ʹ�С�Ľ���
        if (isDrawBG) {
            // canvas.drawCircle(centre, centre, radius, paint); // ����Բ��
            canvas.drawArc(oval, 150, 240 * 1, false, paint); // ���ݽ��Ȼ�Բ��
        }
        /**
         * ��СԲ����
         */
        smallcicrlPaint.setColor(Color.parseColor("#DDDDDD")); // ����Բ������ɫ
        smallcicrlPaint.setStyle(Paint.Style.STROKE); // ���ÿ���
        smallcicrlPaint.setStrokeWidth(5); // ����Բ���Ŀ��
        smallcicrlPaint.setAntiAlias(true); // �������
        int width = 800;
        //���ݸ�����Ļ�ߴ�
        if (width < 720) {
            width = 10;
        } else {
            width = 25;
        }
        RectF smalloval = new RectF(centre - radius + width, centre - radius + width, (centre + radius - width), centre + radius - width);
        // canvas.drawCircle(centre, centre, radius - 25, smallcicrlPaint);
        // ����Բ��
        canvas.drawArc(smalloval, 150, 240 * 1, false, smallcicrlPaint); // ���ݽ��Ȼ�Բ��
        /**
         * �����Ȱٷֱ�
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); // ��������
        int percent = (int) (((float) progress / (float) max) * 100);
//        �м�Ľ��Ȱٷֱȣ���ת����float�ڽ��г������㣬��Ȼ��Ϊ0
        float textWidth = paint.measureText(percent + "%");
//        ���������ȣ�������Ҫ��������Ŀ��������Բ���м�
        if (textIsDisplayable && percent != 0 && style == STROKE) {
            paint.setTextSize(80);
            canvas.drawText(percent + "%", centre - textWidth * 2,
                    centre + textSize / 2, paint); // �������Ȱٷֱ�
        }
        /**
         * ��Բ�� ����Բ���Ľ���
         */
        // ���ý���Ч��
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
        // ���ý�����ʵ�Ļ��ǿ���
        paint.setStrokeWidth(roundWidth); // ����Բ���Ŀ��
        // paint.setColor(roundProgressColor); // ���ý��ȵ���ɫ
        paint.setColor(Color.BLUE);//
        paint.setPathEffect(null);
        // ���ý��ȵ���ɫ
        // RectF oval = new RectF(centre - radius, centre - radius,
        // (centre + radius), centre + radius); // ���ڶ����Բ������״�ʹ�С�Ľ���

        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 150, 240 * progress / max, false, paint); // ���ݽ��Ȼ�Բ��
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0) {
                    canvas.drawArc(oval, -90, 360 * progress / max, true, paint); // ���ݽ��Ȼ�Բ��
                }
                break;
            }
        }
    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * ���ý��ȵ����ֵ
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
     * ��ȡ����.��Ҫͬ��
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * ���ý��ȣ���Ϊ�̰߳�ȫ�ؼ������ڿ��Ƕ��ߵ����⣬��Ҫͬ�� ˢ�½������postInvalidate()���ڷ�UI�߳�ˢ��
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
            postInvalidate();//�������̵߳���
        }
    }

    /**
     * ��ʼloading �����߳��е���
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

    //��ȡԲ����ɫ
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
