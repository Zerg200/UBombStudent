package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Princess extends Decor{
    public Princess() {
        super(true, true, false);
    }

    public void take(Player player) {
        player.setWinnerTrue();
    }

    public String toString() {
        return "Princess";
    }
}
