package com.laserinfinite.java;

import java.awt.*;

public class SmokeEffect {

    private int x;
    private int y;
    private double r;
    private int minR;
    private double fadeRate;
    private Color color;

    public SmokeEffect(int x, int y, int r, int minR, double fadeRate, Color color) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.minR = minR;
        this.fadeRate = fadeRate;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public void setR(int r) {
        this.r = r;
    }

    public int getMinR() {
        return minR;
    }

    public void setMinR(int minR) {
        this.minR = minR;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean shouldDeleteObject() {
        return this.r <= this.minR;
    }

    public void update() {
        this.r -= fadeRate;
    }

    public void draw(Graphics2D g) {
        Color currentColor = new Color(this.color.getRed(),this.color.getGreen(),this.color.getBlue(), (int)(255-((minR/r)*255)));
        g.setColor(currentColor);


        g.fillOval(this.x-(int)this.r, this.y-(int)this.r, (int)this.r*2,(int)this.r*2);
    }
}
