package pathfinding;

/**
 *
 * @author Alumne
 */
public class ParList {
       
        private NodePar head, tail;
        private int size;
        
    /**
     *
     */
    public ParList(){
            head = tail = null;
            size = 0;
        }
        
    /**
     *
     * @param npar
     */
    public void add(NodePar npar){
            if (head == null){
                head = npar;
                tail = head;
            } else if (head == tail){
                head.setNext(npar);
                tail = head.getNext();
            } else {
                tail.setNext(npar);
                tail = tail.getNext();
            }
            ++size;
        }
        
    /**
     *
     * @return
     */
    public int get_size(){
            return size;
        }
        
    /**
     *
     * @param act
     * @return
     */
    public boolean findNode(Node act){
            if (head!= null){
                NodePar temp = head;
                while(temp!=null){
                    if (temp.getFirst().compare(act)) return true;
                    temp = temp.getNext();
                }
            }
            return false;
        }
        
    /**
     *
     * @param last
     * @return
     */
    public Node[] tracePath(NodePar last){
            Node[] path = new Node[last.getDepth()+1];
            NodePar current = last;
            for (int i = last.getDepth(); i >= 0; --i){
                path[i] = current.getFirst();
                current = current.getSource();
            }
            return path;
        }
        
    /**
     *
     */
    public void printList(){
            if (head!=null){
                NodePar current = head;
                while(current!=null){
                    current.print();
                    current = current.getNext();
                }
            }
        }
}