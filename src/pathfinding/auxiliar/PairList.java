package pathfinding.auxiliar;

/**
 * Class used to store a list of Node Pairs.
 * These are used to trace the paths of the pathfinding algorithms.
 * @author Me
 */
public class PairList {
    private NodePair head, tail;
    private int size;
        
    /**
     * Default constructor of an empty PairList
     */
    public PairList(){
        head = tail = null;
        size = 0;
    }
        
    /**
     * Adds an element to the PairList.
     * @param npair the NodePair to add to the PairList.
     */
    public void add(NodePair npair){
        if (head == null){
            head = npair;
            tail = head;
        } else if (head == tail){
            head.setNext(npair);
            tail = head.getNext();
        } else {
            tail.setNext(npair);
            tail = tail.getNext();
        }
        ++size;
    }
        
    /**
     * Returns the size of a PairList
     * @return the size of the implicit parameter
     */
    public int getSize(){
        return size;
    }
        
    /**
     * Finds a node in a PairList
     * @param act the node we're searching for
     * @return Returns true if the node is in the implicit parameter
     */
    public boolean findNode(Node act){
        if (head!= null){
            NodePair temp = head;
            while(temp!=null){
                if (temp.getFirst().compare(act)) return true;
                temp = temp.getNext();
            }
        }
        return false;
    }
        
    /**
     * Given a determinate Node contained within the NodePair last,
     * traces the path backwards from the list, starting with the last element
     * and ending with the first element.
     * @param last The last element of the path
     * @return The path of nodes from the first element to the last one
     */
    public Node[] tracePath(NodePair last){
        Node[] path = new Node[last.getDepth()+1];
        NodePair current = last;
        for (int i = last.getDepth(); i >= 0; --i){
            path[i] = current.getFirst();
            current = current.getSource();
        }
        return path;
   }
        
}