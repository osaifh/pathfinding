package pathfinding;

import java.util.Random;

/**
 * This class implements an object that contains information about an specific tile
 */
public class Tile {
    private int terrain_id, light_level;
    private boolean passable;
    private Subject content;
    private Random gen = new Random();
    
    /**
     * Default constructor
     * Generates a random passable tile
     * Has two different possible types
     */
    public Tile(){
        if (gen.nextInt(2)==1) terrain_id = 0;
        else terrain_id = 5;
        passable = true;
        content = null;
        light_level = 100;
    }
    
    /**
     * Overloaded constructor
     * generates a new tile with an specific ID
     * @param terrain_id specifies the terrain id of the new tile
     */
    public Tile(int terrain_id){
        this.terrain_id = terrain_id;
        if (terrain_id == 1) passable = false;
        else passable = true;
        content = null;
        light_level = 100;
    }
    
    /** 
     * @return if the tile contains an object, returns the ID of the object;
     * if not, it returns the terrain ID of the tile
     */
    public int getID(){
        if (content!=null) return content.getID();
        else return terrain_id;
    }
    
    /**
     * @return returns the terrain ID of the tile
     */
    public int getTerrainID(){
        return terrain_id;
    }
    
    /**
     * @return returns the content of the tile
     */
    public Subject getObject(){
        return content;
    }
    
    /**
     * @return returns the light level of a tile 
     */
    public int getLight(){
        return light_level;
    }
    
    /**
     * @return returns true if the tile is passable
     */
    public boolean isPassable(){
        return passable;
    }
    
    /**
     * sets the terrain ID of a tile
     * @param id specifies a terrain ID
     */
    public void setID(int id){
        this.terrain_id = id;
    }
    
    /**
     * sets the passable property
     * @param b specifies whether the tile is passable or not
     */
    public void setPassable(boolean b){
        passable = b;
    }
    
    /**
     * places a wall in the tile and removes and objects it could have
     */
    public void setWall(){
        terrain_id = 1;
        passable = false;
    }
    
    /**
     * Places an object in a tile
     * @param obj specifies the object to add
     */
    public void setObjecte(Subject obj){
        content = obj;
        if (terrain_id == 1){
            terrain_id = 0;
            passable = true;
        }
    }
    
    /**
     * sets the light value of a tile
     * @param light specifies the new light value
     * if the value is higher than 100, it will be set to 100
     */
    public void setLight(int light){
        light_level = light;
        if (light_level > 100) light_level = 100;
    }
    
    /**
     * adds a light value to a tile
     * @param light specifies the light value to add
     * the value can be negative
     */
    public void addLight(int light){
        light_level += light;
        if (light_level > 100) light_level = 100;
    }
    
    /**
     * removes an object from a tile and sets it to passable
     */
    public void clearObjecte(){
        content = null;
    }
    
    /**
     * removes any content from a tile and sets it to passable
     * if the terrain ID is different than 0 it sets it to 5
     */
    public void clear(){
        if (terrain_id != 0){
            terrain_id = 5;
        }
        content = null;
        passable = true;
    }

}
