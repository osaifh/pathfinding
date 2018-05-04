package pathfinding.actor.Skills;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Creatures.Monster;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

public class CreateMonsterSkill extends Skill {
    
    public CreateMonsterSkill(){
        this.icon = Constants.GREEN_PLAYER_ID;
        this.toggle = false;
    }

    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        //Monster mob = new Monster(target.getNodeCopy(),actorList);
        //table.add(mob);
        //actorList.add(mob, true);
    }
}
