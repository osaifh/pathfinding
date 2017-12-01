package pathfinding.actor.Particles;

import pathfinding.actor.Creatures.Creature;
import pathfinding.Table.Table;
import pathfinding.actor.Actor;
import pathfinding.actor.LightSource;
import pathfinding.auxiliar.Node;

/**
 *
 * @author Alumne
 */
public class Bullet extends Particle {
    private final int tick_max = 30;
    private int tick_counter = 0;
    private int speed;
    private int facing_direction;
    private boolean complex;
    private Node target, origin;
    private float m, n;
    private LightSource l;
    
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
        float a = target.getX() - pos.getX();
        float b = target.getY() - pos.getY();
        System.out.println("a: " + a + " b: " + b);
        if (a == 0){
            a = 1;
        }
        m = b / a;
        n = b - a * m;
        System.out.println("m: " + m + " n: " + a);
        l = new LightSource(3,pos.getX(),pos.getY());
    }
    
    public void collision(Node n, Table t){
        if (t.valid(n) && !t.getTile(n).isEmpty()){
            Actor obj = t.getActor(n);
            if (obj instanceof Creature){
                ((Creature)obj).setHP(((Creature)obj).getHP()-20);
            }
        }
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
                int x = pos.getX()+1;
                int y = (int) (m*x +n);
                Node next = new Node(x,y);
                //System.out.println();
                //pos.iMove(t, next);
                facing_direction = pos.relativeDirection(next);
            }
            Node npos = new Node(pos);
            t.getTile(pos).clearMatchingContent(this);
            if (facing_direction == -1){
                collision(pos,t);
                alive = false;
            }
            else
            if (npos.iMove(t, facing_direction) && t.getTile(npos).isEmpty()){
                pos.iMove(t, facing_direction);
                //t.getTile(npos).addContent(this);
            } else {
                collision(npos,t);
                alive = false;
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
