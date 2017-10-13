package pathfinding.actor;

import pathfinding.Table.Table;
import pathfinding.auxiliar.Node;

/**
 *
 * @author Alumne
 */
public class Door extends Interactable {
    boolean open;
    boolean alive;
    
    public Door(int x, int y, Table t){
        pos = new Node(x,y);
        open = false;
        t.getTile(pos).setPassable(open);
        t.getTile(pos).setOpaque(!open);
        id = 8;
        alive = true;
    }
        
    public void interact(Table t) {
        open = !open;
        t.getTile(pos).setPassable(open);
        t.getTile(pos).setOpaque(!open);
        if (open) id = 7;
        else id = 8;
    }
    
    public void simulate(Table t) {}

    public void print() {
        System.out.println("this is a door");
    }
    
    @Override
    public boolean equalNode(Actor x){
        return pos.compare(x.getNode());
    }

    @Override
    public boolean isAlive(){
        return alive;
    }
    
}
