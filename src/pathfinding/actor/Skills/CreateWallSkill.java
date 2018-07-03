package pathfinding.actor.Skills;

import java.io.Serializable;
import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

public class CreateWallSkill extends Skill implements Serializable {

    public CreateWallSkill(){
        this.icon = Constants.WALL_SKILL_ID;
        this.toggle = true;
    }
    
    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        table.getTile(target).setWall();
    }
    
}
