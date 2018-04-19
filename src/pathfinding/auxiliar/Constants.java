package pathfinding.auxiliar;

import java.util.HashMap;
import java.util.Random;

/**
 * This class is used to store some constant values
 * @author 
 */
public class Constants {
    
    //DIRECTIONS
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
    
    public static final HashMap<String,Node> DIRECTIONS_WORD;
    static {
        DIRECTIONS_WORD = new HashMap<>();
        DIRECTIONS_WORD.put("NW",new Node(-1,-1));
        DIRECTIONS_WORD.put("N",new Node(-1,0));
        DIRECTIONS_WORD.put("NE",new Node(-1,1));
        DIRECTIONS_WORD.put("W",new Node(0,-1));
        DIRECTIONS_WORD.put("E",new Node(0,1));
        DIRECTIONS_WORD.put("SW",new Node(1,-1));
        DIRECTIONS_WORD.put("S",new Node(1,0));
        DIRECTIONS_WORD.put("SE",new Node(1,1));
    }
    
    public static final int NW = 0;
    public static final int N = 1;
    public static final int NE = 2;
    public static final int W = 3;
    public static final int E = 4;
    public static final int SW = 5;
    public static final int S = 6;
    public static final int SE = 7;
    
    //LIGHT
    public static final int MAX_LIGHT = 100;
    
    //RANDOM
    public static final Random RANDOM = new Random();
    
    //IDs
    //objects
    public static final int PLAYER_ID = 2;
    public static final int FOOD_ID = 4;
    public static final int DOOR_OPEN_ID = 7;
    public static final int DOOR_CLOSED_ID = 8;
    public static final int GHOST_S_ID = 10;
    public static final int GHOST_N_ID = 11;
    public static final int GHOST_E_ID = 12;
    public static final int GHOST_W_ID = 13;
    public static final int EXPLOSION_ID = 21;
    
    public static final int RED_PLAYER_ID = 30;
    public static final int BLUE_PLAYER_ID = 31;
    public static final int GREEN_PLAYER_ID = 32;
    public static final int HIGHLIGHT = 40;
    
    //terrain
    public static final int BLACK_ID = -1;
    public static final int WALL_ID = 1;
    public static final int RED_ID = 3;
    public static final int DEEP_WATER_ID = 100;
    public static final int WATER_ID = 101;
    public static final int SHALLOW_WATER_ID = 102;
    public static final int SAND_ID = 103;
    public static final int BROWN_ID = 104;
    public static final int GRASS_BASE_ID = 105;
    public static final int GRASS_ALT1_ID = 106;
    public static final int GRASS_ALT2_ID = 107;
    public static final int ROCK_ID = 108;
    public static final int GROUND_ID = 109;
    
    //indicators
    public static final int DAMAGE_INDICATOR = 1000;
}
