package pathfinding;

public class Node_Data extends Node_Par{
    final double fromSource;
    final double toTarget;
    final double total;
    
    public Node_Data(){
        fromSource=0;
        toTarget=0;
        total=0;
    }
    
    public Node_Data(Node n, Node source, Node target){
        super(n);
        fromSource = n.distance(n,source);
        toTarget = n.distance(n, target);
        total = fromSource + toTarget;
    }
    
    public Node_Data(Node_Par n, Node source, Node target){
        super(n);
        fromSource = n.getFirst().distance(n.getFirst(),source);
        toTarget = n.getFirst().distance(n.getFirst(), target);
        total = fromSource + toTarget;
    }
    
    public Node get_Node(){
        return this.getFirst();
    }
    
    public Node_Par get_Node_Par(){
        return (Node_Par)this;
    }
    
    public double get_Total(){
        return total;
    }
}
