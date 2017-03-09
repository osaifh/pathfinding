package pathfinding;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Generic creature/mob class which is also an object.
 * @author Me
 */
public class Creature extends Subject {
    private boolean move, alive, asleep;
    private Node[] runpath;
    private int runindex, p_index, current_action, hunger, stamina;
    private Memory mem, long_term;
    private Random randint = new Random();
    private final int sight_range = 10;
    private final double tick_max = 30;
    private double tick_counter = 0;
    //WIP
    private int facing_direction;

    /**
     * Empty Constructor
     */
    public Creature(){}
    
    /**
     * Default constructor
     * @param x horizontal coordinate
     * @param y vertical coordinate
     */
    public Creature(int x, int y){
        super(2,x,y);
        move = asleep = false;
        runpath = null;
        runindex = current_action = 0;
        mem = new Memory();
        long_term = new Memory();
        hunger = stamina = 100;
        alive = true;
    }    
    
    /**
     * Gets the player index
     * @return returns the implicit parameter's player index
     */
    public int getIndex(){
        return p_index;
    }
    
    /**
     * Gets the sight range
     * @return Returns the implicit parameter's sight range
     */
    public int getSightRange(){
        return sight_range;
    }
    
    /**
     * Gets the hunger value
     * @return Returns the implicit parameter's hunger value
     */
    public int getHunger(){
        return hunger;
    }
    
    /**
     * Determinates whether the implicit parameter is alive or not
     * @return Returns true if the implicit parameter is alive
     */
    public boolean isAlive(){
        return alive;
    }
    
    /**
     * Determinates whether the implicit parameter is asleep or not
     * @return Returns true if the implicit paramter is asleep
     */
    public boolean isAsleep(){
        return asleep;
    }

    /**
     * sets the implicit parameter's p index value
     * @param x the new p index value
     */
    public void setIndex(int x){
        p_index = x;
    }
    
    /**
     * Adds a certain amount to the hunger value
     * The amount can be negative
     * @param x the value of hunger to add
     */
    public void addHunger(int x){
        hunger += x;
    }

    /**
     * Finds a path to a position in the table
     * If no path is found, runpath will be set to null
     * @param x
     * @param y
     * @param tab
     */
    public void BFS(int x, int y, Table tab){
        Node[] path = iBFS(getNode(), new Node(x,y), tab);
        if (path!= null){
            runpath = path;
            move = true;
            runindex = 1;
        } else {
            runpath = null;
        }
    }

    private static Comparator<NodeData> node_comparator = new Comparator<NodeData>() {
        @Override
        public int compare (NodeData n1, NodeData n2){
            if (n1.getTotal() > n2.getTotal()) return 1;
            else if (n1.getTotal() < n2.getTotal()) return -1;
            else return 0;
        }
    };
            
    private Node[] iBFS (Node act_pos, Node target, Table tab) {
        if (!tab.getTile(target).isPassable()) return null; //early exit
        PriorityQueue qpath = new PriorityQueue(11,node_comparator);
        PairList visitats = new PairList();
        Node source = act_pos;
        Node current = act_pos;
        NodePair current_par = new NodePair(current);
        visitats.add(current_par);
        NodeData current_data = new NodeData(current_par,source,target);
        qpath.add(current_data);
        int limit = 10000;
        boolean first = true;
        while (!qpath.isEmpty() & limit > 0){
            if(!first){
                current_data = (NodeData)qpath.poll();
                current_par = current_data.getNodePar();
                current = current_data.getNode();
            } else first = false;
            if (current.compare(target)) break;
            else {
                for (int i = -1; i < 2; ++i){
                for (int j = -1; j < 2; ++j){
                    if (!(i==0 & j==0) && tab.checkPassable(current.getX() + i,current.getY() + j)){
                        Node temp = new Node();
                        temp.set(current.getX() + i, current.getY() + j);
                        if (!visitats.findNode(temp)){
                            NodePair new_par = new NodePair(temp);
                            new_par.setSource(current_par);
                            NodeData new_data = new NodeData(new_par,source,target);
                            qpath.add(new_data);
                            visitats.add(new_par);
                        }
                    }
                }
                }
            }
            --limit;
        }
        if (!target.compare(current)) return null;
        else{
            Node[] path = visitats.tracePath(current_par);
            return path;
        }
    }

    /**
     * Within a fixed radius, finds the nearest item with a determinate ID
     * @param id the ID to search
     * @param tab the table where we perform the search
     * @param distance the maximum distance to scan
     */
    public void findID (int id, Table tab, int distance){
        Node[] path = iFindID(getNode(), id, tab, distance);
        if (path!= null){
            runpath = path;
            move = true;
            runindex = 1;
        } else {
            runpath = null;
        }
    } 

