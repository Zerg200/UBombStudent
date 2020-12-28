package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.model.go.bombs.Bomb;
import fr.ubx.poo.model.go.monsters.Monster;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class SpriteBomb extends SpriteGameObject{
    private final ColorAdjust effect = new ColorAdjust();


    public SpriteBomb (Pane layer, Bomb bomb) {
        super(layer, null, bomb);
        updateImage();
    }
    @Override
    public void updateImage() {
        Bomb bomb = (Bomb) go;
        setImage(ImageFactory.getInstance().getBomb(bomb.getDirection()));
    }
}
