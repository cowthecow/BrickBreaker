package com.laserinfinite.java;

import com.laserinfinite.java.powerball.Fireball;
import com.laserinfinite.java.powerball.Iceball;
import com.laserinfinite.java.powerball.Powerball;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    //FIELDS
    public static int WIDTH = 1200;
    public static int HEIGHT = 600;

    private Thread thread;

    private BufferedImage image;
    private Graphics2D g;

    private int health = 5;

    private double paddleTurningAngle = 0;

    public static ArrayList<Brick> bricks = new ArrayList<>();
    public static ArrayList<SmokeEffect> smokeEffects = new ArrayList<>();
    public static Paddle paddle = new Paddle(1600, 500);
    public static ArrayList<Paddle> rotors = new ArrayList<>();

    public static ArrayList<Ball> balls = new ArrayList<>();
    public static ArrayList<Powerball> powerballs = new ArrayList<>();

    public static boolean isWaiting;
    public static boolean gameOver = false;
    public static int playerScore = 0;

    public static boolean goingToChangeLevel = false;
    public static boolean waitingForLevel;
    public static long waitStartTime = System.nanoTime();
    public static long lastCollision = 0;
    public static int level = 3;
    public static int firestormLevel = 0;
    public static boolean lowerFirestorm = false;

    public static long gameStartTime = System.nanoTime();

    //CONSTRUCTOR
    public GamePanel() {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);
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
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);


        long startTime;
        long URDTimeMillis;
        long waitTime;

        int frameCount = 0;
        int maxFrameCount = 155;


        int FPS = 155;
        long targetTime = 1000 / FPS;

        g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for (int i = 0; i < 6; i++)
            rotors.add(new Paddle(i * 200 + 150, 500));


        //GAME LOOP
        while (true) {
            startTime = System.nanoTime();

            gameUpdate();
            gameRender();
            gameDraw();

            URDTimeMillis = (System.nanoTime() - startTime) / 1000000;

            waitTime = targetTime - URDTimeMillis;

            if(waitTime > 0) {
                try {
                    Thread.sleep(waitTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            frameCount++;
            if (frameCount == maxFrameCount) {
                frameCount = 0;
            }

        }
    }

    public static void addScore(double score) {
        playerScore += (int) score;
    }

    public void addLevelObjects(int level) {
        if (level + 1 == 1) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 13; j++) {
                    if (i % 2 == 0) {
                        if (j % 2 == 0)
                            bricks.add(new Brick(j * 75 + 150, i * 42 + 125, 80, 40, 1, new Color(255, 200, 200)));
                    }
                }
            }
        } else if (level + 1 == 2) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 13; j++) {
                    if (i % 2 == 0) {
                        if (j % 2 == 0)
                            bricks.add(new Brick(j * 75 + 150, i * 42 + 125, 60, 30, 2, new Color(200, 200, 255)));
                        else bricks.add(new Brick(j * 75 + 150, i * 42 + 125, 60, 30, 2, new Color(200, 200, 255)));
                    } else {
                        if (j != 12)
                            bricks.add(new Brick(j * 75 + 37 + 150, i * 42 + 125, 60, 30, 2, new Color(200, 200, 255)));
                    }
                }
            }
        } else if (level + 1 == 3) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 7; j++) {
                    bricks.add(new Brick(j * 150 + 150, i * 100 + 55, 20, 20, 4, new Color(255, 255, 200)));
                    bricks.add(new Brick(j * 150 + 130, i * 100 + 35, 20, 20, 3, new Color(200, 255, 255)));
                    bricks.add(new Brick(j * 150 + 170, i * 100 + 35, 20, 20, 3, new Color(200, 255, 255)));
                    bricks.add(new Brick(j * 150 + 130, i * 100 + 75, 20, 20, 3, new Color(200, 255, 255)));
                    bricks.add(new Brick(j * 150 + 170, i * 100 + 75, 20, 20, 3, new Color(200, 255, 255)));
                }
            }
        } else if (level + 1 == 4) {
            for (int x = 0; x < 3; x++) {
                int sx = 300 + x * 300;
                int sy = 50;
                for (int i = -2; i <= 2; i++)
                    bricks.add(new Brick(sx + 20 * i, sy, 20, 20, 4, new Color(192, 192, 128)));
                for (int i = -3; i <= 3; i++)
                    bricks.add(new Brick(sx + 20 * i, sy + 20, 20, 20, 4, new Color(192, 192, 128)));
                for (int i = -4; i <= 4; i++)
                    bricks.add(new Brick(sx + 20 * i, sy + 40, 20, 20, 4, new Color(192, 192, 128)));
                for (int i = -4; i <= 4; i++)
                    bricks.add(new Brick(sx + 20 * i, sy + 60, 20, 20, 4, new Color(192, 192, 128)));

                for (int i = -4; i <= 4; i++) {
                    if (i == -3 || i == -2 || i == 2 || i == 3)
                        bricks.add(new Brick(sx + 20 * i, sy + 80, 20, 20, 6, new Color(64, 64, 64)));
                    else
                        bricks.add(new Brick(sx + 20 * i, sy + 80, 20, 20, 4, new Color(192, 192, 128)));
                }
                for (int i = -4; i <= 4; i++) {
                    if (i == -3 || i == -2 || i == 2 || i == 3)
                        bricks.add(new Brick(sx + 20 * i, sy + 100, 20, 20, 6, new Color(64, 64, 64)));
                    else
                        bricks.add(new Brick(sx + 20 * i, sy + 100, 20, 20, 4, new Color(192, 192, 128)));
                }

                for (int i = -4; i <= 4; i++)
                    bricks.add(new Brick(sx + 20 * i, sy + 120, 20, 20, 4, new Color(192, 192, 128)));
                for (int i = -3; i <= 3; i++)
                    bricks.add(new Brick(sx + 20 * i, sy + 140, 20, 20, 4, new Color(192, 192, 128)));
                for (int i = -2; i <= 2; i++)
                    bricks.add(new Brick(sx + 20 * i, sy + 160, 20, 20, 4, new Color(192, 192, 128)));
                for (int i = -2; i <= 2; i++) {
                    if (i % 2 == 0) bricks.add(new Brick(sx + 20 * i, sy + 180, 20, 20, 4, new Color(192, 192, 128)));
                }
            }
            for (int i = 0; i < 30; i++) {
                bricks.add(new Brick(40 * i + 40, 340, 20, 20, 1, new Color(128, 128, 64)));
                bricks.add(new Brick(40 * i + 20, 360, 20, 20, 1, new Color(128, 128, 64)));
            }
        }

    }


    static boolean circleRect(double cx, double cy, double cr, double rx, double ry, double rw, double rh, double r_angle) {
        double unrotatedCircleX = Math.cos(r_angle) * (cx - rx) -
                Math.sin(r_angle) * (cy - ry) + rx;
        double unrotatedCircleY = Math.sin(r_angle) * (cx - rx) +
                Math.cos(r_angle) * (cy - ry) + ry;

        double closestX, closestY;

        if (unrotatedCircleX < rx)
            closestX = rx;
        else closestX = Math.min(unrotatedCircleX, rx + rw);

        if (unrotatedCircleY < ry)
            closestY = ry;
        else closestY = Math.min(unrotatedCircleY, ry + rh);

        double distance = findDistance(unrotatedCircleX, unrotatedCircleY, closestX, closestY);

        return distance < cr;
    }

    public static double findDistance(double fromX, double fromY, double toX, double toY) {
        double a = Math.abs(fromX - toX);
        double b = Math.abs(fromY - toY);

        return Math.sqrt((a * a) + (b * b));
    }

    private void updateBricks() {
        ArrayList<Brick> deleted = new ArrayList<>();
        for (Brick brick : bricks) {
            brick.update();
            if (brick.lifeOver()) {
                deleted.add(brick);
            }
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

    private void updateBalls() {
        ArrayList<Ball> deleted = new ArrayList<>();
        for (Ball ball : balls) {
            ball.update();
            if (ball.lifeOver()) {
                deleted.add(ball);
                health--;
                if (health <= 0) {
                    gameOver = true;
                    return;
                }
                waitingForLevel = false;
                isWaiting = true;
                waitStartTime = System.nanoTime();
            }
        }
        balls.removeAll(deleted);
    }

    //ice ball freezes everything that touches it
    //fire ball deals damage to itself and the bricks and at the end it explodes
    //light ball explodes into 30 small lightballs

    private void updatePowerballs() {
        ArrayList<Powerball> deleted = new ArrayList<>();
        for (Powerball ball : powerballs) {
            ball.update();
            if (ball.lifeOver()) {
                deleted.add(ball);
            }
        }
        powerballs.removeAll(deleted);
    }

    public static void ballPaddleCollision() {
        for (Ball ball : balls) {
            int reachX = (int) (paddle.getX() + Math.cos(Math.PI - paddle.getAngle() + Math.PI / 30) * 75);
            int reachY = (int) (paddle.getY() + Math.sin(Math.PI - paddle.getAngle() + Math.PI / 30) * 75);

            if ((circleRect(ball.getX(), ball.getY(), 3, reachX, reachY, 150, 15, paddle.getAngle()))) {
                if ((System.nanoTime() - lastCollision) / 1000000 > 300) {
                    lastCollision = System.nanoTime();
                    ball.reverseY();

                    double newBallAngle = Ball.slopeToAngle(ball.getDx(), ball.getDy()) + paddle.getAngle() * 2;
                    ball.setX(Ball.angleToSlope(newBallAngle)[0]);
                    ball.setY(Ball.angleToSlope(newBallAngle)[1]);
                }
            }
        }
    }

    private void ballBrickCollision() {
        for (Brick brick : bricks) {
            for (Ball ball : balls) {
                if (circleRect(ball.getX(), ball.getY(), ball.getR(), brick.getX() - (brick.getWidth() / 2.0), brick.getY() - (brick.getWidth() / 2.0), brick.getWidth(), brick.getHeight(), 0)) {
                    if (Math.abs(brick.getX() - ball.getX()) < brick.getWidth() / 2.0) {
                        ball.reverseY();
                    } else {
                        ball.reverseX();
                    }
                    brick.crack();
                    return;
                }
            }
        }
    }

    private void powerballBrickCollision() {
        for (Brick brick : bricks) {
            for (Powerball ball : powerballs) {
                int radiusOfEffect = ball.getR()+15;

                if (circleRect(ball.getX(), ball.getY(), radiusOfEffect, brick.getX() - (brick.getWidth() / 2.0), brick.getY() - (brick.getWidth() / 2.0), brick.getWidth(), brick.getHeight(), 0)) {
                    if(ball.getType().equals("ice")) {
                        brick.freeze();
                    }else if(ball.getType().equals("fire")) {
                        ball.setHealth(ball.getHealth()-brick.getHealth());
                        brick.disintegrate();
                    }else if(ball.getType().equals("light")) {

                    }
                }
            }
        }
    }

    private void gameUpdate() {
        if (!gameOver) {
            for (Paddle p : rotors) p.update();
            paddle.update();

            if (!isWaiting) {
                if (lowerFirestorm) {
                    firestormLevel -= 1f;
                    if (firestormLevel < 0) {
                        firestormLevel = 0;
                        lowerFirestorm = false;
                    }
                }
                ballBrickCollision();
                powerballBrickCollision();

                updatePowerballs();
                updateBalls();
                updateBricks();
                updateSmokeEffects();

                if (bricks.size() == 0) {
                    goingToChangeLevel = true;
                    addLevelObjects(level);
                }
                if (goingToChangeLevel) {
                    health = 5;
                    isWaiting = true;
                    waitingForLevel = true;
                    waitStartTime = System.nanoTime();
                    gameStartTime = System.nanoTime();
                    level++;
                    firestormLevel = 0;
                    lowerFirestorm = false;
                    balls.clear();
                    goingToChangeLevel = false;
                }
            } else {
                if ((System.nanoTime() - waitStartTime) / 1000000 > (waitingForLevel ? 5000 : 3000)) {
                    isWaiting = false;

                    if (!gameOver) balls.add(new Ball(level == 3));
                }
            }
        }
    }

    private void gameRender() {
        //Draw background
        g.setColor(new Color(64, 64, 64));
        g.setFont(new Font("Bahnschrift", Font.BOLD, 40));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        for (Brick b : bricks) b.draw(g);
        for (SmokeEffect b : smokeEffects) b.draw(g);
        for (Ball ball : balls) ball.draw(g);
        try {
            for (Powerball powerball : powerballs) powerball.draw(g);
        }catch (ConcurrentModificationException ignore) {}
        paddle.draw(g);

        for (int i = 0; i < health; i++) {
            int x = i * 25 + 15, y = 15, r = 8;
            g.setPaint(new GradientPaint((x - r), (y - r), Color.WHITE, x, y, new Color(0, 128, 128)));
            g.fillOval((x - r), (y - r), (r * 2), (r * 2));
        }

        if (isWaiting) {
            g.setColor(new Color(255, 255, 255, 64));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            String count = String.valueOf((waitingForLevel ? 5 : 3) - (int) ((System.nanoTime() - waitStartTime) / 1000000000));
            g.setFont(new Font("Bahnschrift", Font.PLAIN, 500));

            int length = (int) g.getFontMetrics().getStringBounds(count, g).getWidth();
            g.drawString(count, GamePanel.WIDTH / 2 - length / 2, GamePanel.HEIGHT - 150);

        }

        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 192));
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setPaint(new GradientPaint(0, GamePanel.HEIGHT / 2, new Color(200, 255, 200), GamePanel.WIDTH, GamePanel.HEIGHT / 2, new Color(200, 200, 255)));

            String gameOver = "GAME OVER!";
            String score = "YOUR SCORE IS " + playerScore;
            String ranking;

            if (playerScore < 500) {
                ranking = "You're currently noob level, stop being a loser and get playing";
            } else if (playerScore < 2000) {
                ranking = "You're an average player, keep playing to achieve more";
            } else if (playerScore < 3000) {
                ranking = "You're a pretty good player, play some more and you'll reach the top";
            } else if (playerScore < 4500) {
                ranking = "You're an amazing player, a few more acquired skills and you'll be the best";
            } else {
                ranking = "You're either a champion or a hacker who's a sore loser";
            }

            g.setFont(new Font("Bahnschrift", Font.PLAIN, 100));
            int gmoverlen = (int) g.getFontMetrics().getStringBounds(gameOver, g).getWidth();
            g.drawString(gameOver, GamePanel.WIDTH / 2 - gmoverlen / 2, 150);

            g.setFont(new Font("Bahnschrift", Font.PLAIN, 75));
            int scorelen = (int) g.getFontMetrics().getStringBounds(score, g).getWidth();
            g.drawString(score, GamePanel.WIDTH / 2 - scorelen / 2, 250);

            g.setFont(new Font("Bahnschrift", Font.PLAIN, 35));
            int ranklen = (int) g.getFontMetrics().getStringBounds(ranking, g).getWidth();
            g.drawString(ranking, GamePanel.WIDTH / 2 - ranklen / 2, 325);
        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Bahnschrift", Font.PLAIN, 35));
            String score = "YOUR SCORE IS " + playerScore;
            int length = (int) g.getFontMetrics().getStringBounds(score, g).getWidth();
            g.drawString(score, 1025 - length / 2, 40);
        }
        g.setColor(new Color(255, 64, 0, firestormLevel));
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
    }

    private void gameDraw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            if (paddleTurningAngle == 0) paddleTurningAngle = 0.01;
            paddleTurningAngle *= 1.15;
            if (paddleTurningAngle > 0.25) paddleTurningAngle = 0.25;
            paddle.setAngle(paddle.getAngle() + paddleTurningAngle);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            if (paddleTurningAngle == 0) paddleTurningAngle = 0.01;
            paddleTurningAngle *= 1.15;
            if (paddleTurningAngle > 0.25) paddleTurningAngle = 0.25;

            paddle.setAngle(paddle.getAngle() - paddleTurningAngle);
        }

        if (!isWaiting) {
            if (e.getKeyCode() == 'F') {
                if (firestormLevel < 255) firestormLevel++;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        paddleTurningAngle = 0;
        if (e.getKeyCode() == 'F') {
            if (firestormLevel < 255) lowerFirestorm = true;
        }
        if(!isWaiting && e.getKeyCode() == KeyEvent.VK_SPACE) {
            powerballs.add(new Fireball(paddle.getX(),paddle.getY(),paddle.getAngle()));
        }
    }
}