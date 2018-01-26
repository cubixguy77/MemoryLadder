package com.MemoryLadder;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

public class InstructionsDialog extends Dialog {
	
	Context context;

	final private static int NUMBERS_SPEED   = Constants.NUMBERS_SPEED;
//	final private static int NUMBERS_LONG    = Constants.NUMBERS_LONG;
//	final private static int NUMBERS_BINARY  = Constants.NUMBERS_BINARY;
	final private static int NUMBERS_SPOKEN  = Constants.NUMBERS_SPOKEN;
	private static final int LISTS_WORDS  = Constants.LISTS_WORDS;
	private static final int LISTS_EVENTS = Constants.LISTS_EVENTS;	
	final private static int SHAPES_FACES    = Constants.SHAPES_FACES;
	final private static int SHAPES_ABSTRACT = Constants.SHAPES_ABSTRACT;
	final private static int CARDS_SPEED  = Constants.CARDS_SPEED;
    final private static int CARDS_LONG   = Constants.CARDS_LONG;
	
	public InstructionsDialog(Context context, int gameType) {
		super(context);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.instructions);
		setCancelable(true);
		setCanceledOnTouchOutside(true);

        ((TextView) findViewById(R.id.InstructionsDialogTitleText)).setText(Constants.getGameDisplayName(gameType));
		
