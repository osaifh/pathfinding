package pathfinding.actor.Skills;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Particles.Bullet;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;

public class ShootBulletSkill extends Skill {
    private static int SPEED = 5;
    
    public ShootBulletSkill(){
        this.icon = Constants.SHOOT_SKILL_ID;
    }
    
    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        Bullet bullet = new Bullet(origin.getNodeCopy(), SPEED, target);
        table.add(bullet);
        actorList.add(bullet, true);
    }
    
}
