package com.xiaoluo.michaelutil.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dsw.calendar.views.CircleCalendarView;
import com.xiaoluo.michaelutil.R;

public class CarlenderActivity extends AppCompatActivity {
    private RelativeLayout layout;
    private CircleCalendarView mCalenderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carlender);
        mCalenderView = new CircleCalendarView(this);
        initView();
    }

    private void initView() {
        layout = (RelativeLayout) findViewById(R.id.activity_carlender);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mCalenderView.setLayoutParams(params);
        layout.addView(mCalenderView);
    }
}
