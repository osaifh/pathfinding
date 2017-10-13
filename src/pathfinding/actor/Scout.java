package pathfinding.actor;

import pathfinding.Table.Table;
import pathfinding.auxiliar.Memory;
import pathfinding.auxiliar.Node;

public class Scout extends Creature {
    private Boolean move;
    private int status, runindex; //0 standby 1 searching 2 returning to base
    private Node[] runpath;
    private Memory mem,longMem;
    private Node objective;
    private Core host;
    private static final int SIGHT_RANGE = 6;
    
    public Scout(int x, int y, Core host){
        alive = true;
        this.host = host;
        id = 22;
        pos = new Node(x,y);
        status = runindex = 0;
        mem = new Memory();
        longMem = new Memory();
        move = false;
        objective = null;
    }
    
    public int getStatus(){
        return status;
    }
    
    public void setStatus(int status){
        this.status = status;
    }
    
    public void run(Table tab){
        if (move && runpath.length > 0 && runindex < runpath.length){
            if (tab.getTile(runpath[runindex]).isPassable()){
                tab.getTile(pos).clearMatchingContent(this);
                pos = (runpath[runindex]);
                tab.getTile(pos).addContent(this);
                if (status == 1) lookAround(tab,SIGHT_RANGE);
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
                if (Node.distance(pos,delta) <= range){
                    if (tab.getTile(delta).getID()==4){
                        if (!host.notYetEvaluated(delta)){
                            //found the objective, time to go home
                            objective = delta;
                            status = 3;
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
    
    public void BFS(int x, int y, Table tab){
        Node[] path = tab.iBFS(pos, new Node(x,y));
        if (path!= null){
            runpath = path;
            move = true;
            runindex = 1;
        } else {
            runpath = null;
        }
    }
    
    @Override
    public void simulate(Table t) {
        ++tick_counter;
        if (tick_counter >= tick_max){
            tick_counter = 0;
            if (status == 1){
                run(t);
                if (objective != null && getNode().compare(host.getNode())){
                    host.sendObjective(objective);
                    status = 0;
                    objective = null;
                }
                if (!move && (status != 0)){
                    if (objective == null) status = 2;
                    else status = 3;
                }
            }
            else if (status == 2){
                runpath = runAway(t);
                longMem.add(pos);
                if (runpath != null){
                    status = 1;
                }
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
