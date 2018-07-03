package pathfinding.actor.Particles;

import pathfinding.Audio.AudioConstants;
import pathfinding.Audio.AudioManager;
import pathfinding.ControllerManager;
import pathfinding.Table.Table;
import pathfinding.actor.Actor;
import pathfinding.actor.Creatures.Creature;
import pathfinding.actor.LightSource;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

public class Grenade extends Particle {
    private final int tick_max = 30;
    private int tick_counter = 0;
    private int speed, damage;
    private int facing_direction;
    private Node target, origin;
    private LightSource l;
    private final int LIMIT = 15;
    private int stepCounter;
    // angle stuff
    private double angle;
    double d_x, d_y;
    
    public Grenade(Node pos, Node target, int speed, int damage) {
       id = Constants.BOMB_ID;
        this.pos = pos;
        this.speed = speed;
        this.damage = damage;
        alive = true;
        this.target = target.getNodeCopy();
        this.origin = pos;
        this.facing_direction = pos.relativeDirection(target);
        //calculate the angle
        double a = target.getY() - pos.getY();
        double b = target.getX() - pos.getX();
        
        if (!(a == 0 && b == 0)){
           
            double alpha = Math.atan(
                    -b / a 
            );
            angle = -alpha;
            
            d_x = origin.getX();
            d_y = origin.getY();

            if (a < 0) angle = Math.toRadians(180) + angle;
            
            l = new LightSource(3,pos.getX(),pos.getY());
        }
        else {
            alive = false;
        }
    }
    
    public void prime(){
        AudioManager.getInstance().playSound(AudioConstants.EXPLOSION_SOUND);
        Explosion explosion = new Explosion(this, pos.getNodeCopy(), ControllerManager.getController().getTable(), ControllerManager.getController().getActorList(), 4, 30, 1);
        alive = false;
        ControllerManager.getController().getTable().getTile(pos).clearMatchingContent(this);
    }
    
    @Override
    public void simulate(Table t) {
        tick_counter += speed;
        if (l!= null){
            l.setNode(pos);
            l.cast_light(t);
        }
        if (tick_counter >= tick_max){
            tick_counter = 0;
            if (alive){
                d_y += Math.cos(angle);
                d_x += Math.sin(angle);
                
                int x = (int) Math.round(d_x);
                int y = (int) Math.round(d_y);
                Node next = new Node(x,y);
                t.getTile(pos).clearMatchingContent(this);
                if (pos.iMove(t, next)){
                    t.getTile(pos).addContent(this);
                    facing_direction = pos.relativeDirection(next);
                }
                else {
                    prime();
                }
                
                //lower speed at 1/2 speed
                if (stepCounter%2 == 0){
                    speed--;
                }
                ++stepCounter;
                if (stepCounter >= LIMIT || speed <= 2){
                    prime();
                }
                
            }
        }
    }
    
}
