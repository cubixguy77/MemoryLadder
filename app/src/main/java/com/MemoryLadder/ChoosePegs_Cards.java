package com.MemoryLadder;

//import com.MemoryLadderFull.R;
import com.MemoryLadder.ChoosePegs_Dialog.OnMyDialogResultTime;
import com.mastersofmemory.memoryladder.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


public class ChoosePegs_Cards extends Activity implements OnClickListener {

	private GridView grid;
	private ListAdapter adapter;
	
	private Button back;
	private Button open;
	private Button howItWorks;
	private Button NumbersButton;
	
	private String prefs_filename = "Peg_Cards";
	private String prefix = "prefs_cards_";
	private int numRows = 13;
	private int numCols = 4;
	private int numItems = numRows * numCols;
	
//	private final int NUMBER = 0;
	private final int CARD = 1;
	
	private String[] pegs;
		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pegs_numbers);
        
        initButtons();
        initPegStrings();
        initGrid();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	saveChanges();
    }
    
    @Override
    public void onRestart() {
    	super.onRestart();
    	initPegStrings();
    }
    
    public void initButtons() {
    	back = (Button) findViewById(R.id.button1);
    	back.setOnClickListener(this);
    	
    	open = (Button) findViewById(R.id.openButton);
    	open.setOnClickListener(this);
        
        howItWorks = (Button) findViewById(R.id.button2);
        howItWorks.setOnClickListener(this);
        
        NumbersButton = (Button) findViewById(R.id.NumbersButton);
        NumbersButton.setBackgroundResource(R.drawable.button_pegs_numbers_off);
        NumbersButton.setTextColor(Color.LTGRAY);
        NumbersButton.setOnClickListener(this);
    }
    
    public void initPegStrings() {
    	    	
    	SharedPreferences prefs = getSharedPreferences(prefs_filename, 0);
    	
    	Boolean firstTime = prefs.getBoolean("firstTime", true);
    	
    	if (firstTime) {
    		loadPegDefaults();
    	}
    	else {
    		pegs = new String[numItems];
    		for (int i=0; i<numItems; i++) {
    			pegs[i] = prefs.getString(prefix + Integer.toString(i), "NULL");
    		}
    	}
    }

    public void loadPegDefaults() {
    	SharedPreferences settings = getSharedPreferences(prefs_filename, 0);
        SharedPreferences.Editor editor = settings.edit();
        
        editor.putBoolean("firstTime", false);
        
        pegs = new String[numItems];        
        for (int i=0; i<numItems; i++) {
        	String string = Utils.getCardSuggestions(i)[0];
			editor.putString(prefix + Integer.toString(i), string);
			pegs[i] = string;
		}
        editor.commit();
    }
    
    public void initGrid() {
    	grid = (GridView) findViewById(R.id.grid);
        grid.setNumColumns(numCols);
        adapter = new ListAdapter(this);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	final int index = getTranspose(position);
            	String currentString = pegs[index];
            	String[] suggestions = Utils.getCardSuggestions(index);

            	ChoosePegs_Dialog dialog = new ChoosePegs_Dialog(ChoosePegs_Cards.this, CARD, index, currentString, suggestions);
    			dialog.setDialogResult(new OnMyDialogResultTime() {
    			    public void finish(String result) {  
    			    	pegs[index] = result;
    			    	adapter.notifyDataSetChanged();
    			    }
    			});
    			dialog.show();
            }
        });
    }
    
    public void saveChanges() { 
    	SharedPreferences settings = getSharedPreferences(prefs_filename, 0);
        SharedPreferences.Editor editor = settings.edit();
        
        for (int i=0; i<pegs.length; i++) {
        	editor.putString(prefix + Integer.toString(i), pegs[i]);
        }
                
        editor.commit();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	Intent i = new Intent(this, Main.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
	@Override
	public void onClick(View v) {
		if (v == back) {
			saveChanges();
			Intent i = new Intent(this, Main.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(i);
			finish();
		}
		else if (v == open) {
			Intent i = new Intent(this, FileChooser.class);
			i.putExtra("getNumbers", false);
			this.startActivity(i);
		}
		else if (v == howItWorks) {
			Intent i = new Intent(this, HowItWorks_Cards.class);	
			this.startActivity(i);
		}			
		else if (v == NumbersButton) {
			Intent i = new Intent(this, ChoosePegs_Numbers.class);	
			this.startActivity(i);
		}
	}
	
	public int getCol(int pos)     {  return pos % numCols;                      }
    public int getRow(int pos)     {  return (pos / numCols) % numRows;          }
	public int getTranspose(int i) {  return (numRows * getCol(i)) + getRow(i);  }
	
	
	
	
	private class ListAdapter extends BaseAdapter {
        
        public ListAdapter(Context context) {  	   }
        public int getCount() {           			 return numItems;        }
        public Object getItem(int position) {        return null;        }
        public long getItemId(int position) {        return 0;        }
        public View getView(int position, View convertView, ViewGroup parent) {
        	
        	View MyView = convertView;
        	TextView numtext;
            TextView wordtext;
        	
            if (convertView == null) {            	        
                MyView = getLayoutInflater().inflate(R.layout.peg_view, null);
                numtext  = (TextView) MyView.findViewById(R.id.index);
            	wordtext = (TextView) MyView.findViewById(R.id.string);
            }
            else {
            	numtext  = (TextView) MyView.findViewById(R.id.index);
            	wordtext = (TextView) MyView.findViewById(R.id.string);
            }
          
            int index = getTranspose(position);
            
            String indexString = Card.getCharacter(getRow(position) + 1);            
            String pegString = pegs[index];
            
            numtext.setText(indexString);
            wordtext.setText(pegString);
            
            return MyView;        
        }
    }
	
	
	
	
	

	
}