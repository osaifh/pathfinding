package pathfinding;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.Timer;
import pathfinding.actor.*;
import pathfinding.actor.Creatures.*;
import pathfinding.actor.Interactables.*;
import pathfinding.actor.Skills.*;
import pathfinding.Table.*;
import pathfinding.auxiliar.Node;
import java.util.Random;
import pathfinding.Audio.AudioEngine;
import pathfinding.Audio.AudioManager;
import pathfinding.Controllers.CampController;
import pathfinding.Listeners.IndicatorListener;
import pathfinding.auxiliar.Constants;

/**
 * This class is used to control all the parameters of the game environment like the map (table), the list of objects, the controllable character
 * the time, handles the keyboard and mouse input, and manages the camera which shows the graphical part of the aplication
 * @author 
 */
public class Controller {
    private boolean running, lights, lightsOn, paused;
    private final int DAY_TIME = 2400;
    private Table tab;
    private Camera cam;
    private Player activePlayer;
    private ActorList objList;
    private int time, UIselected;
    private AudioEngine audioEngine;
    ActionListener taskPerformer = (ActionEvent e) -> {
        gameStep();
    };
    Timer timer = new Timer(10,taskPerformer);
    private Random rng = new Random();
    // delete this too btw
    boolean timeStop = true;

    public ArrayList<Skill> getSkillList(){
       return activePlayer.getSkillList();
    }

    /**
     * Default constructor for the controller class
     */
    public Controller() {
        running = true;
        tab = new Table();
        cam = new Camera(tab,this);
        cam.addKeyListener(kListener);
        cam.setFocusable(true);
        time = 0;
        UIselected = 1;
        objList = new ActorList();
        lights = lightsOn = false;
        audioEngine = AudioManager.getInstance();
    }
    
    /**
     * Initializes and generates some objects on startup
     * 
     */
    public void startup() {
        Node playerPos = new Node();
        do {
            playerPos.generate(tab.getSize());
        } while ((!tab.valid(playerPos) || !tab.checkPassable(playerPos)));
        activePlayer = new Player(playerPos.getX(),playerPos.getY());
        
        generateCamp();
    }
    
    /**
     * Starts the execution of the game
     */
    public void run() {
        /*
        try {
            load();
        }
        catch(Exception ex){
            System.out.println("Failed to load something");
            tab = new Table();
            objList = new ActorList();
            startup();
        }*/
        startup();
        generateActor(activePlayer);
        activePlayer.addIndicatorListener(new IndicatorListener(objList, tab));
        cam.setActivePlayer(activePlayer);
        cam.setPos(activePlayer.getNode());
        cam.setTable(tab);
        cam.fillVisibilityTable(true);
        timer.start();
        cam.update();
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
    
    public void generateCamp(){
        CampController campController = new CampController();
        Node n = tab.generateCamp(campController, objList);
        campController.getExternalNodes().forEach((node)->{
            Node ext = node.getNodeCopy();
            ext.add(1, 1);
            Guard guard = new Guard(ext,objList);
            guard.addIndicatorListener(new IndicatorListener(objList,tab));
            generateActor(guard);
            campController.addGuard(guard);
        });
        //for testing purposes only
        n.add(-1,-1);
        activePlayer.iMove(tab, n);
        cam.updatePosition();
    }
    
    /**
     * Used to know whether it's daytime or not
     * @return returns true if it's daytime
     */
    public boolean isDay(){
        return (time < DAY_TIME/2);
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
            cam.fillVisibilityTable(false);
            activePlayer.lookAround(tab,activePlayer.getSightRange(),cam);
            if (lightsOn) lightsOn = false;
        } else {
            if (!lightsOn){
                cam.fillVisibilityTable(true);
                tab.light();
                lightsOn = true;
            } 
        }
        
        //simulates all the objects and updates the camera
        if (!paused) objList.simulate(tab);
        cam.update();
        //updates the time value
        if(!timeStop) ++time;
        if (time > DAY_TIME) time = 0;
        
        activePlayer.getSkillList().forEach((skill)->{
            if (skill.getMaxCooldown()!=0 && skill.getCurrentCooldown() > 0){
                skill.addCurrentCooldown(-1);
            }
        });
    }
    
    private Skill activatedSkill;
    private boolean skillToggle;
    
    public void handleMouseRelease(int x, int y){
        skillToggle = false;
    }
    
    public void handleMouseHover(int x, int y){
        Node pos = new Node(x,y);
        if (activePlayer!=null){
            activePlayer.setFacingDirection(activePlayer.getNode().relativeDirection(pos));
            activePlayer.updateID();
        }
        if (skillToggle){
            activatedSkill.activate(activePlayer.getNode(), pos, tab, objList);
        }
    }
     
