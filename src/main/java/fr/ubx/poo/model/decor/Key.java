package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Key extends Decor{
    public Key() {
        super(true, true, false, false, false);
    }

    public void take(Player player) {
        player.increaseKey();
    }

    public String toString() {
        return "Key";
    }
}
