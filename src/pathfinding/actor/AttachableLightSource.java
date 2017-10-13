package pathfinding.actor;

import pathfinding.Table.Table;

/**
 *
 * @author Alumne
 */
// DOESN´T WORK FOR SOME REASON
public class AttachableLightSource extends LightSource {
    private Actor source;
    private boolean alive;
    
    public AttachableLightSource(int intensity, int x, int y, Actor source) {
        super(intensity, x, y);
        this.source = source;
        alive = true;
        
    }
    
    public void update(Table tab){
        tab.getTile(pos).clearMatchingContent(this);
        if (source!=null && source.isAlive() && alive){
            this.pos = source.getNode();
            tab.getTile(pos).addContent(this);
            alive = source.isAlive();
        } else {
            source = null;
            alive = false;
        }
    }
    
    public Actor getSource(){
        return source;
    }
    
    @Override
    public void simulate(Table tab){
        if (alive){
            update(tab);
            cast_light(tab); 
        }
    }
    
    @Override
    public void print(){
        System.out.println("this is a light and it's " + alive);
    }
    
    @Override
    public boolean isAlive(){
        return alive;
    }
}
