package pathfinding.actor.Skills;

import pathfinding.Table.Table;
import pathfinding.Table.Terrain;
import pathfinding.Table.Tile;
import pathfinding.actor.ActorList;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;

public class DrawClearSkill extends Skill {
    final int SIZE = 50;
    
    public DrawClearSkill(){
        this.icon = Constants.DEBUG_SKILL_ID;
    }
    
    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        Terrain terrain = new Terrain("WHITE",true,false,Constants.WHITEMARK,0.0f);
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                Node node = target.getNodeCopy();
                node.add(i, j);
                table.getTile(node).clear();
                table.getTile(node).setID(Constants.WHITEMARK);
            }
        }
    }
    
}
