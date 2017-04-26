package com.xiaoluo.michaelutil.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.xiaoluo.michaelutil.R;

/**
 * 属性动画 效果展现
 */
public class AnimatorActivity extends AppCompatActivity {
    private TextView txtView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);
//        mCalenderView = new CircleCalendarView(this);
        initView();
//        HandlerThread thread = new HandlerThread("test");
//         new Handler(thread.getLooper());
    }

    private void initView() {
        txtView = (TextView) findViewById(R.id.txt_content);
        //valueAnimator 主要是 对值 进行一个平滑的过渡
        //ValueAnimator anim = ValueAnimator.ofInt(0, 100);
        //anim.setDuration(800);
        //anim.setStartDelay(1000);
        //anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        //            @Override
        //            public void onAnimationUpdate(ValueAnimator animation) {
        //                txtView.setText(animation.getAnimatedValue() + "");
        //            }
        //        });
        //        anim.start();
        //ObjectAnimator则就不同了，它是可以直接对任意对象的任意属性进行动画操作的
        //ObjectAnimator animator = ObjectAnimator.ofFloat(txtView, "alpha", 1f, 0f, 1f);//这个就是alpha渐变
        //ObjectAnimator animator = ObjectAnimator.ofFloat(txtView, "rotation", 0f, 360f);//旋转
        //float curTranslationX = txtView.getTranslationX();
        //ObjectAnimator animator = ObjectAnimator.ofFloat(txtView, "translationX", curTranslationX, -500f, curTranslationX);//左右平移
        //ObjectAnimator animator = ObjectAnimator.ofFloat(txtView, "scaleY", 1f, 3f, 1f);//垂直缩放
        //animator.setDuration(5000);
        //animator.start();
        ObjectAnimator moveIn = ObjectAnimator.ofFloat(txtView, "translationX", -500f, 0f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(txtView, "rotation", 0f, 360f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(txtView, "alpha", 1f, 0f, 1f);
        //组合动画
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(rotate).with(fadeInOut).after(moveIn);
        animSet.setDuration(3000);
        animSet.start();


    }
}
