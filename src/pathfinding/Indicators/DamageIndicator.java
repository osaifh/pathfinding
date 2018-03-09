package pathfinding.Indicators;

import pathfinding.Table.Table;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

/**
 * Class used to display a determinate amount of damage being dealt to a creature
 */
public class DamageIndicator extends Indicator {
    private final int damage;
    
    public DamageIndicator(int damage, Node pos){
        this.damage = damage;
        this.alive = true;
        this.pos = pos;
        id = Constants.DAMAGE_INDICATOR;
    }
    
    public int getDamage(){
        return damage;
    }
    
    public int getTicks(){
        return tick_counter;
    }
    
    @Override
    public void simulate(Table t) {
        ++tick_counter;
        if (tick_counter >= tick_max){
            alive = false;
        }
    }
}
