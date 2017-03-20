package pathfinding.actor;

import pathfinding.auxiliar.Node;
import pathfinding.Table.Table;
import java.util.Random;

/**
 *
 * @author Me
 */
public abstract class Creature implements Actor {
    Node pos;
    Random randint = new Random();
    Boolean alive;
    final int sight_range = 10;
    final int tick_max = 30;
    int tick_counter = 0;
    int facing_direction;
    int id;
    
    /**
     * Gets the sight range
     * @return Returns the implicit parameter's sight range
     */
    public int getSightRange(){
        return sight_range;
    }
    
    public Boolean isAlive(){
        return alive;
    }
    
    public Actor getActor(){
        return this;
    }

    public Node getNode(){
        return pos;
    }
    
    public int getID(){
        return id;
    }
    
    public void setNode(Node n){
        pos = n;
    }
    
    public void setNode(int x, int y){
        pos.set(x, y);
    }
    
    public Boolean equalNode(Actor x){
        return pos.compare(x.getNode());
    }
    
    /**
     * Moves a single tile in one moveDirection
     * @param tab the table where we move
     * @param i the direction to move
     */
    public void iMove(Table tab, int i){
        //Fix this at some point, this could be done better
        setFacingDirection(i);
        Node npos = new Node(pos);
        if (npos.iMove(tab, i)){
            tab.getTile(pos).clearContent();
            pos.iMove(tab,i);
            tab.getTile(pos).setContent(this);
        }
        else if (tab.checkInteractable(npos.nodeMove(tab, i))){
            ((Interactable)tab.getTile(npos.nodeMove(tab, i)).getContent()).interact(tab);
        }
  
    }
    
    public int getFacingDirection(){
        return facing_direction;
    }
    
    public void setFacingDirection(int d){
        facing_direction = d;
    }
    
}