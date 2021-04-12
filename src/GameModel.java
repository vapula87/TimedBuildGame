import javax.swing.*;
import java.util.Random;

class GameModel
{
    private static final int MAX_PLAYERS = 50;

    private int numPlayers;
    private int numPacks;            // # standard 52-card packs per deck
    // ignoring jokers or unused cards
    private int numJokersPerPack;    // if 2 per pack & 3 packs per deck, get 6
    private int numUnusedCardsPerPack;  // # cards removed from each pack
    private int numCardsPerHand;        // # cards to deal each player
    private Deck deck;               // holds the initial full deck and gets
    // smaller (usually) during play
    private Hand[] hand;             // one Hand for each player
    private Card[] unusedCardsPerPack;   // an array holding the cards not used
    // in the game.  e.g. pinochle does not
    static int computerCards[];
    // use cards 2-8 of any suit

    public GameModel(int numPacks, int numJokersPerPack,
                     int numUnusedCardsPerPack,  Card[] unusedCardsPerPack,
                     int numPlayers, int numCardsPerHand)
    {

        int k;

        // filter bad values
        if (numPacks < 1 || numPacks > 6)
            numPacks = 1;
        if (numJokersPerPack < 0 || numJokersPerPack > 4)
            numJokersPerPack = 0;
        if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
            numUnusedCardsPerPack = 0;
        if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
            numPlayers = 4;
        // one of many ways to assure at least one full deal to all players
        if  (numCardsPerHand < 1 ||
                numCardsPerHand >  numPacks * (52 - numUnusedCardsPerPack)
                        / numPlayers )
            numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;

        // allocate
        this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
        this.hand = new Hand[numPlayers];
        for (k = 0; k < numPlayers; k++)
            this.hand[k] = new Hand();
        deck = new Deck(numPacks);

        // assign to members
        this.numPacks = numPacks;
        this.numJokersPerPack = numJokersPerPack;
        this.numUnusedCardsPerPack = numUnusedCardsPerPack;
        this.numPlayers = numPlayers;
        this.numCardsPerHand = numCardsPerHand;
        for (k = 0; k < numUnusedCardsPerPack; k++)
            this.unusedCardsPerPack[k] = unusedCardsPerPack[k];

        // prepare deck and shuffle
        newGame();
    }

    // constructor overload/default for game like bridge
    public GameModel()
    {
        this(1, 0, 0, null, 4, 13);
    }

    public Hand getHand(int k)
    {
        // hands start from 0 like arrays

        // on error return automatic empty hand
        if (k < 0 || k >= numPlayers)
            return new Hand();

        return hand[k];
    }

    public Card getCardFromDeck() { return deck.dealCard(); }

    public int getNumCardsRemainingInDeck() { return deck.getNumCards(); }

    public void newGame()
    {
        int k, j;

        // clear the hands
        for (k = 0; k < numPlayers; k++)
            hand[k].resetHand();

        // restock the deck
        deck.init(numPacks);

        // remove unused cards
        for (k = 0; k < numUnusedCardsPerPack; k++)
            deck.removeCard( unusedCardsPerPack[k] );

        // add jokers
        for (k = 0; k < numPacks; k++)
            for ( j = 0; j < numJokersPerPack; j++)
                deck.addCard( new Card('X', Card.Suit.values()[j]) );

        // shuffle the cards
        deck.shuffle();
    }

    public boolean deal()
    {
        // returns false if not enough cards, but deals what it can
        int k, j;
        boolean enoughCards;

        // clear all hands
        for (j = 0; j < numPlayers; j++)
            hand[j].resetHand();

        enoughCards = true;
        for (k = 0; k < numCardsPerHand && enoughCards ; k++)
        {
            for (j = 0; j < numPlayers; j++)
                if (deck.getNumCards() > 0)
                    hand[j].takeCard( deck.dealCard() );
                else
                {
                    enoughCards = false;
                    break;
                }
        }

        return enoughCards;
    }

