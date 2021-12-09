package com.laserinfinite.java;

import java.awt.*;

public class Brick implements Entity {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private int health;
    private final Color color;

    private final int maxHealth;
    private boolean isDead = false;

    public Brick(int x, int y, int width, int height, int health, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;
        this.maxHealth = health;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void crack() {
        this.health--;
        if (this.health <= 0) {
            isDead = true;
            GamePanel.addScore(this.maxHealth*1000.0/((System.nanoTime()-GamePanel.gameStartTime)/1000000000.0+3));
        }
    }

    @Override
    public boolean lifeOver() {
        return isDead;
    }

    @Override
    public void update() {
        if(GamePanel.firestormLevel > 240) {
            isDead = true;
            GamePanel.addScore(this.maxHealth*1000.0/((System.nanoTime()-GamePanel.gameStartTime)/1000000000.0+3));
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setPaint(new GradientPaint(this.x, this.y - 10, this.color, this.x, this.y + 10, this.color.darker().darker()));
        g.fillRoundRect(x - width / 2, y - height / 2, width, height, this.width / 10, this.height / 10);

        g.setPaint(null);
        g.setColor(new Color(0, 0, 0, 64));

        for (int i = 0; i < this.maxHealth - this.health; i++) {
            g.fillRoundRect(x - width / 2, y - height / 2, width, height, this.width / 10, this.height / 10);
        }
    }
}
