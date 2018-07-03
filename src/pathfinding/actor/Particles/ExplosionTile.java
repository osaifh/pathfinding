package pathfinding.actor.Particles;

import java.io.Serializable;
import pathfinding.actor.Creatures.Creature;
import java.util.ArrayList;
import pathfinding.Table.Table;
import pathfinding.actor.Actor;
import pathfinding.actor.ActorList;
import pathfinding.actor.LightSource;
import pathfinding.auxiliar.Node;

public class ExplosionTile extends Particle implements Serializable {
    private int tick_counter = 0;
    private final int tick_max = 5;
    private int remainingTime = 10;
    private ArrayList<Node> t_list;
    private Explosion source;
    private ExplosionTile previous;
    private int count;
    private boolean done;
    private ActorList objlist;
    private LightSource l;
    private final int DAMAGE = 50;
    private final int LIGHT_INTENSITY = 3;
    
    public ExplosionTile(Explosion source, Table t, ActorList objlist, Node pos, int count){
        t_list = new ArrayList<>();
        this.source = source;
        this.pos = pos;
        alive = true;
        this.count = count;
        done = false;
        this.objlist = objlist;
        id = 3;
        for (int i = 0; i < t.getTile(pos).getContentSize(); ++i){
            Actor obj = t.getTile(pos).getContent(i);
            if (obj instanceof Creature){
                ((Creature)obj).addHP(-DAMAGE);
            }
        }
        l = new LightSource(LIGHT_INTENSITY,pos.getX(),pos.getY());
    }

    @Override
    public void simulate(Table t) {
        tick_counter++;
        --remainingTime;
        l.cast_light(t);
        if (remainingTime <= 0){
            alive = false;
            l = null;
        }
        if (tick_counter >= tick_max){
            tick_counter = 0;
            if (!done){
                done = true;
                if (count > 0){
                    for (Node x : Node.getDirectionsN()){
                        Node y = new Node(pos);
                        y.add(x);
                        if (t.valid(y) && t.getTile(y).isPassable() && source.matchingNode(y)){
                            ExplosionTile aoe = new ExplosionTile(source, t, objlist, y, count-1);
                            objlist.add(aoe, alive);
                            source.getList().add(this);
                            t.add(aoe);
                        }
                    }
                }
            }
        }    
    }
    
}
