package pathfinding.actor.Particles;

import pathfinding.Indicators.DamageIndicator;
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
public class ShotTile extends Particle{
    private LightSource l;
    private int tick_counter = 0;
    private final int tick_max = 10;
    private ActorList actorList;
    
    /**
     * Default constructor
     * @param node the position of the tile
     * @param actorList the list where the actors are.
     */
    public ShotTile(Node node, ActorList actorList){
        this.id = 3;
        this.pos = node;
        this.alive = true;
        l = new LightSource(2,pos.getX(),pos.getY());
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
                ((Creature)obj).setHP(((Creature)obj).getHP()-20);
                //t.getTile(pos).setID(Constants.RED_ID);
                DamageIndicator damageIndicator = new DamageIndicator(20, pos);
                t.getTile(pos).addContent(damageIndicator);
                actorList.add(damageIndicator, alive);
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
