package com.MemoryLadder.NumbersChallenges;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.mastersofmemory.memoryladder.R;

public class HowItWorks_Numbers extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.howitworks_numbers);
    }
}