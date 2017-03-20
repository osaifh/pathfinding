package pathfinding.auxiliar;

import pathfinding.auxiliar.NodePair;

/**
 *
 * @author Alumne
 */
public class NodeData extends NodePair {
    final double fromSource;
    final double toTarget;
    final double total;
    
    /**
     *
     */
    public NodeData() {
        fromSource=0;
        toTarget=0;
        total=0;
    }
    
    /**
     *
     * @param n
     * @param source
     * @param target
     */
    public NodeData(Node n, Node source, Node target) {
        super(n);
        fromSource = Node.distance(n,source);
        toTarget = Node.distance(n, target);
        total = fromSource + toTarget;
    }
    
    /**
     *
     * @param n
     * @param source
     * @param target
     */
    public NodeData(NodePair n, Node source, Node target) {
        super(n);
        fromSource = Node.distance(n.getFirst(),source);
        toTarget = Node.distance(n.getFirst(), target);
        total = fromSource + toTarget;
    }
    
    /**
     *
     * @return
     */
    public Node getNode() {
        return this.getFirst();
    }
    
    /**
     *
     * @return
     */
    public NodePair getNodePar() {
        return (NodePair)this;
    }
    
    /**
     *
     * @return
     */
    public double getTotal() {
        return total;
    }
}
