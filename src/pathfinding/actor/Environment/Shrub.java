package pathfinding.actor.Environment;

import pathfinding.Table.Table;
import pathfinding.actor.Actor;
import pathfinding.auxiliar.Node;

public class Shrub implements Actor {
    int id;
    Node pos;
    final int tick_max = 30;
    int tick_counter = 0;

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public Node getNode() {
        return pos;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setNode(Node n) {
        pos = n;
    }

    @Override
    public void setNode(int x, int y) {
        pos.set(x, y);
    }

    @Override
    public boolean equalNode(Actor x) {
        return pos.equals(x);
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public void simulate(Table t) {
        tick_counter++;
        if (tick_counter >= tick_max){
            tick_counter = 0;
        }
    }
    
}
