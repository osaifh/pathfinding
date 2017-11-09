package pathfinding.Table;

import java.util.HashMap;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel; 
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import pathfinding.Controller;
import pathfinding.auxiliar.Node;
import pathfinding.actor.Actor;
import pathfinding.actor.Creature;

/**
 * This class is used to render what the camera currently shows.
 * @author Me
 */
public class Camera extends JFrame {
    private final static int TILE_SIZE = 32;
    private static int cameraSize;
    private int panelWidth, panelHeight, leftMargin;
    private Table t;
    private Node position;
    private boolean camera_lock;
    private JPanel jpanel = new JPanel();
    private final Controller parentController;
    private Creature activePlayer;
    private Creature lockedObject;
    private JLabel[][] inputTable;
    private boolean[][] visibility_table;
    private static final HashMap<Integer,ImageIcon> TERRAIN_MAP;
    private static final HashMap<Integer,ImageIcon> SPRITE_MAP;
    private static final HashMap<Integer,ImageIcon> UI_MAP;
    
    static {
        TERRAIN_MAP = new HashMap<>();
        TERRAIN_MAP.put(-1, FilePaths.black);
        TERRAIN_MAP.put(1, FilePaths.wall);
        TERRAIN_MAP.put(10, FilePaths.white);
        TERRAIN_MAP.put(100, FilePaths.deepWater);
        TERRAIN_MAP.put(101, FilePaths.water);
        TERRAIN_MAP.put(102, FilePaths.shallowWater);
        TERRAIN_MAP.put(103, FilePaths.sand);
        TERRAIN_MAP.put(104, FilePaths.brown);
        TERRAIN_MAP.put(105, FilePaths.grass);
        TERRAIN_MAP.put(106, FilePaths.rock);
        SPRITE_MAP = new HashMap<>();
        SPRITE_MAP.put(2, FilePaths.player);
        SPRITE_MAP.put(3, FilePaths.red);
        SPRITE_MAP.put(4, FilePaths.food);
        SPRITE_MAP.put(7, FilePaths.door_open);
        SPRITE_MAP.put(8, FilePaths.door_closed);
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
        jpanel.setBackground(Color.black);
        jpanel.setPreferredSize(new Dimension(640,640));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Simulation");
        setVisible(true);
        this.t = t;
        panelWidth = (int)this.getBounds().getWidth();
        panelHeight = (int)this.getBounds().getHeight();
        cameraSize = panelHeight/32;
        leftMargin = (panelWidth - cameraSize*32)/2;
        inputTable = new JLabel[cameraSize][cameraSize];
        int x, y;
        for (int i = 0; i < cameraSize; i++) {
            for (int j = 0; j < cameraSize; j++) {
                inputTable[i][j] = new JLabel();
                x = j*32 + leftMargin;
                y = i*32 - 32;
                //don't do this at home kids
                inputTable[i][j].setBounds(x, y, TILE_SIZE, TILE_SIZE);
                inputTable[i][j].setHorizontalAlignment(JLabel.RIGHT);
                
                //this looks weird but it just works
                final int a = i;
                final int b = j;
                MouseAdapter mAdapter = new MouseAdapter(){
                    int x, y;
                    
                    @Override
                    public void mouseClicked(MouseEvent e){
                        if (SwingUtilities.isLeftMouseButton(e)){
                            x = a+(position.getX()-(cameraSize/2));
                            y = b+(position.getY()-(cameraSize/2));
                            parentController.handleMouseInput(x, y);
                        }
                    }
                };
                inputTable[i][j].addMouseListener(mAdapter);
                this.add(inputTable[i][j]);
            }
        }
    }
    
