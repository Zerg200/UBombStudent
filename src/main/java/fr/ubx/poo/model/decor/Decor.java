/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Player;

import java.util.Objects;

/***
 * A decor is an element that does not know its own position in the grid.
 */
public class Decor extends Entity {
    private boolean passability;
    private boolean collectables;
    private boolean movability;
    private boolean destructible;
    private boolean wayNextLevel;
    /**
     * 0 - undefined
     * 1 - next level
     * -1 - previous level
     */
    private int whatTransition;

    /**Decor constructor
     * @param passability is it possible to go through this decor
     * @param collectables is it possible to collect this decor
     * @param movability is it possible to move this decor
     * @param destructible is it possible to destroy this decor
     * @param wayNextLevel is it possible to use this decor as a transition to another level or to another door
     */
    public Decor(boolean passability, boolean collectables, boolean movability, boolean destructible, boolean wayNextLevel) {
        this.passability = passability;
        this.collectables = collectables;
        this.movability = movability;
        this.destructible = destructible;
        this.wayNextLevel = wayNextLevel;
        whatTransition = 0;
    }

    /**Method for getting the field value of {@link Decor#passability}
     */
    public boolean getPassability() {
        return passability;
    }

    /**Method for getting the field value of {@link Decor#collectables}
     */
    public boolean getCollectables() {
        return collectables;
    }

    /**Method for getting the field value of {@link Decor#movability}
     */
    public boolean getMovability() {
        return movability;
    }

    /**Method for getting the field value of {@link Decor#destructible}
     */
    public boolean getDestructible() {
        return destructible;
    }

    /**Method for getting the field value of {@link Decor#wayNextLevel}
     */
    public boolean getWayNextLevel() {
        return wayNextLevel;
    }

    /**Method for getting the field value of {@link Decor#wayNextLevel}
     * @return returns the direction of transition in the game by levels
     */
    public int getWhatTransition() {
        return whatTransition;
    }

    /**Method for setting the field value of {@link Decor#wayNextLevel}
     * @param isNext direction (-1,0,1)
     */
    public void setWhatTransition(int isNext) {
        this.whatTransition = isNext;
    }

    public void take(Player player) {
    }

}
