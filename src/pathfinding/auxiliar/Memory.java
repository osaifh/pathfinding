package pathfinding.auxiliar;

/**
 *
 * @author
 */
public class Memory {
    private Node[] content;
    private int index, size;
    private final static int MAX_SIZE = 10;
    
    /**
     *
     */
    public Memory(){
        content = new Node[MAX_SIZE];
        index = size = 0;
    }
    
    /**
     *
     * @param act
     */
    public void add(Node act){
        if(act!=null){
            content[index]= act;
            ++index;
            if (index == 10) index = 0;
            if (size < 10) ++size;
        }
    }
    
    /**
     *
     * @param i
     * @return
     */
    public Node getNode(int i){
        if (i < size){
            return content[i];
        }
        else return null;
    }
    
    /**
     *
     * @param dest
     * @return
     */
    public int check(Node dest){
        int eval = 0;
        if (size > 0){
            int x = 0;
            for (int i = 0; i < size; ++i){
                eval += Node.distance(dest,content[i]);
            }
        }
        return eval;
    }
    
    /**
     *
     */
    public void wipe(){
        size = 0;
        index = 0;
    }
    
    /**
     *
     * @return
     */
    public int getSize(){
        return size;
    }
    
    /**
     *
     */
    public void print(){
        for (int i = 0; i < size; ++i){
            System.out.println("Position " + i + " contains ");
            content[i].print();
        }
    }
}
