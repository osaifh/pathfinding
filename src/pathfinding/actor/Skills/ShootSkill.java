
package pathfinding.actor.Skills;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Particles.ShotSource;
import pathfinding.auxiliar.Node;

import pathfinding.auxiliar.Constants;

public class ShootSkill extends Skill {

    public ShootSkill(){
        this.icon = Constants.FOOD_ID;
    }   
    
    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        ShotSource shotSource = new ShotSource(origin,target,table,actorList);
    }
    
}
