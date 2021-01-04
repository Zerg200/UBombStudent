/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import fr.ubx.poo.model.go.bombs.Bomb;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.monsters.Monster;

public class Game {

    private List<World> world = new ArrayList<>();
    private final Player player;
    private Monster monster; //Cделать как список
    private List<Bomb> bombs = new ArrayList<>();
    //private List<Monster> monsters = new ArrayList<>();
    private final String worldPath;
    public int initPlayerLives;
    public int initLevels;
    public String prefixMondes;
    private boolean isNewLevel[] = {false, false};
    private int nLevel;

    public Game(String worldPath) throws IOException {
        //world = new WorldStatic();
        nLevel = 0;
        this.worldPath = worldPath;
        loadConfig(worldPath);
        loadLevels(worldPath);
        Position positionPlayer = null;
        Position positionMonster = null;

        try {
            positionMonster = world.get(nLevel).findMonsters();
            monster = new Monster(this, positionMonster);
            positionPlayer = world.get(nLevel).findPlayer();
            player = new Player(this, positionPlayer);
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
            world.add(new World(wc.mapEntities));
        }

    }

    public World getWorld(int level) {
        return world.get(level);
    }

    public Player getPlayer() {
        return this.player;
    }

    public Monster getMonster() {
        return this.monster;
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

    public void setNLevel(int nLevel) {
        this.nLevel = nLevel;
    }

    public void incDecNLevel(int d) {
        if(d < 0) {
            if(nLevel > 0)
                nLevel+=d;
        }
        else if(nLevel < initLevels) {
            nLevel+=d;
        }
    }

    public int getNLevel() {
        return nLevel;
    }

}
