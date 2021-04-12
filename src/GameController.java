import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

class GameController
{
    static int NUM_CARDS_PER_HAND = 7;
    static int  NUM_PLAYERS = 2;
    static int NUM_STACKS = 3;
    static int playerCannotPlays = 0, computerCannotPlays = 0;
    static int computerCards[] = new int[NUM_CARDS_PER_HAND];
    Card stacks[] = new Card[NUM_STACKS];
    boolean playerCannotPlay = false, computerCannotPlay = false;

    public GameController()
    {
        //Initiating the game from GameModel
        int card;
        Icon tempIcon;
        int numPacksPerDeck = 1;
        int numJokersPerPack = 2;
        int numUnusedCardsPerPack = 0;
        Card[] unusedCardsPerPack = null;

        GameModel BUILD = new GameModel(numPacksPerDeck,
                numJokersPerPack, numUnusedCardsPerPack,unusedCardsPerPack,
                NUM_PLAYERS, NUM_CARDS_PER_HAND);
        BUILD.deal();

        // Loading the GUI from GameView
        GameView myCardTable = new GameView("CardTable",
                NUM_CARDS_PER_HAND, NUM_PLAYERS);
        myCardTable.setSize(850, 650);
        myCardTable.setLocationRelativeTo(null);
        myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Holds integer representations of card values for the computer
        for (int count = 0; count < NUM_CARDS_PER_HAND; count++)
            computerCards[count] = BUILD.getHand(0).inspectCard(count).getValue();

        for (int i = 0; i < NUM_STACKS; i++) {
           stacks[i] = BUILD.getCardFromDeck();
           myCardTable.setPlayedCardLabels(i, GUICard.getIcon(stacks[i]));
        }
        
        GameView.cardsInDeck.setText("Number of cards in deck: " + BUILD.getNumCardsRemainingInDeck());
        
        //Add mouse listener to each player card
        for (card = 0; card < NUM_CARDS_PER_HAND; card++)
        {
            GameView.humanLabels[card].addMouseListener(
                    new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            playGame(myCardTable.pnlHumanHand.getComponentZOrder
                                    (e.getComponent()), BUILD);
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
        GameView.cannotPlayButton.addMouseListener(
              new MouseListener() {
                 @Override
                 public void mouseClicked(MouseEvent e) {
                    playerCannotPlay = true;
                    playerCannotPlays++;
                    computerPlay(BUILD);
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
        //Set player labels
        for (card = 0; card < NUM_CARDS_PER_HAND; card++)
        {
            tempIcon = GUICard.getIcon(BUILD.getHand(1).inspectCard(card));
            myCardTable.setLabel(card, tempIcon);
        }

    }
    
    
    //Creating certain functions to create the game
    private void playGame(int index, GameModel game){

       int numCardsInHand = game.getHand(1).numCards - 1;
        for(int i = 0; i < NUM_STACKS; i++) {
           int cardInHand = checkRank(game.getHand(1).inspectCard(index));
           int cardInStack = checkRank(stacks[i]);
           if(cardInStack == -1 || cardInHand == -1) {
              return;
           }
           if(cardInHand == cardInStack + 1 || cardInHand == cardInStack - 1) {
              GameView.playedCardLabels[i].setIcon(GameView.humanLabels[index].getIcon());
              stacks[i] = game.getHand(1).inspectCard(index);
              game.getHand(1).playCard(index);
              game.getHand(1).takeCard(game.getCardFromDeck());
              for(int j = index; j < numCardsInHand; j++) {
                 GameView.humanLabels[j].setIcon(GameView.humanLabels[j + 1].getIcon());
              }
              GameView.humanLabels[numCardsInHand].setIcon(GUICard.getIcon(game.getHand(1).inspectCard(numCardsInHand)));
              computerPlay(game);
           }
        }
        noCardsInDeck(game);
        resetStacks(game);
    }

    private void computerPlay(GameModel game)
    {
        boolean breakLoop = false;
        for(int i = 0; i < stacks.length; i++) {
           for(int j = 0; j < game.getHand(0).numCards; j++) {
              int cardInHand = checkRank(game.getHand(0).inspectCard(j));
              int cardInStack = checkRank(stacks[i]);
              if(cardInStack == -1 || cardInHand == -1) {
                 return;
              }
              if(cardInStack == cardInHand + 1 || cardInStack == cardInHand - 1) {
                 GameView.playedCardLabels[i].setIcon(GUICard.getIcon(game.getHand(0).inspectCard(j)));
                 stacks[i] = game.getHand(0).inspectCard(j);
                 game.getHand(0).playCard(j);
                 game.getHand(0).takeCard(game.getCardFromDeck());
                 breakLoop = true;
                 break;
              }
           }
           if(breakLoop) break;
        }
        if(!breakLoop) {
           computerCannotPlays++;
           computerCannotPlay = true;
        }
 
        
        //Decide winner and display the score of the game
        //if (bestCard > highCard) computerScore++;
        //else playerScore++;
        
        updateGame(game);
        resetStacks(game);
    }
    
    private void resetStacks(GameModel game) {
       if(computerCannotPlay && playerCannotPlay) {
          for (int i = 0; i < NUM_STACKS; i++) {
             stacks[i] = game.getCardFromDeck();
             GameView.playedCardLabels[i].setIcon(GUICard.getIcon(stacks[i]));
             noCardsInDeck(game);
          }
          computerCannotPlay = playerCannotPlay = false;
       }
       else return;
    }
    
    //Display the score from the game between the computer and user
    private void updateGame(GameModel game)
    {
        GameView.gameStatus.setText("Cannot play: Computer-" + computerCannotPlays + " Player-" + playerCannotPlays);
        GameView.cardsInDeck.setText("Number of cards in deck: " + game.getNumCardsRemainingInDeck());
        noCardsInDeck(game);
    }
    
    private boolean noCardsInDeck(GameModel game) {
       if(game.getNumCardsRemainingInDeck() < 1) {
          if (computerCannotPlays < playerCannotPlays)
             GameView.gameText.setText("Computer Wins!");
          else if (playerCannotPlays < computerCannotPlays)
             GameView.gameText.setText("You win!");
          else GameView.gameText.setText("Tie game!");
          Timer.stop = true;
          return true;
       }
       else return false;
    }
    
    private int checkRank(Card card) {
       for(int i = 0; i < Card.valuRanks.length; i++) {
          if(card.getValue() == Card.valuRanks[i]) {
             return i;
          }
       }
       return -1;
    }
    
    public static void main(String[] args) {
       new GameController();
   }
}

class Timer extends Thread {
   // Flag to end the loop when the game is over
   public static boolean stop = false;
   private JLabel timer = new JLabel("", JLabel.CENTER);
   private int seconds = 0;
   private int minutes = 0;
   private final int ONE_SECOND = 1000; // Milliseconds
   public Timer() {}
   @Override
   public void run() {
       timer.setVerticalAlignment(JLabel.CENTER);
       timer.setFont(new Font("Sans Serif", Font.BOLD, 20));
       timer.setForeground(Color.RED);
       while(stop == false) {
           try {
               timer.setText(minutes + ":" + String.format("%02d", seconds));
               seconds++;
               sleep(ONE_SECOND);
               if (seconds == 60) {
                   minutes++;
                   seconds = 0;
               }
           }
           catch (Exception e) {
               e.printStackTrace();
           }
       }
   }
   public JLabel getTimer() {
       try {
           return timer;
       }
       catch (NullPointerException e) {
           e.printStackTrace();
           return new JLabel("null");
       }
   }

}
