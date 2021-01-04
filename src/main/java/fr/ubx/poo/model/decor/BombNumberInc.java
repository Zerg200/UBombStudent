package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombNumberInc extends Decor {
    public BombNumberInc() {
        super(true, true, false, true, false);
    }

    public void take(Player player) {
        player.changeBombNumber(1);
    }

    public String toString() {
        return "BombNumberInc";
    }
}
