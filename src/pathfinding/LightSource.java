package pathfinding;

/**
 *
 * @author Alumne
 */
public class LightSource extends Objecte {
    int intensity;
    
    /**
     *
     * @param intensity
     * @param x
     * @param y
     */
    public LightSource(int intensity, int x, int y){
        setID(6);
        setPos(x,y);
        this.intensity = intensity;
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
            tab.getTile(getNode()).addLight(10*intensity);
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
                int currentX = getNode().getX() + deltaX * xx + deltaY * xy;
                int currentY = getNode().getY() + deltaX * yx + deltaY * yy;
                float leftSlope = (deltaX - 0.5f) / (deltaY + 0.5f);
                float rightSlope = (deltaX + 0.5f) / (deltaY - 0.5f);
                if (!(tab.valid(currentX,currentY)) || start < rightSlope){
                    continue;
                } else if (end > leftSlope){
                    break;
                }
                Node delta = new Node(currentX,currentY);
                if (delta.distance(getNode(),delta) <= range){
                    int casted_light = 10*(intensity-((int)Node.distance(delta, getNode())));
                    if (delta.getX()==getNode().getX() || delta.getY()==getNode().getY() 
                    || (Math.abs(delta.getX()-getNode().getX())==Math.abs(delta.getY()-getNode().getY()))){
                        casted_light /= 2;
                    }
                    tab.getTile(delta).addLight(casted_light);
                }
                
                if (blocked){
                    if (!tab.checkPassable(currentX,currentY)){
                        newStart = rightSlope;
                        continue;
                    } else {
                        blocked = false;
                        start = newStart;
                    }
                } else {
                    if (!tab.checkPassable(currentX,currentY) && (int)Node.distance(getNode(),delta) <= range){
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
     * @param delta
     */
    @Override
    public void simulate(Table tab, double delta){
        cast_light(tab);
    }
}
