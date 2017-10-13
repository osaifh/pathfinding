package pathfinding.Table;

import java.util.ArrayList;
import pathfinding.actor.ActorList;
import pathfinding.actor.LightSource;
import pathfinding.auxiliar.Node;
import java.util.Random;
/**
 *
 * @author Alumne
 */
public class Site {
    private Node[] corners;
    private Node centre;
    private Table t;
    private int size;
    private ActorList objList, lightList;
    private Random random;
    
    public Site(Node start, Table t, ActorList objList, ActorList lightList){
        random = new Random();
        this.t = t;
        size = 40;
        corners = new Node[4];
        corners[0] = start;
        corners[1] = new Node(start.getX()+size,start.getY());
        corners[2] = new Node(start.getX(),start.getY()+size);
        corners[3] = new Node(start.getX()+size,start.getY()+size);
        boolean valid = true;
        this.objList = objList;
        this.lightList = lightList;
        for (Node n : corners){
            if (!t.valid(n)) valid = false;
        }
        if (valid){
            centre = new Node(start.getX()+size/2, start.getY()+size/2);
            int x = start.getX();
            int y = start.getY();
            Tile actTile;
            for (int i = 0; i <= size; ++i){
                for (int j = 0; j <= size; ++j){
                    actTile = t.getTile(x+i,y+j);
                    actTile.clear();
                    actTile.setID(10);
                }
            }
            
            for (Node n : corners){
                LightSource ln = new LightSource(10,n.getX(),n.getY());
                t.add(ln);
                lightList.add(ln,true);
                ln.cast_light(t);
            }
            /*
            boolean lightedUp;
            do {
                lightedUp = true;
                System.out.println("entered do while");

                for (int i = 0; i <= size && lightedUp; ++i){
                    for (int j = 0; j <= size && lightedUp; ++j){
                        actTile = t.getTile(x+i,y+j);
                        System.out.println(actTile.getLight());
                        if (actTile.getLight()<=10){
                            System.out.println("tile: x=" + (x+i) + " y=" + (y+j));
                            lightedUp = false;
                            LightSource ln = new LightSource(5,x+i,y+j);
                            t.add(ln);
                            lightList.add(ln,true);
                            ln.cast_light(t);
                            System.out.println("Light that shit up");
                        }
                    }
                }
            } while (!lightedUp);*/
        }
    }
    
}
