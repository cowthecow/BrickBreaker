package com.laserinfinite.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    //FIELDS
    public static int WIDTH = 1200;
    public static int HEIGHT = 600;

    private Thread thread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D g;

    private final int FPS = 155;
    private double averageFPS;
    private int health = 5;

    private double paddleTurningAngle = 0;

    public static ArrayList<Brick> bricks = new ArrayList<>();
    public static ArrayList<SmokeEffect> smokeEffects = new ArrayList<>();
    public static Paddle paddle = new Paddle(1600, 500);
    public static ArrayList<Paddle> rotors = new ArrayList<>();

    public static ArrayList<Ball> balls = new ArrayList<>();

    public static long previousBallSpawn = 0;

    public static boolean isWaiting = false;
    public static boolean gameOver = false;
    public static int playerScore = 0;

    public static long waitStartTime;
    public static long lastCollision = 0;

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
        balls.add(new Ball());
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

        for (int i = 0; i < 6; i++)
            rotors.add(new Paddle(i * 200 + 150, 500));

        //Brick Pattern
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 13; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 0) bricks.add(new Brick(j * 75+150, i * 42 + 125, 60, 30, 1, new Color(255, 200, 200)));
                    else bricks.add(new Brick(j * 75+150, i * 42 + 125, 60, 30, 1, new Color(255, 200, 200)));
                } else {
                    if (j != 17) bricks.add(new Brick(j * 75 + 37+150, i * 42 + 125, 60, 30, 2, new Color(200, 200, 255)));
                }
            }
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

    public static void addScore(double score) {
        playerScore += (int) score;
    }


    static boolean circleRect(double cx, double cy, double cr, double rx, double ry, double rw, double rh, double r_angle) {
        double unrotatedCircleX = Math.cos(r_angle) * (cx - rx) -
                Math.sin(r_angle) * (cy - ry) + rx;
        double unrotatedCircleY = Math.sin(r_angle) * (cx - rx) +
                Math.cos(r_angle) * (cy - ry) + ry;

        double closestX, closestY;

        if (unrotatedCircleX < rx)
            closestX = rx;
        else if (unrotatedCircleX > rx + rw)
            closestX = rx + rw;
        else
            closestX = unrotatedCircleX;

        if (unrotatedCircleY < ry)
            closestY = ry;
        else if (unrotatedCircleY > ry + rh)
            closestY = ry + rh;
        else
            closestY = unrotatedCircleY;


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

    private void updateBalls() {
        boolean newBall = false;
        ArrayList<Ball> deleted = new ArrayList<>();
        for (Ball ball : balls) {
            ball.update();
            if (ball.lifeOver()) {
                deleted.add(ball);
                //Spawn a new ball
                health--;
                if(health <= 0) {
                    gameOver = true;
                    return;
                }
                isWaiting = true;
                waitStartTime = System.nanoTime();

            }
        }
        balls.removeAll(deleted);
    }

    public static void ballPaddleCollision() {
        for (Ball ball : balls) {
            int reachX = (int) (paddle.getX() + Math.cos(Math.PI - paddle.getAngle() + Math.PI / 30) * 75);
            int reachY = (int) (paddle.getY() + Math.sin(Math.PI - paddle.getAngle() + Math.PI / 30) * 75);

            if ((circleRect(ball.getX(), ball.getY(), 3, reachX, reachY, 150, 15, paddle.getAngle()))) {
                if ((System.nanoTime() - lastCollision) / 1000000 > 300) {
                    lastCollision = System.nanoTime();
                    System.out.println("A ball has hit the paddle. It was coming at an angle of " + Ball.slopeToAngle(ball.getDx(), ball.getDy()));
                    ball.reverseY();


                    double newBallAngle = Ball.slopeToAngle(ball.getDx(), ball.getDy()) + paddle.getAngle() * 2;
                    ball.setX(Ball.angleToSlope(newBallAngle)[0]);
                    ball.setY(Ball.angleToSlope(newBallAngle)[1]);
                    System.out.println("The ball has bounced off the paddle, which was tilted at an angle of " + paddle.getAngle() + ", at an angle of " + newBallAngle + ".");
                }
            }

        }
    }

    private void ballBrickCollision() {
        for (Brick brick : bricks) {
            for (Ball ball : balls) {
                if (circleRect(ball.getX(), ball.getY(), ball.getR(), brick.getX() - (brick.getWidth() / 2.0), brick.getY() - (brick.getWidth() / 2.0), brick.getWidth(), brick.getHeight(), 0)) {
                    if (Math.abs(brick.getY() - ball.getY()) < brick.getHeight() / 2.0) {
                        //Hit the side of the brick
                        ball.reverseY();
                    } else {
                        if(ball.getY() < brick.getY()) {
                            ball.reverseY();
                        }
                        ball.reverseX();
                    }
                    brick.crack();
                    return;
                }
            }
        }
    }

    private void gameUpdate() {
        if(!gameOver) {
            for (Paddle p : rotors) p.update();
            paddle.update();

            if (!isWaiting) {
                ballBrickCollision();

                updateBalls();
                updateBricks();
                updateSmokeEffects();

                if ((System.nanoTime() - previousBallSpawn) / 1000000 > 100) {
                    //balls.add(new Ball());
                    previousBallSpawn = System.nanoTime();
                }
            } else {
                if ((System.nanoTime() - waitStartTime) / 1000000 > 3000) {
                    isWaiting = false;
                    if(!gameOver) balls.add(new Ball());
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
        //for (Paddle p : rotors) p.draw(g);
        paddle.draw(g);
        for (Ball ball : balls) ball.draw(g);

        g.setPaint(new GradientPaint((int) (WIDTH / 2.0), 575, Color.RED, (int) (WIDTH / 2.0), 590, Color.RED.darker().darker()));
        g.fillRoundRect(0, 575, WIDTH, 10, 20, 20);

        g.setColor(Color.RED);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 160; j++) {
                int x = j * 10;
                int y = i * 10 + 450;

                int reachX = (int) (paddle.getX() + Math.cos(Math.PI - paddle.getAngle() + Math.PI / 30) * 75);
                int reachY = (int) (paddle.getY() + Math.sin(Math.PI - paddle.getAngle() + Math.PI / 30) * 75);

                //g.setPaint(new GradientPaint(this.x, this.y, new Color(200,255,200), reachX, reachY, new Color(200,255,200).darker().darker()));

                //left side of paddle is reachX and reachY
                if ((circleRect(x, y, 3, reachX, reachY, 150, 15, paddle.getAngle()))) {
                    //g.fillOval(x - 3, y - 3, 6, 6);
                }

            }
        }
        for (int i = 0; i < health; i++) {
            int x = i * 25 + 15, y = 15, r = 8;
            g.setPaint(new GradientPaint((int) (x - r), (int) (y - r), Color.WHITE, (int) x, (int) y, new Color(0, 128, 128)));
            g.fillOval((int) (x - r), (int) (y - r), (int) (r * 2), (int) (r * 2));
        }
        if (isWaiting) {
            g.setColor(new Color(255, 255, 255, 64));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            String count = String.valueOf(3 - (int) ((System.nanoTime() - waitStartTime) / 1000000000));
            g.setFont(new Font("Bahnschrift", Font.PLAIN, 500));

            int length = (int) g.getFontMetrics().getStringBounds(count, g).getWidth();
            g.drawString(count, GamePanel.WIDTH / 2 - length / 2, GamePanel.HEIGHT - 150);
        }
        if(gameOver) {

            g.setColor(new Color(0, 0, 0, 192));
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setPaint(new GradientPaint(0,GamePanel.HEIGHT/2,new Color(200,255,200),GamePanel.WIDTH,GamePanel.HEIGHT/2,new Color(200,200,255)));

            String gameOver = "GAME OVER!";
            String score = "YOUR SCORE IS " + playerScore;
            String ranking = "";

            if(playerScore < 500) {
                ranking = "You're currently noob level, stop being a loser and get playing";
            }else if(playerScore < 2000) {
                ranking = "You're an average player, keep playing to achieve more";
            }else if(playerScore < 3000) {
                ranking = "You're a pretty good player, play some more and you'll reach the top";
            }else if(playerScore < 4500) {
                ranking = "You're an amazing player, a few more acquired skills and you'll be the best";
            }else {
                ranking = "You're either a champion or a hacker who's a sore loser";
            }


            g.setFont(new Font("Bahnschrift", Font.PLAIN, 100));
            int gmoverlen = (int) g.getFontMetrics().getStringBounds(gameOver, g).getWidth();
            g.drawString(gameOver, GamePanel.WIDTH/2 - gmoverlen / 2, 150);

            g.setFont(new Font("Bahnschrift", Font.PLAIN, 75));
            int scorelen= (int) g.getFontMetrics().getStringBounds(score, g).getWidth();
            g.drawString(score, GamePanel.WIDTH/2 - scorelen / 2, 250);

            g.setFont(new Font("Bahnschrift", Font.PLAIN, 35));
            int ranklen = (int) g.getFontMetrics().getStringBounds(ranking, g).getWidth();
            g.drawString(ranking, GamePanel.WIDTH/2 - ranklen / 2, 325);

        }else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Bahnschrift", Font.PLAIN, 35));
            String score = "YOUR SCORE IS " + playerScore;
            int length = (int) g.getFontMetrics().getStringBounds(score, g).getWidth();
            g.drawString(score, 1025 - length / 2, 40);
        }

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
    }

    @Override
    public void keyReleased(KeyEvent e) {
        paddleTurningAngle = 0;
    }
}