package com.MemoryLadder;

import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;

public class Utils {

	static int[] shuffleIntArray(int[] array) {
		Integer[] temp = new Integer[array.length];
		for (int i=0; i<array.length; i++) {
			temp[i] = array[i];
		}
		Collections.shuffle(Arrays.asList(temp));
		for (int i=0; i<array.length; i++) {
    		array[i] = temp[i];
    	}
		return array;
 	}
	
	static int[] shuffleIntArrayByRow(int[] array, int numRows, int numCols) {
		Integer[] temp = new Integer[numCols];
		for (int row=0; row<numRows; row++) {			
			for (int col=0; col<numCols; col++) {
				temp[col] = array[(row * numCols) + col];
			}			
			Collections.shuffle(Arrays.asList(temp));			
			for (int col=0; col<numCols; col++) {
				array[(row * numCols) + col] = temp[col];
			}
		}
		return array;
 	}
	
	
	static String getTestString(int gameType) {
		switch (gameType) {
			case Constants.NUMBERS_SPEED:   return "Speed Numbers";
			case Constants.NUMBERS_BINARY:  return "Binary Numbers";
			case Constants.NUMBERS_SPOKEN:  return "Spoken Numbers";
			case Constants.LISTS_WORDS:     return "Random Words";
			case Constants.LISTS_EVENTS:    return "Historic Events";
			case Constants.SHAPES_FACES:    return "Names & Faces";
			case Constants.SHAPES_ABSTRACT: return "Abstract Images";
			case Constants.CARDS_LONG:      return "Speed Cards";
			default:              return "I don't know";
		}
	}
	
		
	private static int[] getSpecsSteps(int gameType, int step) {
		switch (gameType) {
	        case 1:  return Constants.getSpecs_STEPS_Numbers(step);
	        case 3:  return Constants.getSpecs_STEPS_Binary(step);
	        case 4:  return Constants.getSpecs_STEPS_Spoken(step);
	        case 5:  return Constants.getSpecs_STEPS_RandomWords(step);
	        case 6:  return Constants.getSpecs_STEPS_HistoricDates(step);
	        case 7:  return Constants.getSpecs_STEPS_NameAndFaces(step);
	        case 8:  return Constants.getSpecs_STEPS_AbstractImages(step);
	        case 9:  return Constants.getSpecs_STEPS_Cards(step);
	        case 10: return Constants.getSpecs_STEPS_Cards(step);
	        default: return null;
		}
	}
	
	public static double getTargetScore(int gameType, int step) {
		int[] specs = getSpecsSteps(gameType, step);
		return specs != null ? specs[specs.length - 1] : 0;
	}
    
    static Class<?> getReviewClass(int gameType) {
    	switch (gameType) {
	        case 1:  return Numbers_Review.class;
	        case 2:  return Numbers_Review.class;
	        case 3:  return Numbers_Review.class;
	        case 4:  return Numbers_Review.class;
	        case 5:  return Lists_Review.class;
	        case 6:  return Lists_Review.class;
	        case 7:  return Shapes_Review.class;
	        case 8:  return Shapes_Review.class;
        	default: return null;
		}
    }
    

	private static String[] getStrings(String string) {
		StringTokenizer st = new StringTokenizer(string, "*");
		String[] strings = new String[st.countTokens()];
		
		int i = 0;
		 while (st.hasMoreTokens()) {
			strings[i] = st.nextToken();
			i++;
		 }
		 return strings;
	}
	
