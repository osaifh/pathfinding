package pathfinding.actor.Skills;

import java.io.Serializable;
import pathfinding.Listeners.IndicatorListener;
import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Creatures.Guard;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;

public class CreateGuardSkill extends Skill implements Serializable {
    
    public CreateGuardSkill(){
        this.icon = Constants.GUARD_SKILL_ID;
        this.toggle = false;
    }

    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        Guard guard = new Guard(target,actorList);
        guard.addIndicatorListener(new IndicatorListener(actorList,table));
        table.add(guard);
        actorList.add(guard, true);
    }

}
