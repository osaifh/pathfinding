package pathfinding;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import pathfinding.actor.*;
import pathfinding.Table.*;
import pathfinding.auxiliar.Node;
import java.util.Random;

/**
 * This class is used to control all the parameters of the game environment like the map (table), the list of objects, the controllable character
 * the time, handles the keyboard and mouse input, and manages the camera which shows the graphical part of the aplication
 * @author 
 */
public class Controller {
    private boolean running, lights, lightsOn, paused;
    private Table tab;
    private Camera cam;
    private Player activePlayer;
    private ActorList objList, lightList;
    private int time, UIselected;
    private int test;
    ActionListener taskPerformer = (ActionEvent e) -> {
        gameStep();
    };
    Timer timer = new Timer(10,taskPerformer);
    private Random rng = new Random();
    protected Controller controller = this;
    

    /**
     * Default constructor for the controller class
     */
    public Controller() {
        test = 0;
        running = true;
        tab = new Table();
        cam = new Camera(tab,this);
        cam.addKeyListener(kListener);
        cam.setFocusable(true);
        time = 0;
        UIselected = 1;
        objList = new ActorList();
        lightList = new ActorList();
        lights = lightsOn = false;
    }
    
    /**
     * Initializes and generates some objects on startup
     */
    public void startup() {
        tab.getTile(49,49).clear();
        activePlayer = new Player(10,10);
        generateActor(activePlayer);
        cam.setActivePlayer(activePlayer);
        cam.setLockedObject(activePlayer);
        cam.setPos(activePlayer.getNode());
        //Site s = new Site(new Node(activePlayer.getNode().getX()+1,activePlayer.getNode().getY()+1),tab,objList,lightList);
    }
    
    /**
     * Starts the execution of the game
     */
    public void run() {
        startup();
        cam.clearVisibilityTable();
        timer.start();
    }
    
    /**
     * Given an actor, this function adds said actor to the table and to the objectList
     * thus making the actor ready. This must be used on new objects so they're properly generated
     * @param actor the new actor we have generated
     */
    public void generateActor(Actor actor){
        tab.add(actor);
        objList.add(actor, true);
    }
    
    /**
     * Used to know whether it's daytime or not
     * @return returns true if it's daytime
     */
    public boolean isDay(){
        return (time < 1200);
    }
    
    /**
     * Used to know whether the lights are on or not
     * @return returns true if the lights are on
     */
    public boolean lightsOn(){
        return lightsOn;
    }
    
    /**
     * Returns the selected element of the UI
     * @return the selected element of the UI
     */
    public int getSelected(){
        return UIselected;
    }
    
    /**
     * Returns the active controllable character
     * @return the active controllable character
     */
    public Creature getActivePlayer(){
        return activePlayer;
    }
    
    /**
     * Main game loop, called by timer
     */
    public void gameStep(){
        //updates lights and visibility
        if (!lights){
            tab.dark(time);
            cam.fillVisibilityTable();
            activePlayer.lookAround(tab,activePlayer.getSightRange(),cam);
            if (lightsOn) lightsOn = false;
        } else {
            if (!lightsOn){
                cam.clearVisibilityTable();
                tab.light();
                lightsOn = true;
            } 
        }
        
        //simulates all the objects and updates the camera
        lightList.simulate(tab);
        if (!paused) objList.simulate(tab);
        cam.update();
        
        /*
        if (!cam.isLocked()){
            for (int i = 0; i < objList.size() && !cam.isLocked(); ++i){
                Actor x = objList.get(i);
                if (x instanceof Creature && !(x instanceof Player)){
                    cam.toggleLocked((Creature)x);
                    
                }
            }
        }*/
        
        /*
        ++test;
        if (test >= 200){
            Node testNode;
            do {
                testNode = new Node(rng.nextInt(tab.getSize()),rng.nextInt(tab.getSize()));
            } while (!tab.valid(testNode));
            Monster n = new Monster(testNode,objList,this);
            objList.add(n,true);
            tab.add(testNode.getX(),testNode.getY(),n);
            System.out.println("Generated a monster in position " + testNode.toString());
            test = 0;
            for (int i = 0; i < 10; ++i){
                do {
                    testNode = new Node(rng.nextInt(tab.getSize()),rng.nextInt(tab.getSize()));
                } while (!tab.valid(testNode));
               //tab.add(new genericObject(4,testNode.getX(),testNode.getY()));
            }
        }*/
        
        //updates the time value
        ++time;
        if (time > 2400) time = 0;

        //prints some values
        //if (time%100 == 0) System.out.println("time: " + time);
    }
    
