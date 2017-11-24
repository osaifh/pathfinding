package pathfinding.Table;

import java.util.Random;

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
    
    public int getRandomID(){
        Random random = new Random();
        System.out.println(name);
        if (name.equals("grass")){
            int x = random.nextInt(10);
            switch (x) {
                case 0:
                    return 106;
                case 1:
                    return 107;
                default:
                    return 105;
            }
        }
        return -1;
    }
}
