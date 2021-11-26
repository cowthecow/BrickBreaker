package com.laserinfinite.java;

import java.awt.*;

public class Paddle {

    private int x;
    private final int y;

    public Paddle() {
        this.x = 600;
        this.y = 500;
    }

    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void update() {
        double mouseX = MouseInfo.getPointerInfo().getLocation().getX();
        this.x = (int) mouseX - Game.window.getX();
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
