package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.bombs.Bomb;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class SpriteBomb extends SpriteGameObject{
    private final ColorAdjust effect = new ColorAdjust();

    private int nImage = 4;

    public void setNImage(int d) {
        nImage = d;
    }

    public SpriteBomb (Pane layer, Bomb bomb) {
        super(layer, null, bomb);
        updateImage();
    }
    @Override
    public void updateImage() {
        Bomb bomb = (Bomb) go;
        setImage(ImageFactory.getInstance().getNImageBomb(nImage));
        int cl = bomb.getCl();

        if(cl > -1) {
            setNImage(cl);
        }
    }
}
