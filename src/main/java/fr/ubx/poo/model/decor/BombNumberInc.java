package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombNumberInc extends Decor {
    public BombNumberInc() {
        super(true, true, false, true, false);
    }

    /**
     * Method for picking up a bonus to increase the number of bombs.
     * @param player player
     */
    public void take(Player player) {
        player.changeNBombs(1);
    }

    public String toString() {
        return "BombNumberInc";
    }
}
