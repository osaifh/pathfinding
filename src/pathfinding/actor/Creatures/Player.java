package pathfinding.actor.Creatures;

import java.io.Serializable;
import java.util.ArrayList;
import pathfinding.Controller;
import pathfinding.Table.Camera;
import pathfinding.Table.Table;
import pathfinding.actor.Actor;
import pathfinding.actor.LightSource;
import pathfinding.actor.Skills.*;
import pathfinding.auxiliar.Node;
import pathfinding.auxiliar.Constants;

/**
 *
 * @author Alumne
 */
public class Player extends Creature implements Serializable {
    private LightSource l;
    private boolean lightToggle;
    //testing
    //TODO: add a list with cooldowns for each skill, add an "add" function that also adds an entry to the cooldown list, make simulate reduce the cooldown for each skill
    //temp stuff
    private boolean running;
    private int runindex;
    private Node[] runpath;
    
    
    public Player(int x, int y){
        id = Constants.PLAYER_ID;
        pos = new Node(x,y);
        alive = true;
        facing_direction = 4;
        maxHP = 500;
        hp = 500;
        l = new LightSource(6,pos.getX(),pos.getY());
        lightToggle = true;
        sight_range = 30;
        
        skillList = new ArrayList<Skill>();
        skillList.add(new ShootSkill(10));
        skillList.add(new CreateGuardSkill());
        skillList.add(new CreateWallSkill());
        skillList.add(new ForcePushSkill());
        skillList.add(new GrenadeSkill(30));
        skillList.add(new IceWallSkill());
        skillList.add(new CreateMobSkill());
        skillList.add(new ShootBulletSkill());
        //skillList.add(new CreateFoodSkill());
        skillList.add(new PathfindingSkill());
        skillList.add(new DrawClearSkill());
        //skillList.add(new CreateMonsterSkill());
        skillList.add(new MeleeSkill(20,2));
    }
    
    public ArrayList<Skill> getSkillList(){
        return skillList;
    }
    
    /**
     * Determinates the visibility for the active creature
     * @param tab the table where we're looking
     * @param range the sight range of the creature
     * @param cam the camera used to determinate visibility
     */
    public void lookAround(Table tab, int range, Camera cam){
        cam.setVisibilityTable(0,0, true);
        cam.setFOWtable(this.getNode().getX(), this.getNode().getY());            
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
                    cam.setVisibilityTable((delta.getX() - pos.getX()),(delta.getY() - pos.getY()), true);
                    cam.setFOWtable(delta.getX(), delta.getY());
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

    public void toggleLight(){
        lightToggle = !lightToggle;
    }
    
    public void BFS(int x, int y, Table tab, Controller controller){
        Node[] path = tab.mark_iBFS(pos, new Node(x,y), controller);
        if (path!= null){
            runpath = path;
            running = true;
            runindex = 1;
        } else {
            runpath = null;
        }
    }
    
    public void run(Table tab){
        if (running && runpath.length > 0 && runindex < runpath.length){
            if (tab.getTile(runpath[runindex]).isPassable()){
                iMove(tab,runpath[runindex]);

                if (tab.getTile(pos).containsID(4)){
                    tab.getTile(pos).clearMatchingContent(4);
                }
                if (runindex == runpath.length-1) running = false;
                else ++runindex;
            } else {
                running = false;
            }
        }
    }
    
    @Override
    public void simulate(Table t){
        tick_counter+=3;
        if (lightToggle) l.cast_light(t);
        l.setNode(pos);
        if (tick_counter >= tick_max){
            run(t);
            tick_counter = 0;
            if (hp <= 0){
                alive = false;
            }
        }
    }
    
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
