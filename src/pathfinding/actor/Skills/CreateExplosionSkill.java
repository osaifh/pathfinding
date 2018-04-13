package pathfinding.actor.Skills;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Particles.Explosion;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

public class CreateExplosionSkill extends Skill {

    public CreateExplosionSkill(){
        this.icon = Constants.RED_ID;
        maxCooldown = 200;
        currentCooldown = 0;
    }
    
    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        currentCooldown = maxCooldown;
        Explosion ex = new Explosion( null, target, table, actorList, 5);
        table.add(ex);
        actorList.add(ex, true);
    }
    
}