    /**
     * Handles the mouse input for certain given coordinates.
     * It's called every time the user clicks a valid tile
     * @param x horizontal coordinate
     * @param y vertical coordiate
     */
    public void handleMouseInput(int x, int y){
        Node pos = new Node(x,y);
        if (UIselected - 1 < activePlayer.getSkillList().size()){
            Skill skill = activePlayer.getSkillList().get(UIselected-1);
            if (skill instanceof PathfindingSkill){
                ((PathfindingSkill)skill).activate(activePlayer.getNode(), pos.getNodeCopy(), tab, objList, this);
            }
            else if (skill instanceof CreateMonsterSkill){
                ((CreateMonsterSkill)skill).activate(activePlayer.getNode(), pos.getNodeCopy(), tab, objList, this);
            }
            else if (skill.getCurrentCooldown() == 0){
                if (skill.isToggle()){
                    activatedSkill = skill;
                    skillToggle = true;
                }
                skill.activate(activePlayer.getNode(), pos.getNodeCopy(), tab, objList);
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
                    if (!cam.isLocked()) cam.getPos().add(Constants.DIRECTIONS_WORD.get("W"));
                    break;
                case KeyEvent.VK_RIGHT:
                    if (!cam.isLocked()) cam.getPos().add(Constants.DIRECTIONS_WORD.get("E"));
                    break;
                case KeyEvent.VK_UP:
                    if (!cam.isLocked()) cam.getPos().add(Constants.DIRECTIONS_WORD.get("N"));
                    break;
                case KeyEvent.VK_DOWN:
                    if (!cam.isLocked()) cam.getPos().add(Constants.DIRECTIONS_WORD.get("S"));
                    break;
                case KeyEvent.VK_SPACE:
                    break;
                case KeyEvent.VK_A:
                    activePlayer.iMove(tab,Constants.W);
                    if (cam.isLocked()) cam.updatePosition();
                    break;
                case KeyEvent.VK_W:
                    activePlayer.iMove(tab,Constants.N);
                    if (cam.isLocked()) cam.updatePosition();
                    break;
                case KeyEvent.VK_S:
                    activePlayer.iMove(tab,Constants.S);
                    if (cam.isLocked()) cam.updatePosition();
                    break;
                case KeyEvent.VK_D:
                    activePlayer.iMove(tab,Constants.E);
                    if (cam.isLocked()) cam.updatePosition();
                    break;
                case KeyEvent.VK_C:
                    //TODO: check before casting
                    cam.toggleLocked((Creature)tab.getTile(cam.getPos()).getContent());
                    break;
                case KeyEvent.VK_K:
                    save();
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
                case KeyEvent.VK_E:
                   activePlayer.toggleLight();
                    break;
                case KeyEvent.VK_P:
                    paused = !paused;
                    break;
                case KeyEvent.VK_B:
                    tab = new Table();
                    cam.setTable(tab);
                    break;
                case KeyEvent.VK_M:
                    cam.toggleShowMap();
                    break;
                case KeyEvent.VK_N:
                    if(timeStop) timeStop = false;
                    if (time<1200){
                        time = 1000;
                    }
                    else {
                        time = 2200;
                    }
                    break;
                case KeyEvent.VK_1:
                    if (activePlayer.getSkillList().size()>0){
                        UIselected = 1;
                    }
                    break;
                case KeyEvent.VK_2:
                    if (activePlayer.getSkillList().size()>1){
                        UIselected = 2;
                    }
                    break;
                case KeyEvent.VK_3:
                    if (activePlayer.getSkillList().size()>2){
                        UIselected = 3;
                    }
                    break;
                case KeyEvent.VK_4:
                    if (activePlayer.getSkillList().size()>3){
                        UIselected = 4;
                    }
                    break;
                case KeyEvent.VK_5:
                    if (activePlayer.getSkillList().size()>4){
                        UIselected = 5;
                    }
                    break;
                case KeyEvent.VK_6:
                    if (activePlayer.getSkillList().size()>5){
                        UIselected = 6;
                    }
                    break;
                case KeyEvent.VK_7:
                    if (activePlayer.getSkillList().size()>6){
                        UIselected = 7;
                    }
                    break;
                case KeyEvent.VK_8:
                    if (activePlayer.getSkillList().size()>7){
                        UIselected = 8;
                    }
                    break;
                case KeyEvent.VK_9:
                    if (activePlayer.getSkillList().size()>8){
                        UIselected = 9;
                    }
                    break;
                case KeyEvent.VK_0:
                    if (activePlayer.getSkillList().size()>9){
                        UIselected = 10;
                    }
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
    
    
    public void save(){
        String filenameTable = "table.ser";
        String filenameList = "list.ser";
        String filenamePlayer = "player.ser";
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;
        try {
            System.out.println("Starting saving process");
            
            fileOutputStream = new FileOutputStream(filenameTable);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(tab);
            objectOutputStream.close();
            fileOutputStream.close();
            
            fileOutputStream = new FileOutputStream(filenameList);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(objList);
            objectOutputStream.close();
            fileOutputStream.close();
            
            fileOutputStream = new FileOutputStream(filenamePlayer);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(activePlayer);
            objectOutputStream.close();
            fileOutputStream.close();
            
            System.out.println("Game saved");
        }
        catch (NotSerializableException ex){
            System.out.println(ex.toString());
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
    public void load() throws Exception{
        String filenameTable = "table.ser";
        String filenameList = "list.ser";
        String filenamePlayer = "player.ser";
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        try {
            System.out.println("Starting load process");
            
            fileInputStream = new FileInputStream(filenameTable);
            objectInputStream = new ObjectInputStream(fileInputStream);
            tab = (Table)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            
            fileInputStream = new FileInputStream(filenameList);
            objectInputStream = new ObjectInputStream(fileInputStream);
            objList = (ActorList) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            
            fileInputStream = new FileInputStream(filenamePlayer);
            objectInputStream = new ObjectInputStream(fileInputStream);
            activePlayer = (Player) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            
            System.out.println("Game loaded");
        }
        catch (Exception ex){
            System.out.println("File not found, making a new controller");
            throw(ex);
        }
    }
}