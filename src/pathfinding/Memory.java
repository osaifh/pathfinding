package pathfinding;

public class Memory {
    private Node[] content;
    int index, size;
    final int max_size = 10;
    
    public Memory(){
        content = new Node[max_size];
        index = size = 0;
    }
    
    public void add(Node act){
        if(act!=null){
            content[index]= act;
            ++index;
            if (index == 10) index = 0;
            if (size < 10) ++size;
        }
    }
    
    public Node get_node(int i){
        if (i < size){
            return content[i];
        }
        else return null;
    }
    
    public int check(Node dest){
        int eval = 0;
        if (size > 0){
            int x = 0;
            for (int i = 0; i < size; ++i){
                eval += dest.distance(dest,content[i]);
            }
        }
        return eval;
    }
    
    public void wipe(){
        size = 0;
        index = 0;
    }
    
    public int get_size(){
        return size;
    }
    
    public void print(){
        for (int i = 0; i < size; ++i){
            System.out.println("Position " + i + " contains ");
            content[i].print();
        }
    }
}
