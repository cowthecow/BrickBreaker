package com.laserinfinite.java;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Paddle implements Entity {

    private int x;
    private final int y;
    private double directionFacing = 0;

    public Paddle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getAngle() {
        return directionFacing;
    }

    @Override
    public boolean lifeOver() {
        return false;
    }

    @Override
    public void update() {
        if(directionFacing < -Math.PI) directionFacing = Math.PI - (Math.PI+directionFacing);
        if(directionFacing > Math.PI) directionFacing= -Math.PI - (directionFacing-Math.PI);
        //directionFacing+= Math.PI/64;
        double mouseX = MouseInfo.getPointerInfo().getLocation().getX();
        this.x = (int) mouseX - Game.window.getX();
        if (this.x + 75 > GamePanel.WIDTH) this.x = GamePanel.WIDTH - 75;
        if (this.x - 75 < 0) this.x = 75;
    }

    @Override
    public void draw(Graphics2D g) {

        int reachX = (int)(this.x + Math.cos(Math.PI*3/2-directionFacing) * 10);
        int reachY = (int)(this.y + Math.sin(Math.PI*3/2-directionFacing) * 10);

        g.setPaint(new GradientPaint(this.x, this.y, new Color(200,255,200), reachX, reachY, new Color(200,255,200).darker().darker()));

        Polygon bullet = new Polygon(new int[]{this.x-75,this.x-75,this.x+75,this.x+75}, new int[]{this.y+10,this.y-10,this.y-10,this.y+10}, 4);


        AffineTransform old = new AffineTransform();
        old.rotate(Math.PI-(this.directionFacing), x, y);

        Shape newShape = old.createTransformedShape(bullet);
        g.fill(newShape);

        //
//        Polygon scope = new Polygon(new int[]{this.x-2,this.x-2,this.x+2,this.x+2}, new int[]{this.y+75,this.y-75,this.y-75,this.y+75}, 4);
//        Shape newShape1 = old.createTransformedShape(scope);
//        g.fill(newShape1);
//
//        Polygon scope1 = new Polygon(new int[]{this.x-50,this.x-50,this.x-46,this.x-46}, new int[]{this.y+75,this.y-75,this.y-75,this.y+75}, 4);
//        Shape newShape2 = old.createTransformedShape(scope1);
//        g.fill(newShape2);
//
//        Polygon scope2 = new Polygon(new int[]{this.x+46,this.x+46,this.x+50,this.x+50}, new int[]{this.y+75,this.y-75,this.y-75,this.y+75}, 4);
//        Shape newShape3 = old.createTransformedShape(scope2);
//        g.fill(newShape3);
//

    }


    public void setAngle(double v) {
        this.directionFacing = v;
    }
}
