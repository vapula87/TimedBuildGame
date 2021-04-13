import javax.swing.*;
import java.awt.*;

class GameView extends JFrame {
    static int MAX_CARDS_PER_HAND = 56;
    static int MAX_PLAYERS = 2;  // for now, we only allow 2 person games
    static int NUM_STACKS = 3;

    private int numCardsPerHand;
    private int numPlayers;

    public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;


    public static JButton cannotPlayButton;
    public static JLabel gameText, gameStatus, cardsInDeck;

    public static JLabel[] computerLabels, humanLabels, playedCardLabels, playLabelText;
    public static JLabel timer = new JLabel("", JLabel.CENTER);
    public static Timer timerThread = new Timer();

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
        gameText = new JLabel();
        gameStatus = new JLabel();
        cardsInDeck = new JLabel();
        computerLabels = new JLabel[numCardsPerHand];
        humanLabels =  new JLabel[numCardsPerHand];
        playedCardLabels  = new JLabel[NUM_STACKS];

        //Layouts
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        pnlComputerHand.setLayout(new GridLayout());

        pnlPlayArea.setLayout(new GridLayout(0, 3));

        pnlPlayArea.setLayout(new GridLayout(3, 3));

        pnlHumanHand.setLayout(new GridLayout());

        //Borders
        pnlComputerHand.setBorder(BorderFactory.createTitledBorder("Computer Hand"));
        pnlPlayArea.setBorder(BorderFactory.createTitledBorder("Playing Area"));
        pnlHumanHand.setBorder(BorderFactory.createTitledBorder("User Hand"));

        //Add panels to jframe
        add(pnlComputerHand);
        add(pnlPlayArea);
        add(pnlHumanHand);

        // CREATE LABELS ---------------------------------------------------
        GUICard.loadCardIcons();
        gameText = new JLabel("Welcome to the card game!", JLabel.CENTER);
        gameStatus = new JLabel("Click a card from your hand to play!", JLabel.CENTER);
        cardsInDeck = new JLabel("Number of cards in deck: ", JLabel.CENTER);
        cannotPlayButton = new JButton("Cannot play");
        
        gameText.setForeground(Color.RED);
        gameStatus.setForeground(Color.MAGENTA);

        // ADD LABELS TO PANELS -----------------------------------------
        for (int card = 0; card < numCardsPerHand; card++)
        {
            computerLabels[card] = new JLabel(GUICard.getBackCardIcon());
            humanLabels[card] = new JLabel();
            pnlComputerHand.add(computerLabels[card]);
            pnlHumanHand.add(humanLabels[card]);
        }
        pnlHumanHand.add(cannotPlayButton);
        
        Icon tempIcon = GUICard.getBackCardIcon();
        playedCardLabels[0] = new JLabel(tempIcon);
        playedCardLabels[1] = new JLabel(tempIcon);
        playedCardLabels[2] = new JLabel(tempIcon);

        //Add the card labeling
        pnlPlayArea.add(playedCardLabels[0]);
        pnlPlayArea.add(playedCardLabels[1]);
        pnlPlayArea.add(playedCardLabels[2]);
        pnlPlayArea.add(gameText);
        pnlPlayArea.add(gameStatus);
        pnlPlayArea.add(cardsInDeck);
      
        // Timer 
        pnlPlayArea.add(new JLabel("Time Played:"));
        timerThread.start();
        pnlPlayArea.add(timerThread.getTimer()); 
        
        setVisible(true);

    }

    //Accessors
    public int getNumCardsPerHand() { return numCardsPerHand; }
    public int getNumPlayers() { return numPlayers; }

    //Mutators
    public void setLabel(int card, Icon icon) {
        humanLabels[card].setIcon(icon);
    }
    
    public void setPlayedCardLabels(int card, Icon icon) {
       playedCardLabels[card].setIcon(icon);
   }
}



