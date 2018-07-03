package pathfinding.actor.Skills;

import java.io.Serializable;
import pathfinding.Audio.AudioConstants;
import pathfinding.Audio.AudioManager;
import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.actor.Particles.MeleeSource;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;

public class MeleeSkill extends Skill implements Serializable {
    private final int DAMAGE, LENGTH;
    
    //TODO: change damage from static to a certain formula which should use parameters from skillData when it's actually implemented
    public MeleeSkill(int damage, int length){
        this.DAMAGE = damage;
        this.LENGTH = length;
        this.icon = Constants.SHOOT_SKILL_ID;
        this.toggle = true;
    }
    
    @Override
    public void activate(Node origin, Node target, Table table, ActorList actorList) {
        MeleeSource meleeSource = new MeleeSource(origin,target,table,actorList, DAMAGE, LENGTH);
        
        AudioManager.getInstance().playSound(AudioConstants.SHOOT_SOUND);
    }
    
}