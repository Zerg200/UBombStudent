/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.Entity;
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
    private int isNext;

    public Decor(boolean passability, boolean collectables, boolean movability, boolean destructible, boolean wayNextLevel) {
        this.passability = passability;
        this.collectables = collectables;
        this.movability = movability;
        this.destructible = destructible;
        this.wayNextLevel = wayNextLevel;
        isNext = 0;
    }

    public boolean getPassability() {
        return passability;
    }

    public boolean getCollectables() {
        return collectables;
    }

    public boolean getMovability() {
        return movability;
    }

    public boolean getDestructible() {
        return destructible;
    }

    public boolean getWayNextLevel() {
        return wayNextLevel;
    }

    public int getIsNext() {
        return isNext;
    }
    public void setIsNext(int isNext) {
        this.isNext = isNext;
    }

    public void take(Player player) {
    }

}
