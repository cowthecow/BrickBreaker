package com.laserinfinite.java;

import java.awt.*;

public class Paddle {

    private int x;
    private int y;
    private int health;

    public Paddle() {
        this.x = 600;
        this.y = 500;
        this.health = 3;
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void update() {
        double mouseX = MouseInfo.getPointerInfo().getLocation().getX();
        this.x = (int)mouseX - Game.window.getX();
        if(this.x + 75 > GamePanel.WIDTH) this.x = GamePanel.WIDTH-75;
        if(this.x - 75 < 0) this.x = 75;
    }

    public void draw(Graphics2D g) {
        g.setPaint(new GradientPaint(this.x, this.y+10, Color.GREEN, this.x, this.y+30, Color.GREEN.darker().darker()));
        g.fillRoundRect(x-30,y+5,60,20,10,10);

        g.setPaint(new GradientPaint(this.x, this.y-10, Color.LIGHT_GRAY, this.x, this.y+10, Color.LIGHT_GRAY.darker().darker()));
        g.fillRoundRect(x-75, y-10,150,20, 15,15);
    }
}
