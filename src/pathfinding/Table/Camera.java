package pathfinding.Table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import pathfinding.Controller;
import pathfinding.Indicators.DamageIndicator;
import static pathfinding.Table.Sprites.COLOR_MAP;
import pathfinding.auxiliar.Node;
import pathfinding.actor.Creatures.Creature;
import pathfinding.actor.Skills.Skill;
import pathfinding.auxiliar.Constants;

/**
 * This class is used to render what the camera currently shows.
 *
 * @author
 */
public class Camera extends JFrame {

    private final static int TILE_SIZE = 32;
    private static int cameraSize;
    private static int cameraWidth, cameraHeight;
    private int panelWidth, panelHeight, leftMargin;
    private Table table;
    private Node position;
    private boolean cameraLock;
    private JPanel jpanel = new JPanel();
    private final Controller parentController;
    private Creature activePlayer;
    private JLabel[][] inputTable;
    private boolean[][] visibilityTable;

    private boolean showMap = false;

    /**
     * Constructor class for camera
     *
     * @param table indicates the source table
     * @param parentController
     */
    public Camera(Table table, Controller parentController) {
        //A lot of these values should probably be passed into the camera constructor. 
        //What if later down the line you want to make another camera with a different size or cameraLock off?
        //The best practice here is to overload your constructor so you can pass in arguments for these items if you'd like
        //but have defaults when you don'table need to change them. 
        this.parentController = parentController;
        cameraLock = true;
        position = new Node();

        jpanel.setLayout(null);
        jpanel.setBackground(Color.black);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Simulation");
        setVisible(true);
        this.table = table;
        panelWidth = (int) this.getBounds().getWidth();
        panelHeight = (int) this.getBounds().getHeight();
        
        cameraSize = panelHeight / TILE_SIZE;
        cameraHeight = panelHeight / TILE_SIZE;
        final int UI_MARGIN = TILE_SIZE;
        
        cameraWidth = (panelWidth - UI_MARGIN * 2) / TILE_SIZE - 1;
        leftMargin = ((panelWidth - UI_MARGIN * 2) % TILE_SIZE)/2 + TILE_SIZE/2;
        visibilityTable = new boolean[cameraHeight][cameraWidth];
        //leftMargin = (panelWidth - cameraSize * TILE_SIZE) / 2;
        inputTable = new JLabel[cameraHeight][cameraWidth];
        int x, y;
        //this just places labels over the screen to be able to handle the mouse input
        for (int i = 0; i < cameraHeight; i++) {
            for (int j = 0; j < cameraWidth; j++) {
                //don't do this at home kids
                inputTable[i][j] = new JLabel();
                x = j * TILE_SIZE + leftMargin + TILE_SIZE;
                y = i * TILE_SIZE - TILE_SIZE;
                inputTable[i][j].setBounds(x, y, TILE_SIZE, TILE_SIZE);
                inputTable[i][j].setHorizontalAlignment(JLabel.RIGHT);

                //this looks weird but it just works
                final int a = i;
                final int b = j;
                MouseAdapter mAdapter = new MouseAdapter() {
                    int x, y;

                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            x = a + (position.getX() - (cameraHeight / 2));
                            y = b + (position.getY() - (cameraWidth / 2));
                            parentController.handleMouseInput(x, y);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        x = a + (position.getX() - (cameraHeight / 2));
                        y = b + (position.getY() - (cameraWidth / 2));
                        parentController.handleMouseHover(x, y);
                    }
                    
                    @Override
                    public void mouseReleased(MouseEvent e){
                        x = a + (position.getX() - (cameraHeight / 2));
                        y = b + (position.getY() - (cameraWidth / 2));
                        parentController.handleMouseRelease(x, y);
                    }
                    
                };
                inputTable[i][j].addMouseListener(mAdapter);
                this.add(inputTable[i][j]);
            }
        }
    }

    /**
     * Gets the size of the camera
     *
     * @return Returns the size of the camera
     */
    public int getCameraSize() {
        return cameraSize;
    }

    /**
     * Gets the current position in the table of the camera
     *
     * @return Returns the current position in the table of the camera
     */
    public Node getPos() {
        return position;
    }

    /**
     * Returns whether the camera is locked to the player position
     *
     * @return Returns whether the camera is locked
     */
    public boolean isLocked() {
        return cameraLock;
    }

    /**
     * Locks or unlocks the camera to the player position
     *
     * @param creature the creature to lock the camera to
     */
    public void toggleLocked(Creature creature) {
        if (creature!= null)
        if (!cameraLock) {
            
            this.activePlayer = (Creature) creature;
            activePlayer.setCamera(this);
            cameraLock = true;
        } else {
            if (activePlayer != null) {
                activePlayer.setCamera(null);
                activePlayer = null;
            }
            cameraLock = false;
        }
        else cameraLock = false;
    }

    /**
     * Sets the position of the camera
     *
     * @param n
     */
    public void setPos(Node n) {
        position.setToNode(n);
    }

    /**
     * Overloaded function that sets the position of the camera using specific
     * coordinates instead of a node
     *
     * @param x horizontal coordinate
     * @param y vertical coordinate
     */
    public void setPos(int x, int y) {
        position.set(x, y);
    }

    /**
     * Sets the creature to track with the camera
     *
     * @param activePlayer the creature to track with the camera
     */
    public void setActivePlayer(Creature activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     * Checks whether a creature matches the creature the camera is currently
     * locked on
     *
     * @param creature the creature to compare
     * @return Returns true if the camera is locked on the creature
     */
    public boolean checkLockedCreature(Creature creature) {
        return (activePlayer == creature);
    }

    /**
     * updates the position of the camera to the object it's tracking
     */
    public void updatePosition() {
        if (cameraLock && activePlayer != null) {
            this.position.setToNode(activePlayer.getNode());
        }
    }

    /**
     * Sets all the values of the visibility table to b used to either clear the
     * visibility table or make it all dark
     */
    public void fillVisibilityTable(boolean b) {
        for (int i = 0; i < visibilityTable.length; ++i) {
            for (int j = 0; j < visibilityTable[0].length; ++j) {
                visibilityTable[i][j] = b;
            }
        }
    }

    /**
     * Sets a tile of the visibility table to b
     *
     * @param x the horizontal coordinate
     * @param y the vertical coordinate
     * @param b the boolean value to set the tile
     */
    public void setVisibilityTable(int x, int y, boolean b) {
        x += cameraHeight / 2;
        y += cameraWidth / 2;
        if (x >= 0 && x < cameraHeight && y >= 0 && y < cameraWidth) {
            visibilityTable[x][y] = b;
        }
    }

    /**
     * Sets a tile of the visibility table to b
     *
     * @param n the node of the tile to set
     * @param b the boolean value to set the tile
     */
    public void setVisibilityTable(Node n, boolean b) {
        int x = n.getX() + cameraHeight / 2;
        int y = n.getY() + cameraWidth / 2;
        if (x >= 0 && x < cameraHeight && y >= 0 && y < cameraWidth) {
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
    private void updateTable() {
        int x, y, drawX, drawY;
        Tile tile;
        //I copied the bufferStrategy part of the code so I'm not sure what it does exactly
        //but it works
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();

        //this draws the stuff we see
        for (int i = 0; i < cameraHeight; ++i) {
            for (int j = 0; j < cameraWidth; ++j) {
                //x and y are the coordinates of the matching tiles of the table
                x = i + (position.getX() - (cameraHeight / 2));
                y = j + (position.getY() - (cameraWidth / 2));
                //drawX and drawY are the coordinates in which we draw the tiles
                drawX = j * TILE_SIZE + leftMargin + TILE_SIZE;
                drawY = i * TILE_SIZE;
                if (table.valid(x, y)) {
                    tile = table.getTile(x, y);
                    if (visibilityTable[i][j]) {
                        g.drawImage(Sprites.SPRITE_MAP.get(tile.getTerrainID()).getImage(), drawX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
                        if (table.getActor(x, y) != null) {
                            //draws every object in a tile
                            for (int k = 0; k < tile.getContentSize(); ++k) {
                                if (tile.getContent(k) != null && Sprites.SPRITE_MAP.get(tile.getContent(k).getID()) != null) {
                                    g.drawImage(Sprites.SPRITE_MAP.get(tile.getContent(k).getID()).getImage(), drawX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
                                    //SOMEONE give me a better alternative to using instaceof PLEASE
                                    if (tile.getContent(k) instanceof Creature && ((Creature)tile.getContent(k)).isAlive()) {
                                        g.setColor(Color.red);
                                        g.fillRect(drawX, drawY, TILE_SIZE-2, 2);
                                        g.setColor(Color.green);
                                        double max = ((Creature)tile.getContent(k)).getmaxHP();
                                        double current = ((Creature)tile.getContent(k)).getHP();
                                        int bar = (int) Math.round((current / max) * (TILE_SIZE));
                                        g.fillRect(drawX, drawY, bar, 2);
                                    }
                                }
                                //draws an indicator in a tile
                                //this is actually pretty bad and I hope there's a better way to do it
                                if (tile.getContent(k) != null && tile.getContent(k).getID() >= Constants.DAMAGE_INDICATOR){
                                    g.setColor(Color.BLACK);
                                    Font font = new Font("Impact",Font.BOLD,20);
                                    g.setFont(font);
                                    DamageIndicator damageIndicator = (DamageIndicator)tile.getContent(k);
                                    //why 6? because it just works
                                    g.drawString(damageIndicator.getDamage()+"", drawX+(TILE_SIZE/6), drawY+(TILE_SIZE/2)-damageIndicator.getTicks()/2);
                                }
                            }
                        }
                        //only draw a rectangle if the light value isn'table max
                        //this shouldn'table be needed I'm just trying to optimize I guess
                        if (tile.getLight() < 100) {
                            float alpha = 100 - tile.getLight();
                            alpha /= 100;
                            g.setColor(new Color(0, 0, 0, alpha));
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
        if (showMap) {
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
        if (bs == null) {
            this.createBufferStrategy(2);
            return;
        }
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        
        initializeUI(g);
        drawLeftUI(g);
        drawRightUI(g);
        
        g.dispose();
        bs.show();
    }

    /**
     * Initalizes the values of the UI
     *
     * @param g an instace of Graphics2D used to draw
     */
    private void initializeUI(Graphics2D g) {
        int rightX = leftMargin + cameraWidth * TILE_SIZE + TILE_SIZE;
        int leftX = leftMargin;
        int drawY = TILE_SIZE - TOP_MARGIN;
        for (int i = 0; i < cameraHeight; ++i) {
            g.drawImage(Sprites.UI_MAP.get(0).getImage(), leftX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
            if (i > 4){
                g.drawImage(Sprites.UI_MAP.get(0).getImage(), rightX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
            }
            drawY += TILE_SIZE;
        }
    }
    
    private void drawLeftUI(Graphics2D g){
        int drawX, drawY;
        drawX = leftMargin;
        //turns out for some reason this drawY is unaligned by 4 pixels
        //it just works man
        drawY = TILE_SIZE - TOP_MARGIN;
        int aux;
        for (int i = 0; i < parentController.getSkillList().size(); i++){
            Skill skill = parentController.getSkillList().get(i);
            g.drawImage(Sprites.SPRITE_MAP.get(skill.getIcon()).getImage(), drawX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
            if (skill.getMaxCooldown() != 0 && skill.getCurrentCooldown() != 0){
                float alpha = 0.5f;
                g.setColor(new Color(0, 0, 0, alpha));
                int height = skill.getCurrentCooldown() * TILE_SIZE / skill.getMaxCooldown();
                g.fillRect(drawX, drawY + (TILE_SIZE - height), TILE_SIZE , height);
            }
            if (parentController.getSelected()-1 == i){
                g.drawImage(Sprites.UI_MAP.get(5).getImage(), drawX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
            }
            
            drawY += TILE_SIZE;
        }
    }
    
    //testing some stuff
    private float lastPer = -1;
    private final int BAR_SIZE = TILE_SIZE * 5;
    private final float FADE_RATE = 0.005f;
    private final int TOP_MARGIN = 4;
    private final int BAR_MARGIN = 2;
    private final int BAR_WIDTH = TILE_SIZE - 4;
    
    private void drawRightUI(Graphics2D g){
        int drawX, drawY;
        drawX = leftMargin + cameraWidth * TILE_SIZE + TILE_SIZE;
        drawY = TILE_SIZE - TOP_MARGIN;
        
        if (activePlayer != null) {
            //draws the black background for the bar
            g.setColor(Color.BLACK);
            g.fillRect(drawX, drawY, TILE_SIZE, BAR_SIZE);
            
            float currentPer = (float)activePlayer.getHP()/(float)activePlayer.getmaxHP();
            //if (currentPer > 0){
                int currentHeight = (int)Math.round(currentPer*BAR_SIZE);
                int hpBarHeight = BAR_SIZE - currentHeight;
                
                //draws the life bar
                if (currentHeight > 0){
                    g.setColor(Color.GREEN);
                    g.fillRect(drawX+BAR_MARGIN, drawY + hpBarHeight, BAR_WIDTH, currentHeight);
                }
                
                if (lastPer != -1){
                    if (lastPer > currentPer){
                        float diffPer = lastPer - currentPer;
                        int currHeight = (int)Math.round(diffPer*BAR_SIZE);
                        int yOffset = BAR_SIZE - currentHeight - currHeight;
                        
                        //draws a fading grey bar whenever the hitpoints are reduced
                        if (currHeight > 0 && yOffset < BAR_SIZE){
                            g.setColor(Color.GRAY);
                            g.fillRect(drawX+BAR_MARGIN, drawY + yOffset, BAR_WIDTH, currHeight);
                        }
                        //reduces the size of the bar by reducing the percentage by a certain fade rate
                        lastPer -= FADE_RATE;
                        
                        if (lastPer < currentPer) lastPer = currentPer;
                    }
                    else {
                        lastPer = currentPer;
                    }
                }
                else {
                    lastPer = currentPer;
                }
            //}
        } 
        
        drawY+= BAR_SIZE;
        int aux;
        if (parentController.isDay()) {
            aux = 1;
        } else {
            aux = 2;
        }
        g.drawImage(Sprites.UI_MAP.get(aux).getImage(), drawX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
        drawY += TILE_SIZE;
        if (parentController.lightsOn()) {
            aux = 3;
        } else {
            aux = 4;
        }
        g.drawImage(Sprites.UI_MAP.get(aux).getImage(), drawX, drawY, TILE_SIZE, TILE_SIZE, rootPane);
        drawY += TILE_SIZE;
    }

    
    
    /**
     * Draws the minimap, which shows the global map
     *
     * @param g an instance of Graphics2D used to draw
     */
    private void drawMap(Graphics2D g) {
        int size = table.getSize();
        int drawX, drawY;
        final int drawSize = 2;
        g.setColor(Color.black);
        int camWidth = TILE_SIZE * cameraWidth;
        int camHeight = TILE_SIZE * cameraHeight;
        int mapLeftMargin = (camWidth - table.getSize() / drawSize) / 2;
        int mapTopMargin = (camHeight - table.getSize() / drawSize) / 2;
        //TODO: fix the magic numbers
        g.fillRect(leftMargin + mapLeftMargin - 8, mapTopMargin - 8, (camHeight - mapTopMargin * 2) + 16, (camWidth - mapLeftMargin * 2) + 16);
        for (int i = 0; i < table.getSize(); i += drawSize) {
            for (int j = 0; j < table.getSize(); j += drawSize) {
                drawX = (j / drawSize) + leftMargin + mapLeftMargin;
                drawY = (i / drawSize) + mapTopMargin;
                Tile tile = table.getTile(i, j);
                g.setColor(COLOR_MAP.get(tile.getTerrainID()));
                g.fillRect(drawX, drawY, drawSize, drawSize);
            }
        }
        g.setPaint(Color.red);
        if (activePlayer != null) {
            g.fillOval((activePlayer.getNode().getY() / 2 + leftMargin + mapLeftMargin) - 2, (activePlayer.getNode().getX() / 2 + mapTopMargin) - 4, 8, 8);
        }
    }

    /**
     * Toggles the view of the map
     */
    public void toggleShowMap() {
        showMap = !showMap;
        if (!showMap) {
            BufferStrategy bs = this.getBufferStrategy();
            if (bs == null) {
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
