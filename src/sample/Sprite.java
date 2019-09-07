package sample;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite {
    private ImageView image;

    public Sprite() {

    }

    public Sprite(String imageName, int initX, int initY, int width, int height) {
        setImage(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(imageName))));
        setX(initX);
        setY(initY);
        setWidth(width);
        setHeight(height);
    }

    public boolean intersects(Sprite other) {
        return this.getImage().intersects(other.getImage().getBoundsInLocal());
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public double getX() {
        return image.getX();
    }

    public void setX(double initX) {
        image.setX(initX);
    }

    public double getY() {
        return image.getY();
    }

    public void setY(double initY) {
        image.setY(initY);
    }

    public double getWidth() {
        return image.getBoundsInLocal().getWidth();
    }

    public void setWidth(double width) {
         image.setFitWidth(width);
    }

    public double getHeight() {
        return image.getFitHeight();
    }

    public void setHeight(double height) {
        image.setFitHeight(height);
    }
}
