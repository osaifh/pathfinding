package pathfinding;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Comparator;
import java.util.PriorityQueue;
public class Player extends Objecte {
    private boolean move, alive, asleep;
    private Node[] runpath;
    private int runindex, p_index, current_action, hunger, stamina;
    private Memory mem, long_term;
    private Random randint = new Random();
    private LinkedList object_memory;
    private final int sight_range = 10;
    private final double tick_max = 30;
    private double tick_counter = 0;

    public Player(int x, int y){
        set_id(2);
        set_xy(x,y);
        move = asleep = false;
        runpath = null;
        runindex = current_action = 0;
        mem = new Memory();
        long_term = new Memory();
        hunger = stamina = 100;
        alive = true;
    }        
    
    public int get_index(){
        return p_index;
    }
    
    public int get_sight_range(){
        return sight_range;
    }
    
    public boolean is_alive(){
        return alive;
    }
    
    public boolean is_asleep(){
        return asleep;
    }

    public void set_index(int x){
        p_index = x;
    }

    public void BFS(int x, int y, Table tab, Camera cam){
        Node target = new Node();
        target.set(x,y);
        Node[] path = i_BFS(get_node(), target, tab, cam);
        if (path!= null){
            runpath = path;
            move = true;
            runindex = 1;
        } else {
            runpath = null;
        }
    }

    public static Comparator<Node_Data> node_comparator = new Comparator<Node_Data>(){
        @Override
        public int compare (Node_Data n1, Node_Data n2){
            if (n1.get_Total() > n2.get_Total()) return 1;
            else if (n1.get_Total() < n2.get_Total()) return -1;
            else return 0;
        }
    };
            
