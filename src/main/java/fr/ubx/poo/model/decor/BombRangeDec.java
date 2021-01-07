package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombRangeDec extends Decor{
    public BombRangeDec() {
        super(true, true, false, true, false);
    }

    /**
     * Method for picking up a bonus to decrease the range.
     * @param player player
     */
    public void take(Player player) {
        player.changeRange(-1);
    }

    @Override
    public String toString() {
        return "BombRangeDec";
    }
}
