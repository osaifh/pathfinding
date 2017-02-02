package pathfinding;

public class Objecte_list {
    private final int max_size = 50;
    private Objecte[] list;
    private boolean[] active_list;
    private int index;
    
    public Objecte_list(){
        list = new Objecte[max_size];
        active_list = new boolean[max_size];
        index = 0;
    }
    
    public Object i_object(int i){
        if (i >= 0 & i < max_size) return list[i];
        else return null;
    }
    
    public int get_index(){
        return index;
    }
    
    public boolean contains(Objecte a){
        boolean found = false;
        int i = 0;
        while (i < index & !found){
            found = list[i].equals(a);
            ++i;
        }
        return found;
    }
    
    public void set_i(Objecte a, int i){
        if (i >= 0 & i < index) list[i]=a;
    }
    
    public void add(Objecte a, boolean active){
        if (index != (max_size-1)){
            list[index] = a;
            active_list[index] = active;
            ++index;
        }
    }
    
    public void remove(Objecte a, Table t){
        boolean found = false;
        int i = 0;
        while (i < index & !found){
            found = (list[i].equals(a));
            if (!found) ++i;
        }
        if (found){
            t.set(list[i].get_node(), 0);
            list[i]=list[index-1];
            --index;
        }
    }
    
    public void print(){
        for (int i = 0; i < index; i++){
            list[i].print();
            System.out.println(active_list[i]);
        }
    }
    
    public void simulate(Table t, Objecte_list llista, double delta){
        for (int i = 0; i < index; i++){
            if (list[i]!=null && list[i].get_id() == 2 && active_list[i]){
                if (!((Player)list[i]).is_alive()) llista.remove(list[i],t);
            }
            if (active_list[i]){
                list[i].simulate(t,llista,delta);
            }
        }
    }
}