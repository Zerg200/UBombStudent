package fr.ubx.poo.model.decor;

public class Box extends Decor {
    public Box() {
        super(false, false, true, true, false);
    }

    public String toString() {
        return "Box";
    }
}
