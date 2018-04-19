package pathfinding;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.Timer;
import pathfinding.actor.*;
import pathfinding.actor.Creatures.*;
import pathfinding.actor.Interactables.*;
import pathfinding.actor.Particles.*;
import pathfinding.actor.Skills.*;
import pathfinding.Table.*;
import pathfinding.auxiliar.Node;
import java.util.Random;
import pathfinding.Controllers.CampController;
import pathfinding.Indicators.DamageIndicator;
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
    private ActorList objList, lightList;
    private int time, UIselected;
    ActionListener taskPerformer = (ActionEvent e) -> {
        gameStep();
    };
    Timer timer = new Timer(10,taskPerformer);
    private Random rng = new Random();
    protected Controller controller = this;
    // delete this too btw
    boolean timeStop = true;
    private Mob trackingMob;
    //TODO: move this into the player and each creature
    private ArrayList<Skill> skillList;
    
    public ArrayList<Skill> getSkillList(){
        return skillList;
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
        lightList = new ActorList();
        lights = lightsOn = false;
        
        skillList = new ArrayList<Skill>();
        skillList.add(new CreateGuardSkill());
        skillList.add(new ShootSkill(10));
        skillList.add(new CreateWallSkill());
        skillList.add(new CreateExplosionSkill());
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
        generateActor(activePlayer);
        activePlayer.addIndicatorListener(new IndicatorListener(objList, tab));
        cam.setActivePlayer(activePlayer);
        cam.setPos(activePlayer.getNode());
        
        generateCamp();
        //some temporary code, just meant to test some things
        //will be removed later
        //for (int i = 0; i < 10; ++i) tab.generateSquareHouse(10, 1, lightList);
        //tab.generateTown(new Node(playerPos.getX()-1,playerPos.getY()-1), lightList);
        //tab.generateSquareHouse(, 10, 2, lightList);
        //Site s = new Site(new Node(activePlayer.getNode().getX()+1,activePlayer.getNode().getY()+1),tab,objList,lightList);
    }
    
    /**
     * Starts the execution of the game
     */
    public void run() {
        startup();
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
        Node n = tab.generateCamp(campController,lightList);
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
        lightList.simulate(tab);
        if (!paused) objList.simulate(tab);
        cam.update();
        //updates the time value
        if(!timeStop) ++time;
        if (time > DAY_TIME) time = 0;
        
        skillList.forEach((skill)->{
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
        if (UIselected - 1 < skillList.size()){
            Skill skill = skillList.get(UIselected-1);
            if (skill.getCurrentCooldown() == 0){
                if (skill.isToggle()){
                    activatedSkill = skill;
                    skillToggle = true;
                }
                skill.activate(activePlayer.getNode(), pos.getNodeCopy(), tab, objList);
            }
        }
        /*
            case 1:
                ShotSource shotSource = new ShotSource(activePlayer.getNode().getNodeCopy(),pos,tab,objList);
                //System.out.println(tab.getTile(pos).getTerrainID());
                //generateActor(new DamageIndicator(100,pos));
                //primeRunes(pos);
                //testing left click to move
               // activePlayer.BFS(pos.getX(), pos.getY(), tab, this);
                
                
                //Bullet b = new Bullet(activePlayer.getNode().getNodeCopy(),20,pos);
                //generateActor(b);
                
                break;
            case 2:
                tab.getTile(pos).setWall();
                break;
            case 3:
                
                //Rune rune = new Rune(pos);
                //runeList.add(rune);
                //generateActor(rune);
                
                //Bullet b = new Bullet(activePlayer.getNode().getNodeCopy(),20,pos);
                break;
            case 4:
            {
                Door door = new Door(pos.getX(),pos.getY(),tab);
                generateActor(door);
                
                //LightSource ln = new LightSource(5,pos.getX(),pos.getY());
                //lightList.add(ln,true);
                
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
                Guard guard = new Guard(pos,objList);
                generateActor(guard);
                break;
            }
            case 8:
            {
                Mob mob = new Mob(pos.getX(),pos.getY());
                if (trackingMob == null) trackingMob = mob;
                else {
                    mob.setTracking(trackingMob);
                    trackingMob = mob;
                }
                generateActor(mob);
                //tab.generateSquareHouse(pos.getNodeCopy(), 10, 2, lightList);
                break;
            }
            case 9:
            {
                Explosion ex = new Explosion(activePlayer,pos,tab,objList,8);
                generateActor(ex);
                break;
            }
            
        }
        */
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
                    if (skillList.size()>0){
                        UIselected = 1;
                    }
                    break;
                case KeyEvent.VK_2:
                    if (skillList.size()>1){
                        UIselected = 2;
                    }
                    break;
                case KeyEvent.VK_3:
                    if (skillList.size()>2){
                        UIselected = 3;
                    }
                    break;
                case KeyEvent.VK_4:
                    if (skillList.size()>3){
                        UIselected = 4;
                    }
                    break;
                case KeyEvent.VK_5:
                    if (skillList.size()>4){
                        UIselected = 5;
                    }
                    break;
                case KeyEvent.VK_6:
                    if (skillList.size()>5){
                        UIselected = 6;
                    }
                    break;
                case KeyEvent.VK_7:
                    if (skillList.size()>6){
                        UIselected = 7;
                    }
                    break;
                case KeyEvent.VK_8:
                    if (skillList.size()>7){
                        UIselected = 8;
                    }
                    break;
                case KeyEvent.VK_9:
                    if (skillList.size()>8){
                        UIselected = 9;
                    }
                    break;
                case KeyEvent.VK_0:
                    if (skillList.size()>9){
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
    
}