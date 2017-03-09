package pathfinding;

/**
 *
 * @author Alumne
 */
public class SubjectList {
    private final int max_size = 50;
    private Subject[] list;
    private boolean[] active_list;
    private int index;
    private Creature reference = new Creature();
    
    /**
     *
     */
    public SubjectList(){
        list = new Subject[max_size];
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
    public boolean contains(Subject a){
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
    public void set_i(Subject a, int i){
        if (i >= 0 & i < index) list[i]=a;
    }
    
    /**
     *
     * @param a
     * @param active
     */
    public void add(Subject a, boolean active){
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
    public void remove(Subject a, Table t){
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
    private int something = 0;
    
    public void simulate(Table t, SubjectList llista, double delta){
        ++something;
        for (int i = 0; i < index; i++){
            if (list[i]!=null && list[i].getID() == 2 && active_list[i]){
                if (!((Creature)list[i]).isAlive()) llista.remove(list[i],t);
            }
            
            if (something%15 == 0){
                if (list[i].getClass().equals(reference.getClass())){
                    ((Creature)list[i]).run(t);
                }
            }
            if (active_list[i]){
                list[i].simulate(t,delta);
            }
        }
        if (something%15 == 0){
                something = 0;
            }
    }
}