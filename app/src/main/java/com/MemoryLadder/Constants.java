package com.MemoryLadder;

import android.support.annotation.DrawableRes;

import com.MemoryLadder.TakeTest.GameActivity;
import com.MemoryLadder.ListsChallenges.Lists_Mem;
import com.MemoryLadder.NumbersChallenges.DigitSpeed;
import com.MemoryLadder.NumbersChallenges.Numbers_Mem;
import com.MemoryLadder.ShapesChallenges.Shapes_Mem;
import com.mastersofmemory.memoryladder.R;

public class Constants {
	
	final public static int NUMBERS_SPEED   = 1;
	//final public static int NUMBERS_LONG    = 2;
	final public static int NUMBERS_BINARY  = 3;
	final public static int NUMBERS_SPOKEN  = 4;
	final public static int LISTS_WORDS     = 5;
	final public static int LISTS_EVENTS    = 6;
	final public static int SHAPES_FACES    = 7;
	final public static int SHAPES_ABSTRACT = 8;
	//final public static int CARDS_SPEED     = 9;
	final public static int CARDS_LONG      = 10;
	
	final public static int default_cards_deckSize = 10;
	final public static int default_cards_numDecks = 1;
	final public static int default_cards_memTime = 300;
	final public static int default_cards_recallTime = 600;
	final public static int default_cards_cardsPerGroup = 2;
	
	final public static int default_words_numCols = 5;
	final public static int default_words_numRows = 5;
	final public static int default_words_memTime = 300;
	final public static int default_words_recallTime = 600;
	
	public final static int default_dates_numCols = 1;
	public final static int default_dates_numRows = 10;
	public final static int default_dates_memTime = 300;
	public final static int default_dates_recallTime = 600;
	
	final public static int default_faces_numCols = 3;
	final public static int default_faces_numRows = 5;
	final public static int default_faces_memTime = 300;
	final public static int default_faces_recallTime = 600;
	
	final public static int default_abstract_numCols = 5;
	final public static int default_abstract_numRows = 5;
	final public static int default_abstract_memTime = 300;
	final public static int default_abstract_recallTime = 600;
	
	final public static int default_written_numCols = 10;
	final public static int default_written_numRows = 5;
	final public static int default_written_digitsPerGroup = 2;
	final public static int default_written_memTime = 300;
	final public static int default_written_recallTime = 600;
	
	final public static int default_binary_numCols = 10;
	final public static int default_binary_numRows = 5;
	final public static int default_binary_digitsPerGroup = 2;
	final public static int default_binary_memTime = 300;
	final public static int default_binary_recallTime = 600;
	
	final public static int default_spoken_numCols = 10;
	final public static int default_spoken_numRows = 1;
	final public static int default_spoken_recallTime = 600;
	public final static int default_spoken_digitSpeed = DigitSpeed.DIGIT_SPEED_STANDARD;

	final public static int STEPS  = 0;
	//final public static int WMC    = 1;
	final public static int CUSTOM = 2;

	public static String getGameName(int gameType) {
		switch (gameType) {
			case NUMBERS_SPEED: return "NUMBERS_SPEED";
			case NUMBERS_BINARY: return "NUMBERS_BINARY";
			case NUMBERS_SPOKEN: return "NUMBERS_SPOKEN";
			case LISTS_WORDS: return "LISTS_WORDS";
			case LISTS_EVENTS: return "LISTS_EVENTS";
			case SHAPES_FACES: return "SHAPES_FACES";
			case SHAPES_ABSTRACT: return "SHAPES_ABSTRACT";
			case CARDS_LONG: return "CARDS_LONG";
			default: return "Unknown";
		}
	}

