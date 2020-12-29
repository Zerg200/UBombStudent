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
    private long time = 0;
    private int cl;


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

    public GameObject(Game game, Position position, int cl) {
        this.game = game;
        this.position = position;
        this.cl = cl;
    }
}
