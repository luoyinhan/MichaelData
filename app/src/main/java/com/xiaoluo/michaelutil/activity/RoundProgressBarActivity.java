package com.xiaoluo.michaelutil.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xiaoluo.michaelutil.R;
import com.xiaoluo.michaelutil.widght.RoundProgressBar;

public class RoundProgressBarActivity extends AppCompatActivity {
    RoundProgressBar roundbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_progress_bar);
        roundbar = (RoundProgressBar) findViewById(R.id.rp_round);
        roundbar.startProgress(100);
    }
}
