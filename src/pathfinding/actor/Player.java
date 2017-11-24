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
        sight_range = 30;
    }
    
    @Override
    public boolean equalNode(Actor x){
        return pos.equals(x.getNode());
    }

    @Override
    public boolean isAlive() {
        return alive;
    }
    
    /**
     * Determinates the visibility for the active creature
     * @param tab the table where we're looking
     * @param range the sight range of the creature
     * @param cam the camera used to determinate visibility
     */
    public void lookAround(Table tab, int range, Camera cam){
            cam.setVisibilityTable(pos, true);            
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
                    if (tab.getTile(delta).isOpaque() && Node.distance(pos,delta) <= sight_range){
                        blocked = true;
                        lookDirection(tab,distance + 1,start, leftSlope, xx, xy, yx, yy, range,cam);
                        newStart = rightSlope;
                    }
                }
            }
        }
    }

    @Override
    public void simulate(Table t){
        tick_counter++;
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
    public void print(){}
    
    public void updateID(){
        switch (facing_direction) {
            case 0:
                id = 13;
                break;
            case 1:
                id = 11;
                break;
            case 2:
                id = 12;
                break;
            case 3:
                id = 13;
                break;
            case 4:
                id = 12;
                break;
            case 5:
                id = 13;
                break;
            case 6:
                id = 10;
                break;
            case 7:
                id = 12;
                break;
            default:
                id = 2;
                break;
        }
        //delete this
        id = 2;
    }
    
}
