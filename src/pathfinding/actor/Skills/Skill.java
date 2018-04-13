package pathfinding.actor.Skills;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.auxiliar.Node;

public abstract class Skill {
    int currentCooldown, maxCooldown;
    int icon;
    boolean toggle;
    boolean toggled;
    
    public int getCurrentCooldown(){
        return currentCooldown;
    }
    
    public void addCurrentCooldown(int addition){
        currentCooldown += addition;
    }
    
    public int getMaxCooldown(){
        return maxCooldown;
    }
    
    public int getIcon(){
        return icon;
    }
    
    public boolean isToggle(){
        return toggle;
    }
    
    //public abstract void activate2(Object... args);
    
    public abstract void activate(Node origin, Node target, Table table, ActorList actorList);
    
}
