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
import java.util.Iterator;
import java.util.List;

import static fr.ubx.poo.game.Direction.S;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final List<Monster> monsters;
    private final List<Bomb> bombs;
    private final List<Sprite> sprites = new ArrayList<>();
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;
    private final List<SpriteMonster> spritesMonsters = new ArrayList<>();
    private final List<SpriteBomb> spritesBombs = new ArrayList<>();
    /** Field of current level number   */
    private int nowLevel;
    /** Transition states between levels
     * {false, false} - no transition
     * {true, true} - transition to the next level
     * {true, false} - transition to the previous level
     */
    private boolean[] isNewLevel;

    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        this.monsters = game.getMonsters();
        this.bombs = game.getBombs();
        nowLevel = 0;
        isNewLevel = new boolean[]{false, false};
        initialize(stage, game);
        buildAndSetGameLoop();
    }

    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();

        int height = game.getWorld(nowLevel).dimension.height;
        int width = game.getWorld(nowLevel).dimension.width;

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
        game.getWorld(nowLevel).forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        spritePlayer = SpriteFactory.createPlayer(layer, player);

        //Used on first initialization
        if(spritesMonsters.size() == 0) {
            for(Monster m : monsters) {
                spritesMonsters.add((SpriteMonster) SpriteFactory.createMonster(layer, m));
            }
        }

        //Used when changing levels
        if(nowLevel == player.getLevel()) {
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

        player.update(now);

        if(player.getNLives() <= 0)
            player.setAliveFalse();

        if (!player.isAlive()) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }

        //Determination of the player's transition to the next level or previous.
        if(nowLevel > player.getLevel()) {
            isNewLevel[0] = true;
            isNewLevel[1] = false;
            nowLevel = player.getLevel();
        }
        else if (nowLevel < player.getLevel()){
            isNewLevel[0] = true;
            isNewLevel[1] = true;
            nowLevel = player.getLevel();
        }

        if(isNewLevel[0]){
            updateLevel();
        }
        updateBombs(now);
        updateMonsters(now);
    }

    private void render() {

        if(game.getWorld(player.getLevel()).getChangeMap()){
            updateDecorSprites();
        }

        if(!bombs.isEmpty()) {
            bombSpritesRender();
        }

        sprites.forEach(Sprite::render);
        // last rendering to have player in the foreground
        spritePlayer.render();

        for (int i = 0; i < spritesMonsters.size(); i++) {
            //If the player and the monster are not on the same level, then the sprite is not processed.
            if(monsters.get(i).getLevel() == player.getLevel()) {
                spritesMonsters.get(i).render();
            }
        }
    }

    public void start() {
        gameLoop.start();
    }


    /**Method for updating decor sprites in case of their change
     */
    public void updateDecorSprites() {
        sprites.forEach(Sprite::remove);
        sprites.removeAll(sprites);
        game.getWorld(player.getLevel()).forEach( (pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        game.getWorld(player.getLevel()).setChangeMap(false);
    }

    /**Method is applied to bomb sprites if they are updated or removed when the bomb is destroyed
     */
    public void bombSpritesRender() {
        //If the bomb was planted first
        if(spritesBombs.isEmpty()) {
            spritesBombs.add((SpriteBomb) SpriteFactory.createBomb(layer, bombs.get(0)));
            spritesBombs.get(0).render();
        }
        else {
            //If new bombs were planted
            if(bombs.size() > spritesBombs.size()) {
                spritesBombs.add((SpriteBomb) SpriteFactory.createBomb(layer, bombs.get(spritesBombs.size())));
                //If the player and the bomb are not on the same level, then the sprite is not processed.
                if(nowLevel == player.getLevel() && !game.getBombs().isEmpty()) {
                    spritesBombs.get(spritesBombs.size()-1).render();
                }
            }
            else {
                //Processing sprites of existing bombs
                for (SpriteBomb spB : spritesBombs) {
                    if(nowLevel == player.getLevel() && !game.getBombs().isEmpty()) {
                        spB.render();
                    }
                }
            }
        }
    }

    /**Method is used to update levels
     */
    public void updateLevel() {
        stage.close();

        initialize(stage, game);
        Position p;
        //Setting the coordinates of the player relative to the new level
        if(isNewLevel[1]) {
            p = game.getWorld(nowLevel).findDoorPrevOpened();
        }
        else {
            p = game.getWorld(nowLevel).findDoorNextOpened();
        }
        player.setPosition(p);
        player.setDirection(S);
        isNewLevel[0] = false;
    }

    /**Method is used to update bombs or delete their instances or sprites
     * @param now current game time
     */
    public void updateBombs(long now) {
        Iterator<Bomb> iteratorterBombs = bombs.iterator();
        Iterator<SpriteBomb> iteratorSpritesBombs = spritesBombs.iterator();

        while(iteratorterBombs.hasNext()) {
            Bomb bomb = iteratorterBombs.next();
            //If the player has just planted a bomb
            if(bomb.getIsNew()){
                bomb.setTime(now);
                bomb.setIsNew(false);
            }
            else{
                if(!bomb.update(now) || bomb.getNLives() < 1){
                    iteratorterBombs.remove();
                    if(iteratorSpritesBombs.hasNext()) {
                        SpriteBomb spriteBomb = iteratorSpritesBombs.next();
                        spriteBomb.remove();
                        iteratorSpritesBombs.remove();
                        player.changeNBombs(1);
                    }
                }
            }
        }
    }

    /**Method is used to update monsters or delete their instances or sprites
     * @param now current game time
     */
    public void updateMonsters(long now) {
        Iterator<Monster> iteratorterMonsters = monsters.iterator();
        Iterator<SpriteMonster> iteratorSpritesMonsters = spritesMonsters.iterator();

        while(iteratorterMonsters.hasNext()) {
            Monster monster = iteratorterMonsters.next();
            SpriteMonster spriteMonster = iteratorSpritesMonsters.next();
            monster.update(now);

            if(monster.getNLives() < 1) {
                iteratorterMonsters.remove();
                spriteMonster.remove();
                iteratorSpritesMonsters.remove();
            }
        }
    }
}
