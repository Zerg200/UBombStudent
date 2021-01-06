package fr.ubx.poo.model.go.monsters;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Player;

public class Monster extends GameObject implements Movable {

    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    //The monster knows about the existence of the player
    private Player player;

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

    public void update(long now) {
        if(getCl() > -1) {
            if(now - getTime() >= (800000000 - (long) getLevel() * 100000000)) {
                setTime(now);
                setCl(getCl()-1);
                if(getCl() == 0) {
                    if(getLevel() == player.getLevel()) {
                        if(getLevel() == game.getInitLevels()) {
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
           damage(1);
        }
    }

    public void randomMovement() {
        double d = Math.random()*4;

        if((int) d == 0)
            requestMove(Direction.N);
        else if ((int)d == 1)
            requestMove(Direction.E);
        else if ((int)d == 2)
            requestMove(Direction.S);
        else
            requestMove(Direction.W);

        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
    }

    public void directionalMovement() {
        Position playerPosition = player.getPosition();

        int[][] table = new int[game.getWorld(getLevel()).dimension.height][game.getWorld(getLevel()).dimension.width];


        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
    }


    public void damage(int j) {
        Position center = getPosition();
        if(player.getPosition().x == center.x && player.getPosition().y == center.y && player.getCanBeDamaged())
            player.changeNLives(-j);
    }

    public String toString() {
        return "Monster, position: " + getPosition() + ", cl: " + getCl()
                + ", level: " + getLevel() + ", nLives: " + getNLives() + ", direction: " + getDirection();
    }
}

