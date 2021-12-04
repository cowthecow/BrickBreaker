package com.laserinfinite.java;

import java.awt.*;

public class Ball implements Entity {

    private double x;
    private double y;
    private final double r;
    private int speed;
    private double directionFacing;

    private double dx;
    private double dy;

    private double lastSmokeRelease = System.nanoTime();
    private boolean isFireball = false;
    private boolean lifeover = false;

    public Ball() {
        this.x = GamePanel.WIDTH / 2.0;
        this.y = 475;
        this.r = 10;
        this.speed = 5;
        this.directionFacing = Math.PI;

        this.dx = Math.cos((directionFacing)) * speed;
        this.dy = Math.sin((directionFacing)) * speed;



        //turnToFireball();
    }

    public void turnToFireball() {
        isFireball = true;
        this.speed *= 3.0/2.0;
        this.dx = Math.cos((directionFacing)) * speed;
        this.dy = Math.sin((directionFacing)) * speed;
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

    public void setX(double x) {this.dx = x;}

    public void setY(double y) {this.dy = y;}

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public double getDx() {return dx;}

    public double getDy() {return dy;}

    public void updateLocation() {
        this.x += dx;
        this.y += dy;
    }

    @Override
    public boolean lifeOver() {
        return this.lifeover;
    }


    public static double slopeToAngle(double dx, double dy) {
        double angle = (Math.atan2(dy, dx));
        if (angle < 0) angle += Math.PI*2;
        return Math.PI*2-angle;
    }

    public static double[] angleToSlope(double angle) {
        return new double[] {Math.cos(Math.PI*2-angle),Math.sin(Math.PI*2-angle)};
    }

    @Override
    public void update() {
        if(this.y > 575 && !isFireball) turnToFireball();
        if(this.y > 575 && isFireball) this.lifeover = true;

        this.directionFacing = slopeToAngle(this.dx,this.dy);

        //System.out.println("Le ball shall go to le angle " + (360-Math.toDegrees(directionFacing)));
        //System.out.println(Math.PI*2-directionFacing);
        this.x -= angleToSlope((directionFacing))[0] * speed;
        this.y -= angleToSlope((directionFacing))[1] * speed;

        GamePanel.ballPaddleCollision();

        if (x - r <= 0 || x + r >= GamePanel.WIDTH) {
            dx = -dx;
        }
        if (y - r <= 0) {
            dy = -dy;
        }
        if (y + r >= GamePanel.HEIGHT) {
            dy = -dy;
        }

        if (isFireball) {
            if ((System.nanoTime() - lastSmokeRelease) / 1000000 > 1) {
                GamePanel.smokeEffects.add(new SmokeEffect((int) this.x, (int) this.y, (int) (Math.random() * 5) + 5, 1, 0.25, new Color(32, 32, 32)));
                lastSmokeRelease = System.nanoTime();
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (isFireball) {
            g.setPaint(new GradientPaint((int) (this.x - this.r), (int) (this.y - this.r), Color.WHITE, (int) this.x, (int) this.y, new Color(255, 64, 0)));
        } else {
            g.setPaint(new GradientPaint((int) (this.x - this.r), (int) (this.y - this.r), Color.WHITE, (int) this.x, (int) this.y, new Color(0, 128, 128)));
        }
        g.fillOval((int) (this.x - this.r), (int) (this.y - this.r), (int) (this.r * 2), (int) (this.r * 2));
    }

}
