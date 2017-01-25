package pathfinding;

public class Objecte_list {
    private final int max_size = 50;
    private Objecte[] list;
    private int index;
    
    public Objecte_list(){
        list = new Objecte[max_size];
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
    
    public void add(Objecte a){
        if (index != (max_size-1)){
            list[index] = a;
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
        for (int i = 0; i < index; i++) list[i].print();
    }
    
    public void simulate(Table t, Objecte_list llista){
        for (int i = 0; i < index; i++){
            if (list[i].get_id() == 2){
                if (!((Player)list[i]).is_alive()) llista.remove(list[i],t);
            }
            list[i].simulate(t,llista);
            list[i].print();
        }
    }
}