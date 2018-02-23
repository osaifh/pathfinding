package pathfinding.actor.Particles;

import pathfinding.Table.Table;
import pathfinding.actor.Actor;
import pathfinding.actor.Creatures.Creature;
import pathfinding.actor.LightSource;
import pathfinding.auxiliar.Node;

public class ShotTile extends Particle{
    private LightSource l;
    private int tick_counter = 0;
    private final int tick_max = 10;
    
    public ShotTile(Node node){
        this.id = 3;
        this.pos = node;
        this.alive = true;
        l = new LightSource(2,pos.getX(),pos.getY());
    }
    
    public void Collision(Table t){
        if (t.valid(pos) && !t.getTile(pos).isEmpty()){
            Actor obj = t.getActor(pos);
            if (obj != null && obj instanceof Creature){
                ((Creature)obj).setHP(((Creature)obj).getHP()-20);
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
