import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

class GameController
{
    // static for the 57 icons and their corresponding labels
    // normally we would not have a separate label for each card, but
    // if we want to display all at once using labels, we need to.

    // 52 + 4 jokers + 1 back-of-card image
    static final int NUM_CARD_IMAGES = 57; 
    static Icon[] icon = new ImageIcon[NUM_CARD_IMAGES];
    static int NUM_CARDS_PER_HAND = 7;
    static int  NUM_PLAYERS = 2;
    static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
    static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];  
    static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS]; 
    static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS];
    static JLabel gameText = new JLabel();
    static JLabel gameStatus = new JLabel();
    static int playerScore = 0, computerScore = 0;
    static int computerCards[] = new int[NUM_CARDS_PER_HAND];

    /*
     * Build the file names. For each file name, read it in and use it to
     * instantiate each of the 57 Icons in the icon[] array.
    /**
     * returns a new random card for the main to use in its tests
     */
    public static Card randomCardGenerator() {
        // Use the methods above to get a random card and assign to string
        // MIKE: turnIntIntoCardSuit is inaccessible from here, currently in GUICard class
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

    // a simple main to throw all the JLabels out there for the world to see
    public static void main(String[] args)
    {
        // establish main frame in which program will run
        CardTable myCardTable = new CardTable("CardTable",
                NUM_CARDS_PER_HAND, NUM_PLAYERS);
        myCardTable.setSize(800, 600);
        myCardTable.setLocationRelativeTo(null);
        myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Initiating the game from CardGameOutline
        int card;
        Icon tempIcon;
        int numPacksPerDeck = 1;
        int numJokersPerPack = 2;
        int numUnusedCardsPerPack = 0;
        Card[] unusedCardsPerPack = null;

        CardGameOutline highCardGame =
                new CardGameOutline(numPacksPerDeck, numJokersPerPack,
                        numUnusedCardsPerPack,unusedCardsPerPack, NUM_PLAYERS,
                        NUM_CARDS_PER_HAND);
        highCardGame.deal();
        
        //Decide winner and display the score of the game
        // MIKE: bestCard and highCard don't exist anywhere else currently
        if (bestCard > highCard) computerScore++;
        else playerScore++;
        updateGame();
    }
    //Display the score from the game between the computer and user
    private static void updateGame()
    {
        gameStatus.setText("Score: " + computerScore + "-" + playerScore);
        if (computerScore + playerScore == NUM_CARDS_PER_HAND) {
            if (computerScore > playerScore)
                gameText.setText("Computer Wins!");
            else if (playerScore > computerScore)
                gameText.setText("You win!");
            else gameText.setText("Tie game!");
        }
    }
}
