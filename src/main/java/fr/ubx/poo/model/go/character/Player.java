/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.DoorNextClosed;
import fr.ubx.poo.model.decor.DoorNextOpened;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.bombs.Bomb;

public class Player extends GameObject implements Movable {

    private boolean alive = true;
    private boolean canBeDamaged = true;
    Direction direction;
    private boolean moveRequested = false;
    private int level;
    private int lives = 3;
    private int bombs = 10;
    private int range = 1;
    private int keys = 1;
    private boolean canGoToNewLevel = true;
    private boolean winner;

    public Player(Game game, Position position, int level) {
        super(game, position, 3);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public int getLives() {
        return lives;
    }

    public int getBombs() {
        return bombs;
    }

    public int getRange() {
        return range;
    }

    public boolean getCanBeDamaged() {
        return canBeDamaged;
    }

    public void setCanBeDamaged(boolean canBeDamaged) {
        this.canBeDamaged = canBeDamaged;
    }

    public int getKeys() {
        return keys;
    }

    public void setWinnerTrue() {
        this.winner = true;
    }

    public void setAliveFalse() {
        this.alive = false;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void incDecNowLevel(int d) {
        if(d < 0) {
            if(level > 0)
                level+=d;
        }
        else if(level < game.initLevels) {
            level+=d;
        }
    }

    public void changeBombNumber(int n) {
        this.bombs += n;
        if(this.bombs < 0)
            this.bombs = 0;
    }

    public void changeRange(int n) {
        this.range += n;
        if(this.range < 1)
            this.range = 1;
    }

    public void increaseKey() {
        this.keys++;
    }

    public void changeLives(int n) {
        if(n < 0)
            setCanBeDamaged(false);
        this.lives += n;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    public void placeBomb() {
        //Position nextPos = direction.nextPosition(getPosition());

        if(game.getWorld(game.getNNowLevel()).isEmpty(getPosition()) && bombs > 0) {
            //System.out.println("game.getWorld()");
            //game.getWorld().setBombs(nextPos);
            Bomb bomb = new Bomb(game, getPosition(), range, level);
            game.setBomb(bomb);
            bomb = null;
            changeBombNumber(-1);
            //game.getWorld().setChangeMap(true);

        }
    }

    public void openDoor() {
        Position nextPos = direction.nextPosition(getPosition());

        if(!game.getWorld(game.getNNowLevel()).isEmpty(nextPos)) {
            //System.out.println("game.getWorld().get(nextPos)" + game.getWorld().get(nextPos));
            //System.out.println(game.getWorld().get(nextPos).equals(new DoorNextClosed()));
            if(game.getWorld(game.getNNowLevel()).get(nextPos).equals(new DoorNextClosed()) && keys > 0){
                //System.out.println("!");
                keys--;
                game.getWorld(game.getNNowLevel()).set(nextPos, new DoorNextOpened());
                game.getWorld(game.getNNowLevel()).setChangeMap(true);
            }
        }

    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        //System.out.println(game.getNLevel());
        //System.out.println(game.getWorld().getRaw(nextPos));

        if(nextPos.inside(game.getWorld(game.getNNowLevel()).dimension)) {
            Decor decor = game.getWorld(game.getNNowLevel()).get(nextPos);
            //System.out.println(direction + " " + nextPos + " " + decor);
            if(decor == null) {
                return true;
            }
            else if(decor.getPassability()){
                if(decor.getCollectables()) {
                    decor.take(this);
                    //System.out.println(game.getWorld().values());
                    game.getWorld(game.getNNowLevel()).clear(nextPos);
                    game.getWorld(game.getNNowLevel()).setChangeMap(true);

                }
                else if(decor.getWayNextLevel()) {
                    if(decor instanceof DoorNextOpened) {
                        game.incDecNNowLevel(1);
                        incDecNowLevel(1);
                        game.setIsNewLevel(true, true);
                        //System.out.println(game.getNLevel() + "t");
                    }
                    else {
                        game.incDecNNowLevel(-1);
                        incDecNowLevel(-1);
                        game.setIsNewLevel(true, false);
                        //System.out.println(game.getNLevel() + "f");
                    }
                }
                return true;
            }
            else if(decor.getMovability()) {
                Position nextNextPos = direction.nextPosition(nextPos);
                if(game.getWorld(game.getNNowLevel()).isEmpty(nextNextPos) && nextNextPos.inside(game.getWorld(game.getNNowLevel()).dimension)){
                    //System.out.println("Box");
                    game.getWorld(game.getNNowLevel()).set(nextNextPos, new Box());
                    game.getWorld(game.getNNowLevel()).clear(nextPos);
                    game.getWorld(game.getNNowLevel()).setChangeMap(true);
                    return true;
                }
            }
        }
        return false;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void update(long now) {

        if(!getCanBeDamaged()) {
            if(getCl() > -1) {
                if(now - getTime() >= 1000000000) {
                    //System.out.println(now - time);
                    setTime(now);
                    //System.out.println(getCl());
                    setCl(getCl()-1);;
                }
            }
            else {
                setCl(3);
                setCanBeDamaged(true);
            }
        }


        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }

        moveRequested = false;
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        return alive;
    }

}