    private Node[] i_BFS(Node act_pos, Node target, Table tab, Camera cam){
        PriorityQueue qpath = new PriorityQueue(11,node_comparator);
        Par_List visitats = new Par_List();
        Node source = act_pos;
        Node current = act_pos;
        Node_Par current_par = new Node_Par(current);
        visitats.add(current_par);
        Node_Data current_data = new Node_Data(current_par,source,target);
        qpath.add(current_data);
        int limit = 10000;
        boolean first = true;
        while (!qpath.isEmpty() & limit > 0){
            if(!first){
                current_data = (Node_Data)qpath.poll();
                current_par = current_data.get_Node_Par();
                current = current_data.get_Node();
            } else first = false;
            if (current.compare(target)) break;
            else {
                for (int i = -1; i < 2; ++i){
                for (int j = -1; j < 2; ++j){
                    if (!(i==0 & j==0) && tab.check(current.get_x() + i,current.get_y() + j)){
                        Node temp = new Node();
                        temp.set(current.get_x() + i, current.get_y() + j);
                        if (!visitats.findNode(temp)){
                            Node_Par new_par = new Node_Par(temp);
                            new_par.setSource(current_par);
                            Node_Data new_data = new Node_Data(new_par,source,target);
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

    public void find_ID(int id, Table tab, int distance){
        Node[] path = i_find_ID(get_node(), id, tab, distance);
        if (path!= null){
            runpath = path;
            move = true;
            runindex = 1;
        } else {
            runpath = null;
        }
    } 

    private Node[] i_find_ID(Node act_pos, int id, Table tab, int distance){
        Queue qpath = new LinkedList();
        Par_List visitats = new Par_List();
        Node current = act_pos;
        Node_Par current_par = new Node_Par(current);
        visitats.add(current_par);
        qpath.add(current_par);
        int limit = 10000;
        boolean first = true;
        while (!qpath.isEmpty() & limit > 0){
            if(!first){
                current_par = (Node_Par) qpath.poll();
                current = current_par.getFirst();
            } else first = false;
            if (tab.tab_id(current) == id || ((int)current.distance(act_pos,current) > distance)) break;
            else {
                for (int i = -1; i < 2; ++i){
                for (int j = -1; j < 2; ++j){
                    Node temp = new Node();
                    temp.set(current.get_x() + i, current.get_y() + j);
                    if (!(i==0 & j==0) && tab.check_exc(temp, id)){
                        if (!visitats.findNode(temp)){
                            Node_Par new_par = new Node_Par(temp);
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
        if (!(tab.tab_id(current) == id)) return null;
        else{
            Node[] path = visitats.tracePath(current_par);
            return path;
        }
    }

    public void marcar(Table tab){
        if (move){
            for (int i = 0; i < runpath.length; ++i){
                tab.set(runpath[i],3);
            }
        }
    }

    public void i_move(Table tab, int i, Objecte_list llista){
        tab.get_tile(get_node()).clear_objecte();
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
        tab.get_tile(get_node()).set_objecte(this);
        llista.set_i(get_objecte(),p_index);
    }

    public void run(Table tab, Objecte_list llista){
        if (move){
            if (tab.get_tile(runpath[runindex]).is_passable()){
                tab.get_tile(get_node()).clear_objecte();
                //eating
                if (tab.get_tile(runpath[runindex]).get_ID()==4){
                    hunger += 10;
                    tab.get_tile(runpath[runindex]).clear_objecte();
                }
                set_node(runpath[runindex]);
                tab.get_tile(get_node()).set_objecte(this);
                llista.set_i(get_objecte(),p_index);
                if (runindex == runpath.length-1) move = false;
                else ++runindex;
            } else {
                move = false;
            }
        }
    }
    
    private Node[] run_away(Table tab){
        move = true;
        runindex = 0;
        int length = 5 + randint.nextInt(6);
        Node[] newpath = new Node[length];
        for (int i = 0; i < length; ++i){
            Node pos = new Node();
            if (i == 0) pos = get_node();
            else pos = newpath[i-1];
            int[] dir_val = new int[8];
            int max = 0;
            int value_max = -1;
            for (int j = 0; j < 8; ++j){
                    Node aux = pos.direction(j,1);
                    if (tab.check(aux)){
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
    
    public void idle(Table tab, Objecte_list llista){
        int decision = randint.nextInt(2);
        if (decision == 0){
            Node next_step = new Node();
            do {
                decision = randint.nextInt(8);
                next_step = get_node().direction(decision,1);
            } while (!tab.check(next_step) || !tab.get_tile(next_step).is_passable());
            tab.get_tile(get_node()).clear_objecte();
            set_node(next_step);
            tab.get_tile(get_node()).set_objecte(this);
        }
    }
    
    public void sleep(){
        System.out.println("Sleeping");
        stamina += 10;
    }
    
    public void look_around(Table tab, int range){
            tab.get_tile(get_node()).lit = true;
            for (int i = -1; i < 2; ++i){
                for (int j = -1; j < 2; ++j){
                    if (i!=0 && j !=0){
                        look_direction(tab,1,1.0f,0.0f, 0,i,j,0,range);
                        look_direction(tab,1,1.0f,0.0f, i,0,0,j,range);
                    }
                }
            }
    }
    
    private void look_direction(Table tab, int row, float start, float end, int xx, int xy, int yx, int yy, int range){
        float newStart = 0.0f;
        if (start < end){
            return;
        }
        boolean blocked = false;
        for (int distance = row; distance <= range && !blocked; distance++){
            int deltaY = -distance;
            for (int deltaX = -distance; deltaX <= 0; deltaX++){
                int currentX = get_node().get_x() + deltaX * xx + deltaY * xy;
                int currentY = get_node().get_y() + deltaX * yx + deltaY * yy;
                float leftSlope = (deltaX - 0.5f) / (deltaY + 0.5f);
                float rightSlope = (deltaX + 0.5f) / (deltaY - 0.5f);
                if (!(tab.valid(currentX,currentY)) || start < rightSlope){
                    continue;
                } else if (end > leftSlope){
                    break;
                }
                Node delta = new Node(currentX,currentY);
                if (delta.distance(get_node(),delta) <= range){
                    tab.get_tile(delta).set_lit(true);
                }
                
                if (blocked){
                    if (!tab.check_passable(currentX,currentY)){
                        newStart = rightSlope;
                        continue;
                    } else {
                        blocked = false;
                        start = newStart;
                    }
                } else {
                    if (!tab.check_passable(currentX,currentY) && delta.distance(get_node(),delta) <= 10){
                        blocked = true;
                        look_direction(tab,distance + 1,start, leftSlope, xx, xy, yx, yy, range);
                        newStart = rightSlope;
                    }
                }
            }
        }
    }
    
    @Override
    public void simulate(Table tab,Objecte_list llista, double delta){
        tick_counter += delta;
        if (tick_counter >= tick_max){
        tick_counter = 0;
        --hunger;
        if (!asleep) --stamina;
        
        if (current_action == 0){
            long_term.add(get_node());
            if (stamina<25){
                current_action = 3;
                asleep = true;
            }
            else if (hunger<75){
                find_ID(4,tab,6);
                if (move & runpath != null){
                    current_action = 1;
                } else {
                    runpath = run_away(tab);
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
            run(tab,llista);
            if(!move) current_action = 0;
        }
        else if (current_action == 2){
            idle(tab,llista);
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
            tab.get_tile(get_node()).clear_objecte();
            alive = false;
        }
        }
    }
    
    @Override
    public void print(){
            System.out.println("object ID: " + get_id());
            System.out.println("position x = " + get_node().get_x() + " y = " + get_node().get_y());
            System.out.println("Player index is " + p_index);
            System.out.println("Health: " + hunger + " Stamina: " + stamina);
    }
}