package pathfinding.actor.Particles;

import pathfinding.actor.Creatures.Creature;
import pathfinding.Table.Table;
import pathfinding.actor.Actor;
import pathfinding.auxiliar.Node;

public class MeleeTile extends Particle {
    private int tick_counter = 0;
    private final int tick_max = 10;
    private int life = 10;
    private Actor source;

    public MeleeTile(Node pos, Table t, Actor source){
        this.source = source;
        id = 3;
        alive = true;
        this.pos = pos;
        for (int i = 0; i < t.getTile(pos).getContentSize(); ++i){
            Actor obj = t.getTile(pos).getContent(i);
            if (obj instanceof Creature && obj != source){
                ((Creature)obj).setHP(((Creature)obj).getHP()-20);
            }
        }
    }

    @Override
    public void simulate(Table t) {
        tick_counter++;
        --life;
        if (life <= 0){
            alive = false;
        }
        if (tick_counter >= tick_max){
            tick_counter = 0;
        }     
    }
    
}
