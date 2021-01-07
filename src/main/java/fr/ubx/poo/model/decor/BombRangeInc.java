package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombRangeInc extends Decor{
    public BombRangeInc() {
        super(true, true, false, true, false);
    }

    /**
     * Method for picking up a bonus to increase the range.
     * @param player player
     */
    public void take(Player player) {
        player.changeRange(1);
    }

    public String toString() {
        return "BombRangeInc";
    }
}
