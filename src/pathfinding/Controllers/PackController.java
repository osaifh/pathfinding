package pathfinding.Controllers;

import java.util.ArrayList;
import pathfinding.actor.Creatures.Mob;

public class PackController {
    private ArrayList<Mob> mobs;
    
    //plan for the AI:
    //Search for food: do the run away function from the mobs, notify of any available food, move the pack thowards the food
    //at night, sleep
    //if any of the mobs is attacked, set alert on
    //if alert is on, cancel any other moves
    //if alert is on, try to move in the oposite direction of the origin of the attack
    //if there's no path, all the mobs fight back, set combatAlert to true
    //if the target runs away, set alert off
    //if the target is dead, set alert off
    
}
