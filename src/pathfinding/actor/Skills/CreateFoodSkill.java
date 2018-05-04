package pathfinding.actor.Skills;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Interactables.genericObject;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

public class CreateFoodSkill extends Skill {
    
    public CreateFoodSkill(){
        this.icon = Constants.RED_ID;
    }

    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        genericObject food = new genericObject(Constants.FOOD_ID,target.getX(),target.getY());
        table.add(food);
    }
}
