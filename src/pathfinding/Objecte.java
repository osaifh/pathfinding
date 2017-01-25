package pathfinding;

public class Objecte {
        private Node pos = new Node();
        private int id;
        
        public Objecte(){}
        
        public Objecte(int id, int x, int y){
            this.id = id;
            pos.set(x,y);
        }
        
        public void delete(){
            pos.set(-1,-1);
            id = -1;
        }
        
        public boolean equals(Objecte x){
            return pos.compare(x.get_node());
        }
        
        public void print(){
            System.out.println("ID objecte: " + id);
            System.out.println("posicio x = " + pos.get_x() + " y = " + pos.get_y());
        }
        
        public void simulate(Table tab,Objecte_list llista){}
        
        public Objecte get_objecte(){
            return this;
        }
        
        public Node get_node(){
            return pos;
        }
        
        public int get_id(){
            return id;
        }
        
        public void set_node(Node n){
            pos.setNode(n);
        }
        
        public void set_xy(int x, int y){
            pos.set(x, y);
        }
        
        public void set_id(int i){
            id = i;
        }
        
        public void moveNW(Table t){
            if (t.check(pos.get_x()-1,pos.get_y()-1)){
                pos.set(pos.get_x()-1,pos.get_y()-1);
            }
        }
    
        public void moveN(Table t){
            if (t.check(pos.get_x()-1,pos.get_y())){
                pos.set(pos.get_x()-1,pos.get_y());
            }
        }
    
        public void moveNE(Table t){
            if (t.check(pos.get_x()-1,pos.get_y()+1)){
                pos.set(pos.get_x()-1,pos.get_y()+1);
            }
        }

        public void moveW(Table t){
            if (t.check(pos.get_x(),pos.get_y()-1)){
                pos.set(pos.get_x(),pos.get_y()-1);
            }
        }

        public void moveE(Table t){
            if (t.check(pos.get_x(),pos.get_y()+1)){
                pos.set(pos.get_x(),pos.get_y()+1);
            }
        }

        public void moveSW(Table t){
            if (t.check(pos.get_x()+1,pos.get_y()-1)){
                pos.set(pos.get_x()+1,pos.get_y()-1);
            }
        }
        
        public void moveS(Table t){
            if (t.check(pos.get_x()+1,pos.get_y())){
                pos.set(pos.get_x()+1,pos.get_y());
            }
        }
        
        public void moveSE(Table t){
            if (t.check(pos.get_x()+1,pos.get_y()+1)){
                pos.set(pos.get_x()+1,pos.get_y()+1);
            }
        }
    }