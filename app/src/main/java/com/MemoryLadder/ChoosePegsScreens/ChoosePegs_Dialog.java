package com.memoryladder.choosepegsscreens;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.memoryladder.taketest.cards.CardImageProvider;
import com.memoryladder.taketest.cards.PlayingCard;
import com.mastersofmemory.memoryladder.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ChoosePegs_Dialog extends Dialog implements OnClickListener {
	
	private Context context;
	private Button CancelButton;
	private Button SaveButton;
	private String oldValue;
	private String[] suggestions;
	private int index;
	private int numOrCards;

	private EditText textbox;

	private OnMyDialogResultTime mDialogResult; // the callback
	
	ChoosePegs_Dialog(Context context, int numOrCards, int index, String oldValue, String[] suggestions) {
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
	
	void setDialogResult(OnMyDialogResultTime dialogResult){
        mDialogResult = dialogResult;
    }
		
	public void initButtons() {
		CancelButton = findViewById(R.id.CancelButton);
		CancelButton.setOnClickListener(this);
		
		SaveButton = findViewById(R.id.SaveButton);
		SaveButton.setOnClickListener(this);

		TextView number = findViewById(R.id.number);
		ImageView cardImage = findViewById(R.id.cardImage);
		textbox = findViewById(R.id.textBox);

		int NUMBER = 0;
		int CARD = 1;
		if (numOrCards == NUMBER) {
			number.setText(Integer.toString(index));
			textbox.setText(oldValue);
			cardImage.setVisibility(View.GONE);
		}
		else if (numOrCards == CARD){
			number.setVisibility(View.GONE);
			textbox.setText(oldValue);
			cardImage.setImageResource(CardImageProvider.getImageResourceId(context, new PlayingCard(index)));
		}
	}	
	
	private void initSuggestions() {
		
		ListView list = findViewById(R.id.listview);
		
		ArrayList<String> strings = new ArrayList<>();
	    strings.addAll( Arrays.asList(suggestions) );	    
	    ArrayAdapter<String> listAdapter = new ArrayAdapter<>(context, R.layout.text, strings);
	
		list.setOnItemClickListener((parent, view, position, id) -> textbox.setText(suggestions[position]));
		
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