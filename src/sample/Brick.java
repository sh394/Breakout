package sample;

import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Brick extends Sprite {
    public static final int BRICK_WIDTH = 70;
    public static final int BRICK_HEIGHT = 25;

    public static final int BRICK_COL_START = 80;
    public static final int BRICK_ROW_START = 200;

    private int brickLife;
    private int row;
    private int col;

    public Brick(String imageName, int brickLife, int row, int col) {
        super(imageName, BRICK_COL_START, BRICK_ROW_START, BRICK_WIDTH, BRICK_HEIGHT);
        setX(BRICK_COL_START + col * 1.1 * BRICK_WIDTH);
        setY(BRICK_ROW_START + row * 2 * BRICK_HEIGHT);
        this.row = row;
        this.col = col;
        setBrickLife(brickLife);
    }

    public boolean collide(Ball ball) {
        return (this.getImage().intersects(ball.getImage().getBoundsInLocal()) && bottom(ball));
    }

    public boolean bottom(Ball ball) {
        return this.getImage().getBoundsInLocal().getMaxY() <= ball.getImage().getBoundsInLocal().getMinY();
    }

    public boolean top(Ball ball) {
        return this.getImage().getBoundsInLocal().getMinY() >= ball.getImage().getBoundsInLocal().getMaxY();
    }

    public boolean left(Ball ball) {
        return this.getImage().getBoundsInLocal().getMinX() >= ball.getImage().getBoundsInLocal().getMaxX();
    }

    public boolean right(Ball ball) {
        return this.getImage().getBoundsInLocal().getMaxX() >= ball.getImage().getBoundsInLocal().getMinX();
    }
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getBrickLife() {
        return brickLife;
    }

    public void setBrickLife(int brickLife) {
        this.brickLife = brickLife;
    }
}
