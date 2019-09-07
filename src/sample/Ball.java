package sample;

import javafx.scene.image.ImageView;

import java.lang.reflect.GenericArrayType;

public class Ball extends Sprite{
    public static final String BALL_IMAGE = "ball.gif";
    public static final int BALL_INIT_X = 300;
    public static final int BALL_INIT_Y = 550;
    public static final int BALL_SIZE = 15;
    public static final int BALL_SPEED = 150;

    private int xSpeed = 1;
    private int ySpeed = -1;

    public Ball() {
        super(BALL_IMAGE, BALL_INIT_X, BALL_INIT_Y, BALL_SIZE, BALL_SIZE);
    }

    public void move(double elapsedTime) {
        setX(getX() + xSpeed * BALL_SPEED * elapsedTime );
        setY(getY() + ySpeed * BALL_SPEED * elapsedTime);
    }

    public void resetBall() {
        setX(BALL_INIT_X);
        setY(BALL_INIT_Y);
        setxSpeed(1);
        setySpeed(-1);
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }
}
