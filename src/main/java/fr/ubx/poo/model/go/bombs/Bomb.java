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
    private int nLives;
    private boolean isNew;
    private int damage;
    private List<Bomb> bombs;
    private Player player;
    private List<Monster> monsters;

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getNLives() {
        return nLives;
    }

    public void setNLives(int nLives) {
        this.nLives = nLives;
    }


    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }


    public Bomb(Game game, Position position, int range, int level) {
        super(game, position, 4, level);
        this.range = range;

        nLives = 1;
        damage = 1;
        isNew = true;
        player = game.getPlayer();
        monsters = game.getMonsters();
        bombs = game.getBombs();
    }

    public boolean update(long now) {

        if(getCl() > -1) {
            if(now - getTime() >= 800000000) {
                setTime(now);
                setCl(getCl()-1);
            }
            return true;
        }
        doDamage();
        return false;
    }

    public void doDamage() {
        Position center = getPosition();
        if(player.getLevel() == getLevel()) {
            if(player.getPosition().x == center.x && player.getPosition().y == center.y && player.getCanBeDamaged())
                player.changeNLives(-damage);
        }
        /*for(int i = 0; i < monsters.size(); i++) {
            if(monsters.get(i).getLevel() == getLevel()) {
                if(monsters.get(i).getPosition().x == center.x && monsters.get(i).getPosition().y == center.y)
                    monsters.get(i).changeLives(-damage);
            }
        }*/
        for(Monster m : monsters) {
            if(m.getLevel() == getLevel()) {
                if(m.getPosition().x == center.x && m.getPosition().y == center.y)
                    m.changeLives(-damage);
            }
        }
        /*for(int i = 1; i < bombs.size(); i++){
            if(bombs.get(i).getLevel() == getLevel()) {
                if(bombs.get(i).getPosition().x == center.x && bombs.get(i).getPosition().y == center.y) {
                    if(bombs.get(i).getNLives() > 0) {
                        bombs.get(i).setNLives(0);
                    }
                }
            }
        }*/
        for(Bomb b : bombs){
            if(b.getLevel() == getLevel()) {
                if(b.getPosition().x == center.x && b.getPosition().y == center.y) {
                    if(b.getNLives() > 0) {
                        b.setNLives(0);
                    }
                }
            }
        }

        damageDirection(Direction.N);
        damageDirection(Direction.S);
        damageDirection(Direction.W);
        damageDirection(Direction.E);
    }


    public void damageDirection(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        for(int i = 0; i < range; i++){
            Decor decor = game.getWorld(getLevel()).get(nextPos);
            if(decor != null) {
                if(decor.getDestructible()) {
                    game.getWorld(getLevel()).clear(nextPos);
                    game.getWorld(getLevel()).setChangeMap(true);
                }
                break;
            }

            if(player.getLevel() == getLevel()) {
                Position playerPos = player.getPosition();
                if(playerPos.x == nextPos.x && playerPos.y == nextPos.y && player.getCanBeDamaged()) {
                    player.changeNLives(-damage);
                }
            }

            /*for(int k = 0; k < monsters.size(); k++) {
                if(monsters.get(k).getLevel() == getLevel()) {
                    if(monsters.get(k).getPosition().x == nextPos.x && monsters.get(k).getPosition().y == nextPos.y) {
                        monsters.get(k).changeLives(-damage);
                        monsters.get(k).changeLives(-damage);
                    }

                }
            }*/
            for(Monster m : monsters) {
                if(m.getLevel() == getLevel()) {
                    if(m.getPosition().x == nextPos.x && m.getPosition().y == nextPos.y) {
                        m.changeLives(-damage);
                    }

                }
            }

            /*for(int k = 1; k < bombs.size(); k++){
                if(bombs.get(k).getLevel() == getLevel()) {
                    if(bombs.get(k).getPosition().x == nextPos.x && bombs.get(k).getPosition().y == nextPos.y) {
                        if(bombs.get(k).getNLives() > 0) {
                            bombs.get(k).doDamage();
                        }
                    }
                }
            }*/
            for(Bomb b : bombs){
                if(b.getLevel() == getLevel()) {
                    if(b.getPosition().x == nextPos.x && b.getPosition().y == nextPos.y) {
                        if(b.getNLives() > 0) {
                            b.doDamage();
                        }
                    }
                }
            }

            nextPos = direction.nextPosition(nextPos);
        }
    }

    public String toString() {
        return "Bomb, position: " + getPosition() + ", cl: " + getCl() + ", range: " + getRange()
                + ", level: " + getLevel() +", nLives: " + getNLives() + ", isNew: " + getIsNew() + ", damage: 1";
    }
}
