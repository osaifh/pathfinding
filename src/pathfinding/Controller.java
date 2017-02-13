package pathfinding;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Alumne
 */
public class Controller {
    private boolean running;
    private Table t;
    private Camera cam;
    private Creature active_player;
    private ObjecteList obj_list;
    private long lastLoopTime;
    private int fps, time;
    private double lastFpsTime;
    private final int TARGET_FPS = 60;
    private final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

    /**
     *
     */
    public LightSource lantern;

    /**
     *
     */
    public Controller() {
        running = true;
        t = new Table();
        cam = new Camera(t,listener);
        cam.setFocusable(true);
        fps = time = 0;
        lastFpsTime = 0;
        lastLoopTime = System.nanoTime();
        obj_list = new ObjecteList();
    }
    
    /**
     *
     */
    public void startup() {
        active_player = new Creature(49,49);
        t.add(49,49,active_player);
        obj_list.add(active_player, false);
        active_player.setIndex(obj_list.get_index());
        t.generateWalls(200);
        t.generateObject(200, 4);
        cam.setPos(active_player.getNode());
        //lantern = new LightSource(10,active_player.getNode().getX(),active_player.getNode().getY());
        //obj_list.add(lantern,true);
    }
    
    /**
     *
     */
    public void run() {
        startup();
        while (running) {
            //GAME LOOP
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME);
            
            t.dark(time);
            active_player.lookAround(t,active_player.getSightRange());
            obj_list.simulate(t,obj_list,delta);
            cam.update();

            // update the frame counter
            lastFpsTime += updateLength;
            fps++;
            //lantern.setPos(active_player.getNode());
            if (lastFpsTime >= 1000000000){
               System.out.println("(FPS: "+fps+")");
               lastFpsTime = 0;
               fps = 0;
               ++time;
               if (time > 240) time = 0;
               System.out.println("time: " + time);
            }

            try {
                Thread.sleep( Math.abs(lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
            } catch(InterruptedException ex){
                Thread.currentThread().interrupt();
            }
        }
    }
    
    KeyListener listener = new KeyListener(){
            @Override
            public void keyPressed(KeyEvent event){
                int action = event.getKeyCode();
                if (!cam.isLocked()){
                    if (action == KeyEvent.VK_LEFT){
                        cam.setPos(cam.getPos().direction(3,1));
                    }
                    else if (action == KeyEvent.VK_RIGHT){
                        cam.setPos(cam.getPos().direction(4,1));
                    }
                    else if (action == KeyEvent.VK_UP){
                        cam.setPos(cam.getPos().direction(1,1));
                    }
                    else if (action == KeyEvent.VK_DOWN){
                        cam.setPos(cam.getPos().direction(6,1));
                    }
                }
                if (action == KeyEvent.VK_SPACE){
                   t.getTile(cam.getPos()).clear();
                }
                if (action == KeyEvent.VK_A){
                   active_player.iMove(t,3,obj_list);
                }
                if (action == KeyEvent.VK_W){
                   active_player.iMove(t,1,obj_list);
                }
                if (action == KeyEvent.VK_S){
                   active_player.iMove(t,6,obj_list);
                }
                if (action == KeyEvent.VK_D){
                   active_player.iMove(t,4,obj_list);
                }
                if (action == KeyEvent.VK_C){
                   if (cam.isLocked()){
                       cam.setLocked(false);
                   }
                   else {
                       cam.setLocked(true);
                       cam.setPos(active_player.getNode());
                   }
                }
                if (action == KeyEvent.VK_1){
                    Creature n = new Creature(cam.getPos().getX(),cam.getPos().getY());
                    obj_list.add(n,true);
                    n.setIndex(obj_list.get_index());
                    t.add(cam.getPos().getX(),cam.getPos().getY(),n);
                }
                if (action == KeyEvent.VK_2){
                    t.set(cam.getPos(),1);
                }
                if (action == KeyEvent.VK_3){
                    t.getTile(cam.getPos()).setObjecte(new Objecte(4,cam.getPos().getX(),cam.getPos().getY()));
                }
                if (action == KeyEvent.VK_4){
                    LightSource ln = new LightSource(5,cam.getPos().getX(),cam.getPos().getY());
                    t.getTile(cam.getPos()).setObjecte(ln);
                    obj_list.add(ln,true);
                }
                if (action == KeyEvent.VK_5){
                    LightSource ln = new LightSource(10,cam.getPos().getX(),cam.getPos().getY());
                    t.getTile(cam.getPos()).setObjecte(ln);
                    obj_list.add(ln,true);
                }
                if (action == KeyEvent.VK_6){
                    LightSource ln = new LightSource(20,cam.getPos().getX(),cam.getPos().getY());
                    t.getTile(cam.getPos()).setObjecte(ln);
                    obj_list.add(ln,true);
                }
                if (action == KeyEvent.VK_0){
                    Hunter h = new Hunter(cam.getPos().getX(),cam.getPos().getY());
                    obj_list.add(h,true);
                    h.setIndex(obj_list.get_index());
                    t.add(cam.getPos().getX(),cam.getPos().getY(),h);
                }
                t.getTile(active_player.getNode()).setLit(true);
            }
            
            @Override
            public void keyReleased(KeyEvent event){
            }
            
            @Override
            public void keyTyped(KeyEvent event){
            }
            
        };

}