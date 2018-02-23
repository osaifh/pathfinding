package pathfinding.Table;

import java.util.HashMap;
import javax.swing.ImageIcon;
import pathfinding.auxiliar.Constants;

/**
 *
 * @author Alumne
 */
public class Sprites {
    public static final HashMap<Integer,ImageIcon> TERRAIN_MAP;
    public static final HashMap<Integer,ImageIcon> SPRITE_MAP;
    public static final HashMap<Integer,ImageIcon> UI_MAP;
    static {
        TERRAIN_MAP = new HashMap<>();
        TERRAIN_MAP.put(Constants.BLACK_ID, FilePaths.black);
        TERRAIN_MAP.put(Constants.WALL_ID, FilePaths.wall);
        TERRAIN_MAP.put(2, FilePaths.red);
        TERRAIN_MAP.put(4, FilePaths.white);
        TERRAIN_MAP.put(10, FilePaths.white);
        TERRAIN_MAP.put(Constants.DEEP_WATER_ID, FilePaths.deepWater);
        TERRAIN_MAP.put(Constants.WATER_ID, FilePaths.water);
        TERRAIN_MAP.put(Constants.SHALLOW_WATER_ID, FilePaths.shallowWater);
        TERRAIN_MAP.put(Constants.SAND_ID, FilePaths.sand);
        TERRAIN_MAP.put(Constants.BROWN_ID, FilePaths.brown);
        TERRAIN_MAP.put(Constants.GRASS_BASE_ID, FilePaths.grassBase);
        TERRAIN_MAP.put(Constants.GRASS_ALT1_ID, FilePaths.grass2);
        TERRAIN_MAP.put(Constants.GRASS_ALT2_ID, FilePaths.grass3);
        TERRAIN_MAP.put(Constants.ROCK_ID, FilePaths.rock);
        TERRAIN_MAP.put(Constants.GROUND_ID, FilePaths.groundTile);
        SPRITE_MAP = new HashMap<>();
        SPRITE_MAP.put(Constants.PLAYER_ID, FilePaths.oldPlayer);
        SPRITE_MAP.put(3, FilePaths.red);
        SPRITE_MAP.put(Constants.FOOD_ID, FilePaths.food);
        SPRITE_MAP.put(Constants.DOOR_OPEN_ID, FilePaths.door_open);
        SPRITE_MAP.put(Constants.DOOR_CLOSED_ID, FilePaths.door_closed);
        SPRITE_MAP.put(Constants.GHOST_S_ID,FilePaths.ghostS);
        SPRITE_MAP.put(Constants.GHOST_N_ID,FilePaths.ghostN);
        SPRITE_MAP.put(Constants.GHOST_E_ID,FilePaths.ghostE);
        SPRITE_MAP.put(Constants.GHOST_W_ID,FilePaths.ghostW);
        SPRITE_MAP.put(Constants.RED_PLAYER_ID,FilePaths.oldPlayer_red);
        SPRITE_MAP.put(Constants.BLUE_PLAYER_ID,FilePaths.oldPlayer_blue);
        SPRITE_MAP.put(Constants.GREEN_PLAYER_ID,FilePaths.oldPlayer_green);
        SPRITE_MAP.put(Constants.HIGHLIGHT,FilePaths.highlight);
        
        UI_MAP = new HashMap<>();
        UI_MAP.put(0,FilePaths.blank);
        UI_MAP.put(1,FilePaths.day);
        UI_MAP.put(2,FilePaths.night);
        UI_MAP.put(3,FilePaths.lightsOn);
        UI_MAP.put(4,FilePaths.lightsOff);
        UI_MAP.put(5,FilePaths.selected);
        UI_MAP.put(6,FilePaths.UIblack);
        UI_MAP.put(7,FilePaths.blue);
    }
}
