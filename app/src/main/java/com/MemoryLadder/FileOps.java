package com.MemoryLadder;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
//import com.MemoryLadderFull.R;

public class FileOps {
	
	final private static int NUMBERS_SPEED   = Constants.NUMBERS_SPEED;
	final private static int NUMBERS_LONG    = Constants.NUMBERS_LONG;
	final private static int NUMBERS_BINARY  = Constants.NUMBERS_BINARY;
	final private static int NUMBERS_SPOKEN  = Constants.NUMBERS_SPOKEN;
	final private static int LISTS_WORDS     = Constants.LISTS_WORDS;
	final private static int LISTS_EVENTS    = Constants.LISTS_EVENTS;	
	final private static int SHAPES_FACES    = Constants.SHAPES_FACES;
	final private static int SHAPES_ABSTRACT = Constants.SHAPES_ABSTRACT;
	final private static int CARDS_SPEED     = Constants.CARDS_SPEED;
    final private static int CARDS_LONG      = Constants.CARDS_LONG;
    
    final private static int STEPS  = Constants.STEPS;
	final private static int WMC    = Constants.WMC;
	final private static int CUSTOM = Constants.CUSTOM;

	private Context context;
	
	public FileOps(Context context) {
		this.context = context;
	}
	
	
	public static String loadPegsFromFileNumbers(String path, Context context) {
		
		SharedPreferences settings = context.getSharedPreferences("Peg_Numbers", 0);
        SharedPreferences.Editor editor = settings.edit();
        
		try {
    	    File file = new File(path);
    	    FileInputStream isr = new FileInputStream(file);
    	    DataInputStream in = new DataInputStream(isr);
			
			String line;                
    	    while ((line = in.readLine()) != null) {
    	    	if (line != null && !line.equals("")) {
    	    		String result = sanitizeStringNumber(line);
    	    		if (result.equals("Success")) {
    	    			int index = getIndexNUM(line);
    	    			String peg = getStringNUM(line);    	    		
    	    			editor.putString("peg_numbers_" + Integer.toString(index), peg);
    	    		}
    	    		else {
    	    			return result;
    	    		}    	    		
    	    	}
    	    }
    	} catch (IOException e)          {    e.printStackTrace(); return "File Access Error: Do you have permission to read this file?";  	}
    	  catch (NullPointerException e) {    e.printStackTrace(); return "Null Exception: File could not be read";	}  
    	  
    	  editor.commit();     	  
    	  return "Success";
	}
	
	public static String loadPegsFromFileCards(String path, Context context) {
		
		SharedPreferences settings = context.getSharedPreferences("Peg_Cards", 0);
        SharedPreferences.Editor editor = settings.edit();
		
		try {
    	    File file = new File(path);
    	    FileInputStream isr = new FileInputStream(file);
    	    DataInputStream in = new DataInputStream(isr);
			
			String line;                
    	    while ((line = in.readLine()) != null) {
    	    	if (line != null && !line.equals("")) {
    	    		String result = sanitizeStringCard(line);
    	    		System.out.println("******** result for line ***********" + result + "*****************");
    	    		if (result.equals("Success")) {
    	    			int index = getIndexCARD(line);
    	    			String peg = getStringCARD(line);    	    		
    	    			editor.putString("prefs_cards_" + Integer.toString(index), peg);
    	    		}
    	    		else {
    	    			return result;
    	    		}
    	    	}
    	    }
		} catch (IOException e)          {    e.printStackTrace(); return "File Access Error: Do you have permission to read this file?";  	}
		  catch (NullPointerException e) {    e.printStackTrace(); return "Null Exception: File could not be read";	}  
 
    	  editor.commit();     	  
    	  return "Success";
	}
	
	public static String sanitizeStringGeneral(String line) {
		if (!line.contains(",") && !line.contains(" "))
			return "Unable to work with the following line since it doesn't contain a comma or space:\n" + line;
		return "Success";
	}
	
	public static String sanitizeStringNumber(String line) {
		String GeneralResult = sanitizeStringGeneral(line);
		if (!GeneralResult.equals("Success"))
			return GeneralResult;
		else {
			if (getIndexNUM(line) == -1)
				return "Unable to work with the following line since it doesn't start with a number (0 to 99):\n" + line;
			return "Success";
		}
	}
	
	public static String sanitizeStringCard(String line) {
		String GeneralResult = sanitizeStringGeneral(line);
		if (!GeneralResult.equals("Success"))
			return GeneralResult;
		else {
			if (!validCard(line))
				return "Unable to find a playing card in the following line:\n" + line;
			return "Success";
		}
	}
	
