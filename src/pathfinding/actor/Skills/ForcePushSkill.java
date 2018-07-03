package pathfinding.actor.Skills;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Particles.ForcePushSource;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;

public class ForcePushSkill extends Skill {

    public ForcePushSkill(){
        this.icon = Constants.FORCEPUSH_SKILL_ID;
        this.maxCooldown = 100;
    }
    
    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        currentCooldown = maxCooldown;
        ForcePushSource forcePushSource = new ForcePushSource(origin.getNodeCopy(), target, table, actorList);
    }
    
}
