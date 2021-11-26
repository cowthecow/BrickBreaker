package com.laserinfinite.java;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {

    //FIELDS
    public static int WIDTH = 1200;
    public static int HEIGHT = 600;

    private Thread thread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D g;

    private final int FPS = 155;
    private double averageFPS;

    public static ArrayList<Brick> bricks = new ArrayList<>();
    public static ArrayList<SmokeEffect> smokeEffects = new ArrayList<>();
    public static Paddle paddle = new Paddle();
    public static Ball ball = new Ball();


    //CONSTRUCTOR
    public GamePanel() {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
    }

    //FUNCTIONS
    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        running = true;

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);


        long startTime;
        long URDTimeMillis;
        long waitTime;
        long totalTime = 0;

        int frameCount = 0;
        int maxFrameCount = 155;


        long targetTime = 1000 / FPS;

        g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //Brick Pattern
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 18; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 0) bricks.add(new Brick(j * 75, i * 42 + 25, 60, 30, 2, new Color(200, 255, 200)));
                    else bricks.add(new Brick(j * 75, i * 42 + 25, 60, 30, 3, new Color(200, 200, 255)));
                } else {
                    if (j != 17) bricks.add(new Brick(j * 75 + 37, i * 42 + 25, 60, 30, 1, new Color(255, 200, 200)));
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            bricks.get(0).crack();
            bricks.get(5).crack();
            bricks.get(25).crack();
            bricks.get(25).crack();
            bricks.get(25).crack();
            bricks.get(34).crack();
            bricks.get(12).crack();
            bricks.get(56).crack();
            bricks.get(14).crack();
            bricks.get(29).crack();
            bricks.get(31).crack();
            bricks.get(98).crack();
        }
        //GAME LOOP
        while (running) {
            startTime = System.nanoTime();

            gameUpdate();
            gameRender();
            gameDraw();

            URDTimeMillis = (System.nanoTime() - startTime) / 1000000;

            waitTime = targetTime - URDTimeMillis;

            try {
                Thread.sleep(waitTime);
            } catch (Exception ignore) {
            }

            totalTime += System.nanoTime() - startTime;

            frameCount++;
            if (frameCount == maxFrameCount) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
            }

        }
    }

    boolean circleRect(double cx, double cy, double radius, double rx, double ry, double rw, double rh) {

        // temporary variables to set edges for testing
        double testX = cx;
        double testY = cy;

        // which edge is closest?
        if (cx < rx)         testX = rx;      // test left edge
        else if (cx > rx+rw) testX = rx+rw;   // right edge
        if (cy < ry)         testY = ry;      // top edge
        else if (cy > ry+rh) testY = ry+rh;   // bottom edge

        // get distance from closest edges
        double distX = cx-testX;
        double distY = cy-testY;
        double distance = Math.sqrt( (distX*distX) + (distY*distY) );

        // if the distance is less than the radius, collision!
        if (distance <= radius) {
            return true;
        }
        return false;
    }

    private void updateBricks() {
        ArrayList<Brick> deleted = new ArrayList<>();
        for (Brick brick : bricks) {
            //brick.update()
            if (brick.lifeOver())
                deleted.add(brick);
        }
        bricks.removeAll(deleted);
    }

    private void updateSmokeEffects() {
        ArrayList<SmokeEffect> deleted = new ArrayList<>();
        for (SmokeEffect smoke : smokeEffects) {
            smoke.update();
            if (smoke.lifeOver())
                deleted.add(smoke);
        }
        smokeEffects.removeAll(deleted);
    }

    private void ballPaddleCollision() {
        if (circleRect(ball.getX(),ball.getY(),ball.getR(),paddle.getX()-75,paddle.getY(),150,30)) {
            System.out.println("Collide");
            if (Math.abs(paddle.getY() - ball.getY()) < 10) {
                ball.reverseY();
            } else {
                ball.reverseX();
            }
            while(circleRect(ball.getX(),ball.getY(),ball.getR(),paddle.getX()-75,paddle.getY(),150,30)) ball.updateLocation();
        }
    }

    private void ballBrickCollision() {
        for(Brick brick : bricks) {
            if(circleRect(ball.getX(), ball.getY(), ball.getR(), brick.getX()-(brick.getWidth()/2.0), brick.getY()-(brick.getWidth()/2.0),brick.getWidth(),brick.getHeight())) {
                if(Math.abs(brick.getY()-ball.getY()) < brick.getHeight()/2.0) {
                    //Hit the side of the brick
                    if(!ball.isFireball())ball.reverseY();
                }else {
                    if(!ball.isFireball()) ball.reverseX();
                }
                brick.crack();
            }
        }
    }

    private void gameUpdate() {
        ballPaddleCollision();
        ballBrickCollision();

        updateBricks();
        updateSmokeEffects();

        paddle.update();
        ball.update();

    }

    private void gameRender() {
        //Draw background
        g.setColor(new Color(64, 64, 64));
        g.setFont(new Font("Bahnschrift", Font.BOLD, 40));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        for (Brick b : bricks) b.draw(g);
        for (SmokeEffect b : smokeEffects) b.draw(g);

        paddle.draw(g);
        ball.draw(g);

    }

    private void gameDraw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

}