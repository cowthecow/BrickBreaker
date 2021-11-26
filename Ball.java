package com.laserinfinite.java;

import java.awt.*;

public class Ball {

    private double x;
    private double y;
    private double r;
    private int speed;
    private double directionFacing;

    private double dx;
    private double dy;

    private double lastSmokeRelease = System.nanoTime();

    private boolean isFireball = true;

    public Ball() {
        this.x = GamePanel.WIDTH / 2.0;
        this.y = GamePanel.HEIGHT / 2.0;
        this.r = 10;
        this.speed = 6;
        this.directionFacing = Math.random() * Math.PI * 2;

        this.dx = Math.cos(directionFacing + (Math.PI / 2)) * speed;
        this.dy = Math.sin(directionFacing + (Math.PI / 2)) * speed;
    }

    public void updateLocation() {
        this.x += dx;
        this.y += dy;
    }

    public void update() {
        this.x += dx;
        this.y += dy;

        if (x - r <= 0 || x + r >= GamePanel.WIDTH) {
            dx = -dx;
        }
        if (y - r <= 0) {
            dy = -dy;
        }
        if (y + r >= GamePanel.HEIGHT) {
            dy = -dy;
        }
        if(isFireball) {
            if ((System.nanoTime() - lastSmokeRelease) / 1000000 > 1) {
                GamePanel.smokeEffects.add(new SmokeEffect((int) this.x, (int) this.y, (int) (Math.random() * 5) + 5, 1, 0.25, new Color(32, 32, 32)));
                lastSmokeRelease = System.nanoTime();
            }
        }
    }

    public void setSpeed(int speed) {
        this.dx /= this.speed;
        this.dy /= this.speed;
        this.speed = speed;
        this.dx *= speed;
        this.dy *= speed;
    }

    public boolean isFireball() {
        return isFireball;
    }

    public void reverseX() {
        this.dx = -dx;
    }

    public void reverseY() {
        this.dy = -dy;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public int getSpeed() {
        return speed;
    }

    public double getDirectionFacing() {
        return directionFacing;
    }

    public void setDirectionFacing(double directionFacing) {
        this.directionFacing = directionFacing;
    }

    public void draw(Graphics2D g) {
        if(isFireball) {
            g.setPaint(new GradientPaint((int) (this.x - this.r), (int) (this.y - this.r), Color.WHITE, (int) this.x, (int) this.y, new Color(255, 64, 0)));
        }else {
            g.setPaint(new GradientPaint((int) (this.x - this.r), (int) (this.y - this.r), Color.WHITE, (int) this.x, (int) this.y, new Color(0, 128, 128)));
        }
        g.fillOval((int) (this.x - this.r), (int) (this.y - this.r), (int) (this.r * 2), (int) (this.r * 2));
    }

}
