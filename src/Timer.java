/**
 * Timer.java 
 * Roadrunner Financial Lab 6
 * Create a seperate thread that will run a timer on screen
 */

import javax.swing.*;
import java.awt.*;

public class Timer extends Thread {
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