package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombRangeDec extends Decor{
    public BombRangeDec() {
        super(true, true, false, true, false);
    }

    public void take(Player player) {
        player.changeRange(-1);
    }

    @Override
    public String toString() {
        return "BombRangeDec";
    }
}
