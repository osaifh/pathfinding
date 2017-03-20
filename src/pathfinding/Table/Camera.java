package pathfinding.Table;

import pathfinding.auxiliar.Node;
import javax.swing.JLabel; 
import javax.swing.JFrame; 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JLayeredPane;
import java.util.HashMap;
import javax.swing.ImageIcon;
import pathfinding.auxiliar.FilePaths;

/**
 * This class is used to render what the camera currently shows.
 * @author Me
 */
public class Camera extends JFrame {

    private final static int CAMERA_SIZE = 11;
    private Table t;
    private Node position;
    private boolean camera_lock;
    private JLayeredPane jpanel = getLayeredPane();
    private JLabel[][]
            terrain_table = new JLabel[CAMERA_SIZE][CAMERA_SIZE],
            object_table = new JLabel[CAMERA_SIZE][CAMERA_SIZE],
            sight_table = new JLabel[CAMERA_SIZE][CAMERA_SIZE],
            light_table = new JLabel[CAMERA_SIZE][CAMERA_SIZE];
    private boolean[][] visibility_table;
    private static final HashMap<Integer,ImageIcon> TERRAIN_MAP;
    private static final HashMap<Integer,ImageIcon> SPRITE_MAP;
    private static final HashMap<Integer,ImageIcon> LIGHT_MAP;
    static{
        TERRAIN_MAP = new HashMap<>();
        TERRAIN_MAP.put(-1, FilePaths.black);
        TERRAIN_MAP.put(0, FilePaths.grass);
        TERRAIN_MAP.put(1, FilePaths.wall);
        TERRAIN_MAP.put(3, FilePaths.red);
        TERRAIN_MAP.put(5, FilePaths.brown);
        TERRAIN_MAP.put(6, FilePaths.grass_NW);
        TERRAIN_MAP.put(7, FilePaths.grass_NE);
        TERRAIN_MAP.put(8, FilePaths.grass_SW);
        TERRAIN_MAP.put(9, FilePaths.grass_SE);
        SPRITE_MAP = new HashMap<>();
        SPRITE_MAP.put(2, FilePaths.player);
        SPRITE_MAP.put(4, FilePaths.food);
        SPRITE_MAP.put(7, FilePaths.door_open);
        SPRITE_MAP.put(8, FilePaths.door_closed);
        LIGHT_MAP = new HashMap<>();
        LIGHT_MAP.put(0, FilePaths.dark10);
        LIGHT_MAP.put(1, FilePaths.dark9);
        LIGHT_MAP.put(2, FilePaths.dark8);
        LIGHT_MAP.put(3, FilePaths.dark7);
        LIGHT_MAP.put(4, FilePaths.dark6);
        LIGHT_MAP.put(5, FilePaths.dark5);
        LIGHT_MAP.put(6, FilePaths.dark4);
        LIGHT_MAP.put(7, FilePaths.dark3);
        LIGHT_MAP.put(8, FilePaths.dark2);
        LIGHT_MAP.put(9, FilePaths.dark1);
        LIGHT_MAP.put(10, null);
    }
    
