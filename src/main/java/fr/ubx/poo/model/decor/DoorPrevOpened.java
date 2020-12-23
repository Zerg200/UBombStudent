package fr.ubx.poo.model.decor;

public class DoorPrevOpened extends Decor{
    public DoorPrevOpened() {
        super(true, false, false);
    }

    public String toString() {
        return "DoorPrevOpened";
    }
}
