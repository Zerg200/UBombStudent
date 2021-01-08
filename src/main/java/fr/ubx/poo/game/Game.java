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

    private final List<World> worlds = new ArrayList<>();
    private final Player player;
    private final List<Bomb> bombs = new ArrayList<>();
    private final List<Monster> monsters = new ArrayList<>();
    private final String worldPath;
    public int initPlayerLives;
    private int initLevels;
    public String prefixWorlds;

    public Game(String worldPath) throws IOException {
        //world = new WorldStatic();
        int nLevel = 0;
        this.worldPath = worldPath;
        loadConfig(worldPath);
        loadLevels(worldPath);
        Position positionPlayer;
        List<Position> positionsMonster;

        try {
            positionPlayer = worlds.get(0).findPlayer();
            player = new Player(this, positionPlayer, nLevel);

            //Finds all monsters at various levels
            for(; nLevel < worlds.size(); nLevel++) {
                positionsMonster = worlds.get(nLevel).findMonsters(initLevels);
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
            prefixWorlds = prop.getProperty("prefix", "level");
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    /**Method for getting the field value of {@link Game#worlds}
     * @param path path to files
     */
    private void loadLevels(String path) throws IOException {
        for(int i = 0; i < initLevels; i++){
            WorldCreatorFromFile wc = new WorldCreatorFromFile(path, prefixWorlds, i+1);
            if(wc.getMapEntities() != null) {
                worlds.add(new World(wc.getMapEntities(), i));
            }
            else {
                break;
            }
        }
    }

    public World getWorld(int level) {
        return worlds.get(level);
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

    public int getInitLevels() {
        return initLevels;
    }

}
