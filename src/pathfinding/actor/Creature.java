package pathfinding.actor;

import pathfinding.auxiliar.Node;
import pathfinding.Table.Table;
import java.util.Random;
import pathfinding.Table.Camera;

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
    int hp, maxHP;
    Camera camera;
    
    /**
     * Gets the sight range
     * @return Returns the implicit parameter's sight range
     */
    public int getSightRange(){
        return sight_range;
    }
    
    @Override
    public boolean isAlive(){
        return alive;
    }
    
    @Override
    public Actor getActor(){
        return this;
    }

    @Override
    public Node getNode(){
        return pos;
    }
    
    @Override
    public int getID(){
        return id;
    }
    
    public int getHP(){
        return hp;
    }
    
    public int getmaxHP(){
        return maxHP;
    }
    
    public int getFacingDirection(){
        return facing_direction;
    }
    
    @Override
    public void setNode(Node n){
        pos = n;
    }
    
    /**
     *
     * @param x
     * @param y
     */
    @Override
    public void setNode(int x, int y){
        pos.set(x, y);
    }
    
    public void setHP(int hp){
        this.hp = hp;
    }
    
    @Override
    public boolean equalNode(Actor x){
        return pos.compare(x.getNode());
    }
    
    /**
     * Moves a single tile in one direction
     * @param tab the table where we move
     * @param i the direction to move
     */
    public void iMove(Table tab, int i){
        setFacingDirection(i);
        Node npos = new Node(pos);
        if (npos.iMove(tab, i)){
            tab.getTile(pos).clearMatchingContent(this);
            pos.iMove(tab,i);
            tab.getTile(pos).addContent(this);
            //experimental code: force the camera to move whenever the creature that is locked on moves
            if (camera!=null){
                camera.updatePosition();
            }
        }
        else if (tab.valid(npos)){
            //npos.nodeMove(tab, i);
            for (int j = 0; j < tab.getTile(npos).getContentSize(); ++j) {
                if (tab.getTile(npos).getContent(j) instanceof Interactable){
                    ((Interactable) tab.getTile(npos).getContent(j)).interact(tab);
                }
            }
        }
    }
    
    public void iMove(Table tab, Node n){
        setFacingDirection(pos.relativeDirection(n));
        Node npos = new Node(n);
        
        if (npos.iMove(tab, n)){
            tab.getTile(pos).clearMatchingContent(this);
            pos.iMove(tab,n);
            tab.getTile(pos).addContent(this);
            //experimental code: force the camera to move whenever the creature that is locked on moves
            if (camera!=null){
                camera.updatePosition();
            }
        }
        else {
            for (int j = 0; j < tab.getTile(n).getContentSize(); ++j) {
                if (tab.getTile(n).getContent(j) instanceof Interactable){
                    ((Interactable) tab.getTile(n).getContent(j)).interact(tab);
                }
            }
        }
    }
    
    public void setCamera(Camera camera){
        this.camera = camera;
    }
    
    public void setFacingDirection(int d){
        facing_direction = d;
    }
    
}