package sample;

public class PowerUp extends Sprite {
    public static final int POWER_UP_SIZE = 15;
    public static final int POWER_UP_SPEED = -200;

    private int itemSpeed = 1;

    public PowerUp(String powerType) {
        super(powerType, 0, 0, POWER_UP_SIZE, POWER_UP_SIZE);
    }

    public void move(double elapsedTime) {
        this.setY(this.getY() - elapsedTime * itemSpeed * POWER_UP_SPEED);
    }

    public void setItemSpeed(int itemSpeed) {
        this.itemSpeed = itemSpeed;
    }
}
