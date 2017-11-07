package pathfinding.actor;

import pathfinding.Table.Table;
import pathfinding.auxiliar.Node;

public class MeleeTile extends Interactable {
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
    public void interact(Table t) {
    
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

    @Override
    public void print() {
        System.out.println("melee tile");
    }
    
}
