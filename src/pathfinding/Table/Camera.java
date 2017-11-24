package pathfinding.Table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel; 
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import pathfinding.Controller;
import pathfinding.auxiliar.Node;
import pathfinding.actor.Creatures.Creature;

/**
 * This class is used to render what the camera currently shows.
 * @author
 */
public class Camera extends JFrame {
    private final static int TILE_SIZE = 32;
    private static int cameraSize;
    private int panelWidth, panelHeight, leftMargin;
    private Table t;
    private Node position;
    private boolean cameraLock;
    private JPanel jpanel = new JPanel();
    private final Controller parentController;
    private Creature activePlayer;
    private JLabel[][] inputTable;
    private boolean[][] visibilityTable;
    
    //temporal stuff: this will be changed later
    private boolean showMap = false;
    
    /**
     * Constructor class for camera
     * @param t indicates the source table
     * @param parentController
     */
    public Camera(Table t, Controller parentController) {
        //A lot of these values should probably be passed into the camera constructor. 
        //What if later down the line you want to make another camera with a different size or cameraLock off?
        //The best practice here is to overload your constructor so you can pass in arguments for these items if you'd like
        //but have defaults when you don't need to change them. 
        this.parentController = parentController;
        cameraLock = true;
        position = new Node();
       
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
         visibilityTable = new boolean[cameraSize][cameraSize];
        leftMargin = (panelWidth - cameraSize*32)/2;
        inputTable = new JLabel[cameraSize][cameraSize];
        int x, y;
        for (int i = 0; i < cameraSize; i++) {
            for (int j = 0; j < cameraSize; j++) {
                 //don't do this at home kids
                inputTable[i][j] = new JLabel();
                x = j*32 + leftMargin;
                y = i*32 - 32;
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
                    
                    @Override
                    public void mouseEntered(MouseEvent e){
                        x = a+(position.getX()-(cameraSize/2));
                        y = b+(position.getY()-(cameraSize/2));
                        parentController.handleMouseHover(x,y);
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
        return cameraLock;
    }
    
    /**
     * Locks or unlocks the camera to the player position
     * @param creature the creature to lock the camera to
     */
    public void toggleLocked(Creature creature) {
        if (!cameraLock){
            this.activePlayer = (Creature) creature;
            cameraLock = true;
        } else {
            if (activePlayer!=null){
                activePlayer.setCamera(null);
                activePlayer = null;
            }
            cameraLock = false;
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
    
    /**
     * Sets the creature to track with the camera
     * @param activePlayer the creature to track with the camera
     */
    public void setActivePlayer(Creature activePlayer){
        this.activePlayer = activePlayer;
    }
    
    /**
     * Checks whether a creature matches the creature the camera is currently locked on
     * @param creature the creature to compare
     * @return Returns true if the camera is locked on the creature
     */
    public boolean checkLockedCreature(Creature creature){
        return (activePlayer == creature);
    }
    
    /**
     * updates the position of the camera to the object it's tracking
     */
    public void updatePosition(){
        if (cameraLock && activePlayer!=null) this.position.setToNode(activePlayer.getNode());
    }
    
    /**
     * Sets all the values of the visibility table to b
     * used to either clear the visibility table or make it all dark
     */
    public void fillVisibilityTable(boolean b) {
        for (int i = 0; i < visibilityTable.length; ++i){
            for (int j = 0; j < visibilityTable.length; ++j){
                visibilityTable[i][j] = b;
            }
        }
    }
    
    /**
     *  Sets a tile of the visilibty table to b
     * @param x the horizontal coordinate
     * @param y the vertical coordinate
     * @param b the boolean value to set the tile
     */
    public void setVisibilityTable(int x, int y, boolean b) {
        x +=cameraSize/2;
        y +=cameraSize/2;
        if (x >= 0 && x < cameraSize && y >= 0 && y < cameraSize){
            visibilityTable[x][y] = b;
        }
    }
    
     /**
     *  Sets a tile of the visilibty table to b
     * @param n the node of the tile to set
     * @param b the boolean value to set the tile
     */
    public void setVisibilityTable(Node n, boolean b) {
        int x = n.getX()+cameraSize/2;
        int y = n.getY()+cameraSize/2;
        if (x >= 0 && x < cameraSize && y >= 0 && y < cameraSize){
            visibilityTable[x][y] = b;
        }
    }
  
    
    /**
     * Updates the picture that the camera currently displays
     */
    public void update() {        
       updateUI();
       updateTable();
    }
    
    /**
     * Updates the main view of the camera
     */
    private void updateTable(){
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
                    if (visibilityTable[i][j]){
                        g.drawImage(Sprites.TERRAIN_MAP.get(tile.getTerrainID()).getImage(), drawX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
                        if (t.getActor(x,y)!=null) {
                            for (int k = 0; k < tile.getContentSize(); ++k){
                                if (tile.getContent(k)!=null && Sprites.SPRITE_MAP.get(tile.getContent(k).getID())!=null){
                                    g.drawImage(Sprites.SPRITE_MAP.get(tile.getContent(k).getID()).getImage(),drawX,drawY,TILE_SIZE,TILE_SIZE, rootPane);
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
        if (showMap){
            drawMap(g);
        }
        g.dispose();
        bs.show();
    }
    
    /**
     * Updates the display of the UI
     */
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
        //turns out for some reason this drawY is unaligned by 1 pixel
        //it just works man
        drawY = TILE_SIZE-1;
        int aux;
        if (activePlayer!=null && activePlayer.isAlive()){
            for (int i = 0; i < 5; ++i){
                if ((activePlayer.getHP()*100)/activePlayer.getmaxHP() > (5-(i+1))*20) aux = 7;
                else aux = 6;
                g.drawImage(Sprites.UI_MAP.get(aux).getImage(),drawX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
                drawY += 32;
            }
        }
        //if not, set the vertical coordinate to the proper value
        else drawY = TILE_SIZE*6-2;
        if (parentController.isDay()) aux = 1;
        else aux = 2;
        g.drawImage(Sprites.UI_MAP.get(aux).getImage(),drawX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
        drawY += 32;
        if (parentController.lightsOn()) aux = 3;
        else aux = 4;
        g.drawImage(Sprites.UI_MAP.get(aux).getImage(),drawX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
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
    
    /**
     * Initalizes the values of the UI
     * @param g an instace of Graphics2D used to draw
     */
    private void initializeUI(Graphics2D g){
        int rightX = leftMargin + cameraSize*TILE_SIZE;
        int leftX = leftMargin - TILE_SIZE;
        int drawY = TILE_SIZE - 4;
        for (int i = 0; i < cameraSize; ++i){
            g.drawImage(Sprites.UI_MAP.get(0).getImage(),leftX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
            g.drawImage(Sprites.UI_MAP.get(0).getImage(),rightX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
            drawY += 32;
        }
    }
    
    /**
     *  Draws the minimap, which shows the global map
     * @param g an instace of Graphics2D used to draw
     */
    private void drawMap(Graphics2D g){
            int size = t.getSize();
            int drawX, drawY;
            final int drawSize = 2;
            g.setColor(Color.black);
            int camSize = TILE_SIZE * cameraSize;
            int mapMargin = (camSize - 1000/drawSize)/2;
            g.fillRect(leftMargin+mapMargin - 8, mapMargin - 8, (camSize - mapMargin*2)+16, (camSize - mapMargin*2)+16);
            for (int i = 0; i < 1000; i+=2){
                for (int j = 0; j < 1000; j+=2){
                    drawX = (j / 2) + leftMargin + mapMargin;
                    drawY = (i / 2) + mapMargin;
                    Tile tile = t.getTile(i, j);
                    g.drawImage(Sprites.TERRAIN_MAP.get(tile.getTerrainID()).getImage(),drawX,drawY,2,2, rootPane);
                }
            }
            g.setPaint(Color.red);
            g.fillOval((activePlayer.getNode().getY()/2+leftMargin+mapMargin)-2,(activePlayer.getNode().getX()/2+mapMargin)-4, 8, 8);
    }
    
    /**
     * Toggles the view of the map
     */
    public void toggleShowMap(){
        showMap = !showMap;
        if (!showMap){
            BufferStrategy bs = this.getBufferStrategy();
            if(bs == null) {
                    this.createBufferStrategy(1);			
                    return;
            }
            Graphics2D g = (Graphics2D) bs.getDrawGraphics();
            g.clearRect(0, 0, panelWidth, panelHeight);
            g.dispose();
            bs.show();
        }
    }

}