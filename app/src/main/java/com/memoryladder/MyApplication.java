package com.memoryladder;

import android.support.multidex.MultiDexApplication;

import com.squareup.leakcanary.LeakCanary;

public class MyApplication extends MultiDexApplication {

    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        LeakCanary.install(this);
    }
}