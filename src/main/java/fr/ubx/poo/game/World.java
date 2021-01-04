/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.bombs.Bomb;
import fr.ubx.poo.model.go.monsters.Monster;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

public class World {
    private final Map<Position, Decor> grid;
    private final WorldEntity[][] raw;
    public final Dimension dimension;
    private boolean changeMap;

    public World(WorldEntity[][] raw) {
        this.raw = raw;


        dimension = new Dimension(raw.length, raw[0].length);
        System.out.println(raw.length + " " + raw[0].length);
        grid = WorldBuilder.build(raw, dimension);
        changeMap = false;
    }

    public Position findPlayer() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Player) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Player");
    }

    public Position findMonsters() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Monster) {
                    //System.out.println("Monster: " + y + " " + x);
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Monster");
    }

    public Position findDoorPrevOpened() {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.DoorPrevOpened) {
                    //System.out.println("DoorPrevOpened: " + y + " " + x);
                    return new Position(x, y+1);
                }
            }
        }
        return null;
    }

    public Position findDoorNextOpened(){
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.DoorNextClosed) {
                    //System.out.println("DoorNextOpened: " + y + " " + x);
                    return new Position(x, y+1);
                }
            }
        }
        return null;
    }


    public Decor get(Position position) {
        return grid.get(position);
    }

    public WorldEntity getRaw(Position position) {
        return raw[position.y][position.x];
    }

    public void set(Position position, Decor decor) {
        grid.put(position, decor);
    }


    public void clear(Position position) {
        grid.remove(position);
    }

    public void forEach(BiConsumer<Position, Decor> fn) {
        grid.forEach(fn);
    }

    public Collection<Decor> values() {
        return grid.values();
    }

    public boolean isInside(Position position) {
        return true; // to update
    }

    public boolean isEmpty(Position position) {
        return grid.get(position) == null;
    }

    public void setChangeMap(boolean changeMap) {
        this.changeMap = changeMap;
    }

    public boolean getChangeMap() {
        return changeMap;
    }
}