    /**
     * Gets the size of the camera
     * @return Returns the size of the camera
     */
    public int getCameraSize() {
        return cameraSize;
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
     * @param actor the actor to lock the camera to
     */
    public void toggleLocked(Actor actor) {
        if (!camera_lock){
            if (actor instanceof Creature){
                this.lockedObject = (Creature) actor;
                System.out.println(lockedObject.toString());
                camera_lock = true;
            }
        } else {
            if (lockedObject!=null){
                lockedObject.setCamera(null);
                lockedObject = null;
            }
            camera_lock = false;
        }
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
    
    public void setLockedObject(Creature creature){
        this.lockedObject = creature;
    }
    
    public boolean checkLockedCreature(Creature creature){
        return (lockedObject == creature);
        
    }
    
    public void updatePosition(){
        if (camera_lock && lockedObject!=null) this.position.setToNode(lockedObject.getNode());
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
        drawMap();
        /*
        int x, y, drawX, drawY;
        Tile tile;
        //I copied the bufferStrategy part of the code so I'm not sure what it does exactly
        //but it works
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
                this.createBufferStrategy(3);			
                return;
        }
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        //call this function to update the UI
        updateUI();
        //this draws the stuff we see
        for (int i = 0; i < cameraSize; ++i) {
            for (int j = 0; j < cameraSize; ++j) {
                //x and y are the coordinates of the matching tiles of the table
                x = i+(position.getX()-(cameraSize/2));
                y = j+(position.getY()-(cameraSize/2));
                //drawX and drawY are the coordinates in which we draw the tiles
                drawX = j*32 + leftMargin;
                drawY = i*32;
                if (t.valid(x,y)) {
                    tile = t.getTile(x, y);
                    if (visibility_table[x][y]){
                        g.drawImage(TERRAIN_MAP.get(tile.getTerrainID()).getImage(), drawX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
                        if (t.getActor(x,y)!=null) {
                            for (int k = 0; k < tile.getContentSize(); ++k){
                                if (tile.getContent(k)!=null && SPRITE_MAP.get(tile.getContent(k).getID())!=null){
                                    g.drawImage(SPRITE_MAP.get(tile.getContent(k).getID()).getImage(),drawX,drawY,TILE_SIZE,TILE_SIZE, rootPane);
                                }
                            }
                        }
                        //only draw a rectangle if the light value isn't max
                        //this shouldn't be needed I'm just trying to optimize I guess
                        if (tile.getLight()<100) {
                            float alpha = 100 - tile.getLight();
                            alpha /= 100;
                            g.setColor(new Color(0,0,0,alpha));
                            g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                        }
                    }
                    //the coordinate is not visible to the controllable actor
                    else {
                        g.setColor(Color.black);
                        g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                    }
                }
                //the coordinate is outside of the table
                else {
                    g.setColor(Color.black);
                    g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                }
            }
        }
        g.dispose();
        bs.show();
        */
    }
    
    private void updateUI() {
        
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(2);			
            return;
        }
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        initializeUI(g);
        int drawX, drawY;
        drawX = leftMargin + cameraSize*32;
        //turns out for some reason this drawY is unaligned by 2 pixels
        //it just works man
        drawY = TILE_SIZE-2;
        int aux;
        if (activePlayer.isAlive()){
            for (int i = 0; i < 5; ++i){
                if ((activePlayer.getHP()*100)/activePlayer.getmaxHP() > (5-(i+1))*20) aux = 7;
                else aux = 6;
                g.drawImage(UI_MAP.get(aux).getImage(),drawX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
                drawY += 32;
            }
        }
        //if not, set the vertical coordinate to the proper value
        else drawY = TILE_SIZE*6-2;
        if (parentController.isDay()) aux = 1;
        else aux = 2;
        g.drawImage(UI_MAP.get(aux).getImage(),drawX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
        drawY += 32;
        if (parentController.lightsOn()) aux = 3;
        else aux = 4;
        g.drawImage(UI_MAP.get(aux).getImage(),drawX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
        drawY += 32;
        /*
        for (int i = 0; i < left_UI.length; ++i){
            if (i == (parentController.getSelected()-1)){
                left_UI[i][0].setIcon(UI_MAP.get(5));
            }
            else {
                left_UI[i][0].setIcon(null);
            }
        }
        */
        g.dispose();
        bs.show();
    }
    
    private void initializeUI(Graphics2D g){
        int rightX = leftMargin + cameraSize*TILE_SIZE;
        int leftX = leftMargin - TILE_SIZE;
        int drawY = TILE_SIZE - 4;
        for (int i = 0; i < cameraSize; ++i){
            g.drawImage(UI_MAP.get(0).getImage(),leftX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
            g.drawImage(UI_MAP.get(0).getImage(),rightX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
            drawY += 32;
        }
    }
    
    private void drawMap(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
                this.createBufferStrategy(3);			
                return;
        }
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        int size = t.getSize();
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; ++j){
                Tile tile = t.getTile(i, j);
                g.drawImage(TERRAIN_MAP.get(tile.getTerrainID()).getImage(),j,i,1,1, rootPane);
            }
        }
        g.dispose();
        bs.show();
    }
    
}