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
    public static JLabel[] computerLabels, humanLabels, playedCardLabels, playLabelText;

    public static JLabel gameText, gameStatus;

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
        computerLabels = new JLabel[numCardsPerHand];
        humanLabels =  new JLabel[numCardsPerHand];
        playedCardLabels  = new JLabel[numPlayers];
        playLabelText  = new JLabel[numPlayers];

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

        // CREATE LABELS ---------------------------------------------------
        GUICard.loadCardIcons();
        playLabelText[0] = new JLabel( "Computer", JLabel.CENTER );
        playLabelText[1] = new JLabel( "User", JLabel.CENTER );
        gameText = new JLabel("Welcome to the card game!", JLabel.CENTER);
        gameStatus = new JLabel("Click a card from your hand to play!", JLabel.CENTER);
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

        Icon tempIcon = GUICard.getBackCardIcon();
        playedCardLabels[0] = new JLabel(tempIcon);
        playedCardLabels[1] = new JLabel(tempIcon);

        //Add the card labeling
        pnlPlayArea.add(playedCardLabels[0]);
        pnlPlayArea.add(gameText);
        pnlPlayArea.add(playedCardLabels[1]);
        pnlPlayArea.add(playLabelText[0]);
        pnlPlayArea.add(gameStatus);
        pnlPlayArea.add(playLabelText[1]);

        setVisible(true);

    }

    //Accessors
    public int getNumCardsPerHand() { return numCardsPerHand; }
    public int getNumPlayers() { return numPlayers; }

    //Mutators
    public void setLabel(int card, Icon icon) {
        humanLabels[card].setIcon(icon);
    }
}



