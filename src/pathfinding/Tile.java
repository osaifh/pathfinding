package pathfinding;

import java.util.Random;
public class Tile {
    int terrain_id;
    boolean passable, lit;
    Objecte content;
    Random gen = new Random();
    
    public Tile(){
        if (gen.nextInt(2)==1) terrain_id = 0;
        else terrain_id = 5;
        passable = true;
        content = null;
        lit = false;
    }
    
    public Tile(int terrain_id){
        this.terrain_id = terrain_id;
        if (terrain_id == 1) passable = false;
        else passable = true;
        content = null;
    }

    public int get_ID(){
        if (content!=null) return content.get_id();
        else return terrain_id;
    }
    
    public int get_terrain_ID(){
        return terrain_id;
    }
    
    public Objecte get_object(){
        return content;
    }
    
    public boolean is_passable(){
        return passable;
    }
    
    public void set_ID(int id){
        this.terrain_id = id;
    }
    
    public void set_passable(boolean b){
        passable = b;
    }
    
    public void set_free(){
        terrain_id = 0;
        passable = true;
    }
    
    public void set_wall(){
        terrain_id = 1;
        passable = false;
    }
    
    public void set_objecte(int id){
        this.terrain_id = id;
    }
    
    public void set_objecte(Objecte obj){
        content = obj;
    }
    
    public void clear_objecte(){
        content = null;
        passable = true;
    }
    
    public void set_lit(boolean b){
        lit = b;
    }
}
