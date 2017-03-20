/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinding.actor;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import pathfinding.Table.Table;
import pathfinding.auxiliar.Memory;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.NodeData;
import pathfinding.auxiliar.NodePair;
import pathfinding.auxiliar.PairList;

/**
 *
 * @author Alumne
 */
public class Mob extends Creature {
    private boolean move, asleep;
    private Node[] runpath;
    private Memory mem, long_term;
    private int runindex, current_action, hunger, stamina;
    
    public Mob(int x, int y){
        id = 2;
        pos = new Node(x,y);
        move = asleep = false;
        runpath = null;
        runindex = current_action = 0;
        mem = new Memory();
        long_term = new Memory();
        hunger = stamina = 100;
        alive = true;
    }
    
    /**
     * Gets the hunger value
     * @return Returns the implicit parameter's hunger value
     */
    public int getHunger(){
        return hunger;
    }
    
    /**
     * Determinates whether the implicit parameter is asleep or not
     * @return Returns true if the implicit paramter is asleep
     */
    public boolean isAsleep(){
        return asleep;
    }

    
    /**
     * Performs a single step from the planned path
     * @param tab the table where we're moving
     */
    public void run(Table tab){
        if (move && runpath.length > 0 && runindex < runpath.length){
            if (tab.getTile(runpath[runindex]).isPassable()){
                tab.getTile(pos).clearContent();
                pos.print();
                //eating
                if (tab.getTile(runpath[runindex]).getID()==4){
                    hunger += 10;
                    tab.getTile(runpath[runindex]).clearContent();
                }
                pos = (runpath[runindex]);
                pos.print();
                tab.getTile(pos).setContent(this);
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
                    Node aux = new Node();
                    aux.setToNode(pos);
                    aux.moveDirection(j,1);
                    if (tab.checkPassable(aux)){
                        dir_val[j] = mem.check(aux) + long_term.check(aux)*10;
                        if (dir_val[j]>value_max){
                            value_max = dir_val[j];
                            max = j;
                        }
                    } else dir_val[j]=-1;
            }
            if (value_max>=0){
                Node step = new Node();
                step.setToNode(pos);
                step.moveDirection(max, 1);
                mem.add(step);
                newpath[i] = step;
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
                next_step.setToNode(pos);
                next_step.moveDirection(decision,1);
            } while (!tab.checkPassable(next_step));
            tab.getTile(pos).clearContent();
            pos = next_step;
            tab.getTile(pos).setContent(this);
        }
    }
    
    /**
     * Sleeps during a single step, increasing the creature's stamina
     */
    public void sleep(){
        stamina += 10;
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
        Node[] path = iBFS(pos, new Node(x,y), tab);
        if (path!= null){
            runpath = path;
            move = true;
            runindex = 1;
        } else {
            runpath = null;
        }
    }

    private static Comparator<NodeData> node_comparator = (NodeData n1, NodeData n2) -> {
        if (n1.getTotal() > n2.getTotal()) return 1;
        else if (n1.getTotal() < n2.getTotal()) return -1;
        else return 0;
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
        Node[] path = iFindID(pos, id, tab, distance);
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
     * simulates a single step for a creature
     * @param tab
     */
    @Override
    public void simulate(Table tab){
        tick_counter ++;
        if (tick_counter >= tick_max){
            tick_counter = 0;
            --hunger;
            if (!asleep) --stamina;

            if (current_action == 0){
                long_term.add(pos);
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

            switch (current_action) {
                case 1:
                    run(tab);
                    if(!move) current_action = 0;
                    break;
                case 2:
                    idle(tab);
                    if (hunger < 75 || stamina < 25) current_action = 0;
                    break;
                case 3:
                    sleep();
                    if (stamina >= 75){
                        asleep = false;
                        current_action = 0;
                    }   break;
                default:
                    break;
            }

            if (hunger <= 0 || stamina <= 0){
                tab.getTile(pos).clearContent();
                alive = false;
            }
        }
    }
    
    /**
     * Prints some relevant information from the creature.
     * Used only for testing.
     */
    public void print(){
            System.out.println("object ID: " + id);
            System.out.println("position x = " + pos.getX() + " y = " + pos.getY());
            System.out.println("Health: " + hunger + " Stamina: " + stamina);
    }
}