    void sortHands()
    {
        int k;

        for (k = 0; k < numPlayers; k++)
            hand[k].sort();
    }

    Card playCard(int playerIndex, int cardIndex)
    {
        // returns bad card if either argument is bad
        if (playerIndex < 0 ||  playerIndex > numPlayers - 1 ||
                cardIndex < 0 || cardIndex > numCardsPerHand - 1)
        {
            //Creates a card that does not work
            return new Card('M', Card.Suit.SPADES);
        }

        // return the card played
        return hand[playerIndex].playCard(cardIndex);

    }
    boolean takeCard(int playerIndex)
    {
        // returns false if either argument is bad
        if (playerIndex < 0 || playerIndex > numPlayers - 1)
            return false;

        // Are there enough Cards?
        if (deck.getNumCards() <= 0)
            return false;

        return hand[playerIndex].takeCard(deck.dealCard());
    }
}
class Card
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
class Deck {
    // Members and constants
    private static final int DECK_SIZE = 56;    // 52 cards + 4 jokers
    private static boolean allocated = false;
    private static Card[] masterPack = new Card[DECK_SIZE - 4]; //does not include jokers
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
     *
     * @param numPacks
     */
    public Deck(int numPacks) {
        allocateMasterPack();
        init(numPacks);
    }

    /**
     * fill the cards array with Card objects from the masterPack
     *
     * @param numPacks
     */
    public void init(int numPacks) {
        if (numPacks < 1) numPacks = 1;
        else if (numPacks > MAX_PACKS) numPacks = MAX_PACKS;
        cards = new Card[(numPacks * DECK_SIZE)];
        topCard = 0;
        // an 'iterator' that should not be > 52 for masterPack reference
        int j = 0;
        for (int i = 0; i < cards.length - (4 * numPacks); ++i) {
            cards[i] =
                    new Card(masterPack[j].getValue(), masterPack[j].getSuit());
            j++;
            if (i % (DECK_SIZE - 5) == 0 && i != 0) {    // Start at masterPack[0] again
                j = 0;                      // array range is 0 < j < 51
            }
        }
    }

    /**
     * Mixes up the cards with the help of the standard random number generator
     */
    public void shuffle() {
        //Locate the last non-null index
        int lastIndex = cards.length;
        for (int i = cards.length - 1; i >= 0; --i) {
            if (cards[i] != null) {
                lastIndex = i;
                break;
            }
        }
        // Populate a temp array with the cards (deep copy)
        Card[] temp = new Card[lastIndex + 1];
        for (int i = 0; i < temp.length; ++i)
            temp[i] = new Card(cards[i].getValue(), cards[i].getSuit());
        //Randomly swap values in the temp array
        Random r = new Random();
        for (int i = 0; i < temp.length; ++i) {
            int j = r.nextInt(temp.length);  // Create a new random number
            Card tempCard = new Card(temp[j].getValue(), temp[j].getSuit());
            temp[j] = new Card(temp[i].getValue(), temp[i].getSuit());
            temp[i] = tempCard;
        }
        //Copy the temp array back to the main array
        for (int i = 0; i < temp.length; ++i)
            cards[i] = temp[i];
    }

    /**
     * dealCard returns and removes the card in the top occupied position of
     * cards[]. Make sure there are still cards available.  This is an object
     * copy, not a reference copy, since the source of the Card might destroy or
     * change its data after it is sent out.
     *
     * @return A card or null if none available
     */
    public Card dealCard() {
        if (topCard == cards.length) return new Card('M', Card.Suit.SPADES);
        else if (cards[topCard] != null) {
            Card dealtCard =        // Make an object copy of the top card
                    new Card(cards[topCard].getValue(), cards[topCard].getSuit());
            cards[topCard] = null;  // Remove the top card
            topCard++;              // Increment the top card "pointer"
            return dealtCard;       // Deal the card
        } else return new Card('M', Card.Suit.SPADES);
    }

