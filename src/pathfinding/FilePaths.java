package pathfinding;

import java.io.File;
import javax.swing.ImageIcon;

/**
 * This is a simple class that contains some filepaths used mostly for rendering.
 * It is used to avoid cluttering on other classes.
 * @author Me
 */
public class FilePaths {
    public static String filePath = new File("").getAbsolutePath();
    //Sprite paths
    public static ImageIcon black = new ImageIcon(filePath+"\\src\\resources\\Black.png");
    public static ImageIcon red = new ImageIcon(filePath+"\\src\\resources\\Red.png");
    public static ImageIcon grass = new ImageIcon(filePath+"\\src\\resources\\Grass.png");
    public static ImageIcon grass_NW = new ImageIcon(filePath+"\\src\\resources\\Grass_edge_NW.png");
    public static ImageIcon grass_NE = new ImageIcon(filePath+"\\src\\resources\\Grass_edge_NE.png");
    public static ImageIcon grass_SW = new ImageIcon(filePath+"\\src\\resources\\Grass_edge_SW.png");
    public static ImageIcon grass_SE = new ImageIcon(filePath+"\\src\\resources\\Grass_edge_SE.png");
    public static ImageIcon brown = new ImageIcon(filePath+"\\src\\resources\\Brown.png");
    public static ImageIcon player = new ImageIcon(filePath+"\\src\\resources\\Player.png");
    public static ImageIcon wall = new ImageIcon(filePath+"\\src\\resources\\Wall.png");
    public static ImageIcon food = new ImageIcon(filePath+"\\src\\resources\\Food.png");
    public static ImageIcon dark1 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark1.png");
    public static ImageIcon dark2 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark2.png");
    public static ImageIcon dark3 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark3.png");
    public static ImageIcon dark4 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark4.png");
    public static ImageIcon dark5 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark5.png");
    public static ImageIcon dark6 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark6.png");
    public static ImageIcon dark7 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark7.png");
    public static ImageIcon dark8 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark8.png");
    public static ImageIcon dark9 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark9.png");
    public static ImageIcon dark10 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark10.png");
}
