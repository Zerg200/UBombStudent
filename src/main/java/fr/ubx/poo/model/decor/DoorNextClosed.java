package fr.ubx.poo.model.decor;

public class DoorNextClosed extends Decor {
    public DoorNextClosed() {
        super(true, false, false);
    }

    public String toString() {
        return "DoorNextClosed";
    }
}
