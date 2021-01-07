/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import fr.ubx.poo.model.go.bombs.Bomb;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.monsters.Monster;

public class Game {

    private List<World> world = new ArrayList<>();
    private final Player player;
    private List<Bomb> bombs = new ArrayList<>();
    private List<Monster> monsters = new ArrayList<>();
    private final String worldPath;
    public int initPlayerLives;
    private int initLevels;
    public String prefixMondes;
    private boolean[] isNewLevel;
    private int nNowLevel;

    public Game(String worldPath) throws IOException {
        //world = new WorldStatic();
        nNowLevel = 0;
        int nLevel = 0;
        this.worldPath = worldPath;
        isNewLevel = new boolean[]{false, false};
        loadConfig(worldPath);
        loadLevels(worldPath);
        Position positionPlayer;
        List<Position> positionsMonster;

        try {

            positionPlayer = world.get(0).findPlayer();
            player = new Player(this, positionPlayer, nLevel);

            for(; nLevel < world.size(); nLevel++) {
                positionsMonster = world.get(nLevel).findMonsters(initLevels);
                if(positionsMonster != null) {
                    for(Position p : positionsMonster) {
                        monsters.add(new Monster(this, p, nLevel));
                    }
                }
            }

        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public int getInitPlayerLives() {
        return initPlayerLives;
    }

    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
            initLevels = Integer.parseInt(prop.getProperty("levels", "3"));
            prefixMondes = prop.getProperty("prefix", "level");
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    private void loadLevels(String path) throws IOException {
        for(int i = 0; i < initLevels; i++){
            WorldCreatorFromFile wc = new WorldCreatorFromFile(path, prefixMondes, i+1);
            world.add(new World(wc.mapEntities, i));
        }

    }

    public World getWorld(int level) {
        return world.get(level);
    }

    public Player getPlayer() {
        return this.player;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public void setBomb(Bomb bomb) {
        bombs.add(bomb);
    }

    public void setIsNewLevel(boolean isNewLevel, boolean isNext) {
        this.isNewLevel[0] = isNewLevel;
        this.isNewLevel[1] = isNext;
    }

    public boolean[] getIsNewLevel() {
        return isNewLevel;
    }

    public int getInitLevels() {
        return initLevels;
    }

    public void setNNowLevel(int nNowLevel) {
        this.nNowLevel = nNowLevel;
    }

    public void incDecNNowLevel(int d) {
        if(d < 0) {
            if(nNowLevel > 0)
                nNowLevel+=d;
        }
        else if(nNowLevel < initLevels) {
            nNowLevel+=d;
        }
    }

    public int getNNowLevel() {
        return nNowLevel;
    }

}
