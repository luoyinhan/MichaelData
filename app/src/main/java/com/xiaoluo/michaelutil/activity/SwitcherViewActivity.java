package com.xiaoluo.michaelutil.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xiaoluo.michaelutil.widght.AutoVerticalScrollTextView;

import java.util.ArrayList;
import java.util.List;

//Switch
public class SwitcherViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      setContentView(R.layout.activity_circle_view);
        List<String> data = new ArrayList<>();
        data.add("张三");
        data.add("李四");
        data.add("王五");
        AutoVerticalScrollTextView switcherView = new AutoVerticalScrollTextView(this);
        switcherView.setData(data);
        switcherView.start();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = 50;
        addContentView(switcherView, params);
    }
}
