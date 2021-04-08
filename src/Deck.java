/**
 * Lab 3: Decks of Cards
 * CST 338: Software Design (Spring B 2021)
 * 
 * Deck: class object that will contain the source of all Card objects. Contains
 * and array of Card objects cards[]. It will contain a multiple 
 * of 52 cards, as some games rely on multiple decks.
 */
import java.util.Random;

public class Deck {
    // Members and constants
    private static final int DECK_SIZE = 56;    // 52 cards + 4 jokers
    private static boolean allocated = false; 
    private static Card[] masterPack = new Card[DECK_SIZE]; // contains every card type
    private Card[] cards;
    private int topCard;
    public static final int MAX_PACKS = 6;
    public static final int MAX_CARDS = DECK_SIZE * MAX_PACKS;    


    // Public Methods
    /**
     * Default Constructor assumes 1 pack
     */
    public Deck() {
        allocateMasterPack();
        init(1);
    }

    /**
     * Constructor will populate the arrays
     * @param numPacks
     */
    public Deck(int numPacks) {
        allocateMasterPack();        
        init(numPacks);
    }

    /**
     * fill the cards array with Card objects from the masterPack
     * @param numPacks
     */
    public void init(int numPacks) {
        if (numPacks < 1) numPacks = 1;
        else if (numPacks > MAX_PACKS) numPacks = MAX_PACKS;
        cards = new Card[(numPacks * DECK_SIZE)];
        topCard = 0;
        // an 'iterator' that should not be > 52 for masterPack reference
        int j = 0;      
        for (int i = 0; i < cards.length; ++i) {
            cards[i] = 
                new Card(masterPack[j].getValue(), masterPack[j].getSuit());
            j++;
            if (i % (DECK_SIZE - 1) == 0 && i != 0) {    // Start at masterPack[0] again
                j = 0;                      // array range is 0 < j < 51
            }
        }
    }

    /**
     * Mixes up the cards with the help of the standard random number generator
     */
    public void shuffle() { 
        Random r = new Random();
        // Populate a temp array with the cards
        Card[] temp = new Card[cards.length];
        for (int i = 0; i < cards.length; ++i) {
           temp[i] = cards[i];
           cards[i] = null;
        }
        for (int i = 0; i < cards.length; ++i) {
            int j = r.nextInt(cards.length);  // Create a new random number 
            if (cards[j] == null)
                cards[j] = temp[i];
            else 
                --i;    // Decrement the iterator to try again 
        }
    }

    /**
     * dealCard returns and removes the card in the top occupied position of 
     * cards[]. Make sure there are still cards available.  This is an object
     * copy, not a reference copy, since the source of the Card might destroy or
     * change its data after it is sent out.
     * @return A card or null if none available
     */

    public Card dealCard() {
        if (topCard == cards.length) return null;
        else if (cards[topCard] != null) {
            Card dealtCard =        // Make an object copy of the top card
                new Card(cards[topCard].getValue(), cards[topCard].getSuit());
            cards[topCard] = null;  // Remove the top card 
            topCard++;              // Increment the top card "pointer"
            return dealtCard;       // Deal the card 
        }
        else {
            return null;
        }
    }


    public int getTopCard() {
        return topCard;
    }
    
    /**
     * Accessor for an individual card.  Returns a card with errorFlag = true if
     * k is bad.  Also returns an object copy, not a reference copy.
     * @param k
     */
    public Card inspectCard(int k) {
        if (k > 0 && k < cards.length) {
            return new Card(cards[k].getValue(), cards[k].getSuit());
        }
        else {
            // This should force an error flag without access to the bool
            return new Card('e', cards[k].getSuit()); 
        } 

    }

    /**
     * make sure that there are not too many instances of the card in the deck 
     * if you add it.  Return false if there will be too many.  It should put 
     * the card on the top of the deck.
     * @param card
     * @return bool
     */
    public boolean addCard(Card card) {
        for (int i = 0; i < cards.length; ++i) {
            if (card.equals(cards[i]))
                return false;
        }
        if (topCard > 0) {
            cards[topCard - 1] = new Card(card.getValue(),card.getSuit());
            return true;
        }
        else {
            return false; // This means the deck is full 
        }

    }

    /**
     * remove a specific card from the deck.  Put the current top card into its 
     * place.  Be sure the card you need is actually still in the deck, if not 
     * return false.
     * @param card
     * @return bool
     */
    public boolean removeCard(Card card) {
        for (int i = 0; i < cards.length; ++i) {
            if (card.equals(cards[i])) {
                cards[i] = new Card(cards[topCard].getValue(), 
                    cards[topCard].getSuit());
                cards[topCard] = null; 
                topCard++; 
                return true;
            }
        }
        return false;
    }

    /**
     * put all of the cards in the deck back into the right order according to 
     * their values.
     */
    public void sort() {
        Card.arraySort(cards, cards.length);
    }

    /**
     * return the number of cards remaining in the deck
     * @return numCards
     */
    public int getNumCards() {
        int numCards = 0;
        for (int i = 0; i < cards.length; ++i) {
            if (cards[i] != null) 
                numCards++;
        }
        return numCards;
    }

    /**
     * Fill the masterPack static array with one of each card. This is 
     * accomplished with a double for-loop, which should be acceptable due 
     * to the small, unchanging data set. This will trigger the 'allocated' 
     * static boolean flag, so it should only execute once at the first Deck
     * objects' creation.
     */
    private static void allocateMasterPack() {
        if(allocated == false) {
            char[] values = { 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T',
                 'J', 'Q', 'K'};
            Card.Suit[] suits = { Card.Suit.CLUBS, Card.Suit.DIAMONDS, 
                Card.Suit.HEARTS, Card.Suit.SPADES };
            int k = 0; // Reference for the location in masterPack[]
            for (int i = 0; i < 4; ++i) {
                for(int j = 0; j < 13; ++j) {
                    masterPack[k] = new Card(values[j], suits[i]);
                    k++;
                }
            }
            // compensate for null references in the last 4 slots
            for (int i = 0; i < 4; ++i) {
                masterPack[k] = new Card();
                k++;
            }

            allocated = true;
        }
    }
}
