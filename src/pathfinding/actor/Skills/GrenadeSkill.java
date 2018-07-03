package pathfinding.actor.Skills;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Particles.Grenade;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

public class GrenadeSkill extends Skill {
    private final int DAMAGE;
    private final int SPEED = 8;
    
    public GrenadeSkill(int damage){
        this.icon = Constants.EXPLOSION_SKILL_ID;
        this.maxCooldown = 200;
        this.DAMAGE = damage;
    }
    
    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        currentCooldown = maxCooldown;
        Grenade grenade = new Grenade(origin.getNodeCopy(), target, SPEED, DAMAGE);
        table.add(grenade);
        actorList.add(grenade, true);
    }
    
}
