package pathfinding.actor;
import java.io.Serializable;
import pathfinding.Table.Table;
import java.util.ArrayList;

/**
 *
 * @author Alumne
 */
public class ActorList implements Serializable {
    private ArrayList<Actor> list;
    private ArrayList<Boolean> activeList;
    
    /**
     *
     */
    public ActorList(){
        list = new ArrayList<>();
        activeList = new ArrayList<>();
    }
    
    /**
     *
     * @param i
     * @return
     */
    public Object i_object(int i){
        if (i>=0 && i<list.size()) return list.get(i);
        else return null;
    }
    
    /**
     *
     * @param a
     * @return
     */
    public boolean contains(Actor a){
        return list.contains(a);
    }
    
    public int size(){
        return list.size();
    }
    
    public Actor get(int i){
        if (i >= 0 && i < list.size()){
            return list.get(i);
        }
        else {
            return null;
        }
    }
    
    /**
     *
     * @param a
     * @param i
     */
    public void set_i(Actor a, int i){
        if (i >= 0 & i < list.size()) list.set(i, a);
    }
    
    /**
     *
     * @param a
     * @param active
     */
    public void add(Actor a, boolean active){
        list.add(a);
        activeList.add(active);
    }
    
    /**
     *
     * @param a
     * @param t
     */
    public void remove(Actor a, Table t){
        activeList.remove(list.indexOf(a));
        list.remove(a);
    }
    
    /**
     *
     * @param t
     */
    public void simulate(Table t){
        for (int i = 0; i < list.size(); i++){
            if (i < activeList.size() && activeList.get(i) && list.get(i)!= null){
                if (!(list.get(i)).isAlive()){
                    if (list.get(i).getNode()!=null){
                        t.getTile(list.get(i).getNode()).clearMatchingContent(list.get(i));
                        remove(list.get(i),t); 
                    }
                } else {
                    list.get(i).simulate(t);
                }
            }
        }
    }
}