	static String[] getNumberSuggestions(int num) {
		switch (num) {
			case 0:  return getStrings( "Seesaw*Sow*Saw*" );
			case 1:  return getStrings( "Soot*Dye*Tea*Tie*Hat*" );
			case 2:  return getStrings( "Sun*Knee*New*Noah*Hen*" );
			case 3:  return getStrings( "Sum*Ma*Me*Ham*" );
			case 4:  return getStrings( "Sore*Row*Ear*Rye*" );
			case 5:  return getStrings( "Sail*Law*Owl*Hill*" );
			case 6:  return getStrings( "Sash*Shoe*Gay*" );
			case 7:  return getStrings( "Sack*Cow*Key*" );
			case 8:  return getStrings( "Safe*Fee*UFO*Ivy*" );
			case 9:  return getStrings( "Sub*Bay*Bee*" );
			case 10: return getStrings( "Daze*Doze*Dash*Toes*" );
			case 11: return getStrings( "Dad*Dead*Tot*" );
			case 12: return getStrings( "Tuna*Dan*Dune*Tan*" );
			case 13: return getStrings( "Dam*Dim*Atom*Dime*" );
			case 14: return getStrings( "Tire*Dairy*Deer*" );
			case 15: return getStrings( "Dale*Dual*Tale*Towel*Doll*" );
			case 16: return getStrings( "Dash*Dog*Tissue*" );
			case 17: return getStrings( "Deck*Duck*Duke*Tack*" );
			case 18: return getStrings( "Daffy*Dove*TV*" );
			case 19: return getStrings( "Dab*Tuba*Tap*Tape*" );
			case 20: return getStrings( "NASA*Noose*Nose*" );
			case 21: return getStrings( "Net*Ant*Newt*" );
			case 22: return getStrings( "Nun*Noon*Nan*" );
			case 23: return getStrings( "Name*Gnome*Enemy*Nemo*" );
			case 24: return getStrings( "Nero*Honor*" );
			case 25: return getStrings( "Nail*Noel*Nile*" );
			case 26: return getStrings( "Niche*Notch*Wing*Nachos*" );
			case 27: return getStrings( "Nag*Neck*Ink*" );
			case 28: return getStrings( "Navy*Knife*" );
			case 29: return getStrings( "Nab*Nip*Newbie*Nib*Knob*" );
			case 30: return getStrings( "Mace*Mice*Mouse*" );
			case 31: return getStrings( "Mat*Mud*Myth*" );
			case 32: return getStrings( "Man*Moon*Mine*Moan*" );
			case 33: return getStrings( "Ma'am*Mum*Memo*Mummy*" );
			case 34: return getStrings( "Mare*Mower*Humor*" );
			case 35: return getStrings( "Mail*Email*Mule*" );
			case 36: return getStrings( "Mash*Image*Match*" );
			case 37: return getStrings( "Mac*Make*Macho*Mic*Mug*" );
			case 38: return getStrings( "Mafia*Movie*" );
			case 39: return getStrings( "Map*Amoeba*Amp*" );
			case 40: return getStrings( "Race*Rose*Horse*" );
			case 41: return getStrings( "Rat*Road*Heart*Wart*" );
			case 42: return getStrings( "Rain*Heroin*Iran*" );
			case 43: return getStrings( "Ram*Arm*Room*" );
			case 44: return getStrings( "Roar*Rear*Arrow*Mower*Aurora*" );
			case 45: return getStrings( "Rail*Rule*Royal*" );
			case 46: return getStrings( "Rage*Rush*Roach*Rash*" );
			case 47: return getStrings( "Rack*Wreck*Rich*Rake*Rock*" );
			case 48: return getStrings( "Roof* Rafia*Review*" );
			case 49: return getStrings( "Rap*Rope*Robe*" );
			case 50: return getStrings( "Lace*Lassie*Loose*Lasso*" );
			case 51: return getStrings( "Lad*Loot*Old*Lid*" );
			case 52: return getStrings( "Lane*Lion*Loan*Alien*" );
			case 53: return getStrings( "Lamb*Lame*Lama*Llama*Lime*" );
			case 54: return getStrings( "Lair*Lure*Liar*Lyre*" );
			case 55: return getStrings( "Lily*Hello*LOL*" );
			case 56: return getStrings( "Lash*Leech*Leg*" );
			case 57: return getStrings( "Lake*Leak*Log*" );
			case 58: return getStrings( "Lava*Wolf*Love*Loaf*" );
			case 59: return getStrings( "Lab*Lip*Loop*" );
			case 60: return getStrings( "Chase*Chess*Goose*Cheese*" );
			case 61: return getStrings( "Chat*Goat*Sheet*" );
			case 62: return getStrings( "Chain*Chin*Gun*" );
			case 63: return getStrings( "Chime*Game*Jam*" );
			case 64: return getStrings( "Chair*Gray*Cherry*" );
			case 65: return getStrings( "Cello*Chill*Galaxy*Jello*" );
			case 66: return getStrings( "Chacha*Egg*Church*Judge*" );
			case 67: return getStrings( "Check*Chalk*Joke*" );
			case 68: return getStrings( "Chaff*Chief*Goofy*Sheaf*Chef*" );
			case 69: return getStrings( "Chap*Chip*Jeep*Ship*" );
			case 70: return getStrings( "Case*Cheese*Gas*" );
			case 71: return getStrings( "Cat*Kite*Coyote*" );
			case 72: return getStrings( "Can*Cane*China*Icon*" );
			case 73: return getStrings( "Comb*Coma*Cameo*" );
			case 74: return getStrings( "Car*Acre*Crow*" );
			case 75: return getStrings( "Call*Cool*Cola*Coil*Coal*" );
			case 76: return getStrings( "Cage*Cash*Chug*Keg*" );
			case 77: return getStrings( "Cake*Coke*Rake*" );
			case 78: return getStrings( "Cafe*Cave*Chef*Chief*" );
			case 79: return getStrings( "Cab*Cub*Chip*Cape*" );
			case 80: return getStrings( "Face*Fuss*Fish*Fez*" );
			case 81: return getStrings( "Fad*Foot*Fat*" );
			case 82: return getStrings( "Fan*Fun*Phone*Fin*" );
			case 83: return getStrings( "Fame*Foam*" );
			case 84: return getStrings( "Fair*Fur*Fairy*Fire*" );
			case 85: return getStrings( "Fall*Fly*File*" );
			case 86: return getStrings( "Fish*Fog*Fiji*" );
			case 87: return getStrings( "Fog*Fake*Vig*" );
			case 88: return getStrings( "Fife*FIFA*" );
			case 89: return getStrings( "Fab*FBI*Vibe*Fib*" );
			case 90: return getStrings( "Base*Bus*Us*" );
			case 91: return getStrings( "Bat*Boat*Bite*" );
			case 92: return getStrings( "Ban*Bun*Pin*Bone*Pen*" );
			case 93: return getStrings( "Bam!*Beam*Bum*Opium*" );
			case 94: return getStrings( "Bar*Beer*Bear*Pear*" );
			case 95: return getStrings( "Ball*Pool*Bull*Bell*" );
			case 96: return getStrings( "Bash*Bush*Pig*Peach*" );
			case 97: return getStrings( "Back*Book*Bike*" );
			case 98: return getStrings( "Beef*Puff*Beehive*" );
			case 99: return getStrings( "Babe*Bib*Pipe*Pope*" );

			default: return null;
		}
	}

    
	private static int getNumericEquiv(int index) {
	    return (10 * ((index+1) % 13)) + (index / 13) + 1;    
	}
    
    
	public static String[] getCardSuggestions(int index) {
		
		switch (index) {
	      
	      /* Ace */
	      case 0: return new String[] { "Spade" }; // Spade
	      case 13: return new String[] { "Heart" }; // Heart
	      case 26: return new String[] { "Club" }; // Club
	      case 39: return new String[] { "Diamond" }; // Diamond
	      
	      /* Ten */
	      case  9: return new String[] { "Zit", "Site", "Soot" }; // Spade
	      case 22: return new String[] { "Sun", "Sign", "Zone" }; // Heart
	      case 35: return new String[] { "Seam", "Sumo", "Zoom" }; // Club
	      case 48: return new String[] { "Zorro", "Sour", "Sewer" }; // Diamond
	      
	      /* Jack */
	      case 10: return getNumberSuggestions(11); // Spade
	      case 23: return getNumberSuggestions(12); // Heart
	      case 36: return getNumberSuggestions(13); // Club
	      case 49: return getNumberSuggestions(14); // Diamond
	      
	      /* Queen */
	      case 11: return new String[] { "hat" }; // Spade
	      case 24: return new String[] { "hen" }; // Heart
	      case 37: return new String[] { "ham" }; // Club
	      case 50: return new String[] { "rye" }; // Diamond
	      
	      /* King */
	      case 12: return new String[] { "sing",  "steam", "sting" }; // Spade
	      case 25: return new String[] { "hinge", "hang" }; // Heart
	      case 38: return new String[] { "king" }; // Club
	      case 51: return new String[] { "drink", "dean" }; // Diamond
	        
	        /* Two - Nine */
	      default: return getNumberSuggestions(getNumericEquiv(index));
	    }
	}
	
	
	
