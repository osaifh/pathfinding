package pathfinding;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class used to put everything together
 * @author Alumne
 */
public class Controller {
    private boolean running, lights, lightson;
    private Table tab;
    private Camera cam;
    private Creature active_player;
    private SubjectList obj_list;
    private long lastLoopTime;
    private int fps, time;
    private double lastFpsTime;
    private final int TARGET_FPS = 60;
    private final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

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
        fps = time = 0;
        lastFpsTime = 0;
        lastLoopTime = System.nanoTime();
        obj_list = new SubjectList();
        lights = lightson = false;
    }
    
    /**
     * Generates some objects needed to start
     */
    public void startup() {
        active_player = new Creature(49,49);
        tab.add(49,49,active_player);
        obj_list.add(active_player, false);
        active_player.setIndex(obj_list.get_index());
        tab.generateWalls(200);
        tab.generateObject(200, 4);
        cam.setPos(active_player.getNode());
    }
    
    /**
     * Main game loop
     */
    public void run() {
        startup();
        cam.clearVisibilityTable();
        while (running) {
            //updates the time values
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME);
            
            //updates lights and visibility
            if (!lights){
                tab.dark(time);
                cam.fillVisibilityTable();
                active_player.lookAround(tab,active_player.getSightRange(),cam);
                if (lightson) lightson = false;
            } else {
                if (!lightson){
                    cam.clearVisibilityTable();
                    tab.light();
                    lightson = true;
                } 
            }
            
            //simulates all the objects and updates the camera
            obj_list.simulate(tab,obj_list,delta);
            cam.update();

            // update the frame counter
            lastFpsTime += updateLength;
            fps++;
            
            //prints some values and resets the FPS counter
            if (lastFpsTime >= 1000000000){
               System.out.println("(FPS: "+fps+")");
               lastFpsTime = 0;
               fps = 0;
               ++time;
               if (time > 240) time = 0;
               System.out.println("time: " + time);
            }

            try {
                Thread.sleep(Math.abs(lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
            } catch(InterruptedException ex){
                Thread.currentThread().interrupt();
            }
        }
    }
    
    MouseAdapter mAdapter = new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e){
            Point p = e.getPoint();
            //fucking magic numbers how do they work?
            int x = cam.getPos().getX()*64;
            int y = cam.getPos().getY()*64;
            x += ((int)p.getY()-320)-32;
            y += ((int)p.getX()-320);
            x /= 64;
            y /= 64;
            ++y;
            //really made me think
            active_player.BFS(x, y, tab);
        }
    };
    
    KeyListener kListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event){
                int action = event.getKeyCode();
                switch (action) {
                    case KeyEvent.VK_LEFT:
                        if (!cam.isLocked()) cam.setPos(cam.getPos().direction(3,1));
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (!cam.isLocked()) cam.setPos(cam.getPos().direction(4,1));
                        break;
                    case KeyEvent.VK_UP:
                        if (!cam.isLocked()) cam.setPos(cam.getPos().direction(1,1));
                        break;
                    case KeyEvent.VK_DOWN:
                        if (!cam.isLocked()) cam.setPos(cam.getPos().direction(6,1));
                        break;
                    case KeyEvent.VK_SPACE:
                        if (!cam.isLocked()) tab.getTile(cam.getPos()).clear();
                        break;
                    case KeyEvent.VK_A:
                        active_player.iMove(tab,3);
                        break;
                    case KeyEvent.VK_W:
                        active_player.iMove(tab,1);
                        break;
                    case KeyEvent.VK_S:
                        active_player.iMove(tab,6);
                        break;
                    case KeyEvent.VK_D:
                        active_player.iMove(tab,4);
                        break;
                    case KeyEvent.VK_C:
                        if (cam.isLocked()){
                            cam.setLocked(false);
                        }
                        else {
                            cam.setLocked(true);
                            cam.setPos(active_player.getNode());
                        }
                        break;
                    case KeyEvent.VK_L:
                        if (lights) lights = false;
                        else lights = true;
                        break;
                    case KeyEvent.VK_1:
                        Creature n = new Creature(cam.getPos().getX(),cam.getPos().getY());
                        obj_list.add(n,true);
                        n.setIndex(obj_list.get_index());
                        tab.add(cam.getPos().getX(),cam.getPos().getY(),n);
                        break;
                    case KeyEvent.VK_2:
                        tab.set(cam.getPos(),1);
                        break;
                    case KeyEvent.VK_3:
                        tab.getTile(cam.getPos()).setObjecte(new Subject(4,cam.getPos().getX(),cam.getPos().getY()));
                        break;
                    case KeyEvent.VK_4:
                        {
                            LightSource ln = new LightSource(5,cam.getPos().getX(),cam.getPos().getY());
                            tab.getTile(cam.getPos()).setObjecte(ln);
                            obj_list.add(ln,true);
                            break;
                        }
                    case KeyEvent.VK_5:
                        {
                            LightSource ln = new LightSource(10,cam.getPos().getX(),cam.getPos().getY());
                            tab.getTile(cam.getPos()).setObjecte(ln);
                            obj_list.add(ln,true);
                            break;
                        }
                    case KeyEvent.VK_6:
                        {
                            LightSource ln = new LightSource(20,cam.getPos().getX(),cam.getPos().getY());
                            tab.getTile(cam.getPos()).setObjecte(ln);
                            obj_list.add(ln,true);
                            break;
                        }
                    case KeyEvent.VK_0:
                        Hunter h = new Hunter(cam.getPos().getX(),cam.getPos().getY());
                        obj_list.add(h,true);
                        h.setIndex(obj_list.get_index());
                        tab.add(cam.getPos().getX(),cam.getPos().getY(),h);
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