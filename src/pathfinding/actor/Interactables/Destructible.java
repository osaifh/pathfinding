package pathfinding.actor.Interactables;

import pathfinding.actor.Interactables.Interactable;
import pathfinding.Table.Table;

/**
 *
 * @author Alumne
 */
public class Destructible extends Interactable {
    int hp;
    
    public Destructible(int id, int hp){
        this.id = id;
        this.alive = true;
        this.hp = hp;
    }

    public int getHP(){
        return hp;
    }
    
    public void setHp(int hp){
        this.hp = hp;
        if (hp <= 0) alive = false;
    }
    
    @Override
    public void interact(Table t) {}

    @Override
    public void simulate(Table t) {}

}
