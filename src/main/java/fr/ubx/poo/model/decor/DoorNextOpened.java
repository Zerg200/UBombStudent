package fr.ubx.poo.model.decor;

public class DoorNextOpened extends Decor{
    public DoorNextOpened() {
        super(true, false, false, false, true);
        super.setWhatTransition(1);
    }

    public String toString() {
        return "DoorNextOpened";
    }
}
