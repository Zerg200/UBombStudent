package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombNumberDec extends Decor {

    public BombNumberDec() {
        super(true, true, false, true, false);
    }

    /**
     * Method for picking up a bonus to decrease the number of bombs.
     * @param player player
     */
    public void take(Player player) {
        if(player.getNBombs() > 1)
            player.changeNBombs(-1);
    }

    public String toString() {
        return "BombNumberDec";
    }
}
