package pathfinding.actor.Skills;

import pathfinding.Listeners.IndicatorListener;
import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Creatures.Guard;
import pathfinding.actor.Creatures.Mob;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;

public class CreateMobSkill extends Skill {
    
    public CreateMobSkill(){
        this.icon = Constants.BLUE_PLAYER_ID;
        this.toggle = false;
    }

    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        Mob mob = new Mob(target.getX(),target.getY());
        table.add(mob);
        actorList.add(mob, true);
    }

}
