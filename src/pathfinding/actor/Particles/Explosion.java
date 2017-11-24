package pathfinding.actor.Particles;

import pathfinding.actor.Particles.Particle;
import java.util.ArrayList;
import pathfinding.Table.Table;
import pathfinding.actor.Actor;
import pathfinding.actor.ActorList;
import pathfinding.actor.Particles.ExplosionTile;
import pathfinding.auxiliar.Node;

/**
 *
 * @author Alumne
 */
public class Explosion extends Particle {
    private int tick_counter = 0;
    private final int tick_max = 30;
    private ArrayList<ExplosionTile> aoe_list;
    private Actor source;
    private int size;
    
    public Explosion(Actor source, Node pos, Table t, ActorList objList, int size){
        aoe_list = new ArrayList<>();
        this.source = source;
        this.pos = pos;
        this.size = size;
        alive = true;
        if (t.getTile(pos).isPassable()){
            ExplosionTile aoe = new ExplosionTile(this, t, objList, pos, size);
            objList.add(aoe, alive);
            aoe_list.add(aoe);
            t.add(aoe);
        }
    }
    
    public ArrayList<ExplosionTile> getList(){
        return aoe_list;
    }
    
    public boolean matchingNode(Node n){
        for (int i = 0; i < aoe_list.size(); ++i){
            if (n.equals(aoe_list.get(i).getNode())) return false;
        }
        return true;
    }

    @Override
    public void simulate(Table t) {
        tick_counter++;
        if (tick_counter >= tick_max){
            tick_counter = 0;
            boolean done = true;
            for (int i = 0; i < aoe_list.size() && !done;++i){
                ExplosionTile x = aoe_list.get(i);
                done = !x.isAlive();
            }
            if (done) alive = false;
        }
    }

    @Override
    public void print() {
        System.out.println("Explosion");
    }
    
}
