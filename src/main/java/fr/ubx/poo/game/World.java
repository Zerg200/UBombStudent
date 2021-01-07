/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;

import java.util.*;
import java.util.function.BiConsumer;

public class World {
    private final Map<Position, Decor> grid;
    private final WorldEntity[][] raw;
    public final Dimension dimension;
    /** Signal that the map of the world has changed */
    private boolean changeMap;
    /** The level number of this world */
    private int level;

    public World(WorldEntity[][] raw, int level) {
        this.raw = raw;
        this.level = level;
        dimension = new Dimension(raw.length, raw[0].length);
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

    public List<Position> findMonsters(int maxLevel) throws PositionNotFoundException {
        List<Position> positions = new ArrayList<>();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Monster) {
                    positions.add(new Position(x, y));
                }
            }
        }
        if(positions.size() > 0) {
            return positions;
        }
        if(level < maxLevel-1) {
            return null;
        }
        throw new PositionNotFoundException("Monster");
    }

    public Position findDoorPrevOpened() {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.DoorPrevOpened) {
                    //y+1, lets spawn in front of the door
                    return new Position(x, y+1);
                }
            }
        }
        return null;
    }

    public Position findDoorNextOpened(){
        //Since a closed door and an open door are formed in the same place,
        // there is an opportunity to use the coordinates of a closed door,
        // since an open door is not created in raw
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.DoorNextClosed) {
                    //y+1, lets spawn in front of the door
                    return new Position(x, y+1);
                }
            }
        }
        return null;
    }

    public Decor get(Position position) {
        return grid.get(position);
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

