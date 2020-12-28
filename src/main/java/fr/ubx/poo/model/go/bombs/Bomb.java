package fr.ubx.poo.model.go.bombs;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.GameObject;

import static java.lang.Thread.sleep;

public class Bomb extends GameObject {

    Direction direction;
    private int range;
    private int level;
    private boolean isNew = true;
    private long time = 0;
    private int cl = 0;

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void setTime(long now) {
        this.time = now;
    }

    public Direction getDirection() {
        return direction;
    }

    public Bomb(Game game, Position position, int range, int level) {
        super(game, position);
        direction = Direction.W;
        this.range = range;
        this.level = level;
    }

    public boolean update(long now) {

        if(cl < 4) {
            if(now - time >= 1000000000) {
                System.out.println(now - time);
                time = now;
                System.out.println(cl);
                cl++;
                if(cl == 1)
                    direction = Direction.S;
                else if(cl == 2)
                    direction = Direction.E;
                else
                    direction = Direction.N;
            }
            return true;
        }
        return false;
    }

    public void damage() {

    }
}
