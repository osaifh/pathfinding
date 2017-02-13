package pathfinding;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

/**
 *
 * @author Alumne
 */
public class Hunter extends Creature{
    private boolean move, alive, asleep;
    private Node[] runpath;
    private int runindex, p_index, current_action, hunger, stamina;
    private Memory mem, long_term;
    private Random randint = new Random();
    private final int sight_range = 10;
    private final double tick_max = 30;
    private double tick_counter = 0;
    private Creature target;
    
    /**
     *
     */
    public Hunter(){}
    
    /**
     *
     * @param x
     * @param y
     */
    public Hunter(int x,int y){
        setID(10);
        setPos(x,y);
        move = asleep = false;
        runpath = null;
        runindex = current_action = 0;
        mem = new Memory();
        long_term = new Memory();
        hunger = stamina = 100;
        alive = true;
        target = null;
    }
    
    /**
     *
     * @param tab
     * @param delta
     */
    @Override
    public void simulate(Table tab, double delta){
        tick_counter += delta;
        if (tick_counter >= tick_max || (target!=null && tick_counter >= tick_max/2)){
        tick_counter = 0;
        --hunger;
        if (!asleep) --stamina;
        
        if (current_action == 0){
            long_term.add(getNode());
            if (stamina<25){
                current_action = 3;
                asleep = true;
            }
            else if (hunger<1000){
                lookAround(tab,sight_range);
                System.out.println("searching for something");
                if (target!= null){
                    System.out.println("found a victim");
                    current_action = 1;
                    move = true;
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
            stalk(tab);
            if(!move) current_action = 0;
        }
        else if (current_action == 2){
            idle(tab);
            System.out.println("idle");
            if (hunger < 75 || stamina < 25) current_action = 0;
        }
        else if (current_action == 3){
            sleep();
            if (stamina >= 75){
                asleep = false;
                current_action = 0;
            }
        }
        //CAN'T DIE
        //if (hunger <= 0 || stamina <= 0){
        //    tab.getTile(getNode()).clearObjecte();
        //    alive = false;
        //}
        }
    }

    /**
     *
     * @param tab
     */
    public void stalk(Table tab){
        if (target != null){
        Node target_pos = target.getNode();
        runpath = iBFS(getNode(),target_pos,tab);
        if (runpath != null && tab.getTile(runpath[runindex]).isPassable() && runpath.length >=2){
            if (getNode()!=target_pos){
                tab.getTile(getNode()).clearObjecte();
                setPos(runpath[1]);
                tab.getTile(getNode()).setObjecte(this);
            }
            if (Node.distance(target_pos, getNode()) >= 1){
                strike(target);
            }
        } else {
            move = false;
            current_action = 0;
        }
        } else {
            move = false;
            current_action = 0;
        }
    }
    
    /**
     *
     * @param victim
     */
    public void strike(Creature victim){
        victim.addHunger(-10);
        /*if (victim.getHunger() <= 0){
            target = null;
            move = false;
            current_action = 0;
        }*/
    }
    
    /**
     *
     * @param tab
     * @param range
     */
    @Override
    public void lookAround(Table tab, int range){
            tab.getTile(getNode()).setLit(true);
            for (int i = -1; i < 2; ++i){
                for (int j = -1; j < 2; ++j){
                    if (i!=0 && j !=0){
                        lookDirection(tab,1,1.0f,0.0f, 0,i,j,0,range);
                        lookDirection(tab,1,1.0f,0.0f, i,0,0,j,range);
                    }
                }
            }
    }
    
    private void lookDirection(Table tab, int row, float start, float end, int xx, int xy, int yx, int yy, int range){
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
                if (Node.distance(getNode(),delta) <= range && (tab.getObject(delta)!= null && tab.getObject(delta).getID()==2)){
                    target = (Creature)tab.getObject(delta);
                }
                
                if (blocked){
                    if (!tab.checkPassable(currentX,currentY)){
                        newStart = rightSlope;
                        continue;
                    } else {
                        blocked = false;
                        start = newStart;
                    }
                } else {
                    if (!tab.checkPassable(currentX,currentY) && Node.distance(getNode(),delta) <= 10 && target!=null){
                        blocked = true;
                        lookDirection(tab,distance + 1,start, leftSlope, xx, xy, yx, yy, range);
                        newStart = rightSlope;
                    }
                }
            }
        }
    }
    
    private static Comparator<NodeData> node_comparator = (NodeData n1, NodeData n2) -> {
        if (n1.getTotal() > n2.getTotal()) return 1;
        else if (n1.getTotal() < n2.getTotal()) return -1;
        else return 0;
    };

    private Node[] iBFS(Node act_pos, Node target, Table tab){
         PriorityQueue qpath = new PriorityQueue(11,node_comparator);
         ParList visitats = new ParList();
         Node source = act_pos;
         Node current = act_pos;
         NodePar current_par = new NodePar(current);
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
                             NodePar new_par = new NodePar(temp);
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
     *
     * @param tab
     * @return
     */
    @Override
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
}
