package sample;


public class Ball extends Sprite{
    public static final String BALL_IMAGE = "ball.gif";
    public static final int BALL_INIT_X = 300;
    public static final int BALL_INIT_Y = 550;
    public static final int BALL_SIZE = 15;

    private int ballSpeed = 150;
    private int xSpeed = 1;
    private int ySpeed = -1;

    public Ball() {
        super(BALL_IMAGE, BALL_INIT_X, BALL_INIT_Y, BALL_SIZE, BALL_SIZE);
    }

    public void ballOffBoundary(int xBoundary, int yBoundary) {
        if(this.getImage().getX() >= xBoundary - this.getWidth()) {this.setxSpeed(-1);}
        else if(this.getImage().getX() <= 0) { this.setxSpeed(1);}
        else if(this.getImage().getY() <= yBoundary / 10) {this.setySpeed(1);}
    }

    public void move(double elapsedTime) {
        setX(getX() + xSpeed * ballSpeed * elapsedTime );
        setY(getY() + ySpeed * ballSpeed * elapsedTime);
    }

    public void resetBall() {
        setX(BALL_INIT_X);
        setY(BALL_INIT_Y);
        setxSpeed(1);
        setySpeed(-1);
    }

    public void setBallSpeed(int ballSpeed) {
        this.ballSpeed = ballSpeed;
    }

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }
}
