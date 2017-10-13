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
 * Class used to put everything together
 * @author Alumne
 */
public class Controller {
    private boolean running, lights, lightsOn;
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
     * Generates some objects needed to start
     */
	 //Pass in the startup as arguments, never use magic numbers like this
	 //Or have them declared final somewhere and give them a name. 
	 //This just helps with maintainability. 
    public void startup() {
        tab.generateWalls(200);
        tab.generateObject(200, 4);
        tab.getTile(49,49).clear();
        activePlayer = new Player(49,49);
        cam.setActivePlayer(activePlayer);
        tab.add(activePlayer);
        objList.add(activePlayer, true);
        cam.setPos(activePlayer.getNode());
        Site s = new Site(new Node(activePlayer.getNode().getX()+1,activePlayer.getNode().getY()+1),tab,objList,lightList);
    }
    

    public void run() {
        startup();
        cam.clearVisibilityTable();
        timer.start();
    }
    
    public boolean isDay(){
        return (time < 1200);
    }
    
    public boolean lightsOn(){
        return lightsOn;
    }
    
    public int getSelected(){
        return UIselected;
    }
    
    public Creature getActivePlayer(){
        return activePlayer;
    }
    
    /**
     * Main game loop
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
        
        //test this??
        /*
        if (!activePlayer.isAlive()){
            tab.getTile(activePlayer.getNode()).clearMatchingContent(activePlayer);
            activePlayer = null;
            timer.stop();
        }*/

        //simulates all the objects and updates the camera
        lightList.simulate(tab);
        objList.simulate(tab);
        cam.update();

        ++test;
        if (test >= 200){
            Node testNode;
            do {
                testNode = new Node(rng.nextInt(tab.getSize()),rng.nextInt(tab.getSize()));
            } while (!tab.valid(testNode));
            Monster n = new Monster(testNode,objList);
            objList.add(n,true);
            tab.add(testNode.getX(),testNode.getY(),n);
            System.out.println("Generated a monster in position " + testNode.toString());
            test = 0;
        }
        //prints some values
        //++time;
        time = 2000;
        if (time > 2400) time = 0;
        //if (time%100 == 0)
        //System.out.println("time: " + time);
    }
    
    public void handleMouseInput(int x, int y){
        Node pos = new Node(x,y);
        Bullet b = new Bullet(activePlayer.getNode().getNodeCopy(),8,pos.getNodeCopy());
        objList.add(b, true);
        tab.add(b);
    }
    
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
                    Node npos = new Node(activePlayer.getNode());
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
                        //DOESN'T WORK
                        AttachableLightSource ALS = new AttachableLightSource(3,npos.getX(),npos.getY(),b);
                        lightList.add(ALS,true);
                        tab.add(ALS);
                        
                    }
                   */
                    
                    switch (UIselected) {
                        case 1:
                            if (npos.iMove(tab,activePlayer.getFacingDirection())){
                                Bullet b = new Bullet(npos,activePlayer.getFacingDirection(),4);
                                objList.add(b, true);
                                tab.add(b);
                            }
                            /*
                            Creature n = new Mob(cam.getPos().getX(),cam.getPos().getY());
                            objList.add(n,true);
                            tab.add(cam.getPos().getX(),cam.getPos().getY(),n);
                            */
                            break;
                        case 2:
                            tab.getTile(cam.getPos()).setWall();
                            break;
                        case 3:
                            Door d = new Door(cam.getPos().getX(),cam.getPos().getY(), tab);
                            tab.add(d);
                            break;
                        case 4:
                        {
                            LightSource ln = new LightSource(5,cam.getPos().getX(),cam.getPos().getY());
                            tab.add(ln);
                            lightList.add(ln,true);
                            break;
                        }
                        case 5:
                        {
                            LightSource ln = new LightSource(10,cam.getPos().getX(),cam.getPos().getY());
                            tab.add(ln);
                            lightList.add(ln,true);
                            break;
                        }
                        case 6:
                        {
                            LightSource ln = new LightSource(20,cam.getPos().getX(),cam.getPos().getY());
                            tab.getTile(cam.getPos()).addContent(ln);
                            lightList.add(ln,true);
                            break;
                        }
                        case 7:
                            Core c = new Core(cam.getPos().getX(),cam.getPos().getY());
                            objList.add(c,true);
                            tab.add(cam.getPos().getX(),cam.getPos().getY(),c);
                            break;
                        case 8:
                            for (int i = 0; i < objList.size(); ++i){
                                objList.get(i).print();
                                if (objList.get(i) instanceof Core){
                                    ((Core) objList.get(i)).makeScout(tab,objList);
                                }
                            }
                            break;
                        case 9:
                            tab.generateObject(200, 4);
                            break;
                        case 10:
                            for (int i = 0; i < objList.size(); ++i){
                                objList.get(i).print();
                                if (objList.get(i) instanceof Core){
                                    ((Core) objList.get(i)).makeWorker(tab,objList);
                                }
                            }
                            break;
                    }
                    
                    //if (!cam.isLocked()) tab.getTile(cam.getPos()).clearContent();
                    break;
                case KeyEvent.VK_A:
                    activePlayer.iMove(tab,3);
                    if (cam.isLocked()) cam.setPos(activePlayer.getNode());
                    break;
                case KeyEvent.VK_W:
                    activePlayer.iMove(tab,1);
                    if (cam.isLocked()) cam.setPos(activePlayer.getNode());
                    break;
                case KeyEvent.VK_S:
                    activePlayer.iMove(tab,6);
                    if (cam.isLocked()) cam.setPos(activePlayer.getNode());
                    break;
                case KeyEvent.VK_D:
                    activePlayer.iMove(tab,4);
                    if (cam.isLocked()) cam.setPos(activePlayer.getNode());
                    break;
                case KeyEvent.VK_C:
                    cam.setLocked(!cam.isLocked());
                    if (cam.isLocked()) cam.setPos(activePlayer.getNode());
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
                    if (timer.isRunning()) timer.stop();
                    else timer.start();
                    break;
                case KeyEvent.VK_Z:
                    Monster n = new Monster(cam.getPos(),objList);
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