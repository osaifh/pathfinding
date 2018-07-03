package pathfinding.actor.Skills;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Creatures.IceWall;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;

public class IceWallSkill extends Skill {
    private final int DURATION = 500;
    private int counter;
    private final int MAX_COUNTER = 7;
    
    public IceWallSkill(){
        this.icon = Constants.ICEWALL_SKILL_ID;
        this.maxCooldown = 400;
        this.toggle = true;
    }
    
    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
       if (this.currentCooldown <= 0){
           IceWall iceWall = new IceWall(target, DURATION);
           table.add(iceWall);
           actorList.add(iceWall, true);
           ++counter;
           if (counter >= 7){
               this.currentCooldown = maxCooldown;
               counter = 0;
           }
       }
    }
    
}
