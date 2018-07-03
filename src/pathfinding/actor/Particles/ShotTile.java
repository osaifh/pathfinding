package pathfinding.actor.Particles;

import java.io.Serializable;
import pathfinding.Table.Table;
import pathfinding.actor.Actor;
import pathfinding.actor.ActorList;
import pathfinding.actor.Creatures.Creature;
import pathfinding.actor.LightSource;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

/**
 * An individual tile of a shot
 */
public class ShotTile extends Particle implements Serializable {
    private LightSource l;
    private int tick_counter = 0;
    private final int tick_max = 10;
    private ActorList actorList;
    private final int DAMAGE;
    private final int LIGHT_INTENSITY = 2;
    
    /**
     * Default constructor
     * @param node the position of the tile
     * @param actorList the list where the actors are.
     */
    public ShotTile(Node node, ActorList actorList, int damage){
        this.id = Constants.RED_ID;
        this.DAMAGE = damage;
        this.pos = node;
        this.alive = true;
        l = new LightSource(LIGHT_INTENSITY,pos.getX(),pos.getY());
        this.actorList = actorList;
    }
    
    /**
     * Event called when the shot hits something
     * @param t the table where the object is
     */
    public void Collision(Table t){
        if (t.valid(pos) && !t.getTile(pos).isEmpty()){
            Actor obj = t.getActor(pos);
            if (obj != null && obj instanceof Creature){
                ((Creature)obj).addHP(-DAMAGE);
            }
        }
    }
    
    @Override
    public void simulate(Table t) {
        ++tick_counter;
        l.cast_light(t);
        if (tick_counter >= tick_max){
            tick_counter = 0;
            alive = false;
        }
    }
    
}
