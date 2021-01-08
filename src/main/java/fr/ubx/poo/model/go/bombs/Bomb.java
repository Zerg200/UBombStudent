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
    /** The number of lives */
    private int nLives;
    /** Is this a new bomb */
    private boolean isNew;
    private int damage;
    /** The bomb can interact with other bombs */
    private List<Bomb> bombs;
    /** The bomb can interact with the player */
    private final Player player;
    /** The bomb can interact with monsters */
    private final List<Monster> monsters;

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

    /**Bomb constructor
     * @param game current game
     * @param position object's current position
     * @param range the explosion area
     * @param level the current level where the object is located
     */
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

    /**The method updates the index of operations relative to the saved time (4 seconds per 5 sprites)
     * After reaching the last index (cl), an explosion occurs (damage spread)
     * @param now current game time
     * @return returns false if the bomb has completed its operations
     */
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

    /**The method spreads the damage over the area according to the range
     */
    public void doDamage() {
        Position center = getPosition();
        //Check is made to see if a monster or a player is at the location of the bomb
        if(player.getLevel() == getLevel()) {
            if(player.getPosition().equals(center) && player.getCanBeDamaged()) {
                player.changeNLives(-damage);
            }
        }

        for(Monster m : monsters) {
            if(m.getLevel() == getLevel()) {
                if(m.getPosition().equals(center)) {
                    m.changeLives(-damage);
                }
            }
        }
        //The bomb then loses its only life.
        setNLives(0);
        //Damage spread according to range in directions
        damageDirection(Direction.N);
        damageDirection(Direction.S);
        damageDirection(Direction.W);
        damageDirection(Direction.E);
    }

    /**The method spreads the damage over the area according to the range and one direction
     */
    public void damageDirection(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        for(int i = 0; i < range; i++){
            Decor decor = game.getWorld(getLevel()).get(nextPos);
            //Any decor will stop the explosion from spreading, the destructible decor will be destroyed
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
            //If there were other bombs in the area of the bomb explosion, they will explode in a chain
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
