import javax.swing.*;
import java.awt.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;

class GameView extends JFrame {
    static int MAX_CARDS_PER_HAND = 56;
    static int MAX_PLAYERS = 2;  // for now, we only allow 2 person games

    private int numCardsPerHand;
    private int numPlayers;

    public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;
    //Filters input, adds panels to JFrame, establishes layouts
    public GameView(String title, int numCardsPerHand, int numPlayers) {
        super(title);
        //Validation
        if (numCardsPerHand < 1) numCardsPerHand = 1;
        else if (numCardsPerHand > MAX_CARDS_PER_HAND) numCardsPerHand = MAX_CARDS_PER_HAND;
        if (numPlayers < 1) numPlayers = 1;
        else if (numPlayers > MAX_PLAYERS) numPlayers = MAX_PLAYERS;

        //Initialization
        this.numCardsPerHand = numCardsPerHand;
        this.numPlayers = numPlayers;
        pnlComputerHand = new JPanel();
        pnlHumanHand = new JPanel();
        pnlPlayArea = new JPanel();

        //Layouts
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        pnlComputerHand.setLayout(new GridLayout());
        pnlPlayArea.setLayout(new GridLayout(2, 3));
        pnlHumanHand.setLayout(new GridLayout());

        //Borders
        pnlComputerHand.setBorder(BorderFactory.createTitledBorder("Computer Hand"));
        pnlPlayArea.setBorder(BorderFactory.createTitledBorder("Playing Area"));
        pnlHumanHand.setBorder(BorderFactory.createTitledBorder("User Hand"));

        //Add panels to jframe
        add(pnlComputerHand);
        add(pnlPlayArea);
        add(pnlHumanHand);
    }
    //Accessors
    public int getNumCardsPerHand() { return numCardsPerHand; }
    public int getNumPlayers() { return numPlayers; }
    
    public class GUICard {
    private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A thru K + joker
    private static Icon iconBack;
    static boolean iconsLoaded = false;

    /**
     * Loads card icons into a 2D array. The card back icon
     * is stored separately.
     */
    static void loadCardIcons() {
        if (iconsLoaded) return;
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 14; k++) {
                String fileName = "images/" + Assig5.turnIntIntoCardValue(k) +
                        Assig5.turnIntIntoCardSuit(j) + ".gif";
                iconCards[k][j] = new ImageIcon(fileName);
            }
        }
        iconBack = new ImageIcon("images/BK.gif");
        iconsLoaded = true;
    }

    /**
     * Returns a card's icon.
     *
     * @return null on error
     */
    public static Icon getIcon(Card card) {
        if (card.errorFlag()) return null;
        int row = getRowFromChar(card.getValue());
        int col = suitAsInt(card.getSuit());
        if (iconCards[row][col] == null) return iconBack;
        else return iconCards[row][col];
    }

    /**
     * Returns the card back icon.
     */
    public static Icon getBackCardIcon() {
        return iconBack;
    }

    /**
     * Returns a card suit as an integer.
     */
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

    /**
     * Returns the correct row for a card.
     * @param character The card's value
     */
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
} 
}
