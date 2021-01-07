/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.DoorNextOpened;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.bombs.Bomb;
import fr.ubx.poo.model.go.monsters.Monster;

import java.util.List;


public class Player extends GameObject implements Movable {

    private boolean alive;
    private boolean canBeDamaged;
    Direction direction;
    private boolean moveRequested = false;
    private int nLives;
    private int nBombs;
    private int range;
    private int keys;
    private boolean winner;
    private List<Monster> monsters;
    private List<Bomb> bombs;

    public Player(Game game, Position position, int level) {
        super(game, position, 1, level);
        monsters = game.getMonsters();
        bombs = game.getBombs();
        this.direction = Direction.S;
        this.nLives = game.getInitPlayerLives();
        alive = true;
        canBeDamaged = true;
        nBombs = 1;
        range = 1;
        keys = 0;
    }


    public int getNLives() {
        return nLives;
    }

    public int getNBombs() {
        return nBombs;
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

    public int getNKeys() {
        return keys;
    }

    public void setWinnerTrue() {
        this.winner = true;
    }

    public void setAliveFalse() {
        this.alive = false;
    }

    public void changeNBombs(int n) {
        nBombs += n;
        if(nBombs < 0)
            nBombs = 0;
    }

    public void changeRange(int n) {
        this.range += n;
        if(this.range < 1)
            this.range = 1;
    }

    public void increaseKey() {
        this.keys++;
    }

    public void changeNLives(int n) {
        if(n < 0)
            setCanBeDamaged(false);
        this.nLives += n;
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

        //if(game.getWorld(game.getNNowLevel()).isEmpty(getPosition()) && nBombs > 0) {
        if(nBombs > 0) {
            boolean can = true;

            for(Bomb b : bombs) {
                if(b.getPosition().equals(getPosition())){
                    can = false;
                }
            }

            if(can) {
                game.setBomb(new Bomb(game, getPosition(), range, getLevel()));
                changeNBombs(-1);
            }
        }

    }

    public void openDoor() {

        Position nextPos = direction.nextPosition(getPosition());
        Decor decor = game.getWorld(game.getNNowLevel()).get(nextPos);

        if(!game.getWorld(game.getNNowLevel()).isEmpty(nextPos)) {
            if(decor.getWayNextLevel() && decor.getIsNext() == 0 && keys > 0){
                keys--;
                game.getWorld(game.getNNowLevel()).set(nextPos, new DoorNextOpened());
                game.getWorld(game.getNNowLevel()).setChangeMap(true);
            }
        }

    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        if(nextPos.inside(game.getWorld(game.getNNowLevel()).dimension)) {
            Decor decor = game.getWorld(game.getNNowLevel()).get(nextPos);

            if(decor == null) {
                return true;
            }
            else if(decor.getPassability()){
                if(decor.getCollectables()) {
                    decor.take(this);
                    game.getWorld(game.getNNowLevel()).clear(nextPos);
                    game.getWorld(game.getNNowLevel()).setChangeMap(true);

                }
                else if(decor.getWayNextLevel()) {
                    if(decor.getIsNext() == 1) {
                        game.incDecNNowLevel(1);
                        incDecNowLevel(1);
                        game.setIsNewLevel(true, true);
                    }
                    else if(decor.getIsNext() == -1){
                        game.incDecNNowLevel(-1);
                        incDecNowLevel(-1);
                        game.setIsNewLevel(true, false);
                    }
                }
                return true;
            }
            else if(decor.getMovability()) {
                Position nextNextPos = direction.nextPosition(nextPos);
                boolean canMoveBox = true;

                if(game.getWorld(game.getNNowLevel()).isEmpty(nextNextPos) && nextNextPos.inside(game.getWorld(game.getNNowLevel()).dimension)){
                    for(Monster m : monsters) {
                        if (m.getLevel() == getLevel()) {
                            if (m.getPosition().equals(nextNextPos))
                                canMoveBox = false;
                        }
                    }
                    if(canMoveBox) {
                        game.getWorld(game.getNNowLevel()).set(nextNextPos, new Box());
                        game.getWorld(game.getNNowLevel()).clear(nextPos);
                        game.getWorld(game.getNNowLevel()).setChangeMap(true);
                        return true;
                    }
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
                    setTime(now);
                    setCl(getCl()-1);
                }
            }
            else {
                setCl(1);
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

    public String toString() {
        return "Player, position: " + getPosition() + ", cl: " + getCl() + ", alive: " + isAlive()
                + ", level: " + getLevel() + ", nLives: " + getNLives() + ", direction: " + getDirection() +
                ", nBombs: " + getNBombs() + ", range: " + getRange();
    }

}
