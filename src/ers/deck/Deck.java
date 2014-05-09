package ers.deck;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A custom data structure to represent a deck of cards
 */
public class Deck {
	private ArrayList<Card> deck;

    /**
     * Default constructor
     */
	public Deck() {
		deck = new ArrayList<Card>();
	}

    /**
     * Gets the size of the deck
     * @return The size of the deck as an int
     */
	public int size() {
		return deck.size();
	}

    /**
     * Pushes a card to the top of the deck
     * @param card The card to add
     */
	public void push(Card card) {
		deck.add(0,card);
	}

    /**
     * Get the first card in the deck. Does not alter the deck.
     * @return The first card in the deck
     */
	public Card peek() {
		return deck.get(0);
	}

    /**
     * Get the first card in the deck and remove it from the deck
     * @return The first card in the deck
     */
	public Card pop() {
		return deck.remove(0);
	}

    /**
     * Get the last card in the deck and remove it from the deck
     * @return The last card in the deck
     */
	public Card bottomPop() {
		return deck.remove(deck.size()-1);
	}

    /**
     * Peek at several cards at the top of the deck
     * @param num The number of cards to peek at
     * @return An array of cards
     */
	public Card[] peek(int num) {
		Card[] cards = new Card[num];
		for(int i=0; i<num && i<deck.size(); i++)
			cards[i] = deck.get(i);
		return cards;
	}

    /**
     * Adds the cards in another Deck to the bottom of this Deck
     * @param d The Deck to add to the bottom
     */
	public void addToBottom(Deck d) {
		for(int i=d.size()-1; i>=0; i--) 
			deck.add(d.bottomPop());
	}

    /**
     * Add a card to the bottom of the deck
     * @param c The card to add
     */
	public void addToBottom(Card c) {
		deck.add(c);
	}

    /**
     * Shuffle the deck
     */
	public void shuffle() {
		Collections.shuffle(deck);
	}
}
