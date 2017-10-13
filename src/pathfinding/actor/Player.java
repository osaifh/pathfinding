package pathfinding.actor;

import pathfinding.Table.Camera;
import pathfinding.Table.Table;
import pathfinding.auxiliar.Node;

/**
 *
 * @author Alumne
 */
public class Player extends Creature {
    private LightSource l;
    
    public Player(int x, int y){
        id = 2;
        pos = new Node(x,y);
        alive = true;
        facing_direction = 4;
        maxHP = 100;
        hp = 100;
        l = new LightSource(6,pos.getX(),pos.getY());
    }
    
    
    
    /**
     * Determinates the visibility for the active creature
     * @param tab the table where we're looking
     * @param range the sight range of the creature
     * @param cam the camera used to determinate visibility
     */
    public void lookAround(Table tab, int range, Camera cam){
            cam.setVisibilityTable(pos, true);
            /* 
            switch (facing_direction){
                case 1:
                    //facing moveDirection = 1 (N)
                    lookDirection(tab,1,1.0f,0.0f, 0,1,-1,0,range,cam);
                    lookDirection(tab,1,1.0f,0.0f, 0,1,1,0,range,cam);
                    break;
                case 3:
                    //facing moveDirection = 3 (W)
                    lookDirection(tab,1,1.0f,0.0f, -1,0,0,1,range,cam);
                    lookDirection(tab,1,1.0f,0.0f, 1,0,0,1,range,cam);
                    break;
                case 4:
                    //facing moveDirection = 4 (E)
                    lookDirection(tab,1,1.0f,0.0f, 1,0,0,-1,range,cam);
                    lookDirection(tab,1,1.0f,0.0f, -1,0,0,-1,range,cam);
                    break;
                case 6:
                    //facing moveDirection = 6 (S)
                    lookDirection(tab,1,1.0f,0.0f, 0,-1,1,0,range,cam);
                    lookDirection(tab,1,1.0f,0.0f, 0,-1,-1,0,range,cam);
                    break;
                default:
                    break;
            }
            */
            
            for (int i = -1; i < 2; ++i){
                for (int j = -1; j < 2; ++j){
                    if (i!=0 && j !=0){
                        lookDirection(tab,1,1.0f,0.0f, 0,i,j,0,range,cam);
                        lookDirection(tab,1,1.0f,0.0f, i,0,0,j,range,cam);
                    }
                }
            }
            
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
                    cam.setVisibilityTable(delta, true);
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
                        lookDirection(tab,distance + 1,start, leftSlope, xx, xy, yx, yy, range,cam);
                        newStart = rightSlope;
                    }
                }
            }
        }
    }
    
    public void print(){
        
    }
    
    public void simulate(Table t){
        tick_counter ++;
        l.cast_light(t);
        l.setNode(pos);
        if (tick_counter >= tick_max){
            tick_counter = 0;
            if (hp <= 0){
                alive = false;
            }
        }
    }
    
    @Override
    public boolean equalNode(Actor x){
        return pos.compare(x.getNode());
    }

    @Override
    public boolean isAlive() {
        return alive;
    }
}
