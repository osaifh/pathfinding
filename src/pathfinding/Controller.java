package pathfinding;

import pathfinding.actor.LightSource;
import pathfinding.actor.Creature;
import pathfinding.actor.ActorList;
import pathfinding.Table.Camera;
import pathfinding.Table.Table;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import pathfinding.actor.Door;
import pathfinding.actor.Interactable;
import pathfinding.actor.Mob;
import pathfinding.actor.Player;
import pathfinding.auxiliar.Node;

/**
 * Class used to put everything together
 * @author Alumne
 */
public class Controller {
    private boolean running, lights, lightsOn;
    private Table tab;
    private Camera cam;
	//Use camel case for non static final variables.
    private Player activePlayer;
    private ActorList objList, lightList;
    private int time;
    ActionListener taskPerformer = (ActionEvent e) -> {
        gameStep();
    };
    Timer timer = new Timer(10,taskPerformer);
    

    /**
     * Default constructor for the controller class
     */
    public Controller() {
        running = true;
        tab = new Table();
        cam = new Camera(tab);
        cam.addMouseListener(mAdapter);
        cam.addKeyListener(kListener);
        cam.setFocusable(true);
        time = 0;
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
        activePlayer = new Player(49,49);
        tab.add(49,49,activePlayer);
        objList.add(activePlayer, false);
        tab.generateWalls(200);
        tab.generateObject(200, 4);
        cam.setPos(activePlayer.getNode());
    }
    

    public void run() {
        startup();
        cam.clearVisibilityTable();
        timer.start();
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

        //simulates all the objects and updates the camera
        objList.simulate(tab);
        lightList.simulate(tab);
        cam.update();


        //prints some values
           ++time;
           if (time > 2400) time = 0;
           if (time%100 == 0)
           System.out.println("time: " + time);
    }
    
	//You sure learn how to use lambda expressions someday :)
    MouseAdapter mAdapter = new MouseAdapter(){
        @Override
		//I'm assuming you're trying to limit click events to inside the JFrame? 
		//If that's the case, you should be able to attach the adapter to a container or something
		//Been a while since I used a mouse adapter but I know there's a better way
        public void mouseClicked(MouseEvent e){
            /*
            Point p = e.getPoint();
            //fucking magic numbers how do they work?
			//They don't. Stop using them. Or at least comment them
            int x = cam.getPos().getX()*64;
            int y = cam.getPos().getY()*64;
            x += ((int)p.getY()-320)-32;
            y += ((int)p.getX()-320);
            x /= 64;
            y /= 64;
            ++y;
            //really made me think
            activePlayer.BFS(x, y, tab);
            */
        }
    };
    

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
                        if (!cam.isLocked()) tab.getTile(cam.getPos()).clear();
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
                    case KeyEvent.VK_Q:
                        if (tab.checkInteractable(cam.getPos())){
                            ((Interactable)tab.getTile(cam.getPos()).getContent()).interact(tab);
                        }
                    break;
                    case KeyEvent.VK_P:
                        if (timer.isRunning()) timer.stop();
                        else timer.start();
                        break;
                    case KeyEvent.VK_1:
                        Creature n = new Mob(cam.getPos().getX(),cam.getPos().getY());
                        objList.add(n,true);
                        tab.add(cam.getPos().getX(),cam.getPos().getY(),n);
                        break;
                    case KeyEvent.VK_2:
                        tab.set(cam.getPos(),1);
                        break;
                    case KeyEvent.VK_3:
                        Door d = new Door(cam.getPos().getX(),cam.getPos().getY(), tab);
                        tab.getTile(cam.getPos().getX(),cam.getPos().getY()).setContent(d);
                        break;
                    case KeyEvent.VK_4:
                        {
                            LightSource ln = new LightSource(5,cam.getPos().getX(),cam.getPos().getY());
                            tab.getTile(cam.getPos()).setContent(ln);
                            lightList.add(ln,true);
                            break;
                        }
                    case KeyEvent.VK_5:
                        {
                            LightSource ln = new LightSource(10,cam.getPos().getX(),cam.getPos().getY());
                            tab.getTile(cam.getPos()).setContent(ln);
                            lightList.add(ln,true);
                            break;
                        }
                    case KeyEvent.VK_6:
                        {
                            LightSource ln = new LightSource(20,cam.getPos().getX(),cam.getPos().getY());
                            tab.getTile(cam.getPos()).setContent(ln);
                            lightList.add(ln,true);
                            break;
                        }
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