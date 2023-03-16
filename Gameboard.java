

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class Gameboard extends Pane {
    private final ArrayList<Entity> wall = new ArrayList<>();
    private final ArrayList<Entity> coin = new ArrayList<>();
    private final ArrayList<Entity> ghost = new ArrayList<>();
    private final ArrayList<Entity> star = new ArrayList<>();
    Entity player;
    private final int PLAYER_SPEED = 50;
    int score = 0;
    int lives = 3;
    int stage = 1;
    int coinCounter;
    int starCounter; 
    AnimationTimer timer;
    Label scoreLabel = new Label("Score : " + score);
    Label lifeLabel = new Label("Lives : " + lives);
    Label stageLabel = new Label("Stage : " + stage);

    public Gameboard() {
       
        scoreLabel.setFont(new Font("Impact", 20));
        scoreLabel.setLayoutX(20);
        scoreLabel.setLayoutY(15);

        lifeLabel.setFont(new Font("Impact", 20));
        lifeLabel.setLayoutX(130);
        lifeLabel.setLayoutY(15);

        stageLabel.setFont(new Font("Impact", 20));
        stageLabel.setLayoutX(240);
        stageLabel.setLayoutY(15);

        newGame();
        
        timer = new AnimationTimer() {
            double frame = 0.05;

            @Override
            public void handle(long x) {
                if ((frame -= 0.1) < 0) {
                    frame = 0.5;

                    for (Entity ghosts : ghost) {
                        if (ghosts.collision(player)) {
                            if (--lives >= 1) {
                                timer.stop();
                                endGame();
                                timer.start();
                            } else {

                                score = 0;
                                stage = 1;
                                lives = 3;

                                endGame();
                                start();
                            }
                            stageLabel.setText("Stage : " + stage);
                            scoreLabel.setText("Score : " + score);
                            lifeLabel.setText("Lives : " + lives);
                        }
                    }
                    ghost.forEach(s -> s.ghostMove(s, wall));
                    start();
                }
            }
        };

        timer.start();
       
        setOnKeyPressed(e->{
            int speedX = 0;
            int speedY = 0;
            switch (e.getCode()) {
                case UP -> {
                    speedY = -PLAYER_SPEED;
                    player.setRotate(-90);
                }
                case DOWN -> {
                    speedY = PLAYER_SPEED;
                    player.setRotate(90);
                }
                case LEFT -> {
                    speedX = -PLAYER_SPEED;
                    player.setRotate(-180);
                }
                case RIGHT -> {
                    speedX = PLAYER_SPEED;
                    player.setRotate(0);
                }
            }
            player.playerMove(speedX, speedY);
            
            for (var s: wall) {
                if(s.collision(player))
                    player.playerMove(-speedX, -speedY);
            }
           
            for (Entity stars : star) {
                if (stars.starGet(player) && getChildren().contains(stars)) {
                    getChildren().remove(stars);

                    score += 100;
                    scoreLabel.setText("Score : " + score);
                    
                }
            }
           
            for (Entity coins : coin) {
                if (coins.coinGet(player) && getChildren().contains(coins)) {
                    getChildren().remove(coins);

                    score += 10;
                    scoreLabel.setText("Score : " + score);

                    coinCounter--;
                    if (coinCounter == 0) {
                        stage++;
                        stageLabel.setText("Level : " + stage);

                        endGame();
                    }
                }
            }
        });
        setFocusTraversable(true);
        setFocused(true);
    }
    
    private void newGame(){
        int[][] mapArray = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 5, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 3, 3, 3, 3, 3, 5, 1},
                {1, 3, 1, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 3, 1, 1, 3, 1},
                {1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1},
                {1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 3, 1, 3, 1, 1, 3, 1},
                {1, 3, 3, 3, 3, 1, 3, 3, 3, 1, 3, 3, 3, 1, 3, 3, 3, 3, 1},
                {1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1},
                {1, 1, 1, 1, 3, 1, 3, 3, 3, 3, 3, 3, 3, 1, 3, 1, 1, 1, 1},
                {1, 3, 3, 3, 3, 1, 3, 0, 0, 4, 0, 0, 3, 1, 3, 3, 3, 3, 1},
                {1, 5, 1, 1, 3, 3, 3, 4, 1, 1, 1, 4, 3, 3, 3, 1, 1, 5, 1},
                {1, 3, 3, 3, 3, 1, 3, 0, 0, 4, 0, 0, 3, 1, 3, 3, 3, 3, 1},
                {1, 1, 1, 1, 3, 1, 3, 3, 3, 3, 3, 3, 3, 1, 3, 1, 1, 1, 1},
                {1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1},
                {1, 3, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 3, 3, 3, 3, 3, 3, 1},
                {1, 3, 1, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 3, 1, 1, 3, 1},
                {1, 3, 3, 1, 3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 1, 3, 3, 1},
                {1, 1, 3, 1, 3, 1, 3, 1, 1, 1, 1, 1, 3, 1, 3, 1, 3, 1, 1},
                {1, 3, 3, 3, 3, 1, 3, 3, 3, 1, 3, 3, 3, 1, 3, 3, 3, 3, 1},
                {1, 5, 1, 1, 1, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 5, 1},
                {1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        };
        int ghostNum = 0;
        for (int i = 0; i < mapArray.length; i++) {
            for (int j = 0; j < mapArray[0].length; j++) {
                if(mapArray[i][j] == 1){
                    wall.add(new Entity("Wall.png", 50*j, 50*i, 50,50));
                } else if(mapArray[i][j] == 2){
                    player = new Entity("Pacman.png", 50*j, 50*i, 50,50);
                    getChildren().add(player);
                } else if(mapArray[i][j] == 3){
                    coin.add(new Entity("Coin.png", 50*j, 50*i, 50, 50));
                } else if(mapArray[i][j] == 4){
                    ghost.add(new Entity("Ghost" + (++ghostNum) + ".png", 50*j, 50*i, 50, 50));
                } else if (mapArray[i][j] == 5){
                    star.add(new Entity("Star.png", 50*j, 50*i, 50, 50));
                }
            }
        }
        for (Entity coins : coin) {
            getChildren().add(coins);
        }
        for (Entity walls : wall) {
            getChildren().add(walls);
        }
        for (Entity stars : star){
            getChildren().add(stars);
        }
        for (Entity ghosts : ghost){
            getChildren().add(ghosts);
        }
        getChildren().addAll(scoreLabel, lifeLabel, stageLabel);
        coinCounter = coin.size();
        starCounter = star.size(); 
    }
    
    private void endGame(){
        getChildren().clear();
        ghost.clear();
        coin.clear();
        wall.clear();
        star.clear();
        newGame();
    }
}
