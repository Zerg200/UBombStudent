package fr.ubx.poo.model.decor;

public class DoorNextOpened extends Decor{
    public DoorNextOpened() {
        super(true, false, false, false, true);
    }

    public void setIsNext() {
        super.setIsNext(true);
    }

    public String toString() {
        return "DoorNextOpened";
    }
}