	static String getMaxValue(String[] strings) {
    	int max = 0;
    	for (int i=0; i<strings.length; i++) {
    		if (Double.parseDouble(strings[i]) > Double.parseDouble(strings[max]))
    			max = i;
    	}
    	return strings[max];
    }
    
    static String getMinValue(String[] strings) {
    	int min = 0;
    	for (int i=0; i<strings.length; i++) {
    		if (Double.parseDouble(strings[i]) < Double.parseDouble(strings[min]))
    			min = i;
    	}
    	return strings[min];
    }	
    
    public static String formatIntoHHMMSStruncated(long secsIn) {
    	int hours = (int) (secsIn / 3600),
		remainder = (int) (secsIn % 3600),
		minutes = remainder / 60,
		seconds = remainder % 60;			
		if (hours > 0)	
			return  (hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds );
		else
			return (minutes < 10 ? "0"+minutes : minutes) + ":" + (seconds < 10 ? "0" : "") + seconds ;
    }
    
    static String formatIntoHHMMSS(long secsIn) {
    	int hours = (int) (secsIn / 3600),
		remainder = (int) (secsIn % 3600),
		minutes = remainder / 60,
		seconds = remainder % 60;			
		return  "0" + hours + ":" + (minutes < 10 ? "0"+minutes : minutes) + ":" + (seconds < 10 ? "0"+seconds : seconds);
    }
    
    static int getHours(String timestring) {
    	return Character.getNumericValue(timestring.charAt(1));
    }
    
    static int getMinutes(String timestring) {
    	return Integer.parseInt(timestring.substring(3, 5));
    }
    
    static int getSeconds(String timestring) {
    	return Integer.parseInt(timestring.substring(6, 8));
    }
    
    static int getTotalSeconds(String timestring) {
    	return (getHours(timestring) * 3600) + (getMinutes(timestring) * 60) + (getSeconds(timestring));
    }
}