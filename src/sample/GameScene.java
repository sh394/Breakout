package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
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
import javafx.util.converter.FloatStringConverter;

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

    public static final String SIZE_POWER = "sizepower.gif";
    public static final String POINTS_POWER = "pointspower.gif";
    public static final String LASER_POWER = "laserpower.gif";
    public static final String LASER_BEAM = "laser.png";


    private Group gameRoot;
    private Scene gameScene;
    private Stage gameStage;
    private Stage mainStage;

    private Rectangle scoreBoard;

    private  Label scoreLabel;
    private  Label lifeLabel;
    private  Label levelLabel;
    private  Label gameEndLabel;
    private Font font;
    private int score = 0;
    private int currentLevel = 1;
    private int life = 3;
    private int randomItem;
    private int recentlyHit;

    private Paddle playerPaddle;
    private Paddle computerPaddle;
    private Ball ball;
    private Brick brick;

    private PowerUp sizePower;
    private PowerUp pointsPower;
    private PowerUp laserPower;
    private PowerUp laserBeam;

    private Boolean isLoaded = false;
    private Boolean vsModeOn = false;
    private Boolean teleport = false;


    private Scanner sc;
    private File file;

    private List<Label> labels;
    private List<Brick> bricksOnScreen;
    private List<Sprite> toBeRemoved;

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
        gameStage.setTitle(GAME_TITLE);
        gameStage.setScene(gameScene);

        ball = new Ball();
        playerPaddle = new Paddle();
        computerPaddle = new Paddle();
        getBrickInput(currentLevel);

        sizePower = new PowerUp(SIZE_POWER);
        laserPower = new PowerUp(LASER_POWER);
        pointsPower = new PowerUp(POINTS_POWER);
        laserBeam = new PowerUp(LASER_BEAM);

        labels = new ArrayList<>();
        createScoreBoard();
        createLabels();

        toBeRemoved = new CopyOnWriteArrayList<>();
        gameRoot.getChildren().add(playerPaddle.getImage());
        gameRoot.getChildren().add(ball.getImage());

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
        playerPaddle.move();
        sizePower.move(elapsedTime);
        pointsPower.move(elapsedTime);
        laserPower.move(elapsedTime);
        laserBeam.move(elapsedTime);

        checkBall(elapsedTime);
        checkPaddle(elapsedTime);

        updateLabels();
        paddleAbility();
        checkGameFlow();
    }

    private void checkGameFlow() {
        if(bricksOnScreen.size() == 0 && currentLevel != 3) {
            this.currentLevel += 1;
            resetGame();
        } else if(bricksOnScreen.size() == 0 && currentLevel == 3) {
            winScreen();
        } else if(this.life == 0 || this.score < 0) {
            loseScreen();
        } else if(currentLevel == 3) {
            vsMode();
        }
        removeSprites();
    }

    private void createScoreBoard() {
        scoreBoard = new Rectangle(0, 0, GAME_SCENE_WIDTH, GAME_SCENE_HEIGHT / 10);
        scoreBoard.setFill(Color.BLUE);
        gameRoot.getChildren().add(scoreBoard);
    }

    private void updateLabels() {
        lifeLabel.setText("Life: " + life);
        scoreLabel.setText("Score: " + score);
        levelLabel.setText("Level: " + currentLevel);
    }

    private void createLabels() {
        scoreLabel();
        lifeLabel();
        levelLabel();
    }

    private void addLabels(Label label) {
        font = new Font(30);
        label.setLayoutX(20 + labels.size()*200);
        label.setLayoutY(20);
        label.setTextFill(Color.WHITE);
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

    private void winScreen() {
        createEndScreen("YOU WON", Color.BLUE);
    }

    private void loseScreen() {
        createEndScreen("YOU LOST", Color.RED);
    }

    private void createEndScreen(String text, Color color) {
        font = new Font(50);
        gameRoot.getChildren().clear();
        gameEndLabel = new Label(text);
        gameEndLabel.setFont(font);
        gameEndLabel.setTextFill(color);
        gameEndLabel.setLayoutX(200);
        gameEndLabel.setLayoutY(200);
        gameRoot.getChildren().add(gameEndLabel);
    }

    private void paddleAbility() {
        if(this.life == 1) {
            playerPaddle.setWidth(300);
        }
        if(this.score == 100) {
            playerPaddle.setWidth(70);
        }
        if(this.score == 300) {
            isLoaded = true;
        }
    }

    private void paddleTeleport(Paddle paddle) {
        if(paddle.getX() < 0 - paddle.getWidth()) { paddle.setX(GAME_SCENE_WIDTH);}
        else if (paddle.getX() > GAME_SCENE_WIDTH) {
           paddle.setX(0 - paddle.getWidth());
        }
    }

    private void paddleOffBoundary(Paddle paddle) {
        if(paddle.getX() >= GAME_SCENE_WIDTH - paddle.getWidth()) {
            paddle.setX(GAME_SCENE_WIDTH - paddle.getWidth());
        }
    }

    private void checkPaddle(double elapsedTime) {
        //keep the paddle in the boundary
        if(!teleport) {
            paddleOffBoundary(playerPaddle);
        } else {
            paddleTeleport(playerPaddle);
        }

        if(playerPaddle.intersects(sizePower)) {
            playerPaddle.setWidth(200);
            toBeRemoved.add(sizePower);
        }

        if(playerPaddle.intersects(pointsPower)) {
            this.score += 50;
            toBeRemoved.add(pointsPower);
        }

        if(playerPaddle.intersects(laserPower)) {
            isLoaded = true;
            toBeRemoved.add(laserPower);
        }
    }

    private void checkBall(double elapsedTime) {
        //collision with bricks
        for(Brick brickHit : bricksOnScreen) {
            if(brickHit.intersects(ball) && brickHit.bottom((ball))) {
                ball.setySpeed(1);
                battleScore();
                rePaintBrick(brickHit);
            } else if(brickHit.intersects(ball) && brickHit.top((ball))) {
                ball.setySpeed(-1);
                battleScore();
                rePaintBrick(brickHit);
            } else if(brickHit.intersects(ball) && brickHit.left((ball))) {
                ball.setxSpeed(-1);
                battleScore();
                rePaintBrick(brickHit);
            } else if(brickHit.intersects(ball) && brickHit.right((ball))) {
                ball.setxSpeed(1);
                battleScore();
                rePaintBrick(brickHit);
            } else if(brickHit.intersects(laserBeam)) {
                laserBeam.setItemSpeed(1);
                toBeRemoved.add(laserBeam);
                rePaintBrick(brickHit);
            }
        }

        //check collision with the paddle
        if(ball.intersects(playerPaddle)) {
            ball.setySpeed(-1);
            recentlyHit = 1;
        }

        //check collision with walls
        if(ball.getImage().getX() >= GAME_SCENE_WIDTH - ball.getWidth()) {ball.setxSpeed(-1);}
        else if(ball.getImage().getX() <= 0) { ball.setxSpeed(1);}
        else if(ball.getImage().getY() <= scoreBoard.getBoundsInLocal().getMaxY()) {ball.setySpeed(1);}

        ///bottom edge
        if(ball.getImage().getY() >= GAME_SCENE_HEIGHT) {
            this.life -= 1;
            ball.resetBall();
            playerPaddle.resetPaddle();
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
            generateItem(brick);
        }
    }


    private void battleScore() {
        if(recentlyHit == 2) {
            this.score -= 10;
        } else {
            this.score += 10;
        }
    }

    private void getBrickInput(int currentLevel)  {
        List<String[]> bricks = new ArrayList<>();
        if(currentLevel == 1) {
            file = new File("resource/level1.txt");
        } else if (currentLevel == 2) {
            file = new File("resource/level2.txt");
        } else if (currentLevel == 3) {
            file = new File("resource/level3.txt");
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

    private void drawBricks(String brickName, int brickLife, int row, int col) {
        brick = new Brick(brickName, brickLife, row ,col);
        gameRoot.getChildren().add(brick.getImage());
        bricksOnScreen.add(brick);
    }

    private void addItems(PowerUp item, Brick brick) {
        item.setX(brick.getX() + brick.getWidth() / 2);
        item.setY(brick.getY() + brick.getHeight() / 2);
        gameRoot.getChildren().add(item.getImage());
    }

    private void generateItem(Brick brick) {
        randomItem = (int) (Math.random()*20) + 1;
        System.out.println(randomItem);
        if(randomItem == 1) {
            sizePower = new PowerUp(SIZE_POWER);
            addItems(sizePower, brick);
        }

        if(randomItem == 2) {
            pointsPower = new PowerUp(POINTS_POWER);
           addItems(pointsPower, brick);
        }

        if(randomItem == 3) {
            laserPower = new PowerUp(LASER_POWER);
            addItems(laserPower, brick);
        }
    }

    private void laserShoot() {
        laserBeam = new PowerUp("laser.png");
        laserBeam.setX(playerPaddle.getX() + playerPaddle.getWidth() / 2);
        laserBeam.setY(playerPaddle.getY() + playerPaddle.getHeight() / 2);
        laserBeam.setItemSpeed(-1);
        gameRoot.getChildren().add(laserBeam.getImage());
        isLoaded = false;
    }

    private void vsMode() {
        if(!vsModeOn) {
            computerPaddle = new Paddle();
            computerPaddle.setY(100);
            gameRoot.getChildren().add(computerPaddle.getImage());
            vsModeOn = true;
        }
        computerPaddle.setX(ball.getX());
        paddleOffBoundary(computerPaddle);
        if(ball.intersects(computerPaddle)) {
            ball.setySpeed(1);
            this.recentlyHit = 2;
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

    private void removeSprites() {
        for(Sprite removeBrick: toBeRemoved) {
            gameRoot.getChildren().remove(removeBrick.getImage());
        }
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
            this.life += 1;
        } else if (code == KeyCode.DIGIT1) {
            this.currentLevel = 1;
            resetGame();
        } else if (code == KeyCode.DIGIT2) {
            this.currentLevel = 2;
            resetGame();
        } else if (code == KeyCode.DIGIT3) {
            this.currentLevel = 3;
            resetGame();
        } else if (code == KeyCode.SPACE && isLoaded) {
            laserShoot();
        } else if (code == KeyCode.Q) {
            score += 100;
        } else if (code == KeyCode.T) {
            this.teleport = !teleport;
        }
    }
}
