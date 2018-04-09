package pathfinding.actor.Skills;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.auxiliar.Node;

public abstract class Skill {
    int cooldown;
    int icon;
    
    public int getCooldown(){
        return cooldown;
    }
    
    public int getIcon(){
        return icon;
    }
    
    public abstract void activate(Node origin, Node target, Table table, ActorList actorList);
    
}
