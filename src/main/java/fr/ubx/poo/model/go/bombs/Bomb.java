package fr.ubx.poo.model.go.bombs;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.monsters.Monster;

import java.util.List;

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
            if(player.getPosition().equals(center) && player.getCanBeDamaged())
                player.changeNLives(-damage);
        }

        for(Monster m : monsters) {
            if(m.getLevel() == getLevel()) {
                if(m.getPosition().equals(center))
                    m.changeLives(-damage);
            }
        }

        for(Bomb b : bombs){
            if(b.getLevel() == getLevel()) {
                if(b.getPosition().equals(center)) {
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
                if(playerPos.equals(nextPos)) {
                    player.changeNLives(-damage);
                }
            }

            for(Monster m : monsters) {
                if(m.getLevel() == getLevel()) {
                    if(m.getPosition().equals(nextPos)) {
                        m.changeLives(-damage);
                    }

                }
            }

            for(Bomb b : bombs){
                if(b.getLevel() == getLevel()) {
                    if(b.getPosition().equals(nextPos)) {
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
