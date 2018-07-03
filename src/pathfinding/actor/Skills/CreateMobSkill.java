package pathfinding.actor.Skills;

import java.io.Serializable;
import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Creatures.Mob;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;

public class CreateMobSkill extends Skill implements Serializable {
    private Mob trackingMob;
    
    public CreateMobSkill(){
        this.icon = Constants.MOB_SKILL_ID;
        this.toggle = false;
    }

    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        Mob mob = new Mob(target.getX(),target.getY());
        table.add(mob);
        actorList.add(mob, true);
        if (trackingMob == null){
            trackingMob = mob;
        }
        else {
            mob.setTracking(trackingMob);
        }
    }

}
