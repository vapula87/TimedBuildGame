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
    Card stacks[] = new Card[NUM_STACKS];
    boolean playerCannotPlay = false, computerCannotPlay = false, gameOver = false;

    public GameController()
    {
        //Initiating the game from GameModel
        int card;
        Icon tempIcon;
        int numPacksPerDeck = 1;
        int numJokersPerPack = 2;
        int numUnusedCardsPerPack = 0;
        Card[] unusedCardsPerPack = null;

        //creates game BUILD
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
        
        //stacks hold the top card on the stacks in play area
        for (int i = 0; i < NUM_STACKS; i++) {
           stacks[i] = BUILD.getCardFromDeck();
           myCardTable.setPlayedCardLabels(i, GUICard.getIcon(stacks[i]));
        }
        
        //Set initial number of cards in deck after deal
        GameView.cardsInDeck.setText("Number of cards in deck: " + BUILD.getNumCardsRemainingInDeck());
        
        //Add mouse listener to each player card
        for (card = 0; card < NUM_CARDS_PER_HAND; card++)
        {
            GameView.humanLabels[card].addMouseListener(
                    new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if(!gameOver) {
                               playGame(myCardTable.pnlHumanHand.getComponentZOrder(e.getComponent()), BUILD);
                            }
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
      //Add mouse listener to cannot play button
        GameView.cannotPlayButton.addMouseListener(
              new MouseListener() {
                 @Override
                 public void mouseClicked(MouseEvent e) {
                    if(!gameOver) {
                       playerCannotPlay = true;
                       playerCannotPlays++;
                       computerPlay(BUILD);
                    }
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
    
    //This function handles the player's turn
    private void playGame(int index, GameModel game){

       int numCardsInHand = game.getHand(1).numCards - 1;
       //check which stack card the clicked card can play on top of
        for(int i = 0; i < NUM_STACKS; i++) {
           int cardInHand = checkRank(game.getHand(1).inspectCard(index));
           int cardInStack = checkRank(stacks[i]);
           if(cardInStack == -1 || cardInHand == -1) {
              return;
           }
           //compares if card rank is one higher or lower
           if(cardInHand == cardInStack + 1 || cardInHand == cardInStack - 1) {
              GameView.playedCardLabels[i].setIcon(GameView.humanLabels[index].getIcon());
              stacks[i] = game.getHand(1).inspectCard(index);
              game.getHand(1).playCard(index);
              game.getHand(1).takeCard(game.getCardFromDeck());
              //shifts icons over for when new card is added to hand
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

    //This function handles the computers's turn
    private void computerPlay(GameModel game)
    {
        boolean breakLoop = false;
        //check if any card in computer hand can be played on top of any stack card
        for(int i = 0; i < stacks.length; i++) {
           for(int j = 0; j < game.getHand(0).numCards; j++) {
              int cardInHand = checkRank(game.getHand(0).inspectCard(j));
              int cardInStack = checkRank(stacks[i]);
              if(cardInStack == -1 || cardInHand == -1) {
                 return;
              }
              //compares if card rank is one higher or lower
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
        updateGame(game);
        resetStacks(game);
    }
    
    //resets the stacks in the middle if the computer and player can't play
    private void resetStacks(GameModel game) {
       if(computerCannotPlay && playerCannotPlay && !noCardsInDeck(game)) {
          for (int i = 0; i < NUM_STACKS; i++) {
             stacks[i] = game.getCardFromDeck();
             GameView.playedCardLabels[i].setIcon(GUICard.getIcon(stacks[i]));
             if(noCardsInDeck(game))
                i = NUM_STACKS;
          }
          computerCannotPlay = playerCannotPlay = false;
       }
       else return;
    }
    
    //Updates the display for the score and number of cards
    private void updateGame(GameModel game)
    {
        GameView.gameStatus.setText("Cannot play: Computer-" + computerCannotPlays + " Player-" + playerCannotPlays);
        GameView.cardsInDeck.setText("Number of cards in deck: " + game.getNumCardsRemainingInDeck());
        noCardsInDeck(game);
    }
    
    //check if there's no cards in deck and ends game
    private boolean noCardsInDeck(GameModel game) {
       if(game.getNumCardsRemainingInDeck() < 1) {
          if (computerCannotPlays < playerCannotPlays)
             GameView.gameText.setText("Computer Wins!");
          else if (playerCannotPlays < computerCannotPlays)
             GameView.gameText.setText("You win!");
          else GameView.gameText.setText("Tie game!");
          Timer.stop = true;
          gameOver = true;
          return true;
       }
       else return false;
    }
    
    //helper to check the rank of a card ex: X=0,A=1,2=2...T=10,J=11,Q=12,K=13
    private int checkRank(Card card) {
       for(int i = 0; i < Card.valuRanks.length; i++) {
          if(card.getValue() == Card.valuRanks[i]) {
             return i;
          }
       }
       return -1;
    }
    
    //main function
    public static void main(String[] args) {
       new GameController();
   }
}

class Timer extends Thread {
   // Flag to end the loop when the game is over
   public static boolean stop = false;
   
   private int seconds = 0;
   private int minutes = 0;
   private final int ONE_SECOND = 1000; // Milliseconds
   public Timer() {}
   @Override
   public void run() {
       GameView.timer.setVerticalAlignment(JLabel.CENTER);
       GameView.timer.setFont(new Font("Sans Serif", Font.BOLD, 20));
       GameView.timer.setForeground(Color.RED);
       while(stop == false) {
           try {
              GameView.timer.setText(minutes + ":" + String.format("%02d", seconds));
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
           return GameView.timer;
       }
       catch (NullPointerException e) {
           e.printStackTrace();
           return new JLabel("null");
       }
   }

}
