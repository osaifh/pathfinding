package pathfinding.actor.Skills;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Creatures.Guard;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;

public class CreateGuardSkill extends Skill {
    
    public CreateGuardSkill(){
        this.icon = Constants.RED_PLAYER_ID;
        this.toggle = false;
    }

    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        Guard guard = new Guard(target,actorList);
        table.add(guard);
        actorList.add(guard, true);
    }

}
