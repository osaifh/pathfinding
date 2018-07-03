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
    public static ImageIcon grassBase = new ImageIcon(RESOURCE_PATH+"Grass.png");
    public static ImageIcon grass2 = new ImageIcon(RESOURCE_PATH+"Grass2.png");
    public static ImageIcon grass3 = new ImageIcon(RESOURCE_PATH+"Grass3.png");
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
    public static ImageIcon groundTile = new ImageIcon(RESOURCE_PATH+"groundTile.png");
    
    public static ImageIcon redMark = new ImageIcon(RESOURCE_PATH+"REDMARK.png");
    public static ImageIcon whiteMark = new ImageIcon(RESOURCE_PATH+"WHITEMARK.png");
    public static ImageIcon greenMark = new ImageIcon(RESOURCE_PATH+"GREENMARK.png");
    public static ImageIcon blueMark = new ImageIcon(RESOURCE_PATH+"BLUEMARK.png");
    
    //UI elements
    public static ImageIcon blank = new ImageIcon(RESOURCE_PATH+"UI\\Blank.png");
    public static ImageIcon night = new ImageIcon(RESOURCE_PATH+"UI\\Night.png");
    public static ImageIcon day = new ImageIcon(RESOURCE_PATH+"UI\\Day.png");
    public static ImageIcon lightsOn = new ImageIcon(RESOURCE_PATH+"UI\\lightsOn.png");
    public static ImageIcon lightsOff = new ImageIcon(RESOURCE_PATH+"UI\\lightsOff.png");
    public static ImageIcon selected = new ImageIcon(RESOURCE_PATH+"UI\\Selected.png");
    public static ImageIcon UIblack = new ImageIcon(RESOURCE_PATH+"UI\\Black.png");
    public static ImageIcon blue = new ImageIcon(RESOURCE_PATH+"UI\\Blue.png");
    public static ImageIcon emptyIcon = new ImageIcon(RESOURCE_PATH+"UI\\skillbar_empty.png");
    public static ImageIcon explosionIcon = new ImageIcon(RESOURCE_PATH+"UI\\explosion_icon.png");
    public static ImageIcon wallIcon = new ImageIcon(RESOURCE_PATH+"UI\\wall_icon.png");
    public static ImageIcon shootIcon = new ImageIcon(RESOURCE_PATH+"UI\\shoot_icon.png");
    public static ImageIcon guardIcon = new ImageIcon(RESOURCE_PATH+"UI\\createguard_icon.png");
    public static ImageIcon mobIcon = new ImageIcon(RESOURCE_PATH+"UI\\createmob_icon.png");
    public static ImageIcon debugIcon = new ImageIcon(RESOURCE_PATH+"UI\\debugskill_icon.png");
    public static ImageIcon lightSkillIcon = new ImageIcon(RESOURCE_PATH+"UI\\createlight_icon.png");
    
    //player test
    public static ImageIcon playerS = new ImageIcon(RESOURCE_PATH+"player\\playerS.png");
    public static ImageIcon playerN = new ImageIcon(RESOURCE_PATH+"player\\playerN.png");
    public static ImageIcon playerE = new ImageIcon(RESOURCE_PATH+"player\\playerE.png");
    public static ImageIcon playerW = new ImageIcon(RESOURCE_PATH+"player\\playerW.png");
    public static ImageIcon playerNE = new ImageIcon(RESOURCE_PATH+"player\\playerNE.png");
    public static ImageIcon playerNW = new ImageIcon(RESOURCE_PATH+"player\\playerNW.png");
    public static ImageIcon playerSE = new ImageIcon(RESOURCE_PATH+"player\\playerSE.png");
    public static ImageIcon playerSW = new ImageIcon(RESOURCE_PATH+"player\\playerSW.png");
    
    //ghost test
    public static ImageIcon ghostS = new ImageIcon(RESOURCE_PATH+"ghost\\ghostS.png");
    public static ImageIcon ghostN = new ImageIcon(RESOURCE_PATH+"ghost\\ghostN.png");
    public static ImageIcon ghostE = new ImageIcon(RESOURCE_PATH+"ghost\\ghostE.png");
    public static ImageIcon ghostW = new ImageIcon(RESOURCE_PATH+"ghost\\ghostW.png");
    
    
    //old sprites
    public static ImageIcon oldPlayer = new ImageIcon(RESOURCE_PATH+"PlayerOld.png");
    public static ImageIcon oldPlayer_red = new ImageIcon(RESOURCE_PATH+"PlayerOld_red.png");
    public static ImageIcon oldPlayer_blue = new ImageIcon(RESOURCE_PATH+"PlayerOld_blue.png");
    public static ImageIcon oldPlayer_green = new ImageIcon(RESOURCE_PATH+"PlayerOld_green.png");
    public static ImageIcon highlight = new ImageIcon(RESOURCE_PATH+"highlight.png");
    
}