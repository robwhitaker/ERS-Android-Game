package deck;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	private ArrayList<Card> deck;
	
	public Deck() {
		deck = new ArrayList<Card>();
	}
	
	public int size() {
		return deck.size();
	}
	
	public void push(Card card) {
		deck.add(0,card);
	}
	
	public Card peek() {
		return deck.get(0);
	}
	
	public Card pop() {
		return deck.remove(0);
	}
	
	public Card bottomPop() {
		return deck.remove(deck.size()-1);
	}
	
	public Card[] peek(int num) {
		Card[] cards = new Card[num];
		for(int i=0; i<num && i<deck.size(); i++)
			cards[i] = deck.get(i);
		return cards;
	}
	
	public void addToBottom(Deck d) {
		for(int i=d.size()-1; i>=0; i--) 
			deck.add(d.bottomPop());
	}
	
	public void addToBottom(Card c) {
		deck.add(c);
	}
	
	public void shuffle() {
		Collections.shuffle(deck);
	}
}
