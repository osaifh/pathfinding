package pathfinding.actor;

import pathfinding.Table.Camera;
import pathfinding.Table.Table;
import pathfinding.auxiliar.Node;

/**
 *
 * @author Alumne
 */
public interface Actor {
    
    public Actor getActor();
    
    public Node getNode();
    
    public int getID();
    
    public void setNode(Node n);
    
    public void setNode(int x, int y);
    
    public boolean equalNode(Actor x);
    
    public boolean isAlive();
    
    public void simulate(Table t);
    
    public void print();
    
}
