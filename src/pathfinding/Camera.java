package pathfinding;

import javax.swing.JLabel; 
import javax.swing.JFrame; 
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JLayeredPane;
import java.awt.event.KeyListener;
import java.io.File;

/**
 * This class is used to render what the camera currently shows.
 * @author Me
 */
public class Camera extends JFrame {
    private final int camera_size = 11;
    Table t;
    ObjecteList obj_list;
    private Node position = new Node();
    private boolean camera_lock;
    JLayeredPane jpanel = getLayeredPane();
    JLabel[][] terrain_table = new JLabel[camera_size][camera_size];
    JLabel[][] object_table = new JLabel[camera_size][camera_size];
    JLabel[][] sight_table = new JLabel[camera_size][camera_size];
    JLabel[][] light_table = new JLabel[camera_size][camera_size];
    //Sprite paths:
    String filePath = new File("").getAbsolutePath();
    ImageIcon black = new ImageIcon(filePath+"\\src\\resources\\Black.png");
    ImageIcon red = new ImageIcon(filePath+"\\src\\resources\\Red.png");
    ImageIcon grass = new ImageIcon(filePath+"\\src\\resources\\Grass.png");
    ImageIcon grass_NW = new ImageIcon(filePath+"\\src\\resources\\Grass_edge_NW.png");
    ImageIcon grass_NE = new ImageIcon(filePath+"\\src\\resources\\Grass_edge_NE.png");
    ImageIcon grass_SW = new ImageIcon(filePath+"\\src\\resources\\Grass_edge_SW.png");
    ImageIcon grass_SE = new ImageIcon(filePath+"\\src\\resources\\Grass_edge_SE.png");
    ImageIcon brown = new ImageIcon(filePath+"\\src\\resources\\Brown.png");
    ImageIcon player = new ImageIcon(filePath+"\\src\\resources\\Player.png");
    ImageIcon wall = new ImageIcon(filePath+"\\src\\resources\\Wall.png");
    ImageIcon food = new ImageIcon(filePath+"\\src\\resources\\Food.png");
    ImageIcon dark1 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark1.png");
    ImageIcon dark2 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark2.png");
    ImageIcon dark3 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark3.png");
    ImageIcon dark4 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark4.png");
    ImageIcon dark5 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark5.png");
    ImageIcon dark6 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark6.png");
    ImageIcon dark7 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark7.png");
    ImageIcon dark8 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark8.png");
    ImageIcon dark9 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark9.png");
    ImageIcon dark10 = new ImageIcon(filePath+"\\src\\resources\\dark\\dark10.png");
       
    /**
     * Constructor class for camera
     * @param t indicates the source table
     * @param listener contains the listener used to handle controls
     */
    public Camera(Table t, KeyListener listener) {
        camera_lock = true;
        jpanel.setLayout(null);
        jpanel.setBackground(Color.lightGray);
        jpanel.setPreferredSize(new Dimension(640,640));
        this.addKeyListener(listener);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640,640);
        setTitle("Simulation");
        setVisible(true);
        this.t = t;
        for(int i = 0; i < camera_size; i++) {
            for (int j = 0;j < camera_size;j++) {
                    terrain_table[i][j] = new JLabel();
                    terrain_table[i][j].setBounds(new Rectangle(15, (i)*64,(j)*64, 64));
                    terrain_table[i][j].setHorizontalAlignment(JLabel.RIGHT);
                    object_table[i][j] = new JLabel();
                    object_table[i][j].setBounds(new Rectangle(15, (i)*64,(j)*64, 64)); 
                    object_table[i][j].setHorizontalAlignment(JLabel.RIGHT);
                    sight_table[i][j] = new JLabel();
                    sight_table[i][j].setBounds(new Rectangle(15, (i)*64,(j)*64, 64)); 
                    sight_table[i][j].setHorizontalAlignment(JLabel.RIGHT);
                    light_table[i][j] = new JLabel();
                    light_table[i][j].setBounds(new Rectangle(15, (i)*64,(j)*64, 64)); 
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
                    if (t.getTile(x,y).isLit()) sight_table[i][j].setIcon(null);
                    else sight_table[i][j].setIcon(black);
                    id = t.getID(x,y);
                    switch (id) {
                        case 0:
                            terrain_table[i][j].setIcon(grass);
                            break;
                        case 1:
                            terrain_table[i][j].setIcon(wall);
                            break;
                        case 3:
                            terrain_table[i][j].setIcon(red);
                            break;
                        case 5:
                            terrain_table[i][j].setIcon(brown);
                            break;
                        case 6:
                            terrain_table[i][j].setIcon(grass_NW);
                            break;
                        case 7:
                            terrain_table[i][j].setIcon(grass_NE);
                            break;
                        case 8:
                            terrain_table[i][j].setIcon(grass_SW);
                            break;
                        case 9:
                            terrain_table[i][j].setIcon(grass_SE);
                            break;
                        default:
                            terrain_table[i][j].setIcon(null);
                            break;
                    }
                    if (t.getObject(x,y)!=null) {
                             if (t.getObject(x,y).getID()==2 || t.getObject(x,y).getID()==10) object_table[i][j].setIcon(player);
                        else if (t.getObject(x,y).getID()==4) object_table[i][j].setIcon(food);
                    } else {
                        object_table[i][j].setIcon(null);
                    }
                         if (t.getTile(x, y).getLight()>=100) light_table[i][j].setIcon(null);
                    else if (t.getTile(x, y).getLight()>90) light_table[i][j].setIcon(dark1);
                    else if (t.getTile(x, y).getLight()>80) light_table[i][j].setIcon(dark2);
                    else if (t.getTile(x, y).getLight()>70) light_table[i][j].setIcon(dark3);
                    else if (t.getTile(x, y).getLight()>60) light_table[i][j].setIcon(dark4);
                    else if (t.getTile(x, y).getLight()>50) light_table[i][j].setIcon(dark5);
                    else if (t.getTile(x, y).getLight()>40) light_table[i][j].setIcon(dark6);
                    else if (t.getTile(x, y).getLight()>30) light_table[i][j].setIcon(dark7);
                    else if (t.getTile(x, y).getLight()>20) light_table[i][j].setIcon(dark8);
                    else if (t.getTile(x, y).getLight()>10) light_table[i][j].setIcon(dark9);
                    else light_table[i][j].setIcon(dark10);
                } else {
                    terrain_table[i][j].setIcon(black);
                    object_table[i][j].setIcon(null);
                    sight_table[i][j].setIcon(null);
                    light_table[i][j].setIcon(null);
                }
            }
        }
    }
    
}