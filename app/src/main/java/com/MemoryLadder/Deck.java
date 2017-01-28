package com.MemoryLadder;

//import com.MemoryLadderFull.R;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	protected char[] suits = {Card.CLUBS,Card.SPADES,Card.HEARTS,Card.DIAMONDS};
    protected char[] values = {Card.ACE,'2','3','4','5','6','7','8','9',Card.TEN, Card.JACK,Card.QUEEN,Card.KING};
    private ArrayList<Card> deck;    
    private int deckSize;
    
    	public Deck(int deckSize) {
    		this.deckSize = deckSize;
    		ArrayList<Card> deckTemp = new ArrayList<Card>(52);
            for (char suit : suits)  {
                    for (char value : values) {
                            deckTemp.add(new Card(suit, value));
                    }
            }                
            Collections.shuffle(deckTemp);
            
            deck = new ArrayList<Card>(deckSize);
            for (int i=0; i<deckSize; i++) {
            	deck.add((Card) deckTemp.get(i));
            }
        }
    	
    	public int[] getImageIds() {
    		int[] ids = new int[deckSize];
    		for (int i=0; i<deckSize; i++) {
    			ids[i] = getCard(i).getImageResourceId();
    		}
    		return ids;
    	}
    	
    	public String[] getDeckStringArray() {
    		String[] strings = new String[deckSize];
    		for (int i=0; i<deckSize; i++) {
    			strings[i] = getCard(i).getPngName(); // e.g. "card_s7" or "card_hq"
    		}
    		return strings;
    	}
    	
    	public ArrayList<Card> getDeck() {
    		return deck;
    	}
    	
    	public Card getCard(int index) {
    		return deck.get(index);
    	}
    	
    	public int length() {
    		return deckSize;
    	}
        	
}