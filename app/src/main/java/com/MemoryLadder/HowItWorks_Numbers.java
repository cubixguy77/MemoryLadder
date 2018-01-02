package com.MemoryLadder;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.mastersofmemory.memoryladder.R;
//import com.MemoryLadderFull.R;


public class HowItWorks_Numbers extends Activity {
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.howitworks_numbers);
    }
}