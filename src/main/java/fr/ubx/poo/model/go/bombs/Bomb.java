package fr.ubx.poo.model.go.bombs;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.monsters.Monster;

import java.util.List;

import static java.lang.Thread.sleep;

public class Bomb extends GameObject {

    private int range;
    private int level;
    private boolean isNew = true;
    private int lives = 1;
    private List<Bomb> bombs = game.getBombs();
    private Player player = game.getPlayer();
    private List<Monster> monsters = game.getMonsters();

   // private List<Monster> monsters = game.getMonster();

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }


    public Bomb(Game game, Position position, int range, int level) {
        super(game, position, 4);
        this.range = range;
        this.level = level;
    }

    public boolean update(long now) {

        if(getCl() > -1) {
            if(now - getTime() >= 1000000000) {
                //System.out.println(now - time);
                setTime(now);
                //System.out.println(cl);
                setCl(getCl()-1);;
            }
            return true;
        }
        damage(1);
        return false;
    }

    public void damage(int j) {
        Position center = getPosition();
        if(player.getLevel() == level) {
            if(player.getPosition().x == center.x && player.getPosition().y == center.y && player.getCanBeDamaged())
                player.changeLives(-j);
        }
        for(int i = 0; i < monsters.size(); i++) {
            if(monsters.get(i).getLevel() == level) {
                //System.out.println("Нашёл");
                if(monsters.get(i).getPosition().x == center.x && monsters.get(i).getPosition().y == center.y)
                    monsters.get(i).changeLives(-j);
            }
        }
        for(int i = j; i < bombs.size(); i++){
            if(bombs.get(i).getLevel() == level) {
                if(bombs.get(i).getPosition().x == center.x && bombs.get(i).getPosition().y == center.y) {
                    if(bombs.get(i).getLives() != 0) {
                        bombs.get(i).setLives(0);
                    }
                }
            }
        }

        //System.out.println(getPosition());
        //System.out.println("Player: " + player.getPosition());
        damageDirection(Direction.N, j);
        damageDirection(Direction.S, j);
        damageDirection(Direction.W, j);
        damageDirection(Direction.E, j);
    }


    public void damageDirection(Direction direction, int j) {
        Position nextPos = direction.nextPosition(getPosition());

        for(int i = 0; i < range; i++){
            Decor decor = game.getWorld(game.getNNowLevel()).get(nextPos);
            //System.out.println(nextPos + " " + direction);
            if(decor != null) {
                if(decor.getDestructible()) {
                    game.getWorld(game.getNNowLevel()).clear(nextPos);
                    game.getWorld(game.getNNowLevel()).setChangeMap(true);
                    if(decor.getMovability()){
                        break;
                    }
                }
                else {
                    if(!decor.getCollectables()){
                        break;
                    }
                }/*
                if(decor.getMovability()) {
                    game.getWorld().clear(nextPos);
                    game.getWorld().setChangeMap(true);
                    break;
                }
                else if(decor.getCollectables()) {
                    if(decor.getDestructible()) {
                        game.getWorld().clear(nextPos);
                        game.getWorld().setChangeMap(true);
                    }
                }
                else if(!decor.getPassability()) {
                    break;
                }*/
            }

            if(player.getLevel() == level) {
                if(player.getPosition().x == nextPos.x && player.getPosition().y == nextPos.y && player.getCanBeDamaged()) {
                    //System.out.println("Player: " + player.getPosition());
                    player.changeLives(-j);
                }
            }


            for(int k = 0; k < monsters.size(); k++) {
                if(monsters.get(k).getLevel() == level) {
                    if(monsters.get(k).getPosition().x == nextPos.x && monsters.get(k).getPosition().y == nextPos.y)
                        monsters.get(k).changeLives(-j);
                }
            }

            for(int k = j; k < bombs.size(); k++){
                if(bombs.get(k).getLevel() == level) {
                    if(bombs.get(k).getPosition().x == nextPos.x && bombs.get(k).getPosition().y == nextPos.y) {
                        if(bombs.get(k).getLives() != 0) {
                            bombs.get(k).damage(k);
                            bombs.get(k).setLives(0);
                        }

                    }
                }
            }

            nextPos = direction.nextPosition(nextPos);
        }
    }
}
