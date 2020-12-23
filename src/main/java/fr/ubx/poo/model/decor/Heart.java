package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Heart extends Decor{
    public Heart() {
        super(true, true, false);
    }

    public String toString() {
        return "Heart";
    }

    public void take(Player player) {
        player.increaseLives();
    }
}
