package pathfinding.actor.Creatures;

import java.io.Serializable;
import java.util.ArrayList;
import pathfinding.auxiliar.Node;
import pathfinding.Table.Table;
import java.util.Random;
import pathfinding.Listeners.IndicatorListener;
import pathfinding.Table.Camera;
import pathfinding.actor.Actor;
import pathfinding.actor.Interactables.Interactable;
import pathfinding.actor.Skills.Skill;

/**
 *
 * @author Me
 */
public abstract class Creature implements Actor, Serializable {
    Node pos;
    Random randint = new Random();
    Boolean alive;
    int sight_range = 10;
    final int tick_max = 30;
    int tick_counter = 0;
    int facing_direction;
    int id;
    int hp, maxHP;
    Camera camera;
    IndicatorListener indicatorListener;
    ArrayList<Skill> skillList;
    
    int curr_charge, charge_max;
    Boolean charge_active;
    Skill charging_skill;
    Node skill_target;
    
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
    
    public void addHP(int hp){
        this.hp += hp;
        //if hp is negative, reset it
        if (this.hp < 0) this.hp = 0;
        //Notifies the indicator listener if the hp change is damaging
        if (indicatorListener != null){
            indicatorListener.notifyCreateIndicator(pos, hp);
        }
    }
    
    @Override
    public boolean equalNode(Actor x){
        return pos.equals(x.getNode());
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
    
    public void addIndicatorListener(IndicatorListener indicatorListener){
        this.indicatorListener = indicatorListener;
    }
    
    public void Simulate(Table t){
        /*
        if (charge_active){
            curr_charge++;
            if (curr_charge >= charge_max){
                charging_skill.activate(pos, skill_target, t, objList);
            }
        }*/ 
    }
}