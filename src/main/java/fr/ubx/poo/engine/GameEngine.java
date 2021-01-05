/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.bombs.Bomb;
import fr.ubx.poo.model.go.monsters.Monster;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteBomb;
import fr.ubx.poo.view.sprite.SpriteFactory;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.sprite.SpriteMonster;
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

import static fr.ubx.poo.game.Direction.S;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private Player player;
    private List<Monster> monsters = new ArrayList<>();
    private List<Bomb> bombs = new ArrayList<>();
    private final List<Sprite> sprites = new ArrayList<>();
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;
    private List<SpriteMonster> spritesMonsters = new ArrayList<>();
    private List<SpriteBomb> spritesBombs = new ArrayList<>();

    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        this.monsters = game.getMonsters();
        this.bombs = game.getBombs();
        initialize(stage, game, 0);
        buildAndSetGameLoop();
    }

    private void initialize(Stage stage, Game game, int level) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();

        int height = game.getWorld(level).dimension.height;
        int width = game.getWorld(level).dimension.width;
        //System.out.println(height + " " + width);

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
        game.getWorld(level).forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        spritePlayer = SpriteFactory.createPlayer(layer, player);
       // System.out.println("level: " + level + " player: " + player.getLevel());

        if(spritesMonsters.size() == 0) {
            for(int i = 0; i < monsters.size(); i++) {
                spritesMonsters.add((SpriteMonster) SpriteFactory.createMonster(layer, monsters.get(i)));
            }
        }


        if(level == player.getLevel()) {

            for(int i = 0; i < monsters.size(); i++) {
                if(monsters.get(i).getLevel() == player.getLevel()) {
                    spritesMonsters.set(i, (SpriteMonster) SpriteFactory.createMonster(layer, monsters.get(i)));
                }
            }
            for(int i = 0; i < bombs.size(); i++) {
                if(bombs.get(i).getLevel() == player.getLevel()) {
                    spritesBombs.set(i, (SpriteBomb) SpriteFactory.createBomb(layer, bombs.get(i)));
                }
            }

        }

        //spriteMonster = SpriteFactory.createMonster(layer, monster);
        //if(level == 0 && !game.getBombs().isEmpty())
           // spritesBombs.set(0, (SpriteBomb) SpriteFactory.createBomb(layer, bombs.get(0)));


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
            player.requestMove(S);
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

        if(game.getIsNewLevel()[0]){
            stage.close();
            //System.out.println(game.getNLevel());
            //spritesMonsters.clear();
            //spritesBombs.clear();
            initialize(stage, game, game.getNNowLevel());
            Position p;
            if(game.getIsNewLevel()[1]) {
                p = game.getWorld(game.getNNowLevel()).findDoorPrevOpened();
            }
            else {
                p = game.getWorld(game.getNNowLevel()).findDoorNextOpened();
            }
            player.setPosition(p);
            player.setDirection(S);
            game.setIsNewLevel(false, true);
            //System.out.println("Bombs: " + bombs.isEmpty());
            //System.out.println("Sprites: " + spritesBombs.isEmpty());
            //spritesBombs.get(0).render();
        }

        player.update(now);

        for(int i = 0; i < bombs.size(); i++) {
            if(bombs.get(i).getIsNew() == true){
                bombs.get(i).setTime(now);
                bombs.get(i).setIsNew(false);
            }
            else{


                if(!bombs.get(i).update(now) || bombs.get(i).getLives() == 0){
                    bombs.remove(i);
                    spritesBombs.get(i).remove();
                    spritesBombs.remove(i);
                    player.changeBombNumber(1);
                }
            }
        }

        for(int i = 0; i < monsters.size(); i++) {
            monsters.get(i).update(now);
            if(monsters.get(i).getLives() < 1){
                System.out.println(monsters.get(i).getLives() + " " + monsters.get(i).getLevel() + " " + monsters.get(i).getPosition());
                monsters.remove(i);
                spritesMonsters.get(i).remove();
                spritesMonsters.remove(i);
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
        if(game.getWorld(game.getNNowLevel()).getChangeMap()){
            //System.out.println(sprites);
            sprites.forEach(Sprite::remove);
            sprites.removeAll(sprites);
            game.getWorld(game.getNNowLevel()).forEach( (pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
            //System.out.println(sprites);
            game.getWorld(game.getNNowLevel()).setChangeMap(false);
        }

        if(!bombs.isEmpty()) {
            if(spritesBombs.isEmpty()) {
                spritesBombs.add((SpriteBomb) SpriteFactory.createBomb(layer, bombs.get(0)));
                spritesBombs.get(0).render();

            }
            else {
                if(bombs.size() > spritesBombs.size()) {
                    spritesBombs.add((SpriteBomb) SpriteFactory.createBomb(layer, bombs.get(spritesBombs.size())));
                    if(game.getNNowLevel() == player.getLevel() && !game.getBombs().isEmpty())
                        spritesBombs.get(spritesBombs.size()-1).render();
                }
                else {
                    for (int i = 0; i < spritesBombs.size(); i++) {
                        if(game.getNNowLevel() == player.getLevel() && !game.getBombs().isEmpty())
                            spritesBombs.get(i).render();
                        //System.out.println(bombs.get(0).getPosition());
                    }
                }
            }
        }
        /*if(!bombs.isEmpty()) {
            for(int i = 0; i < bombs.size(); i++) {
                if(bombs.get(i) != null){
                    if(spritesBombs.isEmpty()) {
                        spritesBombs.add((SpriteBomb) SpriteFactory.createBomb(layer, bombs.get(i)));
                    }
                    else if(spritesBombs.get(i) != null) {
                        spritesBombs.get(i).render();
                    }
                    else {
                        spritesBombs.add((SpriteBomb) SpriteFactory.createBomb(layer, bombs.get(i)));
                    }
                }
                else {
                    spritesBombs.get(i).remove();
                    spritesBombs.remove(i);
                }
            }
        }
        else {
            if(!spritesBombs.isEmpty()){
                spritesBombs.get(0).remove();
                spritesBombs.remove(0);
            }
        }*/



        sprites.forEach(Sprite::render);
        // last rendering to have player in the foreground
        spritePlayer.render();

        for (int i = 0; i < spritesMonsters.size(); i++) {
            if(monsters.get(i).getLevel() == player.getLevel()) {
                spritesMonsters.get(i).render();
            }

        }
        //spriteMonster.render();

    }


    public void start() {
        gameLoop.start();
    }

}
