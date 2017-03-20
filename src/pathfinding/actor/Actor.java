package pathfinding.actor;

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
    
    public Boolean equalNode(Actor x);
    
    public void simulate(Table t);
    
    public void print();
    
}
