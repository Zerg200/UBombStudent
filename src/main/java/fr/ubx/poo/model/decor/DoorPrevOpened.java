package fr.ubx.poo.model.decor;

public class DoorPrevOpened extends Decor{

    public DoorPrevOpened() {
        super(true, false, false, false, true);
        super.setIsNext(-1);
    }

    public String toString() {
        return "DoorPrevOpened";
    }
}
