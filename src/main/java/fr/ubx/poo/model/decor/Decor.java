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

    public Decor(boolean passability, boolean collectables, boolean movability) {
        this.passability = passability;
        this.collectables = collectables;
        this.movability = movability;
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

    public void take(Player player) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Decor decor = (Decor) o;
        return passability == decor.passability && collectables == decor.collectables && movability == decor.movability;
    }

    @Override
    public int hashCode() {
        return Objects.hash(passability, collectables, movability);
    }
}
