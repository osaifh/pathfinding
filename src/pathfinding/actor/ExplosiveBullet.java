/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinding.actor;

import pathfinding.Table.Table;
import pathfinding.auxiliar.Node;

/**
 *
 * @author Alumne
 */
public class ExplosiveBullet extends Bullet{
    private ActorList objList;
    
    public ExplosiveBullet(Node n, int facing_direction, int speed, ActorList objList) {
        super(n, facing_direction, speed);
        this.objList = objList;
    }
    
    public ExplosiveBullet(Node n, int speed, Node target, ActorList objList){
        super(n, speed, target);
        this.objList = objList;
    }
    
    @Override
    public void collision(Node n, Table t){
        if (!t.getTile(n).isEmpty()){
            Actor obj = t.getActor(n);
            if (obj instanceof Creature){
                ((Creature)obj).setHP(((Creature)obj).getHP()-20);
            }   
        }
        Explosion e = new Explosion(this, pos, t, objList, 3);
        objList.add(e, true);
        t.add(e);
    }
}
