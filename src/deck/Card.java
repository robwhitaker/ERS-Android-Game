package deck;

import android.graphics.Bitmap;

/**
 * Class to represent a card
 */
public class Card {
	private int id;
	private int suit;
	private int rank;
	private Bitmap bmp;
	
	public static final int TWO = 2;
	public static final int THREE = 3;
	public static final int FOUR = 4;
	public static final int FIVE = 5;
	public static final int SIX = 6;
	public static final int SEVEN = 7;
	public static final int EIGHT = 8;
	public static final int NINE = 9;
	public static final int TEN = 10;
	public static final int JACK = 11;
	public static final int QUEEN = 12;
	public static final int KING = 13;
	public static final int ACE = 14;

    /**
     * Card default constructor.
     * @param newId The numeric id of the card that represents that value, rank and suit
     */
	public Card(int newId) {
		id = newId;
		suit = Math.round((id/100) * 100);
		rank = id - suit; 
	}

    /**
     * Sets the bitmap for the card
     * @param newBitmap The bitmap to associate with the card
     */
	public void setBitmap(Bitmap newBitmap) {
		bmp = newBitmap; 
	}

    /**
     * Getter for the card's Bitmap
     * @return A Bitmap
     */
	public Bitmap getBitmap() {
		return bmp;
	}

    /**
     * Getter for the card's id
     * @return The card's id. Int.
     */
	public int getId() {
		return id;
	}

    /**
     * Getter for the card's suit
     * @return The card's suit. Int.
     */
	public int getSuit() {
		return suit;
	}

    /**
     * Getter for the card's rank
     * @return The card's rank. Int.
     */
	public int getRank() {
		return rank;
	}

}
