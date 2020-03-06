package com.example.instantMessaging;

import com.example.common.app.Activity;
import com.example.instantMessaging.Activities.AccountActivity;

/**
 * @author brsmsg
 * @time 2020/3/5
 */
public class LaunchActivity extends Activity {


    @Override
    protected int getContentLayotId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initData() {
        super.initData();

        AccountActivity.show(this);
    }
}
