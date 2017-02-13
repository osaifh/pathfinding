package pathfinding;

/**
 *
 * @author Alumne
 */
public class Objecte {
        private Node pos;
        private int id;
        
    /**
     * Default empty constructor
     */
    public Objecte() {
        pos = new Node();
    }
        
    /**
     * Constructor that uses a determinate ID and coordinates
     * @param id The ID for the new objecte
     * @param x Horizontal coordinate
     * @param y Vertical coordinate
     */
    public Objecte(int id, int x, int y) {
        pos = new Node(x,y);
        this.id = id;
    }
        
    /**
     * Compares the position of two objects
     * @param x The object to compare
     * @return Returns true if the implicit parameter is in the same position as x
     */
    public boolean equals(Objecte x) {
        return pos.compare(x.getNode());
    }
        
    /**
     * Gets a reference to the implicit parameter
     * @return Returns the implicit parameter
     */
    public Objecte getObjecte(){
        return this;
    }
        
    /**
     * Gets the node that contains the position of an object
     * @return Returns the implicit paramter's position 
     */
    public Node getNode(){
        return pos;
    }
        
    /**
     * Gets the ID of an object
     * @return Returns the implicit parameter's ID
     */
    public int getID(){
        return id;
    }
        
    /**
     * Sets the position of an object
     * @param n The new position of the object
     */
    public void setPos(Node n){
        pos.setNode(n);
    }
        
    /**
     * Overload of the setPos function that sets the position of an object 
     * to a determinate coordinates
     * @param x Horizontal coordinate
     * @param y Vertical coordinate
     */
    public void setPos(int x, int y){
        pos.set(x, y);
    }
        
    /**
     * Sets the ID of an object
     * @param id the new ID to set
     */
    public void setID(int id){
        this.id = id;
    }
        
    /**
     * Moves an object one tile to the north-west
     * @param t the table where the object is
     */
    public void moveNW(Table t){
        if (t.checkPassable(pos.getX()-1,pos.getY()-1)){
            pos.set(pos.getX()-1,pos.getY()-1);
        }
    }
    
    /**
     * Moves an object one tile to the north
     * @param t the table where the object is
     */
    public void moveN(Table t){
        if (t.checkPassable(pos.getX()-1,pos.getY())){
            pos.set(pos.getX()-1,pos.getY());
        }
    }
    
    /**
     * Moves an object one tile to the north-east
     * @param t the table where the object is
     */
    public void moveNE(Table t){
        if (t.checkPassable(pos.getX()-1,pos.getY()+1)){
            pos.set(pos.getX()-1,pos.getY()+1);
        }
    }

    /**
     * Moves an object one tile to the west
     * @param t the table where the object is
     */
    public void moveW(Table t){
        if (t.checkPassable(pos.getX(),pos.getY()-1)){
            pos.set(pos.getX(),pos.getY()-1);
        }
    }

    /**
     * Moves an object one tile to the east
     * @param t the table where the object is
     */
    public void moveE(Table t){
        if (t.checkPassable(pos.getX(),pos.getY()+1)){
            pos.set(pos.getX(),pos.getY()+1);
        }
    }

    /**
     * Moves an object one tile to the south-west
     * @param t the table where the object is
     */
    public void moveSW(Table t){
        if (t.checkPassable(pos.getX()+1,pos.getY()-1)){
            pos.set(pos.getX()+1,pos.getY()-1);
        }
    }
        
    /**
     * Moves an object one tile to the south
     * @param t the table where the object is
     */
    public void moveS(Table t){
        if (t.checkPassable(pos.getX()+1,pos.getY())){
            pos.set(pos.getX()+1,pos.getY());
        }
    }
        
    /**
     * Moves an object one tile to the south-east
     * @param t the table where the object is
     */
    public void moveSE(Table t){
        if (t.checkPassable(pos.getX()+1,pos.getY()+1)){
            pos.set(pos.getX()+1,pos.getY()+1);
        }
    }
    
    /**
     * Simulates a step for an object
     * Every different type of object has a different code
     * @param tab The table where the object is simulated
     * @param delta The value of the step
     */
    public void simulate(Table tab, double delta){}
    
            
    /**
     * Prints some information about the object
     */
    public void print() {
        System.out.println("ID objecte: " + id);
        System.out.println("posicio x = " + pos.getX() + " y = " + pos.getY());
    }
    
}