    private Node[] iFindID(Node act_pos, int id, Table tab, int distance){
        Queue qpath = new LinkedList();
        PairList visitats = new PairList();
        Node current = act_pos;
        NodePair current_par = new NodePair(current);
        visitats.add(current_par);
        qpath.add(current_par);
        int limit = 10000;
        boolean first = true;
        while (!qpath.isEmpty() & limit > 0){
            if(!first){
                current_par = (NodePair) qpath.poll();
                current = current_par.getFirst();
            } else first = false;
            if (tab.getID(current) == id || ((int)Node.distance(act_pos,current) > distance)) break;
            else {
                for (int i = -1; i < 2; ++i){
                for (int j = -1; j < 2; ++j){
                    Node temp = new Node();
                    temp.set(current.getX() + i, current.getY() + j);
                    if (!(i==0 & j==0) && tab.checkPassable(temp)){
                        if (!visitats.findNode(temp)){
                            NodePair new_par = new NodePair(temp);
                            new_par.setSource(current_par);
                            qpath.add(new_par);
                            visitats.add(new_par);
                        }
                    }
                }
                }
            }
            --limit;
        }
        if (!(tab.getID(current) == id)) return null;
        else{
            Node[] path = visitats.tracePath(current_par);
            return path;
        }
    }

    /**
     * Moves a single tile in one direction
     * @param tab the table where we move
     * @param i the direction to move
     */
    public void iMove(Table tab, int i){
        tab.getTile(getNode()).clearObjecte();
        switch (i) {
            case 0:
                moveNW(tab);
                break;
            case 1:
                moveN(tab);
                break;
            case 2:
                moveNE(tab);
                break;
            case 3:
                moveW(tab);
                break;
            case 4:
                moveE(tab);
                break;
            case 5:
                moveSW(tab);
                break;
            case 6:
                moveS(tab);
                break;
            case 7:
                moveSE(tab);
                break;
            default:
                break;
        }
        setFacingDirection(i);
        tab.getTile(getNode()).setObjecte(this);
    }

    /**
     * Performs a single step from the planned path
     * @param tab the table where we're moving
     */
    public void run(Table tab){
        if (move && runpath.length > 0 && runindex < runpath.length){
            if (tab.getTile(runpath[runindex]).isPassable()){
                tab.getTile(getNode()).clearObjecte();
                //eating
                if (tab.getTile(runpath[runindex]).getID()==4){
                    hunger += 10;
                    tab.getTile(runpath[runindex]).clearObjecte();
                }
                setPos(runpath[runindex]);
                tab.getTile(getNode()).setObjecte(this);
                //llista.set_i(getSubject(),p_index);
                if (runindex == runpath.length-1) move = false;
                else ++runindex;
            } else {
                move = false;
            }
        }
    }
    
    /**
     * Returns a path moving as far away as possible from the current position and the near past positions
     * @param tab the table where we're moving
     * @return a path of adjacent nodes
     */
    public Node[] runAway(Table tab){
        move = true;
        runindex = 0;
        int length = 5 + randint.nextInt(6);
        Node[] newpath = new Node[length];
        for (int i = 0; i < length; ++i){
            Node pos = new Node();
            if (i == 0) pos = getNode();
            else pos = newpath[i-1];
            int[] dir_val = new int[8];
            int max = 0;
            int value_max = -1;
            for (int j = 0; j < 8; ++j){
                    Node aux = pos.direction(j,1);
                    if (tab.checkPassable(aux)){
                        dir_val[j] = mem.check(aux) + long_term.check(aux)*10;
                        if (dir_val[j]>value_max){
                            value_max = dir_val[j];
                            max = j;
                        }
                    } else dir_val[j]=-1;
            }
            if (value_max>=0){
                mem.add(pos.direction(max,1));
                newpath[i] = pos.direction(max,1);
            }
            else {
                if (i == 0) return null;
                else {
                    Node [] resized_path = new Node[i];
                    for (int j = 0; j < i; ++j) resized_path[j] = newpath[j];
                    return resized_path;
                }
            }
        }
        return newpath;
    }
    
    /**
     * Performs a random idle movement
     * @param tab the tab where we're moving
     */
    public void idle(Table tab){
        int decision = randint.nextInt(2);
        if (decision == 0){
            Node next_step = new Node();
            do {
                decision = randint.nextInt(8);
                next_step = getNode().direction(decision,1);
            } while (!tab.checkPassable(next_step) || !tab.getTile(next_step).isPassable());
            tab.getTile(getNode()).clearObjecte();
            setPos(next_step);
            tab.getTile(getNode()).setObjecte(this);
        }
    }
    
    /**
     * Sleeps during a single step, increasing the creature's stamina
     */
    public void sleep(){
        stamina += 10;
    }
    
