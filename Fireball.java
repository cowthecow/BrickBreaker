package com.laserinfinite.java.powerball;

import com.laserinfinite.java.GamePanel;
import com.laserinfinite.java.SmokeEffect;

import java.awt.*;

public class Fireball extends Powerball {

    private int x;
    private int y;
    private int r;
    private int health;
    private double directionFacing;
    private boolean lifeOver = false;

    private long previousRelease = System.nanoTime();

    public Fireball(int x, int y, double directionFacing) {
        super(x, y, 30);
        this.x = x;
        this.y = y;
        this.r = 30;
        this.health = 100;
        this.directionFacing = directionFacing;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getR() {
        return r;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public String getType() {
        return "fire";
    }

    @Override
    public boolean lifeOver() {
        return lifeOver;
    }

    @Override
    public void update() {
        this.x += Math.cos(directionFacing + Math.PI / 2) * 3;
        this.y -= Math.sin(directionFacing + Math.PI / 2) * 3;

        if (this.x - r > GamePanel.WIDTH) {
            lifeOver = true;
        }
        if (this.x + r < 0) {
            lifeOver = true;
        }
        if (this.y + r < 0) {
            lifeOver = true;
        }
        if (this.y - r > GamePanel.HEIGHT) {
            lifeOver = true;
        }
        if (this.health <= 0) {
            lifeOver = true;
        }
        double percentageOfHealth = (this.health / 100.0);
        if ((System.nanoTime() - previousRelease) / 1000000 > 1) {
            GamePanel.smokeEffects.add(new SmokeEffect((int) this.x, (int) this.y, (int) (((Math.random() * 10) + 10) * percentageOfHealth), 1, 0.3, new Color(32, 32, 32)));
            previousRelease = System.nanoTime();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (health > 0) {
            double percentageOfHealth = (this.health / 100.0);
            int newR = (int) (this.r * percentageOfHealth);
            g.setPaint(new GradientPaint((this.x - newR), (this.y - newR), Color.WHITE, this.x, this.y, new Color(255, 64, 0)));
            g.fillOval((this.x - newR), (this.y - newR), (newR * 2), (newR * 2));
        }
    }
}