    /**
     * Handles the mouse input for certain given coordinates.
     * It's called every time the user clicks a valid tile
     * @param x horizontal coordinate
     * @param y vertical coordiate
     */
    public void handleMouseInput(int x, int y){
        Node pos = new Node(x,y);
        switch (UIselected) {
            case 1:
                Bullet b = new Bullet(activePlayer.getNode().getNodeCopy(),8,pos);
                generateActor(b);
                break;
            case 2:
                tab.getTile(pos).setWall();
                break;
            case 3:
                Door d = new Door(pos.getX(),pos.getY(), tab);
                tab.add(d);
                break;
            case 4:
            {
                LightSource ln = new LightSource(5,pos.getX(),pos.getY());
                lightList.add(ln,true);
                break;
            }
            case 5:
            {
                LightSource ln = new LightSource(10,pos.getX(),pos.getY());
                lightList.add(ln,true);
                break;
            }
            case 6:
            {
                LightSource ln = new LightSource(20,pos.getX(),pos.getY());
                lightList.add(ln,true);
                break;
            }
            case 7:
            {
                Monster n = new Monster(pos,objList,controller);
                generateActor(n);
                break;
            }
        }
    }
    
    /**
     * Handles the keyboard input
     */
    KeyListener kListener = new KeyListener() {
        int action;
        
        @Override
        public void keyPressed(KeyEvent event){
            action = event.getKeyCode();
            //MAPS MAPS MAPS MAPS MAPS MAPS
            //This could easily be abstracted to key,value pairs!
            //It'd be something like if(!cam.isLocked()) cam.setPos(cam.getPos().moveDirection(inputMap.get(action),1));
            //Rule of thumb: If you're writing code over and over and over again then there's a better way to do it. ALWAYS
            switch (action) {
                case KeyEvent.VK_LEFT:
                    if (!cam.isLocked()) cam.getPos().moveDirection(3,1);
                    break;
                case KeyEvent.VK_RIGHT:
                    if (!cam.isLocked()) cam.getPos().moveDirection(4,1);
                    break;
                case KeyEvent.VK_UP:
                    if (!cam.isLocked()) cam.getPos().moveDirection(1,1);
                    break;
                case KeyEvent.VK_DOWN:
                    if (!cam.isLocked()) cam.getPos().moveDirection(6,1);
                    break;
                case KeyEvent.VK_SPACE:
                    
                    /*Melee m = new Melee(2,1,activePlayer,objList,tab);
                    objList.add(m, true);
                    tab.add(m);*/
                    //explosion testing
                    /*
                    Explosion e = new Explosion(activePlayer, npos, tab, objList, 5);
                    objList.add(e, true);
                    tab.add(e);
                    */
                    /* bullet testing
                    if (npos.iMove(tab,activePlayer.getFacingDirection())){
                        Bullet b = new Bullet(npos,activePlayer.getFacingDirection(),4);
                        objList.add(b, true);
                        tab.add(b);
                    }
                   */

                    //if (!cam.isLocked()) tab.getTile(cam.getPos()).clearContent();
                    break;
                case KeyEvent.VK_A:
                    activePlayer.iMove(tab,3);
                    if (cam.checkLockedCreature(activePlayer) && cam.isLocked()) cam.updatePosition();
                    break;
                case KeyEvent.VK_W:
                    activePlayer.iMove(tab,1);
                    if (cam.checkLockedCreature(activePlayer) && cam.isLocked()) cam.updatePosition();
                    break;
                case KeyEvent.VK_S:
                    activePlayer.iMove(tab,6);
                    if (cam.checkLockedCreature(activePlayer) && cam.isLocked()) cam.updatePosition();
                    break;
                case KeyEvent.VK_D:
                    activePlayer.iMove(tab,4);
                    if (cam.checkLockedCreature(activePlayer) && cam.isLocked()) cam.updatePosition();
                    break;
                case KeyEvent.VK_C:
                    cam.toggleLocked(tab.getTile(cam.getPos()).getContent());
                    break;
                case KeyEvent.VK_L:
                    lights = !lights;
                    break;
                 case KeyEvent.VK_R:
                    tab.getTile(cam.getPos()).clearContent();
                    break;
                case KeyEvent.VK_Q:
                    for (int i = 0; i < tab.getTile(cam.getPos()).getContentSize(); ++i){
                        if (tab.getTile(cam.getPos()).getContent(i) instanceof Interactable){
                            ((Interactable)tab.getTile(cam.getPos()).getContent(i)).interact(tab);
                        }
                    }
                    break;
                case KeyEvent.VK_P:
                    paused = !paused;
                    break;
                case KeyEvent.VK_Z:
                    Monster n = new Monster(cam.getPos(),objList,controller);
                    objList.add(n,true);
                    tab.add(cam.getPos().getX(),cam.getPos().getY(),n);
                    break;
                case KeyEvent.VK_1:
                    UIselected = 1;
                    break;
                case KeyEvent.VK_2:
                    UIselected = 2;
                    break;
                case KeyEvent.VK_3:
                    UIselected = 3;
                    break;
                case KeyEvent.VK_4:
                    UIselected = 4;
                    break;
                case KeyEvent.VK_5:
                    UIselected = 5;
                    break;
                case KeyEvent.VK_6:
                    UIselected = 6;
                    break;
                case KeyEvent.VK_7:
                    UIselected = 7;
                    break;
                case KeyEvent.VK_8:
                    UIselected = 8;
                    break;
                case KeyEvent.VK_9:
                    UIselected = 9;
                    break;
                case KeyEvent.VK_0:
                    UIselected = 10;
                    break;
                default:
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent event){
        }

        @Override
        public void keyTyped(KeyEvent event){
        }

    };
    
}