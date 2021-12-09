package com.laserinfinite.java;

import java.awt.*;

public class SmokeEffect implements Entity {

    private final int x;
    private final int y;
    private double r;
    private final int minR;
    private final double fadeRate;
    private final Color color;

    public SmokeEffect(int x, int y, int r, int minR, double fadeRate, Color color) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.minR = minR;
        this.fadeRate = fadeRate;
        this.color = color;
    }

    @Override
    public boolean lifeOver() {
        return this.r <= this.minR;
    }

    @Override
    public void update() {
        this.r -= fadeRate;
    }

    @Override
    public void draw(Graphics2D g) {
        Color currentColor = new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), (int) (255 - ((minR / r) * 255)));
        g.setColor(currentColor);
        g.fillOval(this.x - (int) this.r, this.y - (int) this.r, (int) this.r * 2, (int) this.r * 2);
    }
}
