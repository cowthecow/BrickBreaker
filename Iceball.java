package com.laserinfinite.java.powerball;

import com.laserinfinite.java.GamePanel;
import com.laserinfinite.java.SmokeEffect;

import java.awt.*;

public class Iceball extends Powerball {

    private int x;
    private int y;
    private int r;
    private double directionFacing;
    private boolean lifeOver = false;

    private long previousRelease = System.nanoTime();

    public Iceball(int x, int y, double directionFacing) {
        super(x, y, 30);
        this.x = x;
        this.y = y;
        this.r = 30;
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
        return 0;
    }

    @Override
    public void setHealth(int health) {

    }

    @Override
    public String getType() {
        return "ice";
    }

    @Override
    public boolean lifeOver() {
        return lifeOver;
    }

    @Override
    public void update() {
        this.x += Math.cos(directionFacing+Math.PI/2) * 3;
        this.y -= Math.sin(directionFacing+Math.PI/2) * 3;

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
        if((System.nanoTime() - previousRelease)/1000000 > 50) {
            previousRelease = System.nanoTime();
            GamePanel.smokeEffects.add(new SmokeEffect(this.x,this.y,20,0,0.3,new Color(225,225,225)));
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setPaint(new GradientPaint((int) (this.x - this.r), (int) (this.y - this.r), Color.WHITE, (int) this.x, (int) this.y, new Color(192, 255, 255)));
        g.fillOval((int) (this.x - this.r), (int) (this.y - this.r), (int) (this.r * 2), (int) (this.r * 2));
    }
}
