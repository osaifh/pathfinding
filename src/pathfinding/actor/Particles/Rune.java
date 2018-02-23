package pathfinding.actor.Particles;

import pathfinding.Table.Table;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

public class Rune extends Particle {
    private boolean merged;
    private int type, mergeCount;
    private Rune origin;
    
    public Rune(Node n){
        //change this SHIT
        type = 1;
        id = 3;
        pos = n.getNodeCopy();
        merged = false;
        alive = true;
        mergeCount = 1;
    }
    
    public boolean isMerged(){
        return merged;
    }
    
    public int getType(){
        return type;
    }
    
    /**
     * Merges a rune with it's surrounding runes
     * @param t the table where the runes are
     */
    public void Merge(Table t){
        for (int i = -1; i < 2; i++){
            for (int j = -1;  j < 2; j++){
                if (!(i == 0) & (j == 0)){
                    Node aux = new Node(i,j);
                    aux.add(pos);
                    //this needs to be changed to detect the different directions since different effects may result in different directions
                    //therefore, needing to change the type of the resulting rune
                    for (int k = 0; k < t.getTile(aux).getContentSize(); k++){
                        if (t.getTile(aux).getContent(k) instanceof Rune){
                            Rune toMerge = (Rune)t.getTile(aux).getContent(k);
                            Merge(toMerge, t);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Merges a rune with it's origin and it's surroundings
     * @param rune the origin rune that is being merged
     * @param t the table where the runes are
     */
    private void Merge(Rune rune, Table t){
        this.merged = true;
        this.origin = rune;
        for (int i = -1; i < 2; i++){
            for (int j = -1;  j < 2; j++){
                if (!(i == 0) & (j == 0)){
                    Node aux = new Node(i,j);
                    aux.add(pos);
                    for (int k = 0; k < t.getTile(aux).getContentSize(); k++){
                        if (t.getTile(aux).getContent(k) instanceof Rune){
                            Rune toMerge = (Rune)t.getTile(aux).getContent(k);
                            Merge(toMerge, t);
                        }
                    }
                }
            }
        }
    }
    
    private void addToCount(){
        if (origin == null){
            mergeCount++;
        }
        else {
            origin.addToCount();
        }
    }
    
    @Override
    public void simulate(Table t) {
        
    }
    
    @Override
    public String toString(){
        return "RUNE " + pos.toString();
    }
}
