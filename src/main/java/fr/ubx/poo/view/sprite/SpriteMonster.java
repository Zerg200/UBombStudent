package fr.ubx.poo.view.sprite;


import fr.ubx.poo.model.go.monsters.Monster;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class SpriteMonster extends SpriteGameObject{
    private final ColorAdjust effect = new ColorAdjust();

    /*private Monster linkToObject = null;

    public void setlinkToObject(Monster linkToObject) {
        this.linkToObject = linkToObject;
    }

    public Monster getlinkToObject() {
        return linkToObject;
    }*/

    public SpriteMonster (Pane layer, Monster monster) {
        super(layer, null, monster);
        updateImage();
        //linkToObject = monster;
    }
    @Override
    public void updateImage() {
        Monster monster = (Monster) go;
        setImage(ImageFactory.getInstance().getMonster(monster.getDirection()));
    }
}
