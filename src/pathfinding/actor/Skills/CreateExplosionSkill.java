package pathfinding.actor.Skills;

import java.io.Serializable;
import pathfinding.Audio.AudioConstants;
import pathfinding.Audio.AudioManager;
import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Particles.Explosion;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

public class CreateExplosionSkill extends Skill implements Serializable {

    public CreateExplosionSkill(){
        this.icon = Constants.EXPLOSION_SKILL_ID;
        this.maxCooldown = 200;
    }
    
    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        currentCooldown = maxCooldown;
        Explosion ex = new Explosion( null, target, table, actorList, 5);
        table.add(ex);
        actorList.add(ex, true);
        
        AudioManager.getInstance().playSound(AudioConstants.EXPLOSION_SOUND);
    }
    
}
