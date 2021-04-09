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
     */
    static void loadCardIcons()
    {
       int indexTracker = 0;
       for (int j = 0; j < 4; j++)
       {
           for (int k = 0; k < 14; k++)
           {
               String fileName = "images/" + turnIntIntoCardValue(k) +
                       turnIntIntoCardSuit(j) + ".gif";
               icon[indexTracker++] = new ImageIcon(fileName);
           }
       }
       icon[indexTracker] = new ImageIcon("images/BK.gif");
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

        // CREATE LABELS ---------------------------------------------------
        GUICard.loadCardIcons();
        playLabelText[0] = new JLabel( "Computer", JLabel.CENTER );
        playLabelText[1] = new JLabel( "User", JLabel.CENTER );
        gameText = new JLabel("Welcome to the card game!", JLabel.CENTER);
        gameStatus = new JLabel("Click a card from your hand to play!", JLabel.CENTER);
        gameText.setForeground(Color.RED);
        gameStatus.setForeground(Color.MAGENTA);
        //Holds integer representations of card values for the computer
        for (int count = 0; count < NUM_CARDS_PER_HAND; count++)
        {
            computerCards[count] = highCardGame.getHand(0).inspectCard(count).getValue();
        }
        for (card = 0; card < NUM_CARDS_PER_HAND; card++)
        {
            computerLabels[card] = new JLabel(GUICard.getBackCardIcon());
            tempIcon = GUICard.getIcon(highCardGame.getHand(1).inspectCard(card));
            humanLabels[card] = new JLabel(tempIcon);
            //Mouse listener
            humanLabels[card].addMouseListener(
                    new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            playGame(myCardTable.pnlHumanHand.getComponentZOrder
                                    (e.getComponent()), highCardGame);
                        }

                        @Override
                        public void mousePressed(MouseEvent e) { }

                        @Override
                        public void mouseReleased(MouseEvent e) { }

                        @Override
                        public void mouseEntered(MouseEvent e) { }

                        @Override
                        public void mouseExited(MouseEvent e) { }
                    }
            );
        }

        // ADD LABELS TO PANELS -----------------------------------------
        for (card = 0; card < NUM_CARDS_PER_HAND; card++)
        {
            myCardTable.pnlComputerHand.add(computerLabels[card]);
            myCardTable.pnlHumanHand.add(humanLabels[card]);
        }

        tempIcon = GUICard.getBackCardIcon();
        playedCardLabels[0] = new JLabel(tempIcon);
        playedCardLabels[1] = new JLabel(tempIcon);

        //Add the card labeling
        myCardTable.pnlPlayArea.add(playedCardLabels[0]);
        myCardTable.pnlPlayArea.add(gameText);
        myCardTable.pnlPlayArea.add(playedCardLabels[1]);
        myCardTable.pnlPlayArea.add(playLabelText[0]);
        myCardTable.pnlPlayArea.add(gameStatus);
        myCardTable.pnlPlayArea.add(playLabelText[1]);

        // show everything to the user
        myCardTable.setVisible(true);
    }
    //Creating certain functions to create the game
    private static void playGame(int index, CardGameOutline highCardGame){
        humanLabels[index].setVisible(false);
        playedCardLabels[1].setIcon(humanLabels[index].getIcon());
        computerPlay(highCardGame.getHand(1).inspectCard(index).getValue(),highCardGame);
    }
    private static void computerPlay(int highCard, CardGameOutline highCardGame)
    {
        int bestCard = 0;
        int index = 0;
        for (int count = 0; count < NUM_CARDS_PER_HAND; count++)
        {
            int cardValue = computerCards[count];
            if (cardValue == -1) continue;
            if (cardValue > bestCard) {
                bestCard = cardValue;
                index = count;
            }
        }
        computerLabels[index].setVisible(false);
        playedCardLabels[0].setIcon(GUICard.getIcon(highCardGame.getHand(0).inspectCard(index)));
        computerCards[index] = -1;

        //Decide winner and display the score of the game
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
