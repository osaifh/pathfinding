package pathfinding;

public class Par_List {
       
        private Node_Par head, tail;
        private int size;
        
        public Par_List(){
            head = tail = null;
            size = 0;
        }
        
        public void add(Node_Par npar){
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
        
        public int get_size(){
            return size;
        }
        
        public boolean findNode(Node act){
            if (head!= null){
                Node_Par temp = head;
                while(temp!=null){
                    if (temp.getFirst().compare(act)) return true;
                    temp = temp.getNext();
                }
            }
            return false;
        }
        
        public Node[] tracePath(Node_Par last){
            Node[] path = new Node[last.getDepth()+1];
            Node_Par current = last;
            for (int i = last.getDepth(); i >= 0; --i){
                path[i] = current.getFirst();
                current = current.getSource();
            }
            return path;
        }
        
        public void printList(){
            if (head!=null){
                Node_Par current = head;
                while(current!=null){
                    current.print();
                    current = current.getNext();
                }
            }
        }
}