package sample;

import javafx.scene.image.ImageView;

import java.lang.reflect.GenericArrayType;

public class Paddle extends Sprite{
    public static final String PADDLE_IMAGE = "paddle.gif";
    public static final int PADDLE_INIT_X = 260;
    public static final int PADDLE_INIT_Y = 600;
    public static final int PADDLE_WIDTH = 100;
    public static final int PADDLE_HEIGHT = 15;
    public static final int PADDLE_BASE_SPEED  = 5;

    private int xSpeed = 0;

    public Paddle() {
        super(PADDLE_IMAGE, PADDLE_INIT_X, PADDLE_INIT_Y, PADDLE_WIDTH, PADDLE_HEIGHT );
    }


    public void resetPaddle() {
        this.setX(PADDLE_INIT_X);
        this.setY(PADDLE_INIT_Y);
        this.setWidth(PADDLE_WIDTH);
        this.setHeight(PADDLE_HEIGHT);
    }

    public void paddleOffBoundary(int xBoundary) {
        if(this.getX() >= xBoundary - this.getWidth()) {
            this.setX(xBoundary - this.getWidth());
        } else if(this.getX() <= 0) {
           this.setX(0);
        }
    }

    public void paddleTeleport(int xBoundary) {
        if(this.getX() < 0 - this.getWidth()) { this.setX(xBoundary);}
        else if (this.getX() > xBoundary) {
           this.setX(0 - this.getWidth());
        }
    }

    public void move() {
        if(this.getxSpeed() == 1 || this.getxSpeed() == -1) {
            this.setX(this.getX()+this.getxSpeed()  * PADDLE_BASE_SPEED);
        }
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }
}