	public static String getGameSku(int gameType) {
		switch (gameType) {
			case NUMBERS_SPEED: return "com.memoryladder.numbers";
			case NUMBERS_BINARY: return "com.memoryladder.numbers";
			case NUMBERS_SPOKEN: return "com.memoryladder.numbers";
			case LISTS_WORDS: return "com.memoryladder.randomwords";
			case LISTS_EVENTS: return "com.memoryladder.historicdates";
			case SHAPES_FACES: return "com.memoryladder.namesandfaces";
			case SHAPES_ABSTRACT: return "com.memoryladder.abstractimages";
			case CARDS_LONG: return "com.memoryladder.cards";
			default: return "Unknown";
		}
	}

	public static String getGameDisplayName(int gameType) {
        switch (gameType) {
            case NUMBERS_SPEED: return "Speed Numbers";
            case NUMBERS_BINARY: return "Binary Numbers";
            case NUMBERS_SPOKEN: return "Spoken Numbers";
            case LISTS_WORDS: return "Random Words";
            case LISTS_EVENTS: return "Historic Dates";
            case SHAPES_FACES: return "Names and Faces";
            case SHAPES_ABSTRACT: return "Abstract Images";
            case CARDS_LONG: return "Speed Cards";
            default: return "Unknown";
        }
    }

    public static Class getClass(int gameType) {
        switch (gameType) {
            case NUMBERS_SPEED: return GameActivity.class;
            case NUMBERS_BINARY: return GameActivity.class;
            case NUMBERS_SPOKEN: return Numbers_Mem.class;
            case LISTS_WORDS: return GameActivity.class;
            case LISTS_EVENTS: return Lists_Mem.class;
            case SHAPES_FACES: return Shapes_Mem.class;
            case SHAPES_ABSTRACT: return Shapes_Mem.class;
            case CARDS_LONG: return GameActivity.class;
            default: return Numbers_Mem.class;
        }
    }

    public static String getPrefsName(int gameType) {
        switch (gameType) {
            case NUMBERS_SPEED: return "Number_Preferences";
            case NUMBERS_BINARY: return "Number_Preferences";
            case NUMBERS_SPOKEN: return "Number_Preferences";
            case LISTS_WORDS: return "List_Preferences";
            case LISTS_EVENTS: return "List_Preferences";
            case SHAPES_FACES: return "Shape_Preferences";
            case SHAPES_ABSTRACT: return "Shape_Preferences";
            case CARDS_LONG: return "Card_Preferences";
            default: return "";
        }
    }

	public static int[] getSpecs_STEPS_Numbers(int level) {
    	int[] specs = new int[5];
    	switch (level) {
    		case 1: 
    			specs[0] = 1;
    			specs[1] = 5;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 5; break;
    		case 2: 
    			specs[0] = 2;
    			specs[1] = 5;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 10; break;
    		case 3: 
    			specs[0] = 3;
				specs[1] = 10;
				specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 25; break;
    		case 4: 
    			specs[0] = 6;
				specs[1] = 10;
				specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 50; break;
    		case 5: 
    			specs[0] = 10;
				specs[1] = 10;
				specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 80; break;
    	}
    	return specs;
    }
	
	public static int[] getSpecs_STEPS_Binary(int level) {
    	int[] specs = new int[5];
    	switch (level) {
    		case 1: 
    			specs[0] = 1;
    			specs[1] = 5;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 5; break;
    		case 2: 
    			specs[0] = 1;
    			specs[1] = 10;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 10; break;
    		case 3: 
    			specs[0] = 2;
				specs[1] = 10;
				specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 15; break;
    		case 4: 
    			specs[0] = 3;
				specs[1] = 10;
				specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 25; break;
    		case 5: 
    			specs[0] = 5;
				specs[1] = 10;
				specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 45; break;
    	}
    	return specs;
    }
	