    public int getTopCard() {
        return topCard;
    }

    /**
     * Accessor for an individual card.  Returns a card with errorFlag = true if
     * k is bad.  Also returns an object copy, not a reference copy.
     *
     * @param k
     */
    public Card inspectCard(int k) {
        if (k > 0 && k < cards.length) {
            return new Card(cards[k].getValue(), cards[k].getSuit());
        } else {
            // This should force an error flag without access to the bool
            return new Card('e', cards[k].getSuit());
        }

    }

    /**
     * make sure that there are not too many instances of the card in the deck
     * if you add it.  Return false if there will be too many.  It should put
     * the card on the top of the deck.
     *
     * @param card
     * @return bool
     */
    public boolean addCard(Card card) {
        if (card == null || topCard == cards.length) return false;
        for (int i = 0; i < cards.length; ++i) {
            if (cards[i] != null && card.equals(cards[i]))
                return false;
        }
        if (cards[topCard] == null) {
            cards[topCard] = new Card(card.getValue(), card.getSuit());
            return true;
        }
        //Places top card on the bottom of the deck and new card on top
        for (int i = topCard; i < cards.length; ++i) {
            if (cards[i] == null) {
                //swap
                cards[i] = new Card(cards[topCard].getValue(), cards[topCard].getSuit());
                ;
                cards[topCard] = new Card(card.getValue(), card.getSuit());
                return true;
            }
        }
        return false; // This means the deck is full
    }

    /**
     * remove a specific card from the deck.  Put the current top card into its
     * place.  Be sure the card you need is actually still in the deck, if not
     * return false.
     *
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
     *
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
        if (allocated == false) {
            char[] values = {'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T',
                    'J', 'Q', 'K'};
            Card.Suit[] suits = {Card.Suit.CLUBS, Card.Suit.DIAMONDS,
                    Card.Suit.HEARTS, Card.Suit.SPADES};
            int k = 0; // Reference for the location in masterPack[]
            for (int i = 0; i < 4; ++i) {
                for (int j = 0; j < 13; ++j) {
                    masterPack[k] = new Card(values[j], suits[i]);
                    k++;
                }
            }
            allocated = true;
        }
    }
}
class Hand
{
    public final int MAX_CARDS = 54;
    Card[] myCards;
    int numCards;
    public Hand()
    {
        numCards = 0;
        myCards = new Card[MAX_CARDS];
    }
    //Public methods
    /**
     * Method to remove all cards from hand. Resets
     * the array index to 0.
     */
    public void resetHand()
    {
        numCards = 0;
    }
    /**
     * Method to add a new card to the hand.
     * Adds an object copy of the card to the
     * current array position and increments numCards.
     *
     * @param card The card to add to the hand
     * @return false if an invalid card was pulled, otherwise true
     */
    public boolean takeCard(Card card)
    {
        if(card.errorFlag() == true || card == null || numCards == MAX_CARDS)
        {
            return false;
        }
        else
        {
            myCards[numCards++] = new Card(card.getValue(),card.getSuit());
            return true;
        }
    }
    /**
     * Returns and removes the top card to be played.
     * Decrements numCards and returns a new card
     * object with copied values.
     * Updated for lab 5 using the provided code
     *
     * @return The card to be played
     */
    public Card playCard(int cardIndex)
    {
        if ( numCards == 0 ) //error
        {
            //Creates a card that does not work
            return new Card('M', Card.Suit.SPADES);
        }
        //Decreases numCards.
        Card card = myCards[cardIndex];

        numCards--;
        for(int i = cardIndex; i < numCards; i++)
        {
            myCards[i] = myCards[i+1];
        }

        myCards[numCards] = null;

        return card;
    }
    /**
     * Method to return the full hand as a string
     */
    public String toString()
    {
        String str = "";
        for(int i = 0; i < numCards; i++)
        {
            //checks if last card which doesn't need comma
            if(i == numCards - 1)
            {
                str += myCards[i].getValue() + " of " + myCards[i].getSuit();
            }
            else
            {
                str += myCards[i].getValue() + " of " + myCards[i].getSuit() + ", ";
            }
        }
        return str;
    }

