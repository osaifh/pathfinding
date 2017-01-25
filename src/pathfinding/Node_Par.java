package pathfinding;

public class Node_Par {
   
        private Node first;
        private Node_Par source;
        private Node_Par next;
        int depth;
        
        public Node_Par(){}
        
        public Node_Par(Node n){
            first = n;
            next = null;
            source = null;
            depth = 0;
        }
        
        public Node_Par(Node_Par n){
            first = n.first;
            next = n.next;
            source = n.source;
            depth = n.depth;
        }
        
        public void setNext(Node_Par n){
            next = n;
        }
        
        public void setSource(Node_Par s){
            source = s;
            depth = s.getDepth()+1;
        }
        
        public Node getFirst(){
            return first;
        }
        
        public Node_Par getNext(){
            return next;
        }
        
         public Node_Par getSource(){
            return source;
        }
        
        public int getDepth(){
            return depth;
        }
        
        public boolean isFirst(){
            return (source == null);
        }
        
        public void print(){
            System.out.println("primer: x = " + first.get_x() + " y = " + first.get_y());
        }
        
    }