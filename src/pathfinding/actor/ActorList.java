package pathfinding.actor;
import pathfinding.Table.Table;
import java.util.ArrayList;
/**
 *
 * @author Alumne
 */
public class ActorList {
    private ArrayList<Actor> list;
    private ArrayList<Boolean> active_list;
    
    /**
     *
     */
    public ActorList(){
        list = new ArrayList<>();
        active_list = new ArrayList<>();
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
        active_list.add(active);
    }
    
    /**
     *
     * @param a
     * @param t
     */
    public void remove(Actor a, Table t){
        active_list.remove(list.indexOf(a));
        list.remove(a);
    }
    
    /**
     *
     */
    public void print(){
        for (int i = 0; i < list.size(); i++){
            list.get(i).print();
            System.out.println(active_list.get(i));
        }
    }
    
    /**
     *
     * @param t
     */
    public void simulate(Table t){
        for (int i = 0; i < list.size(); i++){
            if (list.get(i)!=null && list.get(i).getID() == 2 && active_list.get(i)){
                if (!((Creature)list.get(i)).isAlive()) this.remove(list.get(i),t);
            }
            if (i < active_list.size() && active_list.get(i) && i < list.size() && list.get(i)!= null){
                list.get(i).simulate(t);
            }
        }
    }
}