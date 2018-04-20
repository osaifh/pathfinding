package pathfinding.actor.Skills;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.LightSource;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

public class CreateLightSkill extends Skill {

    public CreateLightSkill(){
        this.icon = Constants.LIGHT_ICON_ID;
    }
    
    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        LightSource ln = new LightSource(10,target.getX(),target.getY());
        actorList.add(ln,true);
    }
    
}
