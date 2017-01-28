package com.MemoryLadder;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;
//import com.MemoryLadderFull.R;

public class ChoosePegs_Dialog extends Dialog implements OnClickListener {
	
	Context context;
	Button CancelButton;
	Button SaveButton;
	String oldValue;
	String[] suggestions;
	int index;
	int numOrCards;
	
	private TextView number;
	private ImageView cardImage;
	private EditText textbox;
		
	private final int NUMBER = 0;
	private final int CARD = 1;
	
	OnMyDialogResultTime mDialogResult; // the callback
	
	public ChoosePegs_Dialog(Context context, int numOrCards, int index, String oldValue, String[] suggestions) {
		super(context);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.peg_dialog);
		
		this.oldValue = oldValue;
		this.index = index;
		this.numOrCards = numOrCards;
		this.suggestions = suggestions;
		
		initButtons();		
		initSuggestions();
		setCancelable(true);	
	}
	
	public interface OnMyDialogResultTime{
	       void finish(String result);
	}
	
	public void setDialogResult(OnMyDialogResultTime dialogResult){
        mDialogResult = dialogResult;
    }
		
	public void initButtons() {
		CancelButton = (Button) findViewById(R.id.CancelButton);
		CancelButton.setOnClickListener(this);
		
		SaveButton = (Button) findViewById(R.id.SaveButton);
		SaveButton.setOnClickListener(this);
		
		number = (TextView) findViewById(R.id.number);
		cardImage = (ImageView) findViewById(R.id.cardImage);
		textbox = (EditText) findViewById(R.id.textBox);
		
		if (numOrCards == NUMBER) {
			number.setText(Integer.toString(index));
			textbox.setText(oldValue);
			cardImage.setVisibility(View.GONE);
		}
		else if (numOrCards == CARD){
			number.setVisibility(View.GONE);
			textbox.setText(oldValue);
			cardImage.setImageResource(Card.getImageResourceID(context, index));
		}
		
	}	
	
	public void initSuggestions() {
		
		ListView list = (ListView) findViewById(R.id.listview);
		
		ArrayList<String> strings = new ArrayList<String>();  
	    strings.addAll( Arrays.asList(suggestions) );	    
	    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(context, R.layout.text, strings);  
	
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			    textbox.setText(suggestions[position]);
			}
		});
		
		list.setAdapter(listAdapter);
	}
			
	@Override
	public void onClick(View v) {
		if (v == CancelButton) 
			dismiss();
		else if (v == SaveButton) {
			if( mDialogResult != null ){
				if (textbox.getText().toString() .equals(""))
					mDialogResult.finish(oldValue);
				else
					mDialogResult.finish(textbox.getText().toString());
            }
			ChoosePegs_Dialog.this.dismiss();
		}
	}	
}