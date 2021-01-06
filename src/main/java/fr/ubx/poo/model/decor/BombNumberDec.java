package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombNumberDec extends Decor {
    public BombNumberDec() {
        super(true, true, false, true, false);
    }

    public void take(Player player) {
        if(player.getNBombs() > 1)
            player.changeNBombs(-1);
    }

    public String toString() {
        return "BombNumberDec";
    }
}
