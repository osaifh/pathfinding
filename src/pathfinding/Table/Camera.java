package pathfinding.Table;


import java.util.HashMap;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel; 
import javax.swing.JFrame; 
import javax.swing.JLayeredPane;
import javax.swing.ImageIcon;
import pathfinding.Controller;
import pathfinding.auxiliar.Node;
import pathfinding.actor.Actor;
import pathfinding.actor.Creature;

/**
 * This class is used to render what the camera currently shows.
 * @author Me
 */
public class Camera extends JFrame {

    private final static int CAMERA_SIZE = 11;
    private final static int OBJECT_LAYERS = 3;
    private Table t;
    private Node position;
    private boolean camera_lock;
    private JLayeredPane jpanel = getLayeredPane();
    private Controller parentController;
    private Creature activePlayer;
    private JLabel[][]
            right_UI = new JLabel[CAMERA_SIZE][3],
            left_UI = new JLabel[CAMERA_SIZE][3];
    private JLabel[][]
            terrain_table = new JLabel[CAMERA_SIZE][CAMERA_SIZE],
            sight_table = new JLabel[CAMERA_SIZE][CAMERA_SIZE],
            light_table = new JLabel[CAMERA_SIZE][CAMERA_SIZE];
    private JLabel[][][]
            object_table = new JLabel[CAMERA_SIZE][CAMERA_SIZE][OBJECT_LAYERS];
    private boolean[][] visibility_table;
    private static final HashMap<Integer,ImageIcon> TERRAIN_MAP;
    private static final HashMap<Integer,ImageIcon> SPRITE_MAP;
    private static final HashMap<Integer,ImageIcon> LIGHT_MAP;
    private static final HashMap<Integer,ImageIcon> UI_MAP;
    static {
        TERRAIN_MAP = new HashMap<>();
        TERRAIN_MAP.put(-1, FilePaths.black);
        TERRAIN_MAP.put(0, FilePaths.grass);
        TERRAIN_MAP.put(1, FilePaths.wall);
        TERRAIN_MAP.put(3, FilePaths.red);
        TERRAIN_MAP.put(4, FilePaths.food);
        TERRAIN_MAP.put(5, FilePaths.brown);
        TERRAIN_MAP.put(6, FilePaths.grass_NW);
        TERRAIN_MAP.put(7, FilePaths.grass_NE);
        TERRAIN_MAP.put(8, FilePaths.grass_SW);
        TERRAIN_MAP.put(9, FilePaths.grass_SE);
        TERRAIN_MAP.put(10, FilePaths.white);
        SPRITE_MAP = new HashMap<>();
        SPRITE_MAP.put(2, FilePaths.player);
        SPRITE_MAP.put(3, FilePaths.red);
        SPRITE_MAP.put(4, FilePaths.food);
        SPRITE_MAP.put(7, FilePaths.door_open);
        SPRITE_MAP.put(8, FilePaths.door_closed);
        SPRITE_MAP.put(20, FilePaths.core);
        SPRITE_MAP.put(21, FilePaths.scout);
        SPRITE_MAP.put(22, FilePaths.worker);
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
    
    /**
     * Constructor class for camera
     * @param t indicates the source table
     * @param parentController
     */
    public Camera(Table t, Controller parentController) {
        //A lot of these values should probably be passed into the camera constructor. 
        //What if later down the line you want to make another camera with a different size or camera_lock off?
        //The best practice here is to overload your constructor so you can pass in arguments for these items if you'd like
        //but have defaults when you don't need to change them. 
        this.parentController = parentController;
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
            for (int k = 0; k < 3; ++k){
                left_UI[i][k] = new JLabel();
                left_UI[i][k].setBounds(new Rectangle((width-CAMERA_SIZE*64)/2-64, (i)*64, 64, 64));
                left_UI[i][k].setHorizontalAlignment(JLabel.RIGHT);
                jpanel.add(left_UI[i][k], new Integer(0));
                right_UI[i][k] = new JLabel();
                right_UI[i][k].setBounds(new Rectangle((width-CAMERA_SIZE*64)/2-64, (i)*64, CAMERA_SIZE*64 + 64, 64));
                right_UI[i][k].setHorizontalAlignment(JLabel.RIGHT);
                jpanel.add(right_UI[i][k],new Integer(1));
            }
            left_UI[i][2].setIcon(UI_MAP.get(0));
            right_UI[i][2].setIcon(UI_MAP.get(0));
            for (int j = 0; j < CAMERA_SIZE; j++) {
                terrain_table[i][j] = new JLabel();
                terrain_table[i][j].setBounds(new Rectangle((width-CAMERA_SIZE*64)/2, (i)*64,(j)*64, 64));
                terrain_table[i][j].setHorizontalAlignment(JLabel.RIGHT);
                sight_table[i][j] = new JLabel();
                sight_table[i][j].setBounds(new Rectangle((width-CAMERA_SIZE*64)/2, (i)*64,(j)*64, 64)); 
                sight_table[i][j].setHorizontalAlignment(JLabel.RIGHT);
                light_table[i][j] = new JLabel();
                light_table[i][j].setBounds(new Rectangle((width-CAMERA_SIZE*64)/2, (i)*64,(j)*64, 64)); 
                light_table[i][j].setHorizontalAlignment(JLabel.RIGHT);
                //testing mouse adapter
                final int a = i;
                final int b = j;
                MouseAdapter mAdapter = new MouseAdapter(){
                    int x, y;
                    
                    @Override
                    public void mouseClicked(MouseEvent e){
                        x = a+(position.getX()-(CAMERA_SIZE/2));
                        y = b+(position.getY()-(CAMERA_SIZE/2));
                        parentController.handleMouseInput(x, y);
                    }
                };
                light_table[i][j].addMouseListener(mAdapter);
                
                jpanel.add(terrain_table[i][j], new Integer(0));
                jpanel.add(sight_table[i][j], new Integer(3));
                jpanel.add(light_table[i][j], new Integer(4));
                for (int k = 0; k < OBJECT_LAYERS; k++){
                    object_table[i][j][k] = new JLabel();
                    object_table[i][j][k].setBounds(new Rectangle((width-CAMERA_SIZE*64)/2, (i)*64,(j)*64, 64)); 
                    object_table[i][j][k].setHorizontalAlignment(JLabel.RIGHT);
                    jpanel.add(object_table[i][j][k], new Integer(k+1));
                }
            }
        }
        initializeUI();
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
    
    public void setActivePlayer(Creature activePlayer){
        this.activePlayer = activePlayer;
    }
    
    public void fillVisibilityTable() {
        for (int i = 0; i < visibility_table.length; ++i){
            for (int j = 0; j < visibility_table.length; ++j){
                visibility_table[i][j] = false;
            }
        }
    }
    
    public void clearVisibilityTable() {
        for (int i = 0; i < visibility_table.length; ++i){
            for (int j = 0; j < visibility_table.length; ++j){
                visibility_table[i][j] = true;
            }
        }
    }
    
    public void setVisibilityTable(Node n, boolean b) {
        visibility_table[n.getX()][n.getY()] = b;
    }
    
    /**
     * Updates the icons that the camera currently displays
     */
    public void update() {
        int x, y;
        updateUI();
        for (int i = 0; i < CAMERA_SIZE; ++i) {
            for (int j = 0; j < CAMERA_SIZE; ++j) {
                x = i+(position.getX()-(CAMERA_SIZE/2));
                y = j+(position.getY()-(CAMERA_SIZE/2));
                if (t.valid(x,y)) {
                    if (visibility_table[x][y]){
                        sight_table[i][j].setIcon(null);
                        terrain_table[i][j].setIcon(TERRAIN_MAP.get(t.getTile(x,y).getTerrainID()));
                        if (t.getActor(x,y)!=null) {
                            for (int k = 0; k < OBJECT_LAYERS; ++k){
                                if (t.getTile(x,y).getContent(k)!=null){
                                    object_table[i][j][k].setIcon(SPRITE_MAP.get(t.getTile(x,y).getContent(k).getID()));
                                } else {
                                    object_table[i][j][k].setIcon(null);
                                }
                            }
                        } else {
                            for (int k = 0; k < OBJECT_LAYERS; ++k){
                                object_table[i][j][k].setIcon(null);
                            }
                        }
                        light_table[i][j].setIcon(LIGHT_MAP.get(t.getTile(x,y).getLight()/10));
                }
                else sight_table[i][j].setIcon(FilePaths.black);
                } else {
                    terrain_table[i][j].setIcon(FilePaths.black);
                    sight_table[i][j].setIcon(null);
                    light_table[i][j].setIcon(null);
                    for (int k = 0; k < OBJECT_LAYERS; ++k){
                        object_table[i][j][k].setIcon(null);
                    }
                }
            }
        }
    }
    
    private void updateUI() {    
        if (activePlayer.isAlive()){
            for (int i = 0; i < 5; ++i){
                if ((activePlayer.getHP()*100)/activePlayer.getmaxHP() > (5-(i+1))*20){
                    right_UI[i][0].setIcon(UI_MAP.get(7));
                }
                else right_UI[i][0].setIcon(UI_MAP.get(6));
            }
        }
        if (parentController.isDay()) right_UI[9][1].setIcon(UI_MAP.get(1));
        else right_UI[9][1].setIcon(UI_MAP.get(2));
        if (parentController.lightsOn()) right_UI[10][1].setIcon(UI_MAP.get(3));
        else right_UI[10][1].setIcon(UI_MAP.get(4));
        for (int i = 0; i < left_UI.length; ++i){
            if (i == (parentController.getSelected()-1)){
                left_UI[i][0].setIcon(UI_MAP.get(5));
            }
            else {
                left_UI[i][0].setIcon(null);
            }
        }
    }
    
    private void initializeUI() {
        left_UI[0][1].setIcon(SPRITE_MAP.get(2));
        left_UI[1][1].setIcon(TERRAIN_MAP.get(1));
        left_UI[2][1].setIcon(SPRITE_MAP.get(8));
        left_UI[3][1].setIcon(UI_MAP.get(3));
        left_UI[4][1].setIcon(UI_MAP.get(3));
        left_UI[5][1].setIcon(UI_MAP.get(3));
        left_UI[6][1].setIcon(SPRITE_MAP.get(20));
    }
}
