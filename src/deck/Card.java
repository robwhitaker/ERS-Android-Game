package deck;

import android.graphics.Bitmap;

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
	
	public Card(int newId) {
		id = newId;
		suit = Math.round((id/100) * 100);
		rank = id - suit; 
	}
	
	public void setBitmap(Bitmap newBitmap) {
		bmp = newBitmap; 
	}
	
	public Bitmap getBitmap() {
		return bmp;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSuit() {
		return suit;
	}
	
	public int getRank() {
		return rank;
	}

}
