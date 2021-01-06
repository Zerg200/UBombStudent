package fr.ubx.poo.model.decor;

public class DoorNextClosed extends Decor {
    public DoorNextClosed() {
        super(false, false, false, false, true);
    }

    public String toString() {
        return "DoorNextClosed";
    }
}
