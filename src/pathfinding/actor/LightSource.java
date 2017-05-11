package pathfinding.actor;

import pathfinding.auxiliar.Node;
import pathfinding.Table.Table;

/**
 *
 * @author Alumne
 */
public class LightSource implements Actor {
    int intensity;
    Node pos;
    
    /**
     *
     * @param intensity
     * @param x
     * @param y
     */
    public LightSource(int intensity, int x, int y){
        pos = new Node(x,y);
        this.intensity = intensity;
    }
    
    public Boolean equalNode(Actor x){
        return pos.compare(x.getNode());
    }
    
    /**
     *
     * @return
     */
    public int get_intensity(){
        return intensity;
    }
    
    /**
     *
     * @param x
     */
    public void set_intensity(int x){
        intensity += x;
    }
    
    /**
     *
     * @param tab
     */
    public void cast_light(Table tab){
            tab.getTile(pos).addLight(10*intensity);
            for (int i = -1; i < 2; ++i){
                for (int j = -1; j < 2; ++j){
                    if (i!=0 && j !=0){
                        i_cast_light(tab,1,1.0f,0.0f, 0,i,j,0,intensity);
                        i_cast_light(tab,1,1.0f,0.0f, i,0,0,j,intensity);
                    }
                }
            }
    }
    
    private void i_cast_light(Table tab, int row, float start, float end, int xx, int xy, int yx, int yy, int range){
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
                    int casted_light = 10*(intensity-((int)Node.distance(delta, pos)));
                    if (delta.getX()==pos.getX() || delta.getY()==pos.getY() 
                    || (Math.abs(delta.getX()-pos.getX())==Math.abs(delta.getY()-pos.getY()))){
                        casted_light /= 2;
                    }
                    tab.getTile(delta).addLight(casted_light);
                }
                
                if (blocked){
                    if (tab.checkOpaque(currentX,currentY)){
                        newStart = rightSlope;
                        continue;
                    } else {
                        blocked = false;
                        start = newStart;
                    }
                } else {
                    if (tab.checkOpaque(currentX,currentY) && (int)Node.distance(pos,delta) <= range){
                        blocked = true;
                        i_cast_light(tab,distance + 1,start-1, leftSlope, xx, xy, yx, yy, range);
                        newStart = rightSlope;
                    }
                }
            }
        }
    }
    
    /**
     *
     * @param tab
     */
    @Override
    public void simulate(Table tab){
        cast_light(tab);
    }
    
    public void print(){}

    public Actor getActor() {
        return this;
    }

    @Override
    public Node getNode() {
        return pos;
    }

    @Override
    public int getID() {
        return -1;
    }

    @Override
    public void setNode(Node n) {
        pos = n;
    }

    @Override
    public void setNode(int x, int y) {
        pos.set(x, y);
    }
}
