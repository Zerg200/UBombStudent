package fr.ubx.poo.model.go.monsters;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Player;

public class Monster extends GameObject implements Movable {

    /**Field of direction */
    Direction direction;
    /**Field of move request */
    private boolean moveRequested = false;
    /**Field of number of lives */
    private int lives = 1;
    /**Field of player */
    private Player player;

    /**Monster constructor
     * @param game current game
     * @param position object's current position
     * @param level the current level where the object is located
     */
    public Monster(Game game, Position position, int level) {
        super(game, position, 1, level);
        this.direction = Direction.S;
        player = game.getPlayer();
    }

    public int getNLives() {
        return lives;
    }

    public void changeLives(int n) {
        this.lives += n;
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

        if(nextPos.inside(game.getWorld(game.getNNowLevel()).dimension)) {
            Decor decor = game.getWorld(game.getNNowLevel()).get(nextPos);
            if(decor == null || decor.getPassability() && decor.getDestructible()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    /**The method updates the index of operations relative to the saved time
     * After reaching the last index (cl), the monster chooses the direction and makes the movement
     * @param now current game time
     */
    public void update(long now) {
        if(getCl() > -1) {
            //The reaction speed depends on the level at which the monster is
            long t =  900000000 - (long) getLevel() * 100000000;
            //Instant movements are impossible.
            if(t < 400000000) {
                t = 400000000;
            }
            //cl = 1, since the monster needs to perform one action
            if(now - getTime() >= t) {
                setTime(now);
                setCl(getCl() - 1);

                if(getCl() == 0) {
                    if(getLevel() == player.getLevel()) {
                        if(getLevel() == game.getInitLevels() - 1) {
                            randomMovement();
                            //directionalMovement();
                        }
                        else {
                            randomMovement();
                        }
                        moveRequested = false;
                    }
                }
            }
        }
        else {
            setCl(1);
        }

        if(player.getLevel() == getLevel()) {
            doDamage(1);
        }
    }

    /**Method for choosing a random direction and requesting motion
     */
    public void randomMovement() {
        requestMove(Direction.random());

        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
    }

    /**Method for damaging a character
     * @param damage damage dealt
     */
    public void doDamage(int damage) {
        Position center = getPosition();
        if(player.getPosition().equals(center) && player.getCanBeDamaged()) {
            player.changeNLives(-damage);
        }
    }

    public String toString() {
        return "Monster, position: " + getPosition() + ", cl: " + getCl()
                + ", level: " + getLevel() + ", nLives: " + getNLives() + ", direction: " + getDirection();
    }
}

