package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class MainScene extends Application {
    //Scene Constants
    public static final String TITLE= "Breakout Game-Main";
    public static final int WIDTH = 650;
    public static final int HEIGHT = 500;

    //Button Constants
    public static final int BUTTON_BASE_X = 265;
    public static final int BUTTON_BASE_Y = 200;
    public static final int BUTTON_WIDTH = 100;

    //Label Constants
    public static final int LABEL_BASE_X = 70;
    public static final int LABEL_BASE_Y = 350;


    private Group mainRoot;
    private Stage mainStage;

    List<Button> menuButtons;
    List<Label> labels;

    @Override
    public void start(Stage stage) throws Exception{
        mainStage = stage;
        mainRoot = new Group();
        menuButtons = new ArrayList<>();
        labels = new ArrayList<>();
        createLogo();
        createButtons();
        createLabel();

        mainStage.setTitle(TITLE);
        mainStage.setScene(new Scene(mainRoot, WIDTH, HEIGHT));
        mainStage.show();
    }

    private void createLogo() {
        ImageView logo = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream("logo.jpg")));
        logo.setLayoutX(210);
        logo.setLayoutY(50);
        logo.setFitWidth(220);
        logo.setFitHeight(100);
        mainRoot.getChildren().add(logo);
    }

    private void addButton(Button button) {
        button.setLayoutX(BUTTON_BASE_X);
        button.setLayoutY(BUTTON_BASE_Y + menuButtons.size()* 50);
        button.setPrefWidth(BUTTON_WIDTH);
        menuButtons.add(button);
        mainRoot.getChildren().add(button);
    }

    private void createButtons() {
        startButton();
        exitButton();
    }

    private void startButton() {
        Button startButton = new Button("PLAY!");
        addButton(startButton);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameScene gameScene = new GameScene();
                gameScene.startNewGame(mainStage);
            }
        });
    }

    private void exitButton() {
        Button exitButton = new Button("EXIT");
        addButton(exitButton);

       exitButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
                System.exit(0);
           }
       });
    }

    private void createLabel() {
        infoLabel();
    }

    private void addLabel(Label label) {
        label.setTextAlignment(TextAlignment.CENTER);
       label.setTextFill(Color.BLUE);
       label.setLayoutX(LABEL_BASE_X);
       label.setLayoutY(LABEL_BASE_Y);
       labels.add(label);
       mainRoot.getChildren().add(label);
    }

    private void infoLabel() {
        Label infoLabel = new Label("Basic Breakout Game made by Kyle Hong\n"
                        + "Click Play to start\n"
                        + "Use the left and right arrows on your Keyboard to move your paddle\n"
                        + "This game is composed of three items(laser power, extra power, size power)\n "
                        + "two cheat codes(hawaiian shirt => instant win, show me the score= > + 1000 score\n"
                        + "three levels.\n"
                        +"Enjoy!"
        );

        addLabel(infoLabel);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
