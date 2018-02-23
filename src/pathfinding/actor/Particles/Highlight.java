package pathfinding.actor.Particles;

import pathfinding.Table.Table;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;
public class Highlight extends Particle{
    private int tick_counter = 0;
    private final int tick_max = 10;
    
    public Highlight(Node node){
        this.pos = node;
        this.id = Constants.HIGHLIGHT;
    }
    
    @Override
    public void simulate(Table t) {
        if (tick_counter >= tick_max){
            tick_counter = 0;
            alive = false;
        }
    }
    
}
