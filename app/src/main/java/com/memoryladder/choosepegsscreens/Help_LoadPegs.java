package com.memoryladder.choosepegsscreens;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.mastersofmemory.memoryladder.R;

public class Help_LoadPegs extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.help_load_pegs);
    }
}