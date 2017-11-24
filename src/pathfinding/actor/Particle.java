package pathfinding.actor;

import pathfinding.Table.Table;
import pathfinding.auxiliar.Node;

/**
 *
 * @author Alumne
 */
public abstract class Particle implements Actor {
    Node pos;
    int id;
    boolean alive;
    
    public Actor getActor(){
        return this;
    };
    
    public Node getNode(){
        return pos;
    };
    
    public int getID(){
        return id;
    };
    
    public void setNode(Node n){
        pos = n;
    };
    
    public void setNode(int x, int y){
        pos.set(x,y);
    };
    
    public boolean equalNode(Actor x){
        return pos.equals(x.getNode());
    };
    
    public boolean isAlive(){
        return alive;
    }
    
}
