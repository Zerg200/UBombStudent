/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class Sprite {

    public static final int size = 40;
    private final Pane layer;
    private ImageView imageView;
    private Image image;
    private boolean isChangedColor = false;
    private final ColorAdjust effect = new ColorAdjust();

    public Sprite(Pane layer, Image image) {
        this.layer = layer;
        this.image = image;
    }

    public final void setImage(Image image) {
        if (this.image == null || this.image != image) {
            this.image = image;
        }
    }

    public abstract void updateImage();

    public abstract Position getPosition();

    public final void render() {
        if (imageView != null) {
            remove();
        }
        updateImage();
        imageView = new ImageView(this.image);
        if(isChangedColor) {
            //Setting the contrast value
            effect.setContrast(0.4);

            //Setting the hue value
            effect.setHue(-0.05);

            //Setting the brightness value
            effect.setBrightness(0.9);

            //Setting the saturation value
            effect.setSaturation(0.8);

            imageView.setEffect(effect);
            setIsChangedColor(false);
        }
        imageView.setX(getPosition().x * size);
        imageView.setY(getPosition().y * size);
        layer.getChildren().add(imageView);

    }

    public final void remove() {
        layer.getChildren().remove(imageView);
        imageView = null;
    }

    public final void setIsChangedColor(boolean isChangedColor){
        this.isChangedColor = isChangedColor;
    }

}
