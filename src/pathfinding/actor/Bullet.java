package pathfinding.actor;

import pathfinding.Table.Table;
import pathfinding.auxiliar.Node;

/**
 *
 * @author Alumne
 */
public class Bullet extends Interactable {
    private final int tick_max = 30;
    private int tick_counter = 0;
    private int speed;
    private int facing_direction;
    private boolean complex;
    private Node target, origin;
    private int m;
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
    
    public Bullet(Node n, int speed, Node target){
        id = 4;
        pos = n;
        this.speed = speed;
        alive = true;
        complex = true;
        this.target = target.getNodeCopy();
        this.origin = n;
        this.facing_direction = n.relativeDirection(target);
        /*
        int a = target.getY() - n.getY();
        int b = target.getX() - n.getX();
        if (b == 0){
            b = 1;
        }
        m = a / b;
        */
        l = new LightSource(3,pos.getX(),pos.getY());
    }
    
    public void collision(Node n, Table t){
        if (!t.getTile(n).isEmpty()){
            Actor obj = t.getActor(n);
            if (obj instanceof Creature){
                ((Creature)obj).setHP(((Creature)obj).getHP()-20);
            }
        }
    }
    

    
    @Override
    public void interact(Table t) {
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
                facing_direction = pos.relativeDirection(target);
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
                t.getTile(npos).addContent(this);
            } else {
                collision(npos,t);
                alive = false;
            }
            
        }
    }

    @Override
    public void print() {
        System.out.println("This is a bullet");
    }

    @Override
    public boolean equalNode(Actor x){
        return pos.compare(x.getNode());
    }

    @Override
    public boolean isAlive(){
        return alive;
    }
    
    
}
