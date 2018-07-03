package pathfinding.Listeners;

import java.io.Serializable;
import pathfinding.Indicators.DamageIndicator;
import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.auxiliar.Node;

public class IndicatorListener implements Serializable {
    private ActorList actorList;
    private Table table;
            
    public IndicatorListener(ActorList actorList, Table table){
        this.actorList = actorList;
        this.table = table;
    }
    
    public void notifyCreateIndicator(Node node, int damage){
        DamageIndicator damageIndicator = new DamageIndicator(damage, node.getNodeCopy());
        table.getTile(node).addContent(damageIndicator);
        actorList.add(damageIndicator, true);
    }
}
