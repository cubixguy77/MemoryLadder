package com.MemoryLadder.Cards;

import java.io.Serializable;
import java.lang.reflect.Field;

import android.content.Context;
import android.content.SharedPreferences;

import com.MemoryLadder.Utils;
import com.mastersofmemory.memoryladder.R;


/**
 * This class represents a Playing card
 */
public class Card implements Comparable<Card> , Serializable{
        private static final long serialVersionUID = 1L;

        // Suits
        public static final char HEARTS  =  'h';
        public static final char SPADES  =  's';
        public static final char CLUBS  =   'c';
        public static final char DIAMONDS = 'd';
        public static final String HEARTS_STRING = "Hearts";
        public static final String SPADES_STRING = "Spades";
        public static final String CLUBS_STRING = "Clubs";
        public static final String DIAMONDS_STRING = "Diamonds";
        // Tens
        public static final char JACK =  'j';
        public static final char QUEEN = 'q';
        public static final char KING =  'k';
        public static final char TEN =   't';
        public static final char JACKc =  'J';
        public static final char QUEENc = 'Q';
        public static final char KINGc =  'K';
        public static final char TENc =   'T';
        // Others
        public static final char BLACK_SUIT = 'b';
        public static final char RED_SUIT =   'r';
        public static final char JOKER =      'o';
        public static final char ACE =        '1';
        public static final char ACE_AS_FOURTEEN = 'f';
        
        public static final String TWO_STRING     = "Two";
        public static final String THREE_STRING   = "Three";
        public static final String FOUR_STRING    = "Four";
        public static final String FIVE_STRING    = "Five";
        public static final String SIX_STRING     = "Six";
        public static final String SEVEN_STRING   = "Seven";
        public static final String EIGHT_STRING   = "Eight";
        public static final String NINE_STRING    = "Nine";
        public static final String TEN_STRING     = "Ten";
        public static final String JACK_STRING    = "Jack";
        public static final String QUEEN_STRING   = "Queen";
        public static final String KING_STRING    = "King";
        public static final String ACE_STRING     = "Ace";
        
        private char suit;
        private char value;
        private boolean isVisible;
        private boolean isSelected;

        public boolean isVisible() {          return isVisible;       }

        public void setVisible(boolean isVisible) {   this.isVisible = isVisible;      }
        
        protected boolean isSelected(){    return isSelected;        }
        
        public void setSelected(boolean status){ this.isSelected = status;    }
        
        public void setValue(char value) {    this.value = value;        }
        
        public char getValue() {       return value;      }
        
        public void setSuit(char suit) {  this.suit = suit;       }
        
        public char getSuit() {    return suit;     }

        /* filenames follow convention of xy.png
         * x: char of value
         * y: char of suit       
         */
        public String getPngName() {
                return new String("card_" + suit + value);
        }

        public Card(char suit, char value) {
                super();
                this.suit = suit;
                this.value = value;
                this.isSelected = false;
        }
        
        public Card() {
            super();
            this.isSelected = false;
        }
        

        public int getImageResourceId(){
                Field f;
                int id = -1;
                try {
                        f = R.drawable.class.getDeclaredField(getPngName());
                        id = f.getInt(null);                        
                } catch (SecurityException e) {
                        e.printStackTrace();
                } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                } catch (IllegalAccessException e) {
                        e.printStackTrace();
                }
                return id;
        }

        public String toMnemonic(SharedPreferences prefs) {
        	int index = (getSuitValue() * 13) + getIntegerValue() - 1;
        	return "\"" + prefs.getString("prefs_cards_" + index, Utils.getCardSuggestions(index)[0]) + "\"";
        }
        
        public static String getSuit(int index) {
        	switch (index / 13) {
	        	case 0: return "s";       	
	        	case 1: return "h";
	        	case 2: return "c";
	        	case 3: return "d";
	        	default: return null;
        	}
        }
        
        public static String getVal(int index) {
        	switch ((index % 13) + 1) {
	        	case 1: return  "1";
	        	case 2: return  "2";
	        	case 3: return  "3";
	        	case 4: return  "4";
	        	case 5: return  "5";
	        	case 6: return  "6";
	        	case 7: return  "7";
	        	case 8: return  "8";
	        	case 9: return  "9";
	        	case 10: return "t";
	        	case 11: return "j";
	        	case 12: return "q";
	        	case 13: return "k";
	        	default: return null;
        	}
        }
        
        public static int getImageResourceID(Context context, int index) {
        	return context.getResources().getIdentifier("card_" + getSuit(index) + getVal(index), "drawable", context.getPackageName());
        }
        
        
        
        public int getSuitValue() {
        	switch (suit) {
        		case SPADES:
        			return 0;
        		case HEARTS:
        			return 1;
        		case CLUBS:
        			return 2;
        		case DIAMONDS:
        			return 3;
        		default:
        			return -1;
        	}
        }
        
        /**
         * @return an integer value of the card.
         */
        public Integer getIntegerValue(){
                Integer retVal = Character.getNumericValue(this.value);
                switch (value) {
                case TEN:
                        retVal = 10;
                        break;
                case JACK:
                        retVal = 11;
                        break;
                case QUEEN:
                        retVal = 12;
                        break;
                case KING:
                        retVal = 13;
                        break;
                case JOKER:
                        retVal = null;
                        break;
                case ACE_AS_FOURTEEN:
                        retVal = 14;
                        break;
                case ACE:
                		retVal = 1;
                		break;
                default:
                        break;
                }

                return retVal;
        }
        
        @Override
        public String toString() {
            return getCardText(getPngName());
        }
        
        public static String getCharacter(int val) {
    		switch (val) {
    			case 1:  return "A";  
    			case 2:  return "2";  
    			case 3:  return "3";
    			case 4:  return "4"; 
    			case 5:  return "5"; 
    			case 6:  return "6";  
    			case 7:  return "7";
    			case 8:  return "8";
    			case 9:  return "9"; 
    			case 10: return "10";  
    			case 11: return "J"; 
    			case 12: return "Q";
    			case 13: return "K";
    			default: return null;
    		}
        }
                        
        
        public static String getCardText(String string) {
    		char char1 = string.charAt(6);
    		String valueString = "";
    		switch (char1) {
    			case 'x': valueString = "???";   break;
    			case '1': valueString = "Ace";   break;
    			case '2': valueString = "Two";   break;
    			case '3': valueString = "Three"; break;
    			case '4': valueString = "Four";  break;
    			case '5': valueString = "Five";  break;
    			case '6': valueString = "Six";   break;
    			case '7': valueString = "Seven"; break;
    			case '8': valueString = "Eight"; break;
    			case '9': valueString = "Nine";  break;
    			case 't': valueString = "Ten";   break;
    			case 'j': valueString = "Jack";  break;
    			case 'q': valueString = "Queen"; break;
    			case 'k': valueString = "King";  break;
    		}
    		
    		char char2 = string.charAt(5);
    		String suitString = "";
    		switch (char2) {
    			case 'x': suitString = "???";      break;
    			case 's': suitString = "Spades";   break;
    			case 'h': suitString = "Hearts";   break;
    			case 'd': suitString = "Diamonds"; break;
    			case 'c': suitString = "Clubs";    break;
    		}

    		return valueString + " of " + suitString;
    	}
        
        @Override
        public int compareTo(Card other) {
                        return (other==null? -1 : (other.getIntegerValue()==null? 0 : other.getIntegerValue()) ) - (this.getIntegerValue()==null? 0: this.getIntegerValue());
        }
}