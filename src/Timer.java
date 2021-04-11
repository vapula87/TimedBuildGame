/**
 * Timer.java 
 * Roadrunner Financial Lab 6
 * Create a seperate thread that will run a timer on screen
 */

import javax.swing.*;
import java.awt.*;

public class Timer extends Thread {
    private static JLabel timer = new JLabel();
    private static int seconds = 0;
    private static int minutes = 0;
    private static final int ONE_SECOND = 1000; // Milliseconds

    public Timer() {}

    @Override
    public void run() {
        try {
            timer.setText(minutes + ":" + String.format("%02d" + seconds));
            
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static JLabel getTimer() {
        try {
            return timer;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return new JLabel("null");
        }
    }
}