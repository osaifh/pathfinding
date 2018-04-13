package pathfinding.actor.Skills;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

public class CreateWallSkill extends Skill {

    public CreateWallSkill(){
        this.icon = Constants.WALL_ID;
        this.toggle = true;
    }
    
    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        table.getTile(target).setWall();
    }
    
}
