package com.laserinfinite.java;

import java.awt.*;

public class Brick {

    private int x;
    private int y;
    private int width;
    private int height;
    private int health;
    private Color color;

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

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean shouldDelete(){return isDead;}

    public void crack() {
        this.health --;
        if(this.health <= 0) isDead = true;
    }

    public void update() {

    }

    public void draw(Graphics2D g) {
        g.setPaint(new GradientPaint(this.x, this.y-10, this.color, this.x, this.y+10, this.color.darker().darker()));
        g.fillRoundRect(x-width/2, y-height/2,width,height, this.width/10,this.height/10);

        g.setPaint(null);
        g.setColor(new Color(0,0,0,64));
        //Draw cracks on brick
        for(int i = 0; i < this.maxHealth-this.health; i++) {
            g.fillRoundRect(x-width/2, y-height/2,width,height, this.width/10,this.height/10);
        }
    }
}
