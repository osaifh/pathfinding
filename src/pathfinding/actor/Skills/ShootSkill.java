
package pathfinding.actor.Skills;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Particles.ShotSource;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;

public class ShootSkill extends Skill {
    private final int DAMAGE;
    
    //TODO: change damage from static to a certain formula which should use parameters from skillData when it's actually implemented
    public ShootSkill(int damage){
        this.DAMAGE = damage;
        this.icon = Constants.FOOD_ID;
    }
    
    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        ShotSource shotSource = new ShotSource(origin,target,table,actorList, DAMAGE);
    }
    
}
