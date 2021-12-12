package com.laserinfinite.java.powerball;

import com.laserinfinite.java.Entity;

public abstract class Powerball implements Entity {

    private int x;
    private int y;
    private int r;

    public Powerball(int x, int y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public abstract int getX();

    public abstract int getY();

    public abstract int getR();

    public abstract int getHealth();

    public abstract void setHealth(int health);

    public abstract String getType();
}
