package pathfinding.actor.Skills;

import java.io.Serializable;
import pathfinding.Controller;
import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Creatures.Monster;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

public class CreateMonsterSkill extends Skill implements Serializable {
    
    public CreateMonsterSkill(){
        this.icon = Constants.GUARD_SKILL_ID;
        this.toggle = false;
    }

    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        //Monster mob = new Monster(target.getNodeCopy(),actorList);
        //table.add(mob);
        //actorList.add(mob, true);
    }
    
    public void activate(Node origin, Node target, Table table, ActorList actorList, Controller controller) {
        Monster mob = new Monster(target.getNodeCopy(),actorList, controller);
        table.add(mob);
        actorList.add(mob, true);
    }
}
