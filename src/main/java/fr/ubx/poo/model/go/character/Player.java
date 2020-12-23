/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;

import static fr.ubx.poo.game.Direction.N;

public class Player extends GameObject implements Movable {

    private final boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives;
    private int bombs = 0;
    private int range = 1;
    private int keys = 0;
    private boolean winner;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
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

    public int getKeys() {
        return keys;
    }

    public void increaseBombNumber(int n) {
        this.bombs += n;
        if(this.bombs < 0)
            this.bombs = 0;
    }

    public void increaseRange(int n) {
        this.range += n;
        if(this.range < 1)
            this.range = 1;
    }

    public void increaseKey() {
        this.keys++;
    }

    public void increaseLives() {
        this.lives++;
    }

    public Direction getDirection() {
        return direction;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        //System.out.println(direction);
        //System.out.println(nextPos);

        if(nextPos.inside(game.getWorld().dimension)) {
            Decor decor = game.getWorld().get(nextPos);
            if(decor == null) {
                return true;
            }
            else if(decor.getPassability()){
                if(decor.getCollectables()) {
                    decor.take(this);
                    //System.out.println(game.getWorld().values());
                    //game.getWorld().set(nextPos, new Box());
                    game.getWorld().clear(nextPos);
                    //System.out.println(game.getWorld().values());
                    game.setChangeMap(true);

                }
                return true;
            }
            else if(decor.getMovability()) {
                Position nextNextPos = direction.nextPosition(nextPos);
                if(game.getWorld().isEmpty(nextNextPos)){
                    System.out.println("Box");
                    game.getWorld().clear(nextPos);
                    game.getWorld().set(nextNextPos, new Box());
                    game.setChangeMap(true);
                    return true;
                }
            }
        }

        /*if(game.getWorld().isEmpty(nextPos))
            return nextPos.inside(game.getWorld().dimension);
        else if(game.getWorld().get(nextPos).getPassability())
            return true;
        else if(game.getWorld().get(nextPos).getMovability()) {
            Position nextNextPos = direction.nextPosition(nextPos);
            if(game.getWorld().isEmpty(nextNextPos)){
                System.out.println("Box");
                game.getWorld().isInside();
                return true;
            }

        }*/

        return false;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void update(long now) {
        for(int i = 0; i < range; i++) {
            if (moveRequested) {
                if (canMove(direction)) {
                    doMove(direction);
                }
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
