package com.MemoryLadder;

import android.support.annotation.DrawableRes;

import com.MemoryLadder.Cards.CardPrototype;
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
	
	final static int SLOW   = 0;
	final static int MEDIUM = 1;
	final static int FAST   = 2;	
	
	final public static int default_cards_deckSize = 10;
	final public static int default_cards_numDecks = 1;
	final public static int default_cards_memTime = 300;
	final public static int default_cards_recallTime = 600;
	final public static int default_cards_cardsPerGroup = 2;
	
	final public static int default_words_numCols = 5;
	final public static int default_words_numRows = 5;
	final public static int default_words_memTime = 300;
	final public static int default_words_recallTime = 600;
	
	final static int default_dates_numCols = 1;
	final static int default_dates_numRows = 10;
	final static int default_dates_memTime = 300;
	final static int default_dates_recallTime = 600;
	
	final public static int default_faces_numCols = 3;
	final public static int default_faces_numRows = 5;
	final static int default_faces_numImages = default_faces_numCols * default_faces_numRows;
	final public static int default_faces_memTime = 300;
	final public static int default_faces_recallTime = 600;
	
	final public static int default_abstract_numCols = 5;
	final public static int default_abstract_numRows = 5;
	final static int default_abstract_numImages = default_abstract_numCols * default_abstract_numRows;
	final public static int default_abstract_memTime = 300;
	final public static int default_abstract_recallTime = 600;
	
	final public static int default_written_numCols = 10;
	final public static int default_written_numRows = 5;
	final public static int default_written_memTime = 300;
	final public static int default_written_recallTime = 600;
	
	final public static int default_binary_numCols = 10;
	final public static int default_binary_numRows = 5;
	final public static int default_binary_memTime = 300;
	final public static int default_binary_recallTime = 600;
	
	final public static int default_spoken_numCols = 10;
	final public static int default_spoken_numRows = 1;
	final static int default_spoken_numDigits = default_spoken_numCols * default_spoken_numRows;
	final public static int default_spoken_recallTime = 600;
	final static int default_spoken_digitSpeed = MEDIUM;
	final public static float default_spoken_secondsPerDigit = 1;

	final public static int STEPS  = 0;
	//final public static int WMC    = 1;
	final public static int CUSTOM = 2;

	final static int[] cards_tv_cardsperdeck    = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52};
	final static int[] cards_tv_cardspergroup   = {1, 2, 3};
	final static int[] cards_tv_numdecks        = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 30, 35, 40, 45, 50};
	
	final static int[] lists_tv_numcolumns      = {1,2,3,4,5, 10, 15, 20, 25, 30, 35, 40};
	final static int[] lists_tv_wordspercolumn  = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
	final static int[] lists_tv_numdates        = {1, 2, 3, 4, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80, 90, 100, 150, 200};
	
	final static int[] numbers_tv_numlines      = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 55, 60, 65, 70, 75, 80, 90, 100, 125, 150, 175, 200};
	final static int[] numbers_tv_digitsperline = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40};
	final static int[] numbers_tv_numdigits     = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 50, 60, 70, 80, 90, 100, 120, 150, 200, 300, 400, 500, 600, 750, 1000, 2000, 5000};
	
	final static int[] shapes_faces_tv_numimages      = {3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 39, 42, 45, 48, 51, 54, 57, 60, 63, 66, 69, 72, 75, 78, 81, 84, 87, 90, 93, 96, 99, 102, 105, 108, 111, 114, 117, 120};
	final static int[] shapes_abstract_tv_numimages   = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110, 115, 120, 125, 130, 135, 140, 145, 150, 155, 160, 165, 170, 175};

	final static int[] gameTypes = {
        NUMBERS_SPEED,
        NUMBERS_BINARY,
        NUMBERS_SPOKEN,
        LISTS_WORDS,
        LISTS_EVENTS,
        SHAPES_FACES,
        SHAPES_ABSTRACT,
        CARDS_LONG
    };

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

    public static @DrawableRes int getGameIcon(int gameType) {
        switch (gameType) {
            case NUMBERS_SPEED: return R.drawable.icon_numbers;
            case NUMBERS_BINARY: return R.drawable.icon_binary;
            case NUMBERS_SPOKEN: return R.drawable.icon_numbers_spoken;
            case LISTS_WORDS: return R.drawable.icon_randomwords;
            case LISTS_EVENTS: return R.drawable.icon_historicdates;
            case SHAPES_FACES: return R.drawable.icon_namesandfaces;
            case SHAPES_ABSTRACT: return R.drawable.icon_abstract_images;
            case CARDS_LONG: return R.drawable.icon_cards;
            default: return R.drawable.icon_numbers;
        }
    }

    public static Class getClass(int gameType) {
        switch (gameType) {
            case NUMBERS_SPEED: return Numbers_Mem.class;
            case NUMBERS_BINARY: return Numbers_Mem.class;
            case NUMBERS_SPOKEN: return Numbers_Mem.class;
            case LISTS_WORDS: return Lists_Mem.class;
            case LISTS_EVENTS: return Lists_Mem.class;
            case SHAPES_FACES: return Shapes_Mem.class;
            case SHAPES_ABSTRACT: return Shapes_Mem.class;
            case CARDS_LONG: return CardPrototype.class;
            default: return Numbers_Mem.class;
        }
    }

    public static Class<?> getSettingsClass(int gameType) {
        switch (gameType) {
            case NUMBERS_SPEED: return Numbers_Settings.class;
            case NUMBERS_BINARY: return Numbers_Settings.class;
            case NUMBERS_SPOKEN: return Numbers_Settings.class;
            case LISTS_WORDS: return Lists_Settings.class;
            case LISTS_EVENTS: return Lists_Settings.class;
            case SHAPES_FACES: return Shapes_Settings.class;
            case SHAPES_ABSTRACT: return Shapes_Settings.class;
            case CARDS_LONG: return Cards_Settings.class;
            default: return Numbers_Mem.class;
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
    			specs[0] = 1;
    			specs[1] = 10;
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
    			specs[0] = 3;
				specs[1] = 20;
				specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 50; break;
    		case 5: 
    			specs[0] = 5;
				specs[1] = 20;
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
    			specs[3] = 1;
    			specs[4] = 4; break;
    		case 2: 
    			specs[0] = 1;
    			specs[1] = 7;
    			specs[2] = 300;
    			specs[3] = 1;
    			specs[4] = 7; break;
    		case 3: 
    			specs[0] = 1;
				specs[1] = 10;
				specs[2] = 300;
				specs[3] = 1;
				specs[4] = 10; break;
    		case 4: 
    			specs[0] = 1;
				specs[1] = 15;
				specs[2] = 300;
				specs[3] = 1;
				specs[4] = 15; break;
    		case 5: 
    			specs[0] = 1;
				specs[1] = 25;
				specs[2] = 300;
				specs[3] = 1;
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
    			specs[0] = 5;
    			specs[1] = 1;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 5; break;
    		case 2: 
    			specs[0] = 8;
    			specs[1] = 1;
    			specs[2] = 300;
    			specs[3] = 600;
    			specs[4] = 8; break;
    		case 3: 
    			specs[0] = 16;
				specs[1] = 1;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 14; break;
    		case 4: 
    			specs[0] = 26;
				specs[1] = 1;
				specs[2] = 300;
    			specs[3] = 600;
				specs[4] = 24; break;
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