/**
 * Lab 3: Decks of Cards
 * CST 338: Software Design (Spring B 2021)
 *
 * Holds information about an individual card.
 * Contains the value, suit, and an error flag.
 * Uses an enum for card suits.
 */
public class Card
{
   private char value;
   private Suit suit;
   private boolean errorFlag;
   public static char[] valuRanks = 
      { 'X', 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T','J', 'Q', 'K'};
   
   public enum Suit
   {
      CLUBS, DIAMONDS, HEARTS, SPADES
   }
   //Constructors
   public Card(char value, Suit suit)
   {
      set(value, suit);
   }
   public Card()
   {
      value = 'A';
      suit = Suit.SPADES;
   }
   //Accessors and Modifier
   public char getValue()
   {
      return value;
   }
   public Suit getSuit()
   {
      return suit;
   }
   public boolean errorFlag()
   {
      return errorFlag;
   }
   /**
    * A single modifier for all private data.
    * Sets the card's value and suit. Sets an
    * error flag upon failure.
    *
    * @param value The card's value
    * @param suit The card's suit
    * @return The card's error flag
    */
   public boolean set(char value, Suit suit)
   {
      errorFlag = !isValid(value, suit);
      if(!errorFlag)
      {
         this.value = value;
         this.suit = suit;
      }
      return !errorFlag;
   }
   //Public methods
   /**
    * A "stringizer" method that returns a clean representation of a card.
    *
    * @return "[invalid]" if errorFlag == true
    *
   */
   public String toString()
   {
      if(!errorFlag)
      {
         return value + " of " + suit;
      }
      return "[invalid]";
   }
   /**
    * Checks if this card is identical to the provided card.
    *
    * @param card The card to compare
    * @return True if cards are identical
    */
   public boolean equals(Card card)
   {
      return (value == card.value && suit == card.suit && errorFlag == card.errorFlag);
   }

   /**
    * sort the incoming array of cards using a bubble sort routine.
    *
    * Bubble sort algorithm: <code>
    * bubbleSort(array)
    *  for i <- 1 to indexOfLastUnsortedElement-1
    *     if leftElement > rightElement
    *        swap leftElement and rightElement
    * end bubbleSort </code>
    * https://www.programiz.com/dsa/bubble-sort
    * @param cardArray
    * @param arraySize
    */
   public static void arraySort(Card[] cardArray, int arraySize) {
      for (int i = 0; i < arraySize; ++i) {
         for (int j = 0; j < arraySize; ++j) {
            if (cardArray[j].createRanking() > 
               cardArray[j + 1].createRanking()) {
               Card temp = new Card(cardArray[j].getValue(),
                  cardArray[j].getSuit());
               cardArray[j] = new Card(cardArray[j + 1].getValue(),
                  cardArray[j + 1].getSuit());
               cardArray[j + 1] = new Card(temp.getValue(), temp.getSuit());
            }
            else if(cardArray[j] == null) {
               return;
            }
         }
      }
   }

   /**
    * Helper method for arraySort to give each card a numeric value for sorting
    */
   private int createRanking() {
      for (int i = 0; i < valuRanks.length; ++i) {
         if(value == valuRanks[i])
            return i;
      }
      return 0;
   }

   
   //Private methods
   /**
    * Validates that the received data can be
    * used to create a card. Card value must be
    * found in the array of valid characters.
    *
    * @param value The card value to be verified
    * @param suit The card's suit
    * @return True if the card is valid.
    */
   private boolean isValid(char value, Suit suit)
   {
      boolean isValid = false;
      char[] validChars = { 'X', 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K'};
      for (int i = 0; i < validChars.length; i++)
      {
         if (value == validChars[i]) isValid = true;
      }
      return isValid;
   }
}



