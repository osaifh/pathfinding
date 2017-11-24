package pathfinding.Table;

import java.util.HashMap;
import javax.swing.ImageIcon;

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
        TERRAIN_MAP.put(-1, FilePaths.black);
        TERRAIN_MAP.put(1, FilePaths.wall);
        TERRAIN_MAP.put(2, FilePaths.red);
        TERRAIN_MAP.put(4, FilePaths.white);
        TERRAIN_MAP.put(10, FilePaths.white);
        TERRAIN_MAP.put(100, FilePaths.deepWater);
        TERRAIN_MAP.put(101, FilePaths.water);
        TERRAIN_MAP.put(102, FilePaths.shallowWater);
        TERRAIN_MAP.put(103, FilePaths.sand);
        TERRAIN_MAP.put(104, FilePaths.brown);
        TERRAIN_MAP.put(105, FilePaths.grassBase);
        TERRAIN_MAP.put(106, FilePaths.grass2);
        TERRAIN_MAP.put(107, FilePaths.grass3);
        TERRAIN_MAP.put(108, FilePaths.rock);
        TERRAIN_MAP.put(109, FilePaths.groundTile);
        SPRITE_MAP = new HashMap<>();
        SPRITE_MAP.put(2, FilePaths.oldPlayer);
        SPRITE_MAP.put(3, FilePaths.red);
        SPRITE_MAP.put(4, FilePaths.food);
        SPRITE_MAP.put(7, FilePaths.door_open);
        SPRITE_MAP.put(8, FilePaths.door_closed);
        SPRITE_MAP.put(10,FilePaths.ghostS);
        SPRITE_MAP.put(11,FilePaths.ghostN);
        SPRITE_MAP.put(12,FilePaths.ghostE);
        SPRITE_MAP.put(13,FilePaths.ghostW);
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
