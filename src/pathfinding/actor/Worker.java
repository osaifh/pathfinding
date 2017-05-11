package pathfinding.actor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import pathfinding.Table.Table;
import pathfinding.auxiliar.Memory;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.NodeData;
import pathfinding.auxiliar.NodePair;
import pathfinding.auxiliar.PairList;

public class Worker extends Creature {
    private Boolean move;
    private int status, runindex, resources; //0 standby 1 searching 2 returning to base
    private Node[] runpath;
    private Memory mem,longMem;
    private Node objective;
    private Core host;
    private static final int SIGHT_RANGE = 6;
    
    public Worker(int x, int y, Core host){
        alive = true;
        this.host = host;
        id = 21;
        pos = new Node(x,y);
        status = runindex = 0;
        mem = new Memory();
        longMem = new Memory();
        move = false;
        objective = null;
        resources = 0;
    }
    
    public int getStatus(){
        return status;
    }
    
    public void setStatus(int status){
        this.status = status;
    }
    
    public void setObjective(Node objective){
        this.objective = objective;
    }
    
    public void run(Table tab){
        if (move && runpath.length > 0 && runindex < runpath.length){
            if (tab.getTile(runpath[runindex]).isPassable()){
                tab.getTile(pos).clearMatchingContent(this);
                if (tab.getTile(runpath[runindex]).containsID(4)){
                    ++resources;
                    tab.getTile(runpath[runindex]).clearContent();
                    status = 3;
                }
                pos = (runpath[runindex]);
                tab.getTile(pos).addContent(this);
                if (runindex == runpath.length-1) move = false;
                else ++runindex;
            } else {
                move = false;
            }
        }
    }
    
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
                        dir_val[j] = mem.check(aux) + longMem.check(aux)*10;
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
    
    @Override
    public void simulate(Table t) {
        ++tick_counter;
        if (tick_counter >= tick_max){
            tick_counter = 0;
            System.out.println("Status: " + status);
            if (status == 1){
                run(t);
                if (resources != 0 && getNode().compare(host.getNode())){
                    host.addResources(resources);
                    host.objectiveDone(objective);
                    resources = 0;
                    status = 0;
                    objective = null;
                }
                if (!move && (status != 0)){
                    if (objective == null) status = 2;
                    else status = 3;
                }
            }
            else if (status == 2){
                BFS(objective.getX(),objective.getY(),t);
                if (runpath == null) status = 0;
                else status = 1;
            }
            else if (status == 3){
                BFS(host.getNode().getX(),host.getNode().getY(),t);
                if (runpath == null) status = 0;
                else status = 1;
            }
        }
    }

    @Override
    public void print() {
    }
    
}
