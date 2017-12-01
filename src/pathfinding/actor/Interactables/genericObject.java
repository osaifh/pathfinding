package pathfinding.actor.Interactables;

import pathfinding.Table.Table;
import pathfinding.auxiliar.Node;

/**
 *
 * @author Alumne
 */
public class genericObject extends Interactable {
    
    public genericObject(int id, int x, int y){
        pos = new Node(x,y);
        this.id = id;
    }
    
    @Override
    public void interact(Table t) {
       
    }

    @Override
    public void simulate(Table t) {
        
    }

    
}
