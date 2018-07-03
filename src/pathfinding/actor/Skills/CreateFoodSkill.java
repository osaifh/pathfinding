package pathfinding.actor.Skills;

import java.io.Serializable;
import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Interactables.genericObject;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

public class CreateFoodSkill extends Skill implements Serializable {
    
    public CreateFoodSkill(){
        this.icon = Constants.DEBUG_SKILL_ID;
    }

    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        genericObject food = new genericObject(Constants.FOOD_ID,target.getX(),target.getY());
        table.add(food);
    }
}
