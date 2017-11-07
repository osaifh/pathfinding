package pathfinding.Table;

import java.io.File;
import javax.swing.ImageIcon;

/**
 * This is a simple class that contains some filepaths used mostly for rendering.
 * It is used to avoid cluttering on other classes.
 * @author Me
 */
public class FilePaths {

    /**
     *
     */
    public final static String FILE_PATH = new File("").getAbsolutePath();
    public final static String RESOURCE_PATH = "\\resources\\";
    
    public static ImageIcon black = new ImageIcon(FILE_PATH+RESOURCE_PATH+"Black.png");
    public static ImageIcon red = new ImageIcon(FILE_PATH+RESOURCE_PATH+"Red.png");
    public static ImageIcon grass = new ImageIcon(FILE_PATH+RESOURCE_PATH+"Grass.png");
    public static ImageIcon white = new ImageIcon(FILE_PATH+RESOURCE_PATH+"White.png");
    public static ImageIcon brown = new ImageIcon(FILE_PATH+RESOURCE_PATH+"Brown.png");
    public static ImageIcon player = new ImageIcon(FILE_PATH+RESOURCE_PATH+"Player.png");
    public static ImageIcon scout = new ImageIcon(FILE_PATH+RESOURCE_PATH+"Scout.png");
    public static ImageIcon worker = new ImageIcon(FILE_PATH+RESOURCE_PATH+"Worker.png");
    public static ImageIcon core = new ImageIcon(FILE_PATH+RESOURCE_PATH+"Core.png");
    public static ImageIcon wall = new ImageIcon(FILE_PATH+RESOURCE_PATH+"Wall.png");
    public static ImageIcon food = new ImageIcon(FILE_PATH+RESOURCE_PATH+"Food.png");
    public static ImageIcon door_open = new ImageIcon(FILE_PATH+RESOURCE_PATH+"Door_Open.png");
    public static ImageIcon door_closed = new ImageIcon(FILE_PATH+RESOURCE_PATH+"Door_Closed.png");
    public static ImageIcon blank = new ImageIcon(FILE_PATH+RESOURCE_PATH+"UI\\Blank.png");
    public static ImageIcon night = new ImageIcon(FILE_PATH+RESOURCE_PATH+"UI\\Night.png");
    public static ImageIcon day = new ImageIcon(FILE_PATH+RESOURCE_PATH+"UI\\Day.png");
    public static ImageIcon lightsOn = new ImageIcon(FILE_PATH+RESOURCE_PATH+"UI\\lightsOn.png");
    public static ImageIcon lightsOff = new ImageIcon(FILE_PATH+RESOURCE_PATH+"UI\\lightsOff.png");
    public static ImageIcon selected = new ImageIcon(FILE_PATH+RESOURCE_PATH+"UI\\Selected.png");
    public static ImageIcon UIblack = new ImageIcon(FILE_PATH+RESOURCE_PATH+"UI\\Black.png");
    public static ImageIcon blue = new ImageIcon(FILE_PATH+RESOURCE_PATH+"UI\\Blue.png");
}