    /**
     * Constructor class for camera
     * @param t indicates the source table
     */
    public Camera(Table t) {
		//A lot of these values should probably be passed into the camera constructor. 
		//What if later down the line you want to make another camera with a different size or camera_lock off?
		//The best practice here is to overload your constructor so you can pass in arguments for these items if you'd like
		//but have defaults when you don't need to change them. 
        camera_lock = true;
        position = new Node();
        visibility_table = new boolean[t.getSize()][t.getSize()];
        jpanel.setLayout(null);
        jpanel.setBackground(Color.lightGray);
        jpanel.setPreferredSize(new Dimension(640,640));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Simulation");
        setVisible(true);
        this.t = t;
        int width = (int)this.getBounds().getWidth();
        for (int i = 0; i < CAMERA_SIZE; i++) {
            for (int j = 0;j < CAMERA_SIZE;j++) {
                    terrain_table[i][j] = new JLabel();
                    terrain_table[i][j].setBounds(new Rectangle((width-CAMERA_SIZE*64)/2, (i)*64,(j)*64, 64));
                    terrain_table[i][j].setHorizontalAlignment(JLabel.RIGHT);
                    object_table[i][j] = new JLabel();
                    object_table[i][j].setBounds(new Rectangle((width-CAMERA_SIZE*64)/2, (i)*64,(j)*64, 64)); 
                    object_table[i][j].setHorizontalAlignment(JLabel.RIGHT);
                    sight_table[i][j] = new JLabel();
                    sight_table[i][j].setBounds(new Rectangle((width-CAMERA_SIZE*64)/2, (i)*64,(j)*64, 64)); 
                    sight_table[i][j].setHorizontalAlignment(JLabel.RIGHT);
                    light_table[i][j] = new JLabel();
                    light_table[i][j].setBounds(new Rectangle((width-CAMERA_SIZE*64)/2, (i)*64,(j)*64, 64)); 
                    light_table[i][j].setHorizontalAlignment(JLabel.RIGHT);
                    jpanel.add(terrain_table[i][j], new Integer(0));
                    jpanel.add(object_table[i][j], new Integer(1));
                    jpanel.add(sight_table[i][j], new Integer(2));
                    jpanel.add(light_table[i][j], new Integer(3));
                }
           }
    }
    
    /**
     * Gets the size of the camera
     * @return Returns the size of the camera
     */
    public int getCameraSize() {
        return CAMERA_SIZE;
    }
    
    /**
     * Gets the current position in the table of the camera
     * @return Returns the current position in the table of the camera
     */
    public Node getPos() {
        return position;
    }
    
    /**
     * Returns whether the camera is locked to the player position
     * @return Returns whether the camera is locked
     */
    public boolean isLocked() {
        return camera_lock;
    }
    
    /**
     * Locks or unlocks the camera to the player position
     * @param b Whether to set the camera lock to true or false
     */
    public void setLocked(boolean b) {
        camera_lock = b;
    }
    
    /**
     * Sets the position of the camera
     * @param n
     */
    public void setPos(Node n) {
        position.setToNode(n);
    }
    
    /**
     * Overloaded function that sets the position of the camera using specific coordinates instead of a node
     * @param x horizontal coordinate
     * @param y vertical coordinate
     */
    public void setPos(int x, int y) {
        position.set(x, y);
    }
    
    public void fillVisibilityTable(){
        for (int i = 0; i < visibility_table.length; ++i){
            for (int j = 0; j < visibility_table.length; ++j){
                visibility_table[i][j] = false;
            }
        }
    }
    
    public void clearVisibilityTable(){
        for (int i = 0; i < visibility_table.length; ++i){
            for (int j = 0; j < visibility_table.length; ++j){
                visibility_table[i][j] = true;
            }
        }
    }
    
    public void setVisibilityTable(Node n, boolean b){
        visibility_table[n.getX()][n.getY()] = b;
    }
    
    /**
     * Updates the icons that the camera currently displays
     */
    public void update() {
        int x, y;
        for (int i = 0; i < CAMERA_SIZE; ++i) {
            for (int j = 0; j < CAMERA_SIZE; ++j) {
                x = i+(position.getX()-(CAMERA_SIZE/2));
                y = j+(position.getY()-(CAMERA_SIZE/2));
                if (t.valid(x,y)) {
                    if (visibility_table[x][y]){
                        sight_table[i][j].setIcon(null);
                        terrain_table[i][j].setIcon(TERRAIN_MAP.get(t.getTile(x,y).getTerrainID()));
                        if (t.getObject(x,y)!=null) {
                            object_table[i][j].setIcon(SPRITE_MAP.get(t.getTile(x,y).getID()));
                        } else {
                            object_table[i][j].setIcon(null);
                        }
                        light_table[i][j].setIcon(LIGHT_MAP.get(t.getTile(x,y).getLight()/10));
                }
                else sight_table[i][j].setIcon(FilePaths.black);
                } else {
                    terrain_table[i][j].setIcon(FilePaths.black);
                    object_table[i][j].setIcon(null);
                    sight_table[i][j].setIcon(null);
                    light_table[i][j].setIcon(null);
                }
            }
        }
    }
    
}