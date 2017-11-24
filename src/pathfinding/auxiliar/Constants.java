package pathfinding.auxiliar;

import java.util.HashMap;

/**
 * This class is used to store some constant values
 * @author 
 */
public class Constants {
    public static final HashMap<Integer,Node> DIRECTIONS;
     /*
        These are the directions and their respective int value
        0 1 2  NW N NE
        3 X 4  W  X  E
        5 6 7  SW S SE
    */
    static {
        DIRECTIONS = new HashMap<>();
        DIRECTIONS.put(0,new Node(-1,-1));
        DIRECTIONS.put(1,new Node(-1,0));
        DIRECTIONS.put(2,new Node(-1,1));
        DIRECTIONS.put(3,new Node(0,-1));
        DIRECTIONS.put(4,new Node(0,1));
        DIRECTIONS.put(5,new Node(1,-1));
        DIRECTIONS.put(6,new Node(1,0));
        DIRECTIONS.put(7,new Node(1,1));
    }
}
