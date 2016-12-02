package com.xiaoluo.michaelutil.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ant.liao.GifView;
import com.xiaoluo.michaelutil.R;

public class GifviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifview);
        GifView gf1 = (GifView) findViewById(R.id.gif2);
        // 设置Gif图片源
        gf1.setGifImage(R.mipmap.loading_pic);
//        // 添加监听器
//        gf1.setOnClickListener(this);
        // 设置显示的大小，拉伸或者压缩
        gf1.setShowDimension(300, 300);
        // 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
        gf1.setGifImageType(GifView.GifImageType.SYNC_DECODER);
    }
}
