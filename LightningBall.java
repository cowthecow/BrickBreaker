package com.laserinfinite.java.powerball;

import com.laserinfinite.java.GamePanel;
import com.laserinfinite.java.SmokeEffect;

import java.awt.*;

public class LightningBall extends Powerball {

    private int x;
    private int y;
    private int r;
    private int health;
    private double directionFacing;
    private boolean lifeOver = false;

    private long previousRelease = System.nanoTime();

    public LightningBall(int x, int y, double directionFacing) {
        super(x, y, 30);
        this.x = x;
        this.y = y;
        this.r = 30;
        this.health = 2;
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
        return "light";
    }

    @Override
    public boolean lifeOver() {
        return lifeOver;
    }

    @Override
    public void update() {
        this.x += Math.cos(directionFacing+Math.PI/2) * ((this.health == 1) ? 5 : 3);
        this.y -= Math.sin(directionFacing+Math.PI/2) * ((this.health == 1) ? 5 : 3);

        if(this.x - r > GamePanel.WIDTH) {
            lifeOver = true;
        }
        if(this.x + r < 0) {
            lifeOver = true;
        }
        if(this.y + r < 0) {
            lifeOver = true;
        }
        if(this.y-r > GamePanel.HEIGHT) {
            lifeOver = true;
        }
        if(this.health <= 0) {
            lifeOver = true;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if(health > 0) {
            int newR = (health == 2) ? 30 : 10;
            g.setPaint(new GradientPaint((int) (this.x - newR), (int) (this.y - newR), Color.BLACK, (int) this.x, (int) this.y, new Color(255, 255, 128)));
            g.fillOval((int) (this.x - newR), (int) (this.y - newR), (int) (newR * 2), (int) (newR * 2));
        }
    }
}
