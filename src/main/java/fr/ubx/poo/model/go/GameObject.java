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

    /**Field of play */
    protected final Game game;
    /**Object position field in the world */
    private Position position;
    /**Time field */
    private long time;
    /**Counter field */
    private int cl;
    /**The level field on which the object is located */
    private int level;

    /**Game object constructor
     * @param game current game
     * @param position object's current position
     * @param cl the required number of operations to perform an action
     * @param level the current level where the object is located.
     */
    public GameObject(Game game, Position position, int cl, int level) {
        this.game = game;
        this.position = position;
        this.cl = cl;
        time = 0;
        this.level = level;
    }

    /**Method for getting the field value of {@link GameObject#position}
     * @return returns the position of the object
     */
    public Position getPosition() {
        return position;
    }

    /**Method for setting the value of {@link GameObject#position}
     * @param position the position of the object
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**Method for getting the field value of {@link GameObject#cl}
     * @return returns the indec of the current operation
     */
    public int getCl() {
        return cl;
    }

    /**Method for setting the value of {@link GameObject#cl}
     * @param cl the index of the current operation
     */
    public void setCl(int cl) {
        this.cl = cl;
    }

    /**Method for getting the field value of {@link GameObject#time}
     * - initial value = 0
     * @return returns the current value of the game time at which the action was performed
     */
    public long getTime() {
        return time;
    }

    /**Method for setting the value of {@link GameObject#time}
     * @param now the value of the current game time
     */
    public void setTime(long now) {
        this.time = now;
    }

    /**Method for getting the field value of {@link GameObject#level}
     * @return returns the current value of the game level
     */
    public int getLevel() {
        return level;
    }

    /**Method for setting the value of {@link GameObject#level}
     * @param level the value of the game level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**Method for change the field value of {@link GameObject#level}
     * @param d the value to increase or decrease the level value
     */
    public void incDecNowLevel(int d) {
        if(d < 0) {
            if(level > 0) {
                level+=d;
            }
        }
        else {
            if(level < game.getInitLevels()) {
                level+=d;
            }
        }
    }
}
