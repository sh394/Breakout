package sample;

public class PowerUp extends Sprite {
    public static final int POWER_UP_SIZE = 15;
    public static final int POWER_UP_SPEED = -100;


    public PowerUp(String powerType, double xPos, double yPos) {
        super(powerType, xPos, yPos, POWER_UP_SIZE, POWER_UP_SIZE);
    }

    public void move(double elapsedTime) {
        this.setY(this.getY() + elapsedTime * POWER_UP_SPEED);
    }
}
