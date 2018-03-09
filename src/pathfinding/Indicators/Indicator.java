package pathfinding.Indicators;

import pathfinding.actor.Actor;
import pathfinding.auxiliar.Node;

/**
 * Generic indicator abstract class that all indicators must extend
 */
abstract class Indicator implements Actor {
    Node pos;
    int id;
    boolean alive;
    final int tick_max = 15;
    int tick_counter = 0;
    
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
