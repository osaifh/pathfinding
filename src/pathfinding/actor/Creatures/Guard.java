package pathfinding.actor.Creatures;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import pathfinding.Table.Table;
import pathfinding.Table.Tile;
import pathfinding.actor.Actor;
import pathfinding.actor.ActorList;
import pathfinding.actor.Skills.ShootBulletSkill;
import pathfinding.actor.Skills.Skill;
import pathfinding.auxiliar.Memory;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.NodePair;
import pathfinding.auxiliar.PairList;
import pathfinding.auxiliar.Constants;

public class Guard extends Creature {
    private boolean move, asleep;
    private Node[] runpath;
    private Memory mem, longTerm;
    private int runindex, current_action, hunger, stamina, aggroRange;
    private Actor target;
    private ActorList objList;
    private final int tick_max = 15;
    private ArrayList<Node> patrolPoints;
    private int currentPoint;
    private final int AGGRO_RANGE = 15;
    //some more testing to do shit properly
    private final int SHOOT_SKILL_INDEX; 
    
    public Guard(Node pos, ActorList objList){
        id = Constants.PLAYER_ID;
        hp = 100;
        maxHP = 100;
        this.pos = pos.getNodeCopy();
        move = asleep = false;
        runpath = null;
        runindex = 0;
        current_action = 0;
        mem = new Memory();
        longTerm = new Memory();
        hunger = stamina = 100;
        alive = true;
        this.objList = objList;
        sight_range = 30;
        aggroRange = AGGRO_RANGE;
        patrolPoints = new ArrayList();
        skillList = new ArrayList<Skill>();
        skillList.add(new ShootBulletSkill());
        SHOOT_SKILL_INDEX = 0;
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
     * Returns the patrol points of a guard
     * @return the patrol points
     */
    public ArrayList<Node> getPatrolPoints(){
        return patrolPoints;
    }
    
    public void addPatrolPoint(Node node){
        if (patrolPoints.isEmpty()) currentPoint = 0;
        patrolPoints.add(node);
    }
    
    /**
     * Performs a single step from the planned path
     * @param tab the table where we're moving
     */
    public void run(Table tab){
        if (move && runpath.length > 0 && runindex < runpath.length){
            Tile tile = tab.getTile(runpath[runindex]);
            //TODO: remove hardcoded values
            //!tile.ContainsCreature()
            //tile.isPassable()
            if (tile.getLight()>=20){
                iMove(tab,runpath[runindex]);
                
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
            Node pos;
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
                        dir_val[j] = mem.check(aux) + longTerm.check(aux)*10;
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
    private void idle(Table tab){
        int decision = randint.nextInt(2);
        Node next = pos.getNodeCopy();
        next.add(Constants.DIRECTIONS.get(randint.nextInt(8)));
        if (tab.valid(next) && tab.getTile(next).getLight()>=20){
            iMove(tab,next);
        }
    }
    
    /**
     * Sleeps during a single step, increasing the creature's stamina
     */
    private void sleep(){
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
    private void BFS(int x, int y, Table tab){
        Node[] path = tab.iBFS(pos, new Node(x,y), false, true);
        
        if (path!= null){
            runpath = path;
            move = true;
            runindex = 1;
        } else {
            runpath = null;
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
    
    public void lookAround(Table tab, int range){            
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
        if (target != null){
            return;
        }
        boolean blocked = false;
        for (int distance = row; distance <= range && !blocked; distance++){
            int deltaY = -distance;
            for (int deltaX = -distance; deltaX <= 0; deltaX++){
                int currentX = pos.getX() + deltaX * xx + deltaY * xy;
                int currentY = pos.getY() + deltaX * yx + deltaY * yy;
                float leftSlope = (deltaX - 0.5f) / (deltaY + 0.5f);
                float rightSlope = (deltaX + 0.5f) / (deltaY - 0.5f);
                
                if (!(tab.valid(currentX,currentY)) || start < rightSlope){
                    continue;
                } else if (end > leftSlope){
                    break;
                }
                
                Node delta = new Node(currentX,currentY);
                if (Node.distance(pos,delta) <= range  && tab.getTile(delta).getLight()>=20){
                    if (tab.getTile(delta).getContentSize()!=0){
                        for (int i = 0; i < tab.getTile(delta).getContentSize() && target == null; ++i){
                            Actor obj = tab.getTile(delta).getContent(i);
                            if (obj instanceof Player){
                                target = obj;
                                lastKnownNode = target.getNode().getNodeCopy();
                                //tab.getTile(lastKnownNode).setID(Constants.RED_ID);
                                current_action = 0;
                            }
                        }
                    }
                }
                
                if (blocked){
                    if (tab.getTile(delta).isOpaque()){
                        newStart = rightSlope;
                        continue;
                    } else {
                        blocked = false;
                        start = newStart;
                    }
                } else {
                    if (tab.getTile(delta).isOpaque() && Node.distance(pos,delta) <= 10){
                        blocked = true;
                        lookDirection(tab,distance + 1,start, leftSlope, xx, xy, yx, yy, range);
                        newStart = rightSlope;
                    }
                }
            }
        }
    }
    
    private void attackTarget(Table tab){
        skillList.get(SHOOT_SKILL_INDEX).activate(pos.getNodeCopy(),target.getNode().getNodeCopy(),tab, objList);
    }
    
    private void chaseTarget(Table tab){
        if (target!= null){
            Node closest = target.getNode().getNodeCopy();
            for (Node d : Node.getDirections()){
                Node t = target.getNode().getNodeCopy();
                t.add(d);
                if (Node.distance(pos, t) < Node.distance(pos, target.getNode())) closest = t;
            }
            BFS(closest.getX(),closest.getY(),tab);
        }
        else current_action = 0;
    }
    
    private Node[] runAwayFromTarget(Table tab){
        runindex = 0;
        int length = 5 + randint.nextInt(6);
        Node[] newpath = new Node[length];
        if (target == null) return null;
        for (int i = 0; i < length; ++i){
            Node npos;
            if (i == 0) npos = getNode().getNodeCopy();
            else npos = newpath[i-1].getNodeCopy();
            double[] dir_val = new double[8];
            int max = 0;
            double value_max = -1;
            for (int j = 0; j < 8; ++j){
                    Node aux = npos.getNodeCopy();
                    aux.moveDirection(j,1);
                    if (tab.checkPassable(aux)){
                        dir_val[j] = Node.distance(aux,target.getNode());
                        if (dir_val[j]>value_max){
                            value_max = dir_val[j];
                            max = j;
                        }
                    } else dir_val[j]=-1;
            }
            if (value_max>=0){
                npos.moveDirection(max, 1);
                newpath[i] = npos;
            }
            else {
                if (i == 0){
                    return null;
                }
                else {
                    Node [] resized_path = new Node[i];
                    for (int j = 0; j < i; ++j) resized_path[j] = newpath[j];
                    return resized_path;
                }
            }
        }
        return newpath;
    }
    
    private Node lastKnownNode;
    
     /**
     * simulates a single step for a creature
     * @param tab
     */
    @Override
    public void simulate(Table tab) {
        tick_counter++;
        
            
        if (tick_counter >= tick_max){
            tick_counter = 0;
            --hunger;
            //if (!asleep) --stamina;
            lookAround(tab,sight_range);
            if (move && target != null){
                current_action = 0;
            }

            switch (current_action) {
                case 0:
                    longTerm.add(pos);
                    if (hp < 50 && target != null){
                        current_action = 2;
                    }
                    else 
                    if (target==null){
                        if (lastKnownNode != null){
                            current_action = 1;
                        }
                        else {
                            current_action = 4;
                        }
                    }
                    else {
                        if (Node.ManhattanDistance(pos, target.getNode()) <= aggroRange){
                            current_action = 2;
                            move = false;
                        }
                        else current_action = 1;
                    }
                //chase
                case 1:
                    id = Constants.BLUE_PLAYER_ID;
                    if (move){
                        run(tab);
                    }
                    else if (target != null && Node.ManhattanDistance(pos, target.getNode()) <= aggroRange) {
                            move = false;
                            current_action = 2;
                    }
                    else if (target == null && lastKnownNode != null){
                        //go to last known node
                        BFS(lastKnownNode.getX(),lastKnownNode.getY(),tab);
                        lastKnownNode = null;
                        current_action = 1;
                    }
                    else {
                        current_action = 4;
                    }
                    break;
                //attack
                case 2:
                    id = Constants.RED_PLAYER_ID;
                    if (hp < 50){
                        runpath = runAwayFromTarget(tab);
                        if (runpath != null){
                            current_action = 1;
                            move = true;
                        }
                    }
                    else if (target != null && Node.ManhattanDistance(pos, target.getNode()) <= aggroRange){
                        attackTarget(tab);
                        lastKnownNode = target.getNode().getNodeCopy();
                    } else {
                        chaseTarget(tab);
                        current_action = 1;
                    }
                    target = null;
                    if (hunger < 75 || stamina < 25) current_action = 0;
                    break;
                //sleep
                case 3:
                    sleep();
                    if (stamina >= 75){
                        asleep = false;
                        current_action = 0;
                    }
                    break;
                //patrol
                case 4:
                    id = Constants.GREEN_PLAYER_ID;
                    //idle(tab);
                    if (!patrolPoints.isEmpty()){
                        if (currentPoint >= patrolPoints.size()){
                            currentPoint = 0;
                        }
                        Node currentNode = patrolPoints.get(currentPoint);
                        BFS(currentNode.getX(),currentNode.getY(),tab);
                        //if (move) 
                        ++currentPoint;
                        if (!move) idle(tab);
                    }
                    else {
                        idle(tab);
                    }
                    current_action = 1;
                    break;
            }

            if (stamina <= 0 || hp <= 0){
                tab.getTile(pos).clearContent();
                alive = false;
            }
        }
    }
    
}
