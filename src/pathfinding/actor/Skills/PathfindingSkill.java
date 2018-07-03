package pathfinding.actor.Skills;

import pathfinding.Controller;
import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;
public class PathfindingSkill extends Skill {

    public PathfindingSkill(){
        this.icon = Constants.DEBUG_SKILL_ID;
    }
    
    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        
    }
    
    public void activate(Node origin, Node target, Table table, ActorList actorList, Controller controller) {
        table.getTile(target).setID(Constants.GREENMARK);
        Node[] nodes = table.mark_iBFS(origin, target, controller);
        
    }
    
}