	public static Boolean validCard(String line) {
		if (line.length() < 3 || !(line.charAt(2) == ',' || line.charAt(2) == ' ')) 
			return false;
		char suit = line.charAt(0);
		char val = line.charAt(1);
		return (suit == 's' || suit == 'h' || suit == 'c' || suit == 'd'
		   ) || suit == 'S' || suit == 'H' || suit == 'C' || suit == 'D'
		    && (val == '1' || val == 'a' || val == '2' || val == '3' || val == '4' || val == '5' || val == '6' || val == '7' || val == '8' || val == '9' || val == 't' || val == 'j' || val == 'q' || val == 'k'
		     || val == 'T' || val == 'J' || val == 'Q' || val == 'K');
	}
	
	
	public static int getIndexNUM(String string) {
		try  {
			int result = Integer.parseInt(string.substring(0, string.indexOf(","))); 
			if (result < 0 || result > 99)
				return -1;
			return result;
		}
		catch (NumberFormatException e) { return -1; }
		catch (StringIndexOutOfBoundsException e) { return -1; }
	}
	public static String getStringNUM(String string) {
		if (string.contains(","))
			return string.substring(string.indexOf(",") + 1, string.length());
		else
			return string.substring(string.indexOf(" ") + 1, string.length());
	}
	
	public static int getIndexCARD(String string) {
		try {
			if (string.length() >= 2)
				return (getSuitValue(Character.toString(string.charAt(0))) * 13) + getIntegerValue(string.charAt(1)) - 1;
			return -1;
		}
		catch (StringIndexOutOfBoundsException e) { return -1; }
	}
	public static String getStringCARD(String string) {
		if (string.contains(","))
			return string.substring(string.indexOf(",") + 1, string.length());
		else
			return string.substring(string.indexOf(" ") + 1, string.length());
	}
	
	private static int getSuitValue(String suit) {
		if (suit.equalsIgnoreCase("s"))
			return 0;
		else if (suit.equalsIgnoreCase("h"))
			return 1;
		else if (suit.equalsIgnoreCase("c"))
			return 2;
		else if (suit.equalsIgnoreCase("d"))
			return 3;
		return -1;
	}
	
	public static Integer getIntegerValue(char value) {        
        switch (value) {
	        case Card.TEN:          return 10;
	        case Card.JACK:         return 11;
	        case Card.QUEEN:        return 12;
	        case Card.KING:         return 13;
	        case Card.TENc:         return 10;
	        case Card.JACKc:        return 11;
	        case Card.QUEENc:       return 12;
	        case Card.KINGc:        return 13;
	        case Card.ACE:        	return 1;
	        case 'a':               return 1;
	        case 'A':				return 1;
	        default:                return Character.getNumericValue(value);
        }
	}
	
