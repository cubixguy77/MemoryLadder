package com.Tester;

import android.content.Context;
import android.content.SharedPreferences;
//import com.MemoryLadderFull.R;


public class Permissions {
	
	    private Context context;
	    private Boolean FREE = false;
	    
		final private static int NUMBERS_SPEED   = Constants.NUMBERS_SPEED;
		final private static int NUMBERS_LONG    = Constants.NUMBERS_LONG;
		final private static int NUMBERS_BINARY  = Constants.NUMBERS_BINARY;
		final private static int NUMBERS_SPOKEN  = Constants.NUMBERS_SPOKEN;
		private static final int LISTS_WORDS     = Constants.LISTS_WORDS;
		private static final int LISTS_EVENTS    = Constants.LISTS_EVENTS;	
		final private static int SHAPES_FACES    = Constants.SHAPES_FACES;
		final private static int SHAPES_ABSTRACT = Constants.SHAPES_ABSTRACT;
		final private static int CARDS_SPEED     = Constants.CARDS_SPEED;
	    final private static int CARDS_LONG      = Constants.CARDS_LONG;
	    
	    final private static int STEPS  = Constants.STEPS;
//		final private static int WMC    = Constants.WMC;
		final private static int CUSTOM = Constants.CUSTOM;
    
	    public Permissions(Context context) {
    		this.context = context;
        }
    
    	public Boolean[] getPermissions(int mode) {
        	Boolean[] permits = getInitialArray();
        	SharedPreferences prefs = context.getSharedPreferences("Permissions", 0);
        
        	if (prefs.getString("NUMBERS_SPEED", "false") .equals ("true"))
        		permits[NUMBERS_SPEED] = true;
        	if (prefs.getString("NUMBERS_LONG", "false") .equals ("true"))
        		permits[NUMBERS_BINARY] = true;
        	if (prefs.getString("NUMBERS_BINARY", "false") .equals ("true"))
        		permits[NUMBERS_SPOKEN] = true;
        	if (prefs.getString("NUMBERS_SPOKEN", "false") .equals ("true"))
        		permits[LISTS_WORDS] = true;
        	if (prefs.getString("LISTS_WORDS", "false") .equals ("true"))
        		permits[LISTS_EVENTS] = true;
        	if (prefs.getString("LISTS_EVENTS", "false") .equals ("true"))
        		permits[SHAPES_FACES] = true;
        	if (prefs.getString("SHAPES_FACES", "false") .equals ("true"))
        		permits[SHAPES_ABSTRACT] = true;
        	if (prefs.getString("SHAPES_ABSTRACT", "false") .equals ("true"))
        		permits[CARDS_SPEED] = true;
        	if (prefs.getString("CARDS_SPEED", "false") .equals ("true"))
        		permits[NUMBERS_SPEED] = true;
        	if (prefs.getString("CARDS_LONG", "false") .equals ("true"))
        		permits[CARDS_LONG] = true;
        	
        	if (mode == CUSTOM || mode == STEPS) {
        		permits[NUMBERS_LONG] = false;
        		permits[CARDS_SPEED] = false;
        	}
        	
        	return permits;
        	
        }
    	
    	/* Note that the tests start counting at 1, so the first value is irrelevant */
    	private Boolean[] getInitialArray() {
    		if (FREE)
    			return new Boolean[] {false, true, true, true, true, false, false, false, false, false, false };
    		else
    			return new Boolean[] {false, true, true, true, true, true,  true,  true,  true,  true,  true };
    	}
}