/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;


public class Tree extends Decor {
    public Tree() {
        super(false, false, false);
    }

    @Override
    public String toString() {
        return "Tree";
    }
}
