/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

public class Stone extends Decor {
    public Stone() {
        super(false, false, false);
    }

    @Override
    public String toString() {
        return "Stone";
    }
}
