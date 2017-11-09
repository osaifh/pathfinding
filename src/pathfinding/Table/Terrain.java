package pathfinding.Table;

/**
 * Class used to help generate the terrain in table
 * @author 
 */
public class Terrain {
    private String name;
    private boolean passable, opaque;
    private int id;
    private float range;

    public Terrain(String name, boolean passable, boolean opaque, int id, float range) {
        this.name = name;
        this.passable = passable;
        this.opaque = opaque;
        this.id = id;
        this.range = range;
    }

    public String getName(){
        return name;
    }
    
    public boolean isPassable() {
        return passable;
    }

    public boolean isOpaque() {
        return opaque;
    }

    public int getId() {
        return id;
    }
    
    public float getRange(){
        return range;
    }
}
