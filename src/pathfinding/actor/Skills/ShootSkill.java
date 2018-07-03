
package pathfinding.actor.Skills;

import java.io.Serializable;
import pathfinding.Audio.AudioConstants;
import pathfinding.Audio.AudioManager;
import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Particles.ShotSource;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;

public class ShootSkill extends Skill implements Serializable {
    private final int DAMAGE;
    
    //TODO: change damage from static to a certain formula which should use parameters from skillData when it's actually implemented
    public ShootSkill(int damage){
        this.DAMAGE = damage;
        this.icon = Constants.SHOOT_SKILL_ID;
        this.toggle = true;
    }
    
    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        ShotSource shotSource = new ShotSource(origin,target,table,actorList, DAMAGE);
        
        AudioManager.getInstance().playSound(AudioConstants.SHOOT_SOUND);
    }
    
}
