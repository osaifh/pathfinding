package pathfinding.actor.Creatures;

import pathfinding.actor.Creatures.Player;
import pathfinding.actor.Creatures.Creature;
import java.util.ArrayList;
import pathfinding.Table.Table;
import pathfinding.actor.Actor;
import pathfinding.actor.ActorList;
import pathfinding.actor.Particles.Bullet;
import pathfinding.auxiliar.Node;

/**
 *
 * @author Alumne
 */
public class Turret extends Creature {
    private Actor target;
    private int tick_max = 30;
    private ActorList objList;

    public Turret(Node pos, ActorList objList){
        id = 20;
        this.pos = pos.getNodeCopy();
        alive = true;
        hp = 100;
        target = null;
        this.objList = objList;
    }
    
    public void lookAround(Table tab, int range){            
        for (int i = -1; i < 2; ++i){
            for (int j = -1; j < 2; ++j){
                if (i!=0 && j !=0){
                    lookDirection(tab,1,1.0f,0.0f, 0,i,j,0,range);
                    lookDirection(tab,1,1.0f,0.0f, i,0,0,j,range);
                }
            }
        }
    }
    
   
    
    private void lookDirection(Table tab, int row, float start, float end, int xx, int xy, int yx, int yy, int range){
        float newStart = 0.0f;
        if (start < end){
            return;
        }
        boolean blocked = false;
        for (int distance = row; distance <= range && !blocked; distance++){
            int deltaY = -distance;
            for (int deltaX = -distance; deltaX <= 0; deltaX++){
                int currentX = pos.getX() + deltaX * xx + deltaY * xy;
                int currentY = pos.getY() + deltaX * yx + deltaY * yy;
                float leftSlope = (deltaX - 0.5f) / (deltaY + 0.5f);
                float rightSlope = (deltaX + 0.5f) / (deltaY - 0.5f);
                
                if (!(tab.valid(currentX,currentY)) || start < rightSlope){
                    continue;
                } else if (end > leftSlope){
                    break;
                }
                
                Node delta = new Node(currentX,currentY);
                if (Node.distance(pos,delta) <= range){
                    if (tab.getTile(delta).getContentSize()!=0){
                        for (int i = 0; i < tab.getTile(delta).getContentSize() && target == null; ++i){
                            Actor obj = tab.getTile(delta).getContent(i);
                            if (obj instanceof Player){
                                target = obj;
                            }
                        }
                    }
                }
                
                if (blocked){
                    if (tab.getTile(delta).isOpaque()){
                        newStart = rightSlope;
                        continue;
                    } else {
                        blocked = false;
                        start = newStart;
                    }
                } else {
                    if (tab.getTile(delta).isOpaque() && Node.distance(pos,delta) <= 10){
                        blocked = true;
                        lookDirection(tab,distance + 1,start, leftSlope, xx, xy, yx, yy, range);
                        newStart = rightSlope;
                    }
                }
            }
        }
    }
    
    public void shootBullet(Table t, Actor target, int speed){
        Bullet b = new Bullet(pos.getNodeCopy(),speed,target.getNode().getNodeCopy());
        objList.add(b, true);
        t.add(b);
    }
    
    @Override
    public void simulate(Table t) {
        tick_counter++;
        if (tick_counter >= tick_max){
            System.out.println("turret simulate " + (target==null));
            tick_counter = 0;
            if (target == null){
                lookAround(t,8);
                System.out.println("looking around");
            }
            else {
                shootBullet(t,target,4);
                target = null;
            }
        }
    }
    
}
