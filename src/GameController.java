import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

class GameController
{
    static int NUM_CARDS_PER_HAND = 7;
    static int  NUM_PLAYERS = 2;
    static int playerScore = 0, computerScore = 0;
    static int computerCards[] = new int[NUM_CARDS_PER_HAND];

    public GameController()
    {
        //Initiating the game from GameModel
        int card;
        Icon tempIcon;
        int numPacksPerDeck = 1;
        int numJokersPerPack = 2;
        int numUnusedCardsPerPack = 0;
        Card[] unusedCardsPerPack = null;

        GameModel highCardGame = new GameModel(numPacksPerDeck,
                numJokersPerPack, numUnusedCardsPerPack,unusedCardsPerPack,
                NUM_PLAYERS, NUM_CARDS_PER_HAND);

        highCardGame.deal();

        // Loading the GUI from GameView
        GameView myCardTable = new GameView("CardTable",
                NUM_CARDS_PER_HAND, NUM_PLAYERS);
        myCardTable.setSize(800, 600);
        myCardTable.setLocationRelativeTo(null);
        myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Holds integer representations of card values for the computer
        for (int count = 0; count < NUM_CARDS_PER_HAND; count++)
            computerCards[count] = highCardGame.getHand(0).inspectCard(count).getValue();

        //Add mouse listener to each player card
        for (card = 0; card < NUM_CARDS_PER_HAND; card++)
        {
            GameView.humanLabels[card].addMouseListener(
                    new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            playGame(myCardTable.pnlHumanHand.getComponentZOrder
                                    (e.getComponent()), highCardGame);
                        }
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

        //Set player labels
        for (card = 0; card < NUM_CARDS_PER_HAND; card++)
        {
            tempIcon = GUICard.getIcon(highCardGame.getHand(1).inspectCard(card));
            myCardTable.setLabel(card, tempIcon);
        }
    }

    //Creating certain functions to create the game
    private static void playGame(int index, GameModel highCardGame){
        GameView.humanLabels[index].setVisible(false);
        GameView.playedCardLabels[1].setIcon(GameView.humanLabels[index].getIcon());
        computerPlay(highCardGame.getHand(1).inspectCard(index).getValue(),highCardGame);
    }

    private static void computerPlay(int highCard, GameModel highCardGame)
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
        GameView.computerLabels[index].setVisible(false);
        GameView.playedCardLabels[0].setIcon(GUICard.getIcon(highCardGame.getHand(0).inspectCard(index)));
        computerCards[index] = -1;

        //Decide winner and display the score of the game
        if (bestCard > highCard) computerScore++;
        else playerScore++;
        updateGame();
    }

    //Display the score from the game between the computer and user
    private static void updateGame()
    {
        GameView.gameStatus.setText("Score: " + computerScore + "-" + playerScore);
        if (computerScore + playerScore == NUM_CARDS_PER_HAND) {
            if (computerScore > playerScore) {
                GameView.gameText.setText("Computer Wins!");
                Timer.stop = true;
            }
            else if (playerScore > computerScore) {
                GameView.gameText.setText("You win!");
                Timer.stop = true;
            }
            else {
                GameView.gameText.setText("Tie game!"); 
                Timer.stop = true;
            }
        }
    }
}
