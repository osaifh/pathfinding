package pathfinding.actor.Particles;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;

public class MeleeSource {
    private Node origin, target;
    private double angle;
    private double d_x, d_y;
    private final int DAMAGE, LENGTH;
    
    public MeleeSource(Node origin, Node target, Table table, ActorList actorList, int damage, int length){
        this.DAMAGE = damage;
        this.LENGTH = length;
        //calculate the angle
        this.target = target;
        this.origin = origin;
        double a = target.getY() - origin.getY();
        double b = target.getX() - origin.getX();
        
        if (!(a == 0 && b == 0)){
           
            double alpha = Math.atan(
                    -b / a 
            );
            angle = -alpha;
            
            d_x = origin.getX();
            d_y = origin.getY();

            if (a < 0) angle = Math.toRadians(180) + angle;

            //random stuff delete this later or something
            /*
            if (Constants.RANDOM.nextInt(10)<5)
                angle += Math.toRadians(Constants.RANDOM.nextInt(31) - 15);
                */
            TraceShot(table,actorList);
        }
        
    }
    
    private void TraceShot(Table table, ActorList actorList){
        Node next = new Node(origin);
        int current = 0;
        while (table.checkPassable(next) && current < LENGTH){
            current++;
            
            d_y += Math.cos(angle);
            d_x += Math.sin(angle);
            
            int x = (int) Math.round(d_x);
            int y = (int) Math.round(d_y);
            
            if (next.getX() != x || next.getY() != y){
                next = new Node(x,y);
                if (table.checkPassable(next)){
                    //I definetely should stop passing the actorList around
                    ShotTile shotTile = new ShotTile(next, actorList, DAMAGE);
                    table.add(shotTile);
                    actorList.add(shotTile, true);
                    shotTile.Collision(table);
                }
            }
        }
    }
    
}
