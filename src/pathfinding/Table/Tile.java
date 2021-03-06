package pathfinding.Table;

import java.io.Serializable;
import pathfinding.actor.Actor;
import java.util.Random;
import java.util.ArrayList;
import pathfinding.actor.Creatures.Creature;
import pathfinding.auxiliar.Constants;

/**
 * This class implements an object that contains information about an specific tile
 */
public class Tile implements Serializable {
    private int terrain_id, light_level;
    private boolean passable, opaque;
    private ArrayList<Actor> icontent;
    private Random gen = new Random();

    /**
     * Overloaded constructor
     * generates a new tile with an specific ID
     * @param terrain_id specifies the terrain id of the new tile
     */
    public Tile(int terrain_id){
        this.terrain_id = terrain_id;
        passable = true;
        opaque = false;
        light_level = Constants.MAX_LIGHT;
        icontent = new ArrayList<>();
    }
    
    public Tile(Terrain terrain){
        this.terrain_id = terrain.getId();
        this.passable = terrain.isPassable();
        this.opaque = terrain.isOpaque();
        this.light_level = Constants.MAX_LIGHT;
        icontent = new ArrayList<>();
        if(terrain.getName().equals("grass")){
            Random random = new Random();
            int x = random.nextInt(100);
            switch (x) {
                case 0:
                    terrain_id  = 107;
                    break;
                case 1:
                    terrain_id  = 106;
                    break;
                default:
                    terrain_id = 105;
                    break;
            }
        }
    }
    
    /**
     * @param i
     * @return if the tile contains an object, returns the ID of the object;
     */
    public int getID(int i){
        if (i < icontent.size()){
            return icontent.get(i).getID();
        }
        else return -1;
    }
    
    public int getID(){
        if (icontent.size()>0){
            return icontent.get(0).getID();
        }
        else return -1;
    }
    
    public boolean contains(Actor a){
        for (int i = 0 ; i < icontent.size(); ++i){
            if (icontent.get(i)==a) return true;
        }
        return false;
    }
    
    public boolean containsID(int id){
        for (int i = 0; i < icontent.size(); ++i){
            if (icontent.get(i).getID()==id) return true;
        }
        return false;
    }
    
    /**
     * @return returns the terrain ID of the tile
     */
    public int getTerrainID(){
        return terrain_id;
    }
    
    /**
     * @param i
     * @return returns the content of the tile
     */
    public Actor getContent(int i){
        if (i < icontent.size()){
            return icontent.get(i);
        }
        return null;
    }
    
    public Actor getContent(){
        if (!icontent.isEmpty()){
            return icontent.get(0);
        }
        return null;
    }
    
    public ArrayList<Actor> getContentList(){
        return icontent;
    }
    
    public Actor getMatchingContent(Actor x){
        for (int i = 0; i < icontent.size(); ++i){
            if (icontent.get(i)==x){
                return icontent.get(i);
            }
        }
        return null;
    }
    
    public Boolean ContainsCreature(){
        for(Object obj : icontent){
            if (obj instanceof Creature) return true;
        }
        return false;
    }
    
    public void updateMatchingContent(Actor x){
        for (int i = 0; i < icontent.size(); ++i){
            if (icontent.get(i)==x){
                icontent.set(i, x);
            }
        }
    }
    
    public void clearMatchingContent(Actor x){
        boolean found = false;
        for (int i = 0; i < icontent.size() && !found; ++i){
            if (icontent.get(i)==x){
                found = true;
                icontent.remove(i);
            }
        }
    }
    
    public void clearMatchingContent(int id){
        boolean found = false;
        for (int i = 0; i < icontent.size() && !found; ++i){
            if (icontent.get(i).getID()==id){
                found = true;
                icontent.remove(i);
            }
        }
    }
    
    /**
     * @return returns the light level of a tile 
     */
    public int getLight(){
        return light_level;
    }
    
    public int getContentSize(){
        return icontent.size();
    }
    
    /**
     * @return returns true if the tile is passable
     */
    public boolean isPassable(){
        return passable;
    }
    
    /**
     * @return returns true if the tile is passable
     */
    public boolean isOpaque(){
        return opaque;
    }
    
    public boolean isEmpty(){
        return (icontent.isEmpty());
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
    
    public void setOpaque(boolean b){
        opaque = b;
    }
    
    /**
     * places a wall in the tile and removes and objects it could have
     * deprecate this shit soon
     */
    public void setWall(){
        terrain_id = 1;
        passable = false;
        opaque = true;
    }

    public void addContent(Actor obj){
        icontent.add(obj);
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
     * removes an object from a tile
     */
    public void clearContent(){
        icontent.clear();
    }
    
    /**
     * removes any content from a tile and sets it to passable
     * if the terrain ID is different than 0 it sets it to 5
     */
    public void clear(){
        if(!icontent.isEmpty()) icontent.clear();
        passable = true;
        opaque = false;
    }

}