    //Accessors
    public int getNumCards()
    {
        return numCards;
    }
    /**
     * An accessor method for a card at index k.
     * If the requested card does not exist, return
     * an invalid card.
     *
     * @param k The array index
     * @return The card at index k or an invalid card
     */
    public Card inspectCard(int k)
    {
        if(k < 0 || k > MAX_CARDS || myCards[k] == null)
        {
            return new Card('W', Card.Suit.SPADES);
        }
        else
        {
            return new Card(myCards[k].getValue(), myCards[k].getSuit());
        }
    }

    public void sort() {
        Card.arraySort(myCards, numCards);
    }
}
class GUICard {
    private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A thru K + joker
    private static Icon iconBack;
    static boolean iconsLoaded = false;
    static void loadCardIcons() {
        if (iconsLoaded) return;
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 14; k++) {
                String fileName = "images/" + turnIntIntoCardValue(k) +
                        turnIntIntoCardSuit(j) + ".gif";
                iconCards[k][j] = new ImageIcon(fileName);
            }
        }
        iconBack = new ImageIcon("images/BK.gif");
        iconsLoaded = true;
    }
    public static Icon getIcon(Card card) {
        if (card.errorFlag()) return null;
        int row = getRowFromChar(card.getValue());
        int col = suitAsInt(card.getSuit());
        if (iconCards[row][col] == null) return iconBack;
        else return iconCards[row][col];
    }
    public static Icon getBackCardIcon() {
        return iconBack;
    }
    public static int suitAsInt(Card.Suit suit) {
        switch (suit) {
            case CLUBS:
                return 0;
            case DIAMONDS:
                return 1;
            case HEARTS:
                return 2;
            case SPADES:
                return 3;
            default:
                return -1;
        }
    }
    public static int getRowFromChar(char character) {
        switch (character) {
            case 'A':
                return 0;
            case 'T':
                return 9;
            case 'J':
                return 10;
            case 'Q':
                return 11;
            case 'K':
                return 12;
            case 'X':
                return 13;
            default:
                int charToInt = character - '0';
                if (charToInt < 2 || charToInt > 9) return -1;
                return --charToInt;
        }
    }
    // turns 0 - 13 into "A", "2", "3", ... "Q", "K", "X"
    static String turnIntIntoCardValue(int k)
    {
        String[] cardValue = { "A", "2", "3", "4", "5", "6", "7", "8", "9",
                "T", "J", "Q", "K", "X" };
        if (k > -1 && k < 14)
            return cardValue[k];
        else
            return "";
    }

    // turns 0 - 3 into "C", "D", "H", "S"
    static String turnIntIntoCardSuit(int j)
    {
        String[] cardSuit = { "C", "D", "H", "S" };
        if (j > -1 && j < 4)
            return cardSuit[j];
        else
            return "";
    }
    /**
     * returns a new random card for the main to use in its tests
     */
    public static Card randomCardGenerator() {
        // Use the methods above to get a random card and assign to string
        String randomSuit = turnIntIntoCardSuit((int) (Math.random() * 3));
        String randomValue = turnIntIntoCardValue((int) (Math.random() * 13));
        // Determine the suit of the random card from the string
        Card.Suit suit = null;
        switch(randomSuit) {
            case "C": suit = Card.Suit.CLUBS;
                break;
            case "D": suit = Card.Suit.DIAMONDS;
                break;
            case "H": suit = Card.Suit.HEARTS;
                break;
            case "S": suit = Card.Suit.SPADES;
                break;
            default:
                break;
        }
        // The string randomValue should only be 1 character at the first index
        return new Card(randomValue.charAt(0), suit);
    }
}