		if (gameType >= NUMBERS_SPEED && gameType <= NUMBERS_SPOKEN)
			initInstructions_Numbers(gameType);
		else if (gameType == LISTS_WORDS || gameType == LISTS_EVENTS)
			initInstructions_Lists(gameType);
		else if (gameType == SHAPES_FACES || gameType == SHAPES_ABSTRACT)
			initInstructions_Shapes(gameType);
		else if (gameType == CARDS_SPEED || gameType == CARDS_LONG)
			initInstructions_Cards();
	}
	
	private void initInstructions_Numbers(int gameType) {
    	
    	TextView about = findViewById(R.id.about);
    	TextView Point1 = findViewById(R.id.Point1);
    	TextView Point2 = findViewById(R.id.Point2);
    	TextView Point3 = findViewById(R.id.Point3);
    	
    	TextView AddOn1 = findViewById(R.id.AddOn1);
    	TextView AddOn2 = findViewById(R.id.AddOn2);
    	
    	Resources res = context.getResources();	    	
    	
    	if (gameType != NUMBERS_SPOKEN) {    		    	        	       	
        	about.setText(res.getText(R.string.numbers_written_about));
        	
        	TextView preface = findViewById(R.id.preface);
        	preface.setVisibility(View.VISIBLE);
        	preface.setText(res.getText(R.string.numbers_written_preface));
        	
	    	Point1.setText(res.getText(R.string.numbers_written_point1));
	    	Point2.setText(res.getText(R.string.numbers_written_point2));
	    	Point3.setText(res.getText(R.string.numbers_written_point3));
	    	
	    	AddOn1.setText(res.getText(R.string.numbers_written_addon1));
	    	AddOn2.setText(res.getText(R.string.numbers_written_addon2));
    	}
    	else {	    	
    		about.setText(res.getText(R.string.numbers_spoken_about));
    		
    		TextView Bullet1 = findViewById(R.id.Bullet1);Bullet1.setVisibility(View.GONE);
    		TextView Bullet2 = findViewById(R.id.Bullet2);Bullet2.setVisibility(View.GONE);
    		TextView Bullet3 = findViewById(R.id.Bullet3);Bullet3.setVisibility(View.GONE);
	    	Point1.setVisibility(View.GONE);
	    	Point2.setVisibility(View.GONE);
	    	Point3.setVisibility(View.GONE);
	    	
	    	AddOn1.setText(res.getText(R.string.numbers_spoken_addon1));
	    	AddOn2.setText(res.getText(R.string.numbers_spoken_addon2));
    	}    	
    }
	
	
	private void initInstructions_Lists(int gameType) {
    	
    	TextView about = findViewById(R.id.about);
    	TextView Point1 = findViewById(R.id.Point1);
    	TextView Point2 = findViewById(R.id.Point2);
    	TextView Point3 = findViewById(R.id.Point3);
    	
    	TextView AddOn1 = findViewById(R.id.AddOn1);
    	TextView AddOn2 = findViewById(R.id.AddOn2);
    	
    	Resources res = context.getResources();	    	
    	
    	if (gameType == LISTS_WORDS) {    		    	        	       	
        	about.setText(res.getText(R.string.lists_words_about));
        	
        	TextView preface = findViewById(R.id.preface);
        	preface.setVisibility(View.VISIBLE);
        	preface.setText(res.getText(R.string.lists_words_preface));
        	
	    	Point1.setText(res.getText(R.string.lists_words_point1));
	    	Point2.setText(res.getText(R.string.lists_words_point2));
	    	Point3.setText(res.getText(R.string.lists_words_point3));
	    	
	    	AddOn1.setText(res.getText(R.string.lists_words_addon1));
	    	AddOn2.setText(res.getText(R.string.lists_words_addon2));
    	}
    	else {	    	
    		about.setText(res.getText(R.string.lists_dates_about));
    		Point1.setText(res.getText(R.string.lists_dates_point1));
	    	Point2.setText(res.getText(R.string.lists_dates_point2));
    		TextView Bullet3 = findViewById(R.id.Bullet3);Bullet3.setVisibility(View.GONE);
	    	Point3.setVisibility(View.GONE);
    	}    	
    }
	
	
	
	
	private void initInstructions_Shapes(int gameType) {
    	
    	TextView about = findViewById(R.id.about);
    	TextView Point1 = findViewById(R.id.Point1);
    	TextView Point2 = findViewById(R.id.Point2);
    	TextView Point3 = findViewById(R.id.Point3);
    	
    	TextView AddOn1 = findViewById(R.id.AddOn1);
    	TextView AddOn2 = findViewById(R.id.AddOn2);
    	
    	Resources res = context.getResources();	    	
    	
    	if (gameType == SHAPES_FACES) {    		    	        	       	
        	about.setText(res.getText(R.string.shapes_faces_about));
        	
	    	Point1.setText(res.getText(R.string.shapes_faces_point1));
	    	Point2.setText(res.getText(R.string.shapes_faces_point2));
	    	Point3.setText(res.getText(R.string.shapes_faces_point3));
	    	
	    	AddOn1.setText(res.getText(R.string.shapes_faces_addon1));
	    	AddOn2.setText(res.getText(R.string.shapes_faces_addon2));
    	}
    	else {	    	
    		about.setText(res.getText(R.string.shapes_images_about));
    		Point1.setText(res.getText(R.string.shapes_images_point1));
	    	Point2.setText(res.getText(R.string.shapes_images_point2));
	    	Point3.setText(res.getText(R.string.shapes_images_point3));
    	}    	
    }
	
	
	private void initInstructions_Cards() {
    	
    	TextView about = findViewById(R.id.about);
    	TextView Point1 = findViewById(R.id.Point1);
    	TextView Point2 = findViewById(R.id.Point2);
    	TextView Point3 = findViewById(R.id.Point3);
    	
    	TextView AddOn1 = findViewById(R.id.AddOn1);
    	
    	Resources res = context.getResources();	    	
   		    	        	       	
    	about.setText(res.getText(R.string.cards_cards_about));
    	
    	TextView preface = findViewById(R.id.preface);
    	preface.setVisibility(View.VISIBLE);
    	preface.setText(res.getText(R.string.cards_cards_preface));
    	
    	Point1.setText(res.getText(R.string.cards_cards_point1));
    	Point2.setText(res.getText(R.string.cards_cards_point2));
    	Point3.setText(res.getText(R.string.cards_cards_point3));
    	
    	AddOn1.setText(res.getText(R.string.cards_cards_addon1)); 	  	
    }
}