package com.Tester;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import com.MemoryLadderFull.R;

public class Start extends Activity {

	final int STANDARD  = 0;
	final int WMC       = 1;
	final int NATIONAL  = 2;
	
	final int NUMBERS_BINARY  = 0;
	final int NUMBERS_DECIMAL = 1;
	final int NUMBERS_WRITTEN = 0;
	final int NUMBERS_SPOKEN  = 1;
	
	final int SHAPES_FACES  = 0;
	final int SHAPES_ABSTRACT = 1;
	
	final int LISTS_WORDS  = 0;
	final int LISTS_EVENTS = 1;
	
	final int CARDS_SPEED  = 0;
	final int CARDS_LONG = 1;
	
	final int MEMORIZATION = 0;
	final int RECALL       = 1;
	final int REVIEW       = 2;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                       
        startNumbers();
	}
	
	public void startNumbers() {
		Intent i = new Intent(this, Shapes_PreGame.class);
        i.putExtra("gameType",  SHAPES_FACES);
      /*  i.putExtra("numRows",     40);
        i.putExtra("numCols",     5);
        i.putExtra("memTime",     60);
        i.putExtra("recallTime",  120); */
        
        Start.this.startActivity(i);
	}
    
}