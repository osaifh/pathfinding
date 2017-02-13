package pathfinding;

/**
 *
 * @author Alumne
 */
public class ObjecteList {
    private final int max_size = 50;
    private Objecte[] list;
    private boolean[] active_list;
    private int index;
    
    /**
     *
     */
    public ObjecteList(){
        list = new Objecte[max_size];
        active_list = new boolean[max_size];
        index = 0;
    }
    
    /**
     *
     * @param i
     * @return
     */
    public Object i_object(int i){
        if (i >= 0 & i < max_size) return list[i];
        else return null;
    }
    
    /**
     *
     * @return
     */
    public int get_index(){
        return index;
    }
    
    /**
     *
     * @param a
     * @return
     */
    public boolean contains(Objecte a){
        boolean found = false;
        int i = 0;
        while (i < index & !found){
            found = list[i].equals(a);
            ++i;
        }
        return found;
    }
    
    /**
     *
     * @param a
     * @param i
     */
    public void set_i(Objecte a, int i){
        if (i >= 0 & i < index) list[i]=a;
    }
    
    /**
     *
     * @param a
     * @param active
     */
    public void add(Objecte a, boolean active){
        if (index != (max_size-1)){
            list[index] = a;
            active_list[index] = active;
            ++index;
        }
    }
    
    /**
     *
     * @param a
     * @param t
     */
    public void remove(Objecte a, Table t){
        boolean found = false;
        int i = 0;
        while (i < index & !found){
            found = (list[i].equals(a));
            if (!found) ++i;
        }
        if (found){
            t.set(list[i].getNode(), 0);
            list[i]=list[index-1];
            --index;
        }
    }
    
    /**
     *
     */
    public void print(){
        for (int i = 0; i < index; i++){
            list[i].print();
            System.out.println(active_list[i]);
        }
    }
    
    /**
     *
     * @param t
     * @param llista
     * @param delta
     */
    public void simulate(Table t, ObjecteList llista, double delta){
        for (int i = 0; i < index; i++){
            if (list[i]!=null && list[i].getID() == 2 && active_list[i]){
                if (!((Creature)list[i]).isAlive()) llista.remove(list[i],t);
            }
            if (active_list[i]){
                list[i].simulate(t,delta);
            }
        }
    }
}