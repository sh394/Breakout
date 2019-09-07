package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameScene {
    public static final String GAME_TITLE = "BreakOut Game-Game";
    public static final int GAME_SCENE_WIDTH = 650;
    public static final int GAME_SCENE_HEIGHT = 700;

    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    public static final String BRICK_LEVEL_ONE = "brick1.gif";
    public static final String BRICK_LEVEL_TWO = "brick2.gif";
    public static final String BRICK_LEVEL_THREE = "brick3.gif";

    private Group gameRoot;
    private Scene gameScene;
    private Stage gameStage;
    private Stage mainStage;

    private Rectangle scoreBoard;

    private  Label scoreLabel;
    private  Label lifeLabel;
    private  Label levelLabel;
    private int score = 0;
    private int currentLevel = 1;
    private int life = 3;
    private int randomItem;

    private Paddle playerPaddle;
    private Paddle secondPaddle;
    private Paddle computerPaddle;
    private Ball ball;
    private Brick brick;
    private Scanner sc;
    private File file;

    private List<Label> labels;
    private List<Brick> bricksOnScreen;
    private List<Brick> toBeRemoved;

    public GameScene() {
        initialize();

        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();

    }

    private void initialize() {
        gameRoot = new Group();
        gameScene = new Scene(gameRoot, GAME_SCENE_WIDTH, GAME_SCENE_HEIGHT);
        gameStage = new Stage();
        playerPaddle = new Paddle();
        ball = new Ball();
        gameStage.setTitle(GAME_TITLE);
        gameStage.setScene(gameScene);
        labels = new ArrayList<>();
        toBeRemoved = new CopyOnWriteArrayList<>();
        createScoreBoard();
        createLabels();

        gameRoot.getChildren().add(playerPaddle.getImage());
        gameRoot.getChildren().add(ball.getImage());
        getBrickInput(currentLevel);

        gameScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        gameScene.setOnKeyReleased(e -> handleKeyRelease(e.getCode()));

    }

    public void startNewGame(Stage mainStage) {
        this.mainStage = mainStage;
        this.mainStage.hide();
        gameStage.show();
    }

    private void step(double elapsedTime) {
        ball.move(elapsedTime);
        checkBall(elapsedTime);
        playerPaddle.move(elapsedTime);
        checkPaddle(elapsedTime);
        lifeLabel.setText("Life: " + life);
        scoreLabel.setText("Score: " + score);
        levelLabel.setText("Level: " + currentLevel);
        checkEndOfGame();
        paddleAbility();
        checkLevel();
    }

    private void createLabels() {
        scoreLabel();
        lifeLabel();
        levelLabel();
    }

    private void createScoreBoard() {
        scoreBoard = new Rectangle(0, 0, GAME_SCENE_WIDTH, GAME_SCENE_HEIGHT / 10);
        scoreBoard.setFill(Color.BLUE);
        gameRoot.getChildren().add(scoreBoard);
    }

    private void addLabels(Label label) {
        label.setLayoutX(20 + labels.size()*200);
        label.setLayoutY(20);
        label.setTextFill(Color.WHITE);
        Font font = new Font(30);
        label.setFont(font);
        labels.add(label);
        gameRoot.getChildren().add(label);
    }

    private void scoreLabel() {
        scoreLabel = new Label("Score: " + score);
        addLabels(scoreLabel);
    }

    private void lifeLabel() {
        lifeLabel = new Label("Life: " + life);
        addLabels(lifeLabel);
    }

    private void levelLabel() {
        levelLabel = new Label("Level: " + currentLevel);
        addLabels(levelLabel);
    }

    private void paddleAbility() {
        if(this.life == 1) {
            playerPaddle.setWidth(200);
        }
        if(this.score == 100) {
            playerPaddle.setWidth(70);
        }

        if(this.score == 50) {
            return;
        }
    }

    private void checkPaddle(double elapsedTime) {
        //keep the paddle in the boundary
        if(playerPaddle.getX() <= 0) {
          playerPaddle.setX(0);
        } else if(playerPaddle.getX() >= GAME_SCENE_WIDTH-playerPaddle.getWidth()) {
            playerPaddle.setX(GAME_SCENE_WIDTH - playerPaddle.getWidth());
        }
    }

    private void removeBrick(Brick brick) {
        gameRoot.getChildren().remove(brick.getImage());
        bricksOnScreen.remove(brick);
        toBeRemoved.add(brick);
    }

    private void rePaintBrick(Brick brick) {
        if(brick.getBrickLife() == 3) {
            removeBrick(brick);
            brick = new Brick(BRICK_LEVEL_TWO, 2, brick.getRow(),brick.getCol());
            bricksOnScreen.add(brick);
            gameRoot.getChildren().add(brick.getImage());
        } else if(brick.getBrickLife() == 2) {
            removeBrick(brick);
            brick = new Brick(BRICK_LEVEL_ONE, 1, brick.getRow(),brick.getCol());
            bricksOnScreen.add(brick);
            gameRoot.getChildren().add(brick.getImage());
        } else if(brick.getBrickLife() == 1) {
            removeBrick(brick);
        }

        System.out.println("x: " + brick.getRow() + "y: " + brick.getCol());
    }

    private void checkLevel() {
        if(bricksOnScreen.size() == 0 && currentLevel != 3) {
            this.currentLevel += 1;
            resetGame();

        } else if(currentLevel > 3) {
            System.exit(0);
        }
    }

    private void checkEndOfGame() {
        if(this.life == 0) {
            System.out.println("YOU LOST");
            System.exit(0);
        } else if (this.currentLevel == 3 && bricksOnScreen.size() == 0) {
            System.out.println("YOU WON");
        }
    }

    private void checkBall(double elapsedTime) {
        for(Brick brickHit : bricksOnScreen) {
            if(brickHit.intersects(ball) && brickHit.bottom((ball))) {
                ball.setySpeed(1);
                this.score += 10;
                rePaintBrick(brickHit);
                System.out.println("bottom");
            } else if(brickHit.intersects(ball) && brickHit.top((ball))) {
                ball.setySpeed(-1);
                this.score += 10;
                rePaintBrick(brickHit);
                System.out.println("top");
            } else if(brickHit.intersects(ball) && brickHit.left((ball))) {
                ball.setxSpeed(-1);
                this.score += 10;
                rePaintBrick(brickHit);
                System.out.println("left");
            } else if(brickHit.intersects(ball) && brickHit.right((ball))) {
                ball.setxSpeed(1);
                this.score += 10;
                System.out.println("right");
                rePaintBrick(brickHit);
            }
        }

        for(Brick removeBrick: toBeRemoved) {
            gameRoot.getChildren().remove(removeBrick.getImage());
        }

        //check collision with paddle
        if(ball.intersects(playerPaddle)) {
            ball.setySpeed(-1);
        };
        //check collision with walls
        if(ball.getImage().getX() >= GAME_SCENE_WIDTH - ball.getWidth()) {
            ball.setxSpeed(-1);
        } else if(ball.getImage().getX() <= 0) {
            ball.setxSpeed(1);
        } else if(ball.getImage().getY() <= scoreBoard.getBoundsInLocal().getMaxY()) {
            ball.setySpeed(1);
        }
        ///bottom edge
        if(ball.getImage().getY() >= GAME_SCENE_HEIGHT) {
            this.life -= 1;
            ball.resetBall();
            playerPaddle.resetPaddle();
        }
    }

    private void resetGame() {
        gameRoot.getChildren().clear();
        labels.clear();
        gameRoot.getChildren().addAll(ball.getImage(),playerPaddle.getImage(),scoreBoard);
        createLabels();
        ball.resetBall();
        playerPaddle.resetPaddle();
        getBrickInput(currentLevel);
        this.score = 0;
    }

    //check whether player is alive, resets sprites

    private void getBrickInput(int currentLevel)  {
        List<String[]> bricks = new ArrayList<>();
        if(currentLevel == 1) {
            file = new File("/Users/kylehong/IdeaProjects/Breakout/resource/level1.txt");
        } else if (currentLevel == 2) {
            file = new File("/Users/kylehong/IdeaProjects/Breakout/resource/level2.txt");
        } else if (currentLevel == 3) {
            file = new File("/Users/kylehong/IdeaProjects/Breakout/resource/level3.txt");
        }
        try {
            sc = new Scanner(file);
            while(sc.hasNextLine()) {
                String[] brickInputs = sc.nextLine().split(" ");
                bricks.add(brickInputs);
            }
            generateBricks(bricks);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void generateBricks(List<String[]> bricks) {
        bricksOnScreen = new CopyOnWriteArrayList<>();
        for(int i=0; i < bricks.size(); i++) {
            String[] brickInputs = bricks.get(i);
            for(int j=0; j < brickInputs.length; j++) {
                if(brickInputs[j].equals("3")) {
                    drawBricks(BRICK_LEVEL_THREE, 3, i, j);
                } else if(brickInputs[j].equals("2")) {
                    drawBricks(BRICK_LEVEL_TWO, 2, i, j);
                } else if(brickInputs[j].equals("1")) {
                    drawBricks(BRICK_LEVEL_ONE, 1, i, j);
                }
            }
        }
    }

//    private void generateItem() {
//        randomItem = (int) Math.random()*100 + 1;
//
//    }

    private void drawBricks(String brickName, int brickLife, int row, int col) {
        brick = new Brick(brickName, brickLife, row ,col);
        gameRoot.getChildren().add(brick.getImage());
        bricksOnScreen.add(brick);
    }

    private void handleKeyRelease(KeyCode code) {
        if (code == KeyCode.LEFT || code == KeyCode.RIGHT) {
            playerPaddle.setxSpeed(0);
        }
    }

    private void handleKeyInput(KeyCode code) {
        if (code == KeyCode.RIGHT) {
            playerPaddle.setxSpeed(1);
        }
        else if (code == KeyCode.LEFT) {
            playerPaddle.setxSpeed(-1);
        } else if (code == KeyCode.L) {
            this.currentLevel += 1;
            resetGame();
        }
    }
}