	public static String getPastScoreResource(int mode, int gameType) {
    	if (mode == STEPS) {
	    	switch (gameType) {
		    	case NUMBERS_SPEED:	  return "steps_numbersspeed";
				case NUMBERS_BINARY:  return "steps_numbersbinary";
				case NUMBERS_SPOKEN:  return "steps_numbersspoken";
				case LISTS_WORDS:     return "steps_listswords";
				case LISTS_EVENTS:    return "steps_listsevents";
				case SHAPES_FACES:    return "steps_shapesfaces";
				case SHAPES_ABSTRACT: return "steps_shapesabstract";
				case CARDS_SPEED:     return "steps_cardsspeed";
				case CARDS_LONG:     return  "steps_cardslong";
				default:              return null;
			}
    	}
    	else if (mode == WMC) {
    		switch (gameType) {
		    	case NUMBERS_SPEED:	  return "wmc_numbersspeed";
				case NUMBERS_LONG:    return "wmc_numberslong";
				case NUMBERS_BINARY:  return "wmc_numbersbinary";
				case NUMBERS_SPOKEN:  return "wmc_numbersspoken";
				case LISTS_WORDS:     return "wmc_listswords";
				case LISTS_EVENTS:    return "wmc_listsevents";
				case SHAPES_FACES:    return "wmc_shapesfaces";
				case SHAPES_ABSTRACT: return "wmc_shapesabstract";
				case CARDS_SPEED:     return "wmc_cardsspeed";
				case CARDS_LONG:      return "wmc_cardslong";
				default:              return null;
    		}
    	}
    	else if (mode == CUSTOM) {
    		switch (gameType) {
		    	case NUMBERS_SPEED:	  return "custom_numbersspeed";
				case NUMBERS_BINARY:  return "custom_numbersbinary";
				case NUMBERS_SPOKEN:  return "custom_numbersspoken";
				case LISTS_WORDS:     return "custom_listswords";
				case LISTS_EVENTS:    return "custom_listsevents";
				case SHAPES_FACES:    return "custom_shapesfaces";
				case SHAPES_ABSTRACT: return "custom_shapesabstract";
				case CARDS_SPEED:     return "custom_cardsspeed";
				case CARDS_LONG:     return "custom_cardslong";
				default:              return null;
    		}
    	}
    	else
    		return null;
	}
	
	
	public static String getPastScoreSummaryResource(int mode, int gameType) {
    	if (mode == STEPS) {
	    	switch (gameType) {
		    	case NUMBERS_SPEED:	  return "steps_numbersspeed_summary";
				case NUMBERS_BINARY:  return "steps_numbersbinary_summary";
				case NUMBERS_SPOKEN:  return "steps_numbersspoken_summary";
				case LISTS_WORDS:     return "steps_listswords_summary";
				case LISTS_EVENTS:    return "steps_listsevents_summary";
				case SHAPES_FACES:    return "steps_shapesfaces_summary";
				case SHAPES_ABSTRACT: return "steps_shapesabstract_summary";
				case CARDS_SPEED:     return "steps_cardsspeed_summary";
				case CARDS_LONG:     return "steps_cardslong_summary";
				default:              return null;
			}
    	}
    	else if (mode == WMC) {
    		switch (gameType) {
		    	case NUMBERS_SPEED:	  return "wmc_numbersspeed_summary";
				case NUMBERS_LONG:    return "wmc_numberslong_summary";
				case NUMBERS_BINARY:  return "wmc_numbersbinary_summary";
				case NUMBERS_SPOKEN:  return "wmc_numbersspoken_summary";
				case LISTS_WORDS:     return "wmc_listswords_summary";
				case LISTS_EVENTS:    return "wmc_listsevents_summary";
				case SHAPES_FACES:    return "wmc_shapesfaces_summary";
				case SHAPES_ABSTRACT: return "wmc_shapesabstract_summary";
				case CARDS_SPEED:     return "wmc_cardsspeed_summary";
				case CARDS_LONG:      return "wmc_cardslong_summary";
				default:              return null;
    		}
    	}
    	else if (mode == CUSTOM) {
    		switch (gameType) {
		    	case NUMBERS_SPEED:	  return "custom_numbersspeed_summary";
				case NUMBERS_BINARY:  return "custom_numbersbinary_summary";
				case NUMBERS_SPOKEN:  return "custom_numbersspoken_summary";
				case LISTS_WORDS:     return "custom_listswords_summary";
				case LISTS_EVENTS:    return "custom_listsevents_summary";
				case SHAPES_FACES:    return "custom_shapesfaces_summary";
				case SHAPES_ABSTRACT: return "custom_shapesabstract_summary";
				case CARDS_SPEED:     return "custom_cardsspeed_summary";
				case CARDS_LONG:     return "custom_cardslong_summary";
				default:              return null;
    		}
    	}
    	else
    		return null;
	}
    
    public String[] readPastScores(int mode, int gameType) {
    	List<String> scores = new ArrayList<String>();
    	try {
    	    BufferedReader inputReader = new BufferedReader(new InputStreamReader(context.openFileInput(getPastScoreResource(mode, gameType))));
    	    String score;                
    	    while ((score = inputReader.readLine()) != null) {
    	    	if (score != null && !score.equals(""))
    	    		scores.add(score);
    	    }
    	} catch (IOException e) {    e.printStackTrace();  	}
    	  catch (NullPointerException e) {    e.printStackTrace();  	}
    	
    	String[] strings = (String[]) scores.toArray(new String[scores.size()]);
	    return strings;
    }
    
    public void updatePastScores(int mode, int gameType, String score) {
    	try {
    	    FileOutputStream fos = context.openFileOutput(getPastScoreResource(mode, gameType), Context.MODE_PRIVATE | Context.MODE_APPEND);
    	    score += "\n";
    	    fos.write(score.getBytes());
    	    fos.close();
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
    }

    public String[] readScoreSummary(int mode, int gameType) {
    	List<String> scores = new ArrayList<String>();
    	try {
    	    BufferedReader inputReader = new BufferedReader(new InputStreamReader(context.openFileInput(getPastScoreSummaryResource(mode, gameType))));
    	    String line;                
    	    while ((line = inputReader.readLine()) != null) {
    	        if (line != null && !line.equals(""))
    	        	scores.add(line);
    	    }
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    	
    	String[] strings = (String[]) scores.toArray(new String[scores.size()]);
	    return strings;
    }
    
	public void updateScoreSummary(int mode, int gameType, String[] strings) {
		try {
    	    FileOutputStream fos = context.openFileOutput(getPastScoreSummaryResource(mode, gameType), Context.MODE_PRIVATE);
    	    for (int i=0; i<strings.length; i++) {
    	    	strings[i] += "\n";
    	    	fos.write(strings[i].getBytes());
    	    }
    	    fos.close();
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
	}
    
	
}