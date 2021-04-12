import javax.swing.Icon;
import javax.swing.ImageIcon;

public class GUICard {
    private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A thru K + joker
    private static Icon iconBack;
    static boolean iconsLoaded = false;
    static void loadCardIcons() {
        if (iconsLoaded) return;
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 14; k++) {
                String fileName = "images/" + Assig6.turnIntIntoCardValue(k) +
                        Assig6.turnIntIntoCardSuit(j) + ".gif";
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
}