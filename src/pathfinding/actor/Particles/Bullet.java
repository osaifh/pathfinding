package pathfinding.actor.Particles;

import pathfinding.actor.Creatures.Creature;
import pathfinding.Table.Table;
import pathfinding.actor.Actor;
import pathfinding.actor.LightSource;
import pathfinding.auxiliar.Node;

public class Bullet extends Particle {
    private final int tick_max = 30;
    private int tick_counter = 0;
    private int speed;
    private int facing_direction;
    private boolean complex;
    private Node target, origin;
    private LightSource l;
    // angle stuff
    private double angle;
    double d_x, d_y;
    
    public Bullet(Node n, int facing_direction, int speed){
        id = 4;
        pos = n;
        this.facing_direction = facing_direction;
        this.speed = speed;
        alive = true;
        complex = false;
        target = origin = null;
        l = new LightSource(3,pos.getX(),pos.getY());
    }
    
    public Bullet(Node pos, int speed, Node target){
        id = 4;
        this.pos = pos;
        this.speed = speed;
        alive = true;
        complex = true;
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
    
    public boolean collision(Node n, Table t){
        if (t.valid(n) && !t.getTile(n).isEmpty()){
            Actor obj = t.getActor(n);
            if (obj instanceof Creature){
                ((Creature)obj).addHP(-20);
            }
            return true;
        }
        return false;
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
            if (complex && alive){
                d_y += Math.cos(angle);
                d_x += Math.sin(angle);
                
                int x = (int) Math.round(d_x);
                int y = (int) Math.round(d_y);
                Node next = new Node(x,y);
                t.getTile(pos).clearMatchingContent(this);
                if (collision(next, t)) alive = false;
                if (pos.iMove(t, next)){
                    t.getTile(pos).addContent(this);
                    facing_direction = pos.relativeDirection(next);
                }
                else {
                    collision(next,t);
                    alive = false;
                }
            }
            else {
                Node npos = new Node(pos);
                if (facing_direction == -1){
                    collision(pos,t);
                    alive = false;
                }
                else
                if (npos.iMove(t, facing_direction) && t.getTile(npos).isEmpty()){
                     t.getTile(pos).clearMatchingContent(this);
                    pos.iMove(t, facing_direction);
                    t.getTile(pos).addContent(this);
                } else {
                    collision(npos,t);
                    alive = false;
                }

            }
        }
    }

    @Override
    public boolean equalNode(Actor x){
        return pos.equals(x.getNode());
    }

    @Override
    public boolean isAlive(){
        return alive;
    }
    
}
