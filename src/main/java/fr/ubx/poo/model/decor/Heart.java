package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Heart extends Decor{
    public Heart() {
        super(true, true, false, true, false);
    }

    public String toString() {
        return "Heart";
    }

    /**
     * Method for picking up a bonus to increase the number of lives.
     * @param player player
     */
    public void take(Player player) {
        player.changeNLives(1);
    }
}
