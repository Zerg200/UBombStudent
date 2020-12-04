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
import fr.ubx.poo.engine.GameEngine;

public class Player extends GameObject implements Movable {

    private final boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private boolean winner;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
    }

    public int getLives() {
        return lives;
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
    public boolean canMove(Direction direction) { //Подумать ещё
        Position nextPos = direction.nextPosition(getPosition());
        if(game.getWorld().isEmpty(nextPos))
            return nextPos.inside(game.getWorld().dimension);
        else if(!(game.getWorld().get(nextPos).toString().equals("Tree")) && !(game.getWorld().get(nextPos).toString().equals("Stone")))
            return true;
        /*else if(game.getWorld().get(nextPos).toString().equals("Box")) {
            if(direction.equals(Direction.N) && game.getWorld().isEmpty(new Position(nextPos.x, nextPos.y-1))) {
                if(game.getWorld().isEmpty(nextPos))
                    System.out.println("Empty");
                else
                    System.out.println("" + game.getWorld().get(nextPos).toString());
                game.getWorld().clear(nextPos);
                game.getWorld().set(new Position(nextPos.x, nextPos.y-1), new Box());
                setPosition(nextPos);
            }
            else if(direction.equals(Direction.S) && game.getWorld().isEmpty(new Position(nextPos.x, nextPos.y+1))) {
                game.getWorld().clear(nextPos);
                game.getWorld().set(new Position(nextPos.x, nextPos.y+1), new Box());
                setPosition(nextPos);
            }
            else if(direction.equals(Direction.E) && game.getWorld().isEmpty(new Position(nextPos.x+1, nextPos.y))) {
                game.getWorld().clear(nextPos);
                game.getWorld().set(new Position(nextPos.x+1, nextPos.y), new Box());
                setPosition(nextPos);
            }
            else if(direction.equals(Direction.W) && game.getWorld().isEmpty(new Position(nextPos.x-1, nextPos.y))) {
                game.getWorld().clear(nextPos);
                game.getWorld().set(new Position(nextPos.x-1, nextPos.y), new Box());
                setPosition(nextPos);
            }

            game.setChangeMap(true);
            return true;
        }*/



        return false;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
                /*else if(game.getWorld().get(nextPos).toString().equals("Box")) {
                    if(direction.equals(Direction.N) && game.getWorld().isEmpty(new Position(nextPos.x, nextPos.y-1))) {
                        game.getWorld().set(nextPos, null);
                        game.getWorld().set(new Position(nextPos.x, nextPos.y-1), new Box());
                        setPosition(nextPos);
                    }
                    else if(direction.equals(Direction.S) && game.getWorld().isEmpty(new Position(nextPos.x, nextPos.y+1))) {
                        game.getWorld().set(nextPos, null);
                        game.getWorld().set(new Position(nextPos.x, nextPos.y+1), new Box());
                        setPosition(nextPos);
                    }
                    else if(direction.equals(Direction.E) && game.getWorld().isEmpty(new Position(nextPos.x+1, nextPos.y))) {
                        game.getWorld().set(nextPos, null);
                        game.getWorld().set(new Position(nextPos.x+1, nextPos.y), new Box());
                        setPosition(nextPos);
                    }
                    else if(direction.equals(Direction.W) && game.getWorld().isEmpty(new Position(nextPos.x-1, nextPos.y))) {
                        game.getWorld().set(nextPos, null);
                        game.getWorld().set(new Position(nextPos.x-1, nextPos.y), new Box());
                        setPosition(nextPos);
                    }
                }*/
        /*if(!game.getWorld().isEmpty(getPosition())){
            game.getWorld().clear(getPosition());
            game.setChangeMap(true);
        }
        System.out.println("Position: " + getPosition());*/
        /*if(game.getWorld().isEmpty(getPosition()))
            System.out.println("Empty");
        else
            System.out.println("" + game.getWorld().get(getPosition()).toString());*/

        /*if(game.getWorld().isEmpty(getPosition())){
            //game.getWorld().set(getPosition(), new Heart());
            //game.getWorld().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));

            System.out.println("" + game.getWorld().get(getPosition()).toString());
        }*/




        /*if(!(game.getWorld().isEmpty(new Position(nextPos.x, nextPos.y-1)))){
            System.out.println("No");
        }
        if(direction.equals(Direction.N))
            System.out.println("N");
        if(game.getWorld().isEmpty(nextPos))
            System.out.println("Empty");
        else
            System.out.println("" + game.getWorld().get(nextPos).toString());
        System.out.println("" + nextPos);*/
    }

    public void update(long now) {
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
