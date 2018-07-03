package pathfinding.actor.Particles;

import pathfinding.Table.Table;
import pathfinding.actor.Actor;
import pathfinding.actor.Creatures.Creature;
import pathfinding.actor.LightSource;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

public class ForcePush extends Particle {
    private final int tick_max = 30;
    private int tick_counter = 0;
    private int speed;
    private int facing_direction;
    private Node target, origin;
    private LightSource l;
    private final int LIMIT = 15;
    private int stepCounter;
    // angle stuff
    private double angle;
    double d_x, d_y;
    
    public ForcePush(Node pos,  Node target, int speed){
        id = Constants.BLUEMARK;
        this.pos = pos;
        this.speed = speed;
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
    
    public boolean collision(Node n, Table t){
        if (t.valid(n) && !t.getTile(n).isEmpty()){
            Actor obj = t.getActor(n);
            if (obj != null){
                int dir = pos.relativeDirection(obj.getNode());
                Node next = obj.getNode().getNodeCopy();
                next.add(Constants.DIRECTIONS.get(dir));
                
                if (t.valid(next)){
                    if (obj instanceof Creature){
                        //BUG: may push things out of the map and break things
                        if (t.checkPassable(next)){
                            ((Creature) obj).iMove(t, dir);
                            return true;
                        }
                        //collides with a wall
                        else {
                            //TODO: change this to DAMAGE later
                            ((Creature) obj).addHP(-30);
                        }
                    }
                    else if (obj instanceof Particle){
                        ((Particle)obj).alive = false;
                    }
                }
            }
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
            if (alive){
                d_y += Math.cos(angle);
                d_x += Math.sin(angle);
                
                int x = (int) Math.round(d_x);
                int y = (int) Math.round(d_y);
                Node next = new Node(x,y);
                t.getTile(pos).clearMatchingContent(this);
                collision(next, t);
                if (pos.iMove(t, next)){
                    t.getTile(pos).addContent(this);
                    facing_direction = pos.relativeDirection(next);
                }
                else {
                    alive = collision(next,t);
                }
                
                ++stepCounter;
                if (stepCounter >= LIMIT){
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
