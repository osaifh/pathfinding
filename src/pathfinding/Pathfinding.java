package pathfinding;
import java.util.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class Pathfinding {
    
    public static void main(String[] args) throws InterruptedException{
        //variable declarations and inicializations
        boolean running = true;
        //dec is the variable we use to let the user choose what option he wants to do
        int dec = 0;
        Scanner user_input = new Scanner(System.in);
        Table t = new Table();
        Objecte_list object_list = new Objecte_list();
        Camera cam = new Camera(t);
        cam.set_pos_xy(49,49);
        cam.setFocusable(true);
        long lastLoopTime = System.nanoTime();
        int fps = 0;
        double lastFpsTime = 0;
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        /*
        System.out.print("0 = stop \n"
                        +"1 = reset\n"
                        +"2 = generate n walls\n"
                        +"3 = place object with ID 4 at position[x][y]\n"
                        +"4 = place player at position [x][y]\n"
                        +"5 = find path to position [x][y]\n"
                        +"6 = run one iteration\n"
                        +"7 = shows the current path\n"
                        +"8 = prints the list of objects\n"
                        +"9 = run n iterations\n"
                        +"11 = search ID in range x\n"
                        +"12 = generate n objectes with id x\n"
                        +"13 = simulate n iterations\n"
                        +"14 = fill the list with players\n"
                        +"15 = generate n boxes\n"
                        +"16 = generate a spawner\n");
        */
        t.generate_walls(200);
        t.generate_object(200, 4);
        Player p = new Player(49,49);
                        t.add(49,49,p);
                        object_list.add(p);
                        p.set_index(object_list.get_index());
        
        
        cam.set_active_player(p,object_list);
        while (running){
            //gets the user input
            //dec = user_input.nextInt();
            
            //goes to the user choice
            
            //GAME LOOP
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME);

            // update the frame counter
            lastFpsTime += updateLength;
            fps++;
            
            if (lastFpsTime >= 1000000000)
            {
               System.out.println("(FPS: "+fps+")");
               lastFpsTime = 0;
               fps = 0;
            }

            t.dark();
            p.look_around(t,p.get_sight_range());
            //object_list.simulate(t,object_list,delta);
            cam.update();
            try {
                Thread.sleep( Math.abs(lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
                } catch(InterruptedException ex){
                    Thread.currentThread().interrupt();
                }   
      /*
            switch (dec) {
                case 0:
                    running = false;
                    break;
                case 1:
                    t = new Table();
                    object_list = new Objecte_list();
                    break;
                case 2:
                    {
                        System.out.println("Input the number of walls to generate: ");
                        int n = user_input.nextInt();
                        t.generate_walls(n);
                        break;
                    }
                case 3:
                    {
                        System.out.println("Input the position to generate the object [x][y]");
                        int x = user_input.nextInt();
                        int y = user_input.nextInt();
                        t.add(x, y, new Objecte(4,x,y));
                        break;
                    }
                case 4:
                    {
                        System.out.println("Input the position to generate the player [x][y]");
                        int x = user_input.nextInt();
                        int y = user_input.nextInt();
                        p = new Player(x,y);
                        t.add(x,y,p);
                        object_list.add(p);
                        p.set_index(object_list.get_index());
                        cam.set_pos(p.get_node());
                        break;
                    }
                case 5:
                    {
                        System.out.println("Input the position of the position [x][y]");
                        int x = user_input.nextInt();
                        int y = user_input.nextInt();
                        System.out.println("Finding the path to x = " + x + " y = " + y);
                        p.BFS(x,y,t,cam);
                        break;
                    }
                case 6:
                    p.run(t,object_list);
                    cam.set_pos(p.get_node());
                    break;
                case 7:
                    p.marcar(t);
                    break;
                case 8:
                    object_list.print();
                    break;
                case 9:
                    {
                        System.out.println("Introdueix el numero de iteracions");
                        int n = user_input.nextInt();
                        for (int i = 0; i < n; ++i){
                            p.run(t,object_list);
                        }     
                        break;
                    }
                case 11:
                    {
                        System.out.println("Input the ID to search and the maximum search range:");
                        int id = user_input.nextInt();
                        int distance = user_input.nextInt();
                        p.find_ID(id, t, distance);
                        break;
                    }
                case 12:
                    {
                        System.out.println("Input the number of objects and the ID to generate:");
                        int n = user_input.nextInt();
                        int id = user_input.nextInt();
                        t.generate_object(n,id);
                        break;
                    }
                case 13:
                    {
                        System.out.println("Input the number of iterations:");
                        int n = user_input.nextInt();
                        for (int i = 0; i < n - 1; ++i){
                            object_list.simulate(t,object_list);
                        }
                        break;
                    }
                case 14:
                    Random rand = new Random();
                    for (int i = 0; i < 49; ++i){
                        int x = rand.nextInt(100);
                        int y = rand.nextInt(100);
                        p = new Player(x,y);
                        t.add(x,y,p.get_id());
                        object_list.add(p);
                        p.set_index(object_list.get_index());
                    }   cam.set_pos(p.get_node());
                    break;
                case 15:
                    System.out.println("Input the number of boxes");
                    int n = user_input.nextInt();
                    t.generate_boxes(n,cam);
                    break;
                case 16:
                    System.out.println("Input the ID to spawn, the interval and the number of objects");
                    int ID = user_input.nextInt();
                    int interval = user_input.nextInt();
                    int number = user_input.nextInt();
                    Spawner spawn = new Spawner(ID,interval,number);
                    object_list.add(spawn);
                    break;
                default:
                    System.out.println("invalid input");
                    break;
            }
                */
        }
    }
}