	public static int[] getSpecs_STEPS_Spoken(int level) {
    	int[] specs = new int[5];
    	switch (level) {
    		case 1: 
    			specs[0] = 1;
    			specs[1] = 4;
    			specs[2] = 300;
    			specs[3] = DigitSpeed.DIGIT_SPEED_FAST;
    			specs[4] = 4; break;
    		case 2: 
    			specs[0] = 1;
    			specs[1] = 7;
    			specs[2] = 300;
    			specs[3] = DigitSpeed.DIGIT_SPEED_STANDARD;
    			specs[4] = 7; break;
    		case 3: 
    			specs[0] = 1;
				specs[1] = 10;
				specs[2] = 300;
				specs[3] = DigitSpeed.DIGIT_SPEED_SLOW;
				specs[4] = 10; break;
    		case 4: 
    			specs[0] = 1;
				specs[1] = 15;
				specs[2] = 300;
				specs[3] = DigitSpeed.DIGIT_SPEED_SLOW;
				specs[4] = 15; break;
    		case 5: 
    			specs[0] = 1;
				specs[1] = 25;
				specs[2] = 300;
				specs[3] = DigitSpeed.DIGIT_SPEED_STANDARD;
				specs[4] = 25; break;
    	}
    	return specs;
    }
	
	public static int[] getSpecs_STEPS_RandomWords(int level) {
    	int[] specs = new int[5];

    	switch (level) {
    		case 1: 
    			specs[0] = 5;
    			specs[1] = 1;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 4; break;
    		case 2: 
    			specs[0] = 5;
    			specs[1] = 2;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 8; break;
    		case 3: 
    			specs[0] = 5;
				specs[1] = 4;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 16; break;
    		case 4: 
    			specs[0] = 6;
				specs[1] = 5;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 25; break;
    		case 5: 
    			specs[0] = 8;
				specs[1] = 5;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 35; break;
    	}
    	return specs;
    }
	
	public static int[] getSpecs_STEPS_HistoricDates(int level) {
    	int[] specs = new int[5];
    	switch (level) {
    		case 1: 
    			specs[0] = 3;
    			specs[1] = 1;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 3; break;
    		case 2: 
    			specs[0] = 6;
    			specs[1] = 1;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 6; break;
    		case 3: 
    			specs[0] = 9;
				specs[1] = 1;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 9; break;
    		case 4: 
    			specs[0] = 15;
				specs[1] = 1;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 15; break;
    		case 5: 
    			specs[0] = 25;
				specs[1] = 1;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 25; break;
    	}
    	return specs;
    }
	
	public static int[] getSpecs_STEPS_NameAndFaces(int level) {
    	int[] specs = new int[5];
    	switch (level) {
    		case 1: 
    			specs[0] = 1;
    			specs[1] = 3;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 5; break;
    		case 2: 
    			specs[0] = 2;
    			specs[1] = 3;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 10; break;
    		case 3: 
    			specs[0] = 3;
				specs[1] = 3;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 15; break;
    		case 4: 
    			specs[0] = 5;
				specs[1] = 3;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 25; break;
    		case 5: 
    			specs[0] = 10;
				specs[1] = 3;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 50; break;
    	}
    	return specs;
    }
	
	public static int[] getSpecs_STEPS_AbstractImages(int level) {
    	int[] specs = new int[5];
    	switch (level) {
    		case 1: 
    			specs[0] = 1;
    			specs[1] = 5;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 5; break;
    		case 2: 
    			specs[0] = 2;
    			specs[1] = 5;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 10; break;
    		case 3: 
    			specs[0] = 3;
				specs[1] = 5;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 13; break;
    		case 4: 
    			specs[0] = 5;
				specs[1] = 5;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 20; break;
    		case 5: 
    			specs[0] = 10;
				specs[1] = 5;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 50; break;
    	}
    	return specs;
    }
	
	public static int[] getSpecs_STEPS_Cards(int level) {
    	int[] specs = new int[5];
    	switch (level) {
    		case 1: 
    			specs[0] = 4;
    			specs[1] = 1;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 4; break;
    		case 2: 
    			specs[0] = 6;
    			specs[1] = 1;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 6; break;
    		case 3: 
    			specs[0] = 12;
				specs[1] = 1;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 12; break;
    		case 4: 
    			specs[0] = 18;
				specs[1] = 1;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 18; break;
    		case 5: 
    			specs[0] = 52;
				specs[1] = 1;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 52; break;
    	}
    	return specs;
    }
}