    /**
     * Determinates the visibility for the active creature
     * @param tab the table where we're looking
     * @param range the sight range of the creature
     * @param cam the camera used to determinate visibility
     */
    public void lookAround(Table tab, int range, Camera cam){
            cam.setVisibilityTable(getNode(), true);
            switch (facing_direction){
                case 1:
                    //facing direction = 1 (N)
                    lookDirection(tab,1,1.0f,0.0f, 0,1,-1,0,range,cam);
                    lookDirection(tab,1,1.0f,0.0f, 0,1,1,0,range,cam);
                    break;
                case 3:
                    //facing direction = 3 (W)
                    lookDirection(tab,1,1.0f,0.0f, -1,0,0,1,range,cam);
                    lookDirection(tab,1,1.0f,0.0f, 1,0,0,1,range,cam);
                    break;
                case 4:
                    //facing direction = 4 (E)
                    lookDirection(tab,1,1.0f,0.0f, 1,0,0,-1,range,cam);
                    lookDirection(tab,1,1.0f,0.0f, -1,0,0,-1,range,cam);
                    break;
                case 6:
                    //facing direction = 6 (S)
                    lookDirection(tab,1,1.0f,0.0f, 0,-1,1,0,range,cam);
                    lookDirection(tab,1,1.0f,0.0f, 0,-1,-1,0,range,cam);
                    break;
                default:
                    break;
            }
            /*
            for (int i = -1; i < 2; ++i){
                for (int j = -1; j < 2; ++j){
                    if (i!=0 && j !=0){
                        lookDirection(tab,1,1.0f,0.0f, 0,i,j,0,range,cam);
                        lookDirection(tab,1,1.0f,0.0f, i,0,0,j,range,cam);
                    }
                }
            }
            */
    }
    
    private void lookDirection(Table tab, int row, float start, float end, int xx, int xy, int yx, int yy, int range, Camera cam){
        float newStart = 0.0f;
        if (start < end){
            return;
        }
        boolean blocked = false;
        for (int distance = row; distance <= range && !blocked; distance++){
            int deltaY = -distance;
            for (int deltaX = -distance; deltaX <= 0; deltaX++){
                int currentX = getNode().getX() + deltaX * xx + deltaY * xy;
                int currentY = getNode().getY() + deltaX * yx + deltaY * yy;
                float leftSlope = (deltaX - 0.5f) / (deltaY + 0.5f);
                float rightSlope = (deltaX + 0.5f) / (deltaY - 0.5f);
                
                if (!(tab.valid(currentX,currentY)) || start < rightSlope){
                    continue;
                } else if (end > leftSlope){
                    break;
                }
                
                Node delta = new Node(currentX,currentY);
                if (Node.distance(getNode(),delta) <= range){
                    cam.setVisibilityTable(delta, true);
                }
                
                if (blocked){
                    if (!tab.checkPassable(delta)){
                        newStart = rightSlope;
                        continue;
                    } else {
                        blocked = false;
                        start = newStart;
                    }
                } else {
                    if (!tab.checkPassable(delta) && Node.distance(getNode(),delta) <= 10){
                        blocked = true;
                        lookDirection(tab,distance + 1,start, leftSlope, xx, xy, yx, yy, range,cam);
                        newStart = rightSlope;
                    }
                }
            }
        }
    }
    
    /**
     * simulates a single step for a creature
     * @param tab
     * @param delta
     */
    @Override
    public void simulate(Table tab, double delta){
        tick_counter += delta;
        if (tick_counter >= tick_max){
        tick_counter = 0;
        --hunger;
        if (!asleep) --stamina;
        
        if (current_action == 0){
            long_term.add(getNode());
            if (stamina<25){
                current_action = 3;
                asleep = true;
            }
            else if (hunger<75){
                findID(4,tab,6);
                if (move & runpath != null){
                    current_action = 1;
                } else {
                    runpath = runAway(tab);
                    if (runpath != null){
                        current_action = 1;
                    }
                }
            }
            else {
                current_action = 2;
            }
        }
        
        if (current_action == 1){
            run(tab);
            if(!move) current_action = 0;
        }
        else if (current_action == 2){
            idle(tab);
            if (hunger < 75 || stamina < 25) current_action = 0;
        }
        else if (current_action == 3){
            sleep();
            if (stamina >= 75){
                asleep = false;
                current_action = 0;
            }
        }
        
        if (hunger <= 0 || stamina <= 0){
            tab.getTile(getNode()).clearObjecte();
            alive = false;
        }
        }
    }
    
    /**
     * Prints some relevant information from the creature.
     * Used only for testing.
     */
    @Override
    public void print(){
            System.out.println("object ID: " + getID());
            System.out.println("position x = " + getNode().getX() + " y = " + getNode().getY());
            System.out.println("Player index is " + p_index);
            System.out.println("Health: " + hunger + " Stamina: " + stamina);
    }
    
    
    //WIP
    
    public int getFacingDirection(){
        return facing_direction;
    }
    
    public void setFacingDirection(int d){
        facing_direction = d;
    }
}