package com.memoryladder.choosepegsscreens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.memoryladder.FileOps;
import com.memoryladder.taketest.cards.HowItWorks_Cards;
import com.memoryladder.taketest.cards.PlayingCard;
import com.memoryladder.Utils;
import com.aditya.filebrowser.FileChooser;
import com.mastersofmemory.memoryladder.R;

import static com.aditya.filebrowser.Constants.SELECTION_MODE;

public class ChoosePegs_Cards extends Activity implements OnClickListener {

	private ListAdapter adapter;
	
	private Button help;
	private Button open;
	private Button howItWorks;
	private Button NumbersButton;
	
	private String prefs_filename = "Peg_Cards";
	private String prefix = "prefs_cards_";
	private int numRows = 13;
	private int numCols = 4;
	private int numItems = numRows * numCols;

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
		help = findViewById(R.id.importHelpButton);
		help.setOnClickListener(this);
    	
    	open = findViewById(R.id.openButton);
    	open.setOnClickListener(this);
        
        howItWorks = findViewById(R.id.button2);
        howItWorks.setOnClickListener(this);
        
        NumbersButton = findViewById(R.id.NumbersButton);
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
		GridView grid = findViewById(R.id.grid);
        grid.setNumColumns(numCols);
        adapter = new ListAdapter();
        grid.setAdapter(adapter);
        grid.setOnItemClickListener((parent, v, position, id) -> {
            final int index = getTranspose(position);
            String currentString = pegs[index];
            String[] suggestions = Utils.getCardSuggestions(index);

            ChoosePegs_Dialog dialog = new ChoosePegs_Dialog(ChoosePegs_Cards.this, CARD, index, currentString, suggestions);
            dialog.setDialogResult(result -> {
                pegs[index] = result;
                adapter.notifyDataSetChanged();
            });
            dialog.show();
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
	public void onClick(View v) {
		if (v == help) {
			Intent i = new Intent(this, Help_LoadPegs.class);
			startActivity(i);
		}
		else if (v == open) {
            Intent i = new Intent(getApplicationContext(), FileChooser.class);
            i.putExtra(SELECTION_MODE, com.aditya.filebrowser.Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
            i.putExtra(com.aditya.filebrowser.Constants.ALLOWED_FILE_EXTENSIONS, "csv;txt");
            startActivityForResult(i, 0);
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
        
        ListAdapter() {  	   }
        public int getCount() {           			 return numItems;        }
        public Object getItem(int position) {        return null;        }
        public long getItemId(int position) {        return 0;        }
        public View getView(int position, View convertView, ViewGroup parent) {
        	
        	View MyView = convertView;
        	TextView numtext;
            TextView wordtext;
        	
            if (convertView == null) {            	        
                MyView = getLayoutInflater().inflate(R.layout.peg_view, null);
                numtext  = MyView.findViewById(R.id.index);
            	wordtext = MyView.findViewById(R.id.string);
            }
            else {
            	numtext  = MyView.findViewById(R.id.index);
            	wordtext = MyView.findViewById(R.id.string);
            }
          
            int index = getTranspose(position);
            
            String indexString = PlayingCard.getCharacter(getRow(position) + 1);
            String pegString = pegs[index];
            
            numtext.setText(indexString);
            wordtext.setText(pegString);
            
            return MyView;        
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();

                assert uri != null;
                String result = FileOps.loadPegsFromFileCards(uri.getPath(), this);
                if (result.equals("Success")) {
                    initPegStrings();
                    adapter.notifyDataSetChanged();
                    Snackbar.make(open, "Successfully imported peg words!", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                            .setIcon(R.drawable.icon)
                            .setTitle("Error Interpreting Your File")
                            .setMessage(result + "\n" + "Each line should follow the format: \"s3,Mate\"")
                            .setNegativeButton("Help", (dialog, which) -> {
                                Intent i = new Intent(this, Help_LoadPegs.class);
                                startActivity(i);
                            })
                            .setPositiveButton("OK", (dialog, which) -> finish())
                            .show();
                }
            }
        }
    }
}