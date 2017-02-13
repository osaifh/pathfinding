package pathfinding;

/**
 *
 * @author Alumne
 */
public class NodePar {
   
        private Node first;
        private NodePar source;
        private NodePar next;
        int depth;
        
    /**
     *
     */
    public NodePar(){}
        
    /**
     *
     * @param n
     */
    public NodePar(Node n){
            first = n;
            next = null;
            source = null;
            depth = 0;
        }
        
    /**
     *
     * @param n
     */
    public NodePar(NodePar n){
            first = n.first;
            next = n.next;
            source = n.source;
            depth = n.depth;
        }
        
    /**
     *
     * @param n
     */
    public void setNext(NodePar n){
            next = n;
        }
        
    /**
     *
     * @param s
     */
    public void setSource(NodePar s){
            source = s;
            depth = s.getDepth()+1;
        }
        
    /**
     *
     * @return
     */
    public Node getFirst(){
            return first;
        }
        
    /**
     *
     * @return
     */
    public NodePar getNext(){
            return next;
        }
        
    /**
     *
     * @return
     */
    public NodePar getSource(){
            return source;
        }
        
    /**
     *
     * @return
     */
    public int getDepth(){
            return depth;
        }
        
    /**
     *
     * @return
     */
    public boolean isFirst(){
            return (source == null);
        }
        
    /**
     *
     */
    public void print(){
            System.out.println("primer: x = " + first.getX() + " y = " + first.getY());
        }
        
    }