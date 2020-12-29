/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.bombs.Bomb;
import fr.ubx.poo.model.go.monsters.Monster;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteBomb;
import fr.ubx.poo.view.sprite.SpriteFactory;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.sprite.SpriteGameObject;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final Monster monster;
    private List<Bomb> bombs = new ArrayList<>();
    private final List<Sprite> sprites = new ArrayList<>();
    //private List<Monster> monsters = new ArrayList<>();//НЕ уверен
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;
    private Sprite spriteMonster;
    private List<SpriteBomb> spritesBomb = new ArrayList<>();;

    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        this.monster = game.getMonster();
        this.bombs = game.getBombs();
        initialize(stage, game);
        buildAndSetGameLoop();
    }

    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();

        int height = game.getWorld().dimension.height;
        int width = game.getWorld().dimension.width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);
        // Create decor sprites
        game.getWorld().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        spritePlayer = SpriteFactory.createPlayer(layer, player);
        spriteMonster = SpriteFactory.createMonster(layer, monster);

        bombs = game.getBombs();

    }

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);
                // Do actions
                update(now);
                // Graphic update
                render();
                statusBar.update(game);
            }
        };
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        if (input.isMoveDown()) {
            player.requestMove(Direction.S);
        }
        if (input.isMoveLeft()) {
            player.requestMove(Direction.W);
        }
        if (input.isMoveRight()) {
            player.requestMove(Direction.E);
        }
        if (input.isMoveUp()) {
            player.requestMove(Direction.N);
        }
        if (input.isBomb()) {
            player.placeBomb();
        }
        if (input.isKey()) {
            player.openDoor();
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }


    private void update(long now) {

        player.update(now);

        for(int i = 0; i < bombs.size(); i++) {
            if(bombs.get(i).getIsNew() == true){
                bombs.get(i).setTime(now);
                bombs.get(i).setIsNew(false);
            }
            else{


                if(!bombs.get(i).update(now) || bombs.get(i).getLives() == 0){
                    bombs.remove(i);
                    spritesBomb.get(i).remove();
                    spritesBomb.remove(i);
                    player.changeBombNumber(1);
                }
            }
        }


        if(player.getLives() <= 0)
            player.setAliveFalse();

        if (player.isAlive() == false) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }
    }

    private void render() {
        if(game.getWorld().getChangeMap()){
            //System.out.println(sprites);
            sprites.forEach(Sprite::remove);
            sprites.removeAll(sprites);
            game.getWorld().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
            //System.out.println(sprites);
            game.getWorld().setChangeMap(false);
        }

        if(!bombs.isEmpty()) {
            if(spritesBomb.isEmpty()) {
                spritesBomb.add((SpriteBomb) SpriteFactory.createBomb(layer, bombs.get(0)));
                spritesBomb.get(0).render();
            }
            else {
                if(bombs.size() > spritesBomb.size()) {
                    spritesBomb.add((SpriteBomb) SpriteFactory.createBomb(layer, bombs.get(spritesBomb.size())));
                    spritesBomb.get(spritesBomb.size()-1).render();
                }
                else {
                    for (int i = 0; i < spritesBomb.size(); i++) {
                        spritesBomb.get(i).render();
                    }
                }
            }
        }
        /*if(!bombs.isEmpty()) {
            for(int i = 0; i < bombs.size(); i++) {
                if(bombs.get(i) != null){
                    if(spritesBomb.isEmpty()) {
                        spritesBomb.add((SpriteBomb) SpriteFactory.createBomb(layer, bombs.get(i)));
                    }
                    else if(spritesBomb.get(i) != null) {
                        spritesBomb.get(i).render();
                    }
                    else {
                        spritesBomb.add((SpriteBomb) SpriteFactory.createBomb(layer, bombs.get(i)));
                    }
                }
                else {
                    spritesBomb.get(i).remove();
                    spritesBomb.remove(i);
                }
            }
        }
        else {
            if(!spritesBomb.isEmpty()){
                spritesBomb.get(0).remove();
                spritesBomb.remove(0);
            }
        }*/



        sprites.forEach(Sprite::render);
        // last rendering to have player in the foreground
        spritePlayer.render();
        spriteMonster.render();

    }


    public void start() {
        gameLoop.start();
    }

}
