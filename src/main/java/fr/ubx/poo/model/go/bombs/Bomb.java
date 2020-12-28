package fr.ubx.poo.model.go.bombs;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Key;
import fr.ubx.poo.model.decor.Princess;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.monsters.Monster;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Bomb extends GameObject {

    Direction direction;
    private int range;
    private int level;
    private boolean isNew[] = {true,true};
    private int lives = 1;
    private long time = 0;
    private int cl = 0;
    private List<Bomb> bombs = game.getBombs();
    private Player player = game.getPlayer();
   // private List<Monster> monsters = game.getMonster();

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean[] getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNewObj, boolean isNewSprite) {
        this.isNew[0] = isNewObj;
        this.isNew[1] = isNewSprite;
    }

    public void setTime(long now) {
        this.time = now;
    }

    public Direction getDirection() {
        return direction;
    }

    public Bomb(Game game, Position position, int range, int level) {
        super(game, position);
        direction = Direction.W;
        this.range = range;
        this.level = level;
    }

    public boolean update(long now) {

        if(cl < 4) {
            if(now - time >= 1000000000) {
                //System.out.println(now - time);
                time = now;
                //System.out.println(cl);
                cl++;
                if(cl == 1)
                    direction = Direction.S;
                else if(cl == 2)
                    direction = Direction.E;
                else
                    direction = Direction.N;
            }
            return true;
        }
        damage();
        return false;
    }

    public void damage() {
        Position center = getPosition();

            if(player.getPosition().x == center.x && player.getPosition().y == center.y && player.getCanBeDamaged())
                player.changeLives(-1);

            /*if(monsters.get(j).getPosition().x == center.x && monsters.get(j).getPosition().y == center.y)
                monster.changeLives(-1);*/
        for(int i = 0; i < bombs.size(); i++){
            if(bombs.get(i).getPosition().x == center.x && bombs.get(i).getPosition().y == center.y) {
                bombs.get(i).setLives(0);
            }
        }

            for(int i = 0; i < bombs.size(); i++){
                if(bombs.get(i).getPosition().x == center.x && bombs.get(i).getPosition().y == center.y) {
                    bombs.get(i).setLives(0);
                }
            }
        //System.out.println(getPosition());
        //System.out.println("Player: " + player.getPosition());
        damageDirection(Direction.N);
        damageDirection(Direction.S);
        damageDirection(Direction.W);
        damageDirection(Direction.E);
    }


    public void damageDirection(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        for(int i = 0; i < range; i++){
            Decor decor = game.getWorld().get(nextPos);
            //System.out.println(nextPos + " " + direction);
            if(decor != null) {
                if(decor.getMovability()) {
                    game.getWorld().clear(nextPos);
                    game.getWorld().setChangeMap(true);
                    break;
                }
                else if(decor.getCollectables()) {
                    if(!(decor instanceof Key) &&  !(decor instanceof Princess)) {
                        game.getWorld().clear(nextPos);
                        game.getWorld().setChangeMap(true);
                    }
                }
                else if(!decor.getPassability()) {
                    break;
                }
            }
            if(player.getPosition().x == nextPos.x && player.getPosition().y == nextPos.y && player.getCanBeDamaged()) {
                System.out.println("Player: " + player.getPosition());
                player.changeLives(-1);
            }


            /*if(monster.getPosition() == nextPos)
                monster.changeLives(-1);*/

            for(int j = 0; j < bombs.size(); j++){
                if(bombs.get(j).getPosition().x == nextPos.x && bombs.get(j).getPosition().y == nextPos.y) {
                    bombs.get(j).setLives(0);
                }
            }

            nextPos = direction.nextPosition(nextPos);
        }
    }
}
