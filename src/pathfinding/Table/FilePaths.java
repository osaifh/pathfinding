package pathfinding.Table;

import java.io.File;
import javax.swing.ImageIcon;

/**
 * This is a simple class that contains some filepaths used mostly for rendering.
 * It is used to avoid cluttering on other classes.
 * @author Me
 */
public class FilePaths {
    public final static String FILE_PATH = new File("").getAbsolutePath();
    public final static String RESOURCE_PATH = FILE_PATH+"\\resources\\";
    
    public static ImageIcon black = new ImageIcon(RESOURCE_PATH+"Black.png");
    public static ImageIcon red = new ImageIcon(RESOURCE_PATH+"Red.png");
    public static ImageIcon grass = new ImageIcon(RESOURCE_PATH+"Grass.png");
    public static ImageIcon white = new ImageIcon(RESOURCE_PATH+"White.png");
    public static ImageIcon brown = new ImageIcon(RESOURCE_PATH+"Brown.png");
    public static ImageIcon player = new ImageIcon(RESOURCE_PATH+"Player.png");
    public static ImageIcon wall = new ImageIcon(RESOURCE_PATH+"Wall.png");
    public static ImageIcon food = new ImageIcon(RESOURCE_PATH+"Food.png");
    public static ImageIcon door_open = new ImageIcon(RESOURCE_PATH+"Door_Open.png");
    public static ImageIcon door_closed = new ImageIcon(RESOURCE_PATH+"Door_Closed.png");
    public static ImageIcon sand = new ImageIcon(RESOURCE_PATH+"sand.png");
    public static ImageIcon deepWater = new ImageIcon(RESOURCE_PATH+"deepWater.png");
    public static ImageIcon shallowWater = new ImageIcon(RESOURCE_PATH+"shallowWater.png");
    public static ImageIcon water = new ImageIcon(RESOURCE_PATH+"water.png");
    public static ImageIcon rock = new ImageIcon(RESOURCE_PATH+"rock.png");
    
    //UI elements
    public static ImageIcon blank = new ImageIcon(RESOURCE_PATH+"UI\\Blank.png");
    public static ImageIcon night = new ImageIcon(RESOURCE_PATH+"UI\\Night.png");
    public static ImageIcon day = new ImageIcon(RESOURCE_PATH+"UI\\Day.png");
    public static ImageIcon lightsOn = new ImageIcon(RESOURCE_PATH+"UI\\lightsOn.png");
    public static ImageIcon lightsOff = new ImageIcon(RESOURCE_PATH+"UI\\lightsOff.png");
    public static ImageIcon selected = new ImageIcon(RESOURCE_PATH+"UI\\Selected.png");
    public static ImageIcon UIblack = new ImageIcon(RESOURCE_PATH+"UI\\Black.png");
    public static ImageIcon blue = new ImageIcon(RESOURCE_PATH+"UI\\Blue.png");
    
}
