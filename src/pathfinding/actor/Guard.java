package pathfinding.actor;

import java.util.ArrayList;
import java.util.Arrays;
import pathfinding.Table.Table;
import pathfinding.auxiliar.Node;

/**
 *
 * @author Alumne
 */
public class Guard extends Creature {
    private int alertLevel;
    private ArrayList<Node> patrolList;
    private ArrayList<Node[]> pathList;
    private Node[] currentPath;
    private int patrolIndex, pathIndex;
    private Creature objective;
    
    public Guard(int x, int y){
        id = 2;
        pos = new Node(x,y);
        alertLevel = 0;
        facing_direction = 4;
        objective = null;
        patrolIndex = 0;
    }
    
    /**
     * Determinates the visibility for the active creature
     * @param tab the table where we're looking
     * @param range the sight range of the creature
     */
    public void lookAround(Table tab, int range){
            switch (facing_direction){
                case 1:
                    //facing moveDirection = 1 (N)
                    lookDirection(tab,1,1.0f,0.0f, 0,1,-1,0,range);
                    lookDirection(tab,1,1.0f,0.0f, 0,1,1,0,range);
                    break;
                case 3:
                    //facing moveDirection = 3 (W)
                    lookDirection(tab,1,1.0f,0.0f, -1,0,0,1,range);
                    lookDirection(tab,1,1.0f,0.0f, 1,0,0,1,range);
                    break;
                case 4:
                    //facing moveDirection = 4 (E)
                    lookDirection(tab,1,1.0f,0.0f, 1,0,0,-1,range);
                    lookDirection(tab,1,1.0f,0.0f, -1,0,0,-1,range);
                    break;
                case 6:
                    //facing moveDirection = 6 (S)
                    lookDirection(tab,1,1.0f,0.0f, 0,-1,1,0,range);
                    lookDirection(tab,1,1.0f,0.0f, 0,-1,-1,0,range);
                    break;
                default:
                    break;
            }
            
            /*
            for (int i = -1; i < 2; ++i){
                for (int j = -1; j < 2; ++j){
                    if (i!=0 && j !=0){
                        lookDirection(tab,1,1.0f,0.0f, 0,i,j,0,range);
                        lookDirection(tab,1,1.0f,0.0f, i,0,0,j,range);
                    }
                }
            }
            */
    }
    
    private void lookDirection(Table tab, int row, float start, float end, int xx, int xy, int yx, int yy, int range){
        float newStart = 0.0f;
        if (start < end){
            return;
        }
        boolean blocked = false;
        for (int distance = row; distance <= range && !blocked; distance++) {
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
                    if (tab.getActor(pos) instanceof Player && alertLevel !=2){
                        alertLevel = 2;
                        objective = (Creature)tab.getActor(pos);
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
    
    //      - 0
    //   0 - X 0 +
    //      + 0
    public void moveRelative(Node n, Table t){
        int dir = -1;
        if (n.getX()==this.pos.getX()){
            if (n.getY()<this.pos.getY()){
                dir = 1; //NORTH
            } else {
                dir = 6; //SOUTH
            }
        } else if (n.getY()==this.pos.getY()){
            if (n.getX()<this.pos.getX()){
                dir = 3; //WEST
            } else {
                dir = 4; //EAST
            }
        }
        if (dir!=-1){
            iMove(t,dir);
        }
    }
    
    public void assignPatrolPoint(Node n, Table t){
        int minDistance, index, curDistance;
        Node[] auxPath1, auxPath2;
        
        if (patrolList.size()>1){
            //first iteration
            auxPath1 = t.iBFS(n,patrolList.get(0));
            auxPath2 = t.iBFS(patrolList.get(patrolList.size()-1),n);
            minDistance = auxPath1.length + auxPath2.length;
            index = 0;
            //rest of the iterations
            for (int i = 0; i < patrolList.size()-1; ++i){
                auxPath2 = Arrays.copyOf(auxPath1,auxPath1.length);
                auxPath1 = t.iBFS(patrolList.get(i),patrolList.get(i+1));
                curDistance = auxPath1.length + auxPath2.length;
                if (curDistance < minDistance){
                    index = i+1;
                }
            }
            patrolList.add(index,n);
            if (index == 0){
                pathList.add(0,t.iBFS(patrolList.get(patrolList.size()-1),n));
            } else {
                pathList.add(index,t.iBFS(patrolList.get(index-1),patrolList.get(index)));
            }
            pathList.add(index+1,t.iBFS(patrolList.get(index),patrolList.get(index+1)));
        } else {
            patrolList.add(n);
        }
    }
    
    public void patrol(){
        //check if we're at the end of a path
        //if not, just keep moving on that path
            //use relativeMove to move
            //if something goes wrong, recalculate path
        //else:
        //check the pathindex, get next Path
        //if we reach the end of the list, go back to the beggining
        
    }
    
    public void resumePatrol(){
        //tries to find the current node in the nodes
    }
    
    @Override
    public void simulate(Table t) {
        ++tick_counter;
        if (tick_counter >= tick_max){
            tick_counter = 0;
            if (alertLevel == 0){
                //patrol
            }
        }
    }

    @Override
    public void print() {
        
    }
}