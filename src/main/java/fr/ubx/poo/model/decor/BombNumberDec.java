package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombNumberDec extends Decor {
    public BombNumberDec() {
        super(true, true, false);
    }

    public void take(Player player) {
        player.increaseBombNumber(-1);
    }

    public String toString() {
        return "BombNumberDec";
    }
}
