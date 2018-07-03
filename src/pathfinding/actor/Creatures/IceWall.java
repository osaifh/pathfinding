package pathfinding.actor.Creatures;

import pathfinding.Table.Table;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;

public class IceWall extends Creature {
    private final int tick_max = 30;
    private int tick_counter = 0;
    private int counter = 0;
    private final int DURATION;
    
    public IceWall(Node node, int duration){
        this.pos = node;
        this.DURATION = duration;
        this.hp = 200;
        this.maxHP = 200;
        this.alive = true;
        this.id = Constants.ICEWALL_ID;
    }
    
    @Override
    public void simulate(Table t) {
        ++counter;
        if (counter >= DURATION || hp <= 0){
            alive = false;
        }
        //t.getTile(pos).setPassable(!alive);
    }
    
}
