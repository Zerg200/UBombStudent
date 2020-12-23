package fr.ubx.poo.model.decor;

public class DoorNextOpened extends Decor{
    public DoorNextOpened() {
        super(true, false, false);
    }

    public String toString() {
        return "DoorNextOpened";
    }
}
