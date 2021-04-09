/**
 * Timer.java 
 * Roadrunner Financial Lab 6
 * Create a seperate thread that will run a timer on screen
 */

import javax.swing.*;
import java.awt.*;

public class Timer extends Thread {
    public static JLabel timer = new JLabel();

    public Timer() {}

    @Override
    public void run() {
        try {

        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}