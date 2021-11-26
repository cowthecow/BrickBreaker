package com.laserinfinite.java;

import javax.swing.*;

public class Game {
    public static JFrame window = new JFrame("Brick Breaker");

    public static void main(String[] args) {

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setContentPane(new GamePanel());

        window.pack();
        window.setVisible(true);
    }
}
