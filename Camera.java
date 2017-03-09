package pathfinding;

import javax.swing.JLabel; 
import javax.swing.JFrame; 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JLayeredPane;

/**
 * This class is used to render what the camera currently shows.
 * @author Me
 */
public class Camera extends JFrame {
    private final int camera_size = 11;
    private Table t;
    private SubjectList obj_list;
    private Node position = new Node();
    private boolean camera_lock;
    private JLayeredPane jpanel = getLayeredPane();
    private JLabel[][]
            terrain_table = new JLabel[camera_size][camera_size],
            object_table = new JLabel[camera_size][camera_size],
            sight_table = new JLabel[camera_size][camera_size],
            light_table = new JLabel[camera_size][camera_size];
    private boolean[][] visibility_table;
    
    /**
     * Constructor class for camera
     * @param t indicates the source table
     */
    public Camera(Table t) {
        camera_lock = true;
        visibility_table = new boolean[t.getSize()][t.getSize()];
        jpanel.setLayout(null);
        jpanel.setBackground(Color.lightGray);
        jpanel.setPreferredSize(new Dimension(640,640));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640,640);
        setTitle("Simulation");
        setVisible(true);
        this.t = t;
        for(int i = 0; i < camera_size; i++) {
            for (int j = 0;j < camera_size;j++) {
                    terrain_table[i][j] = new JLabel();
                    terrain_table[i][j].setBounds(new Rectangle(0, (i)*64,(j)*64, 64));
                    terrain_table[i][j].setHorizontalAlignment(JLabel.RIGHT);
                    object_table[i][j] = new JLabel();
                    object_table[i][j].setBounds(new Rectangle(0, (i)*64,(j)*64, 64)); 
                    object_table[i][j].setHorizontalAlignment(JLabel.RIGHT);
                    sight_table[i][j] = new JLabel();
                    sight_table[i][j].setBounds(new Rectangle(0, (i)*64,(j)*64, 64)); 
                    sight_table[i][j].setHorizontalAlignment(JLabel.RIGHT);
                    light_table[i][j] = new JLabel();
                    light_table[i][j].setBounds(new Rectangle(0, (i)*64,(j)*64, 64)); 
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
        return camera_size;
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
        position = n;
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
        int id, x, y;
        for (int i = 0; i < camera_size; ++i) {
            for (int j = 0; j < camera_size; ++j) {
                x = i+(position.getX()-(camera_size/2));
                y = j+(position.getY()-(camera_size/2));
                if (t.valid(x,y)) {
                    if (visibility_table[x][y]){
                    sight_table[i][j].setIcon(null);
                    id = t.getID(x,y);
                    switch (id) {
                        case 0:
                            terrain_table[i][j].setIcon(FilePaths.grass);
                            break;
                        case 1:
                            terrain_table[i][j].setIcon(FilePaths.wall);
                            break;
                        case 3:
                            terrain_table[i][j].setIcon(FilePaths.red);
                            break;
                        case 5:
                            terrain_table[i][j].setIcon(FilePaths.brown);
                            break;
                        case 6:
                            terrain_table[i][j].setIcon(FilePaths.grass_NW);
                            break;
                        case 7:
                            terrain_table[i][j].setIcon(FilePaths.grass_NE);
                            break;
                        case 8:
                            terrain_table[i][j].setIcon(FilePaths.grass_SW);
                            break;
                        case 9:
                            terrain_table[i][j].setIcon(FilePaths.grass_SE);
                            break;
                        default:
                            terrain_table[i][j].setIcon(null);
                            break;
                    }
                    if (t.getObject(x,y)!=null) {
                             if (t.getObject(x,y).getID()==2 || t.getObject(x,y).getID()==10) object_table[i][j].setIcon(FilePaths.player);
                        else if (t.getObject(x,y).getID()==4) object_table[i][j].setIcon(FilePaths.food);
                    } else {
                        object_table[i][j].setIcon(null);
                    }
                         if (t.getTile(x, y).getLight()>=100) light_table[i][j].setIcon(null);
                    else if (t.getTile(x, y).getLight()>90) light_table[i][j].setIcon(FilePaths.dark1);
                    else if (t.getTile(x, y).getLight()>80) light_table[i][j].setIcon(FilePaths.dark2);
                    else if (t.getTile(x, y).getLight()>70) light_table[i][j].setIcon(FilePaths.dark3);
                    else if (t.getTile(x, y).getLight()>60) light_table[i][j].setIcon(FilePaths.dark4);
                    else if (t.getTile(x, y).getLight()>50) light_table[i][j].setIcon(FilePaths.dark5);
                    else if (t.getTile(x, y).getLight()>40) light_table[i][j].setIcon(FilePaths.dark6);
                    else if (t.getTile(x, y).getLight()>30) light_table[i][j].setIcon(FilePaths.dark7);
                    else if (t.getTile(x, y).getLight()>20) light_table[i][j].setIcon(FilePaths.dark8);
                    else if (t.getTile(x, y).getLight()>10) light_table[i][j].setIcon(FilePaths.dark9);
                    else light_table[i][j].setIcon(FilePaths.dark10);
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