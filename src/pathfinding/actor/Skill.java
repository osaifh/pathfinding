package pathfinding.actor;

/**
 *
 * @author Alumne
 */
public abstract class Skill {
    private int cooldown;
    
    public int getCooldown(){
        return cooldown;
    }
    
    public abstract void activate();
}
