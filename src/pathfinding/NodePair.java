package pathfinding;

/**
 * This class is used internally for the pathfinding algorithms.
 * It stores a pair of nodes and a reference to the next node used by
 * the PairList class, and also the depth of the pair.
 * @author Me
 */
public class NodePair {
   
        private Node first;
        private NodePair source;
        private NodePair next;
        int depth;
        
    /**
     * default constructor
     */
    public NodePair(){}
        
    /**
     * constructor using a node
     * @param n the first element of the new pair
     */
    public NodePair(Node n){
            first = n;
            next = null;
            source = null;
            depth = 0;
    }
        
    /**
     * Overload that constructs a copy of a NodePair
     * @param n the NodePair to copy
     */
    public NodePair(NodePair n){
            first = n.first;
            next = n.next;
            source = n.source;
            depth = n.depth;
        }
        
    /**
     * Sets the next Node reference
     * @param n the next Node to reference
     */
    public void setNext(NodePair n){
            next = n;
        }
        
    /**
     * Sets the source of a NodePair
     * @param s the source Node to reference
     */
    public void setSource(NodePair s){
            source = s;
            depth = s.getDepth()+1;
        }
        
    /**
     * Gets the first element of a NodePair
     * @return the first element of the implicit parameter
     */
    public Node getFirst(){
            return first;
        }
        
    /**
     * Gets the next element of a NodePair
     * @return the next element of the implicit parameter
     */
    public NodePair getNext(){
            return next;
        }
        
    /**
     * Gets the source element of a NodePair
     * @return the source element of the implicit parameter
     */
    public NodePair getSource(){
            return source;
        }
        
    /**
     * Gets the depth of the NodePair
     * @return the dpeth of the implicit paramter
     */
    public int getDepth(){
            return depth;
        }
        
    /**
     * Returns true if the element is the first in a list.
     * The first element will always have null source
     * @return
     */
    public boolean isFirst(){
            return (source == null);
        }
       
}