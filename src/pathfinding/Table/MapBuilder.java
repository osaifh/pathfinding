package pathfinding.Table;

import java.util.ArrayList;
import pathfinding.actor.Environment.*;

public class MapBuilder {
    private Table table;
    
    public MapBuilder(Table table){
        this.table = table;
    }
    
    public ArrayList<Shrub> createShrubs(){
        ArrayList<Shrub> shrubList = new ArrayList<Shrub>();
        
        for (int y = 0; y < table.getSize(); y++){
            for (int x = 0; x < table.getSize(); x++){
                
            }
        }
        
        return shrubList;
    }
}
