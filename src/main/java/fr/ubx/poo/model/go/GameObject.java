/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.Entity;

/***
 * A GameObject can acces the game and knows its position in the grid.
 */
public abstract class GameObject extends Entity {
    protected final Game game;
    private Position position;
    private long time;
    private int cl;
    private int level;


    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getCl() {
        return cl;
    }
    public void setCl(int cl) {
        this.cl = cl;
    }

    public void setTime(long now) {
        this.time = now;
    }

    public long getTime() {
        return time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void incDecNowLevel(int d) {
        if(d < 0) {
            if(level > 0)
                level+=d;
        }
        else if(level < game.getInitLevels()) {
            level+=d;
        }
    }

    public GameObject(Game game, Position position, int cl, int level) {
        this.game = game;
        this.position = position;
        this.cl = cl;
        time = 0;
        this.level = level;
    }

}
