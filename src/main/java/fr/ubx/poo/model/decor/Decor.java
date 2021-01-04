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
    private boolean isNext;

    public Decor(boolean passability, boolean collectables, boolean movability, boolean destructible, boolean wayNextLevel) {
        this.passability = passability;
        this.collectables = collectables;
        this.movability = movability;
        this.destructible = destructible;
        this.wayNextLevel = wayNextLevel;
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

    public boolean getIsNext() {
        return isNext;
    }
    public void setIsNext(boolean isNext) {
        this.isNext = isNext;
    }

    public void take(Player player) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Decor decor = (Decor) o;
        return passability == decor.passability && collectables == decor.collectables && movability == decor.movability && destructible == decor.destructible;
    }

    @Override
    public int hashCode() {
        return Objects.hash(passability, collectables, movability, destructible);
    